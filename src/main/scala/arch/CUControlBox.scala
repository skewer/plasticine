package plasticine.templates
import plasticine.pisa.parser.Parser
import plasticine.misc.Utils
import Chisel._

import plasticine.ArchConfig
import plasticine.pisa.ir._

/**
 * CUControlBox config register format
 */
case class CUControlBoxOpcode(val numTokens: Int, val numTokenDownLUTs: Int, config: Option[CUControlBoxConfig] = None) extends OpcodeT {

  val udcInit = Vec.tabulate(numTokens) { i =>
    if (config.isDefined) {
      UInt(config.get.udcInit(i), width=4) // TODO: Remove hardcoded '4' !
    } else {
      UInt(width=4)
    }
  }

  val udcInitAtConfig = Vec.tabulate(numTokens) { i =>
    if (config.isDefined) {
      Bool(config.get.udcInitAtConfig(i) > 0) // TODO: Remove hardcoded '4' !
    } else {
      Bool()
    }
  }

  val enableMux = Vec.tabulate(numTokens) { i =>
    if (config.isDefined) {
      Bool(config.get.enableMux(i))
    } else {
      Bool()
    }
  }

  val syncTokenMux = Vec.tabulate(numTokenDownLUTs) { i =>
    if (config.isDefined) UInt(config.get.syncTokenMux(i), width=log2Up(numTokens))
    else UInt(width=log2Up(numTokens))
  }

  val fifoAndTree = Vec.tabulate(ArchConfig.numScratchpads) { i =>
    if (config.isDefined) Bool(config.get.fifoAndTree(i) > 0)
    else Bool()
  }

  val tokenInAndTree = Vec.tabulate(numTokens) { i =>
    if (config.isDefined) Bool(config.get.tokenInAndTree(i) > 0)
    else Bool()
  }

  val fifoMux = Vec.tabulate(numTokens) { i =>
    if (config.isDefined) Bool(config.get.fifoMux(i) > 0)
    else Bool()
  }

  override def cloneType(): this.type = {
    new CUControlBoxOpcode(numTokens, numTokenDownLUTs, config).asInstanceOf[this.type]
  }
}


/**
 * Compute Unit Control Module. Handles incoming tokens, done signals,
 * and outgoing tokens.
 */
class CUControlBox(val numTokens: Int, inst: CUControlBoxConfig) extends ConfigurableModule[CUControlBoxOpcode] {
  val udctrWidth = 4
  val numTokenDownLUTs = 8

  val io = new ConfigInterface {
    val config_enable = Bool(INPUT)
    /* Input tokens */
   val tokenIns = Vec.fill(numTokens) { Bool(INPUT)}

   val fifoNotFull = Vec.fill(ArchConfig.numScratchpads) { Bool(INPUT) }
   val fifoNotEmpty = Vec.fill(ArchConfig.numScratchpads) { Bool(INPUT) }

    /* Input local 'done' signals */
   val done = Vec.fill(numTokens) { Bool(INPUT)}
    /* Output tokens */
   val tokenOuts = Vec.fill(numTokens) { Bool(OUTPUT)}
    /* Output 'enable' signals */
   val enable = Vec.fill(numTokens) { Bool(OUTPUT)}
  }

  val configType = CUControlBoxOpcode(numTokens, numTokenDownLUTs)
  val configIn = CUControlBoxOpcode(numTokens, numTokenDownLUTs)
  val configInit = CUControlBoxOpcode(numTokens, numTokenDownLUTs, Some(inst))
  val config = Reg(configType, configIn, configInit)
  when (io.config_enable) {
    configIn := configType.cloneType().fromBits(Fill(configType.getWidth, io.config_data))
  } .otherwise {
    configIn := config
  }

  // Done crossbar - connects done signals from counter chains to LUTs
  val doneXbar = Module(new Crossbar(1, numTokens, 2*numTokens, inst.doneXbar))
  doneXbar.io.config_enable := io.config_enable
  doneXbar.io.config_data := io.config_data
  doneXbar.io.ins.zip(io.done).foreach { case (in, done) => in := done } // MSB must be in position 0

  // Token out LUTs
  val tokenOutLUTs = List.tabulate(numTokens) { i =>
    val lut = Module(new LUT(1, 1 << 2, inst.tokenOutLUT(i)))
    lut.io.config_enable := io.config_enable
    lut.io.config_data := io.config_data
    lut.io.ins := doneXbar.io.outs.drop(i*2).take(2)
//    io.tokenOuts(i+1) := lut.io.out
    lut
  }

  // Decrement signal crossbar. Inputs is numTokens+1 because there is a constant
  // "zero" input which can also be chosen
  val decXbar = Module(new Crossbar(1, numTokens+1, numTokens, inst.decXbar))
  decXbar.io.config_enable := io.config_enable
  decXbar.io.config_data := io.config_data
  decXbar.io.ins.zipWithIndex.foreach { case (in, i) => if (i == 0) in := UInt(0, width=1) else in := io.done(i-1) }
  val decs = decXbar.io.outs

  // Increment signal crossbar. Inputs is numTokens+1 because there is a constant
  // "zero" input which can also be chosen
  val incXbar = Module(new Crossbar(1, numTokens+1, 2*numTokens, inst.incXbar))
  incXbar.io.config_enable := io.config_enable
  incXbar.io.config_data := io.config_data
  incXbar.io.ins.zipWithIndex.foreach { case (in, i) => if (i == 0) in := UInt(0, width=1) else in := io.tokenIns(i-1) }
  val incs = incXbar.io.outs.take(numTokens)
  val inits = incXbar.io.outs.takeRight(numTokens)

  // Token In Xbar: Used to route enable signals for duplicate counter's remote write signals
  val tokenInXbar = Module(new Crossbar(1, numTokens+1, numTokens, inst.tokenInXbar))
  tokenInXbar.io.config_enable := io.config_enable
  tokenInXbar.io.config_data := io.config_data
  tokenInXbar.io.ins.zipWithIndex.foreach { case (in, i) => if (i == 0) in := UInt(0, width=1) else in := io.tokenIns(i-1) }

  // Up-down counters to handle tokens and credits
  val udCounters = List.tabulate(numTokens) { i =>
    val udc = Module(new UpDownCtr(udctrWidth))
    udc.io.initAtConfig := config.udcInitAtConfig(i)
    udc.io.initval := config.udcInit(i)
    udc.io.strideInc := UInt(1)
    udc.io.strideDec := UInt(1)
    udc.io.init := inits(i)
    udc.io.inc := incs(i)
    udc.io.dec := decs(i)
    udc
  }

  val gtzs = Vec.tabulate(numTokens) { i => udCounters(i).io.gtz }

  // TokenDown LUT: Used to handle route-through tokens from parents to children,
  // as well as barrier cases where more than one token must be received before sending
  // a token out
  val tokenDownLUTs = List.tabulate(numTokenDownLUTs) { lutIdx =>
    val mux = Module(new MuxN(numTokens, 1))
    mux.io.ins := io.tokenIns
    mux.io.sel := config.syncTokenMux(lutIdx)

    val lut = Module(new LUT(1, 1 << (numTokens+1), inst.tokenDownLUT(0)))
    lut.io.config_enable := io.config_enable
    lut.io.config_data := io.config_data
    lut.io.ins.zipWithIndex.foreach { case (in, i) =>
      if (i == 0) in := mux.io.out
      else in := gtzs(i-1)
    }
    lut
  }

  val tokenDownPulser = tokenDownLUTs.map { lut =>
    val pulser = Module(new Pulser())
    pulser.io.in := lut.io.out
    pulser.io.out
  }

  // FIFO And Tree
  val fifoAndMux = List.tabulate(ArchConfig.numScratchpads) { i => Mux(config.fifoAndTree(i), io.fifoNotEmpty(i), Bool(true)) }
  val tokenInAndMux = List.tabulate(numTokens) { i => Mux(config.tokenInAndTree(i), io.tokenIns(i), Bool(true)) }
  val fifoAndTree = fifoAndMux.reduce {_&_}
  val tokenInAndTree = tokenInAndMux.reduce {_&_}

  val fifoTokenTree = fifoAndTree & tokenInAndTree

  val fifoMux = List.tabulate(gtzs.size) { i => Mux(config.fifoMux(i), fifoTokenTree, gtzs(i)) }
  // Enable Mux
  val enableLUTs = List.tabulate(numTokens) { i =>
    val m = Module(new LUT(1, 1 << gtzs.size, inst.enableLUT(i)))
    m.io.config_enable := io.config_enable
    m.io.config_data := io.config_data

    val lutInputs = Vec(List(fifoMux(i)) ++ gtzs.drop(1))
    m.io.ins := lutInputs
    m
  }

  val enableMux = List.tabulate(numTokens) { i => Mux(config.enableMux(i), tokenInXbar.io.outs(i), enableLUTs(i).io.out) }
  io.enable.zip(enableMux) foreach { case (en, mux) => en := mux }

  // Token out xbar
  // Order: 0: 0,
  // 1..numTokenDownLUTs: pulser(i).io.out,
  // (numTokenDownLUTs+1)..(numTokenDownLUTs+1+numTokens-1): tokenOutLUTs(i-numTokenDownLUTs-1)
  val tokenOutXbarIns = Vec(
        List(UInt(0, width=1)) ++
        io.fifoNotFull ++
        tokenDownPulser ++
        tokenOutLUTs.map {_.io.out} ++
        enableLUTs.map {_.io.out}
      )
  val tokenOutXbar = Module(new Crossbar(1, tokenOutXbarIns.size, numTokens, inst.tokenOutXbar))
  tokenOutXbar.io.config_enable := io.config_enable
  tokenOutXbar.io.config_data := io.config_data
  tokenOutXbar.io.ins.zip(tokenOutXbarIns).foreach { case (in, i) => in := i }

  io.enable.zip(enableMux) foreach { case (en, mux) => en := mux }
  io.tokenOuts.zip(tokenOutXbar.io.outs) foreach { case (tout, out) => tout := out }
}

class CUControlBoxTests(c: CUControlBox) extends PlasticineTester(c) {

}

object CUControlBoxTest {
  def main(args: Array[String]): Unit = {
    val (appArgs, chiselArgs) = args.splitAt(args.indexOf("end"))

//    if (appArgs.size != 1) {
//      println("Usage: bin/sadl CUControlBoxTest <pisa config>")
//      sys.exit(-1)
//    }

//    val pisaFile = appArgs(0)
//    val configObj = Parser(pisaFile).asInstanceOf[CUControlBoxConfig]
    val numTokens = 8
    val config = CUControlBoxConfig.getRandom(numTokens, numTokens, numTokens)


    chiselMainTest(chiselArgs, () => Module(new CUControlBox(numTokens, config))) {
      c => new CUControlBoxTests(c)
    }
  }
}