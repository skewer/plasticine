package plasticine.templates

import scala.collection.mutable.Queue
import Chisel._

import plasticine.pisa.ir._

class MultiMemoryUnitTester (
  val w: Int,
  val d: Int,
  val v: Int,
  val numOutstandingBursts: Int,
  val burstSizeBytes: Int,
  val inst: MemoryUnitConfig) extends Module {
  val numMemoryUnits = 4
  val numChnRankBits = 4
  val wordSize = w/8

  val io = new ConfigInterface {
    val config_enable = Bool(INPUT)
    val interconnects = Vec.fill(numMemoryUnits) { new PlasticineMemoryCmdInterface(w, v) }
  }

  def genMemoryUnits = {
    List.tabulate(numMemoryUnits) { i =>
      val mu = Module(new MemoryUnit(w, d, v, numOutstandingBursts, burstSizeBytes, inst))
      mu.io.config_enable := io.config_enable
      mu.io.config_data := io.config_data
      mu.io.interconnect.rdyIn := io.interconnects(i).rdyIn
      mu.io.interconnect.vldIn := io.interconnects(i).vldIn
      io.interconnects(i).rdyOut := mu.io.interconnect.rdyOut
      io.interconnects(i).vldOut := mu.io.interconnect.vldOut
      mu.io.interconnect.addr := io.interconnects(i).addr
      mu.io.interconnect.wdata := io.interconnects(i).wdata
      mu.io.interconnect.dataVldIn := io.interconnects(i).dataVldIn
      mu.io.interconnect.size := io.interconnects(i).size
      io.interconnects(i).rdata := mu.io.interconnect.rdata

      mu
    }
  }

  def genDRAMSims = {
    List.tabulate(numMemoryUnits) { j =>
      val DRAMSimulator = Module(new DRAMSimulator(w+4, burstSizeBytes))

      DRAMSimulator
    }
  }

  val mus = genMemoryUnits
  val sims = genDRAMSims
  // connection example
  val chnRanksIdBits = Vec(UInt(0), UInt(4), UInt(8), UInt(12)) { UInt(width=4) }
  for (id <- 0 to numMemoryUnits - 1) {
    sims(id).io.addr := Cat(chnRanksIdBits(id), mus(id).io.dram.addr)
    sims(id).io.wdata := mus(id).io.dram.wdata
    sims(id).io.tagIn := mus(id).io.dram.tagOut
    sims(id).io.vldIn := mus(id).io.dram.vldOut
    sims(id).io.isWr := mus(id).io.dram.isWr

    mus(id).io.dram.rdata := sims(id).io.rdata
    mus(id).io.dram.vldIn := sims(id).io.vldOut
    mus(id).io.dram.tagIn := sims(id).io.tagOut
  }
}

class MultiMemoryUnitTests(c: MultiMemoryUnitTester) extends Tester(c) {
  val size = 64
  val burstSizeBytes = 64
  val wordsPerBurst = burstSizeBytes / (c.w / 8)

  // Test constants
  val addr1 = 0x1000
  val waddr1 = 0x2000
	val addr2 = 0x1000
	val waddr2 = 0x2000
  val chan0 = 0
  val chan1 = 1
	val chan2 = 2
	val chan3 = 3
  val wdata = List.tabulate(wordsPerBurst) { i => i + 0xcafe }
  val numCmds = c.numOutstandingBursts
  val numChans = 4
  // Test var
  var numTransCompleted = 0

  def getDataInBursts(data: List[Int]) = {
    Queue.tabulate(data.size / wordsPerBurst) { i => data.slice(i*wordsPerBurst, i*wordsPerBurst + wordsPerBurst) }
  }

  def poke(port: Vec[UInt], value: Seq[Int]) {
    port.zip(value) foreach { case (in, i) => poke(in, i) }
  }

  def peek(port: Vec[UInt]): List[Int] = {
    port.map { peek(_).toInt }.toList
  }

  case class Cmd(chan: Int, addr: Int, data: List[Int], wr: Int, tag: Int) {
    override def equals(that: Any) = that match {
      case t: Cmd =>
        (chan == t.chan) &
        (addr == t.addr) &
        (tag == t.tag) &
        (wr == t.wr) &
        (if (wr > 0) data == t.data else true)
      case _ => false
    }

    override def toString = s"Cmd($chan, $addr, $tag, $wr, ${if (wr > 0) data else List()})"
  }

  var expectedTag = 0
  val expectedOrder = Queue[Cmd]()
  val observedOrder = Queue[Cmd]()

  def observeAllForOneCycle() {
    for (chan <- 0 until numChans) {
      if (peek(c.sims(chan).io.vldOut) > 0) {
        numTransCompleted = numTransCompleted + 1
        print("========== transaction at channel " + chan + " finished ==========")
      }
    }

    step(1)
  }

  def incTag {
    expectedTag = (expectedTag + 1) % c.numOutstandingBursts
  }

  def getNumBursts(size: Int) = (size / burstSizeBytes) + (if (size%burstSizeBytes > 0) 1 else 0)

  def issueCmd(chan: Int, burstAddr: Int, isWr: Int, data: List[Int]) {
    val cmd = Cmd(chan, burstAddr, data, isWr, expectedTag)
    expectedOrder += cmd
    incTag
  }

  def updateExpected(chan: Int, addr: Int, size: Int, isWr: Int, data: List[Int]) {
    val dataInBursts = getDataInBursts(data)
    val baseAddr = addr / burstSizeBytes
    val numBursts = getNumBursts(size)
    for (i <- 0 until numBursts) {
      issueCmd(chan, baseAddr+i, isWr, if (dataInBursts.isEmpty) List() else dataInBursts.dequeue)
    }
  }

  def printFail(msg: String) = println(Console.BLACK + Console.RED_B + s"FAIL: $msg" + Console.RESET)
  def printPass(msg: String) = println(Console.BLACK + Console.GREEN_B + s"PASS: $msg" + Console.RESET)

  def verifyAndDequeue: Boolean = {
    if (expectedOrder.size  != observedOrder.size) {
      printFail(s"Queue size mismatch: expected ${expectedOrder.size}, found ${observedOrder.size}")
      printFail(s"Expected: $expectedOrder")
      printFail(s"Observed: $observedOrder")
      false
    } else {
      val res = expectedOrder.zip(observedOrder).map { case (e, o) =>
        if (e != o) printFail(s"Expected $e, observed $o")
        e == o
      }.reduce{_ & _}
      expectedOrder.clear
      observedOrder.clear
      res
    }
  }

  def check(s: String) = verifyAndDequeue match {
    case true => printPass(s)
    case _ => printFail(s)
  }

//  def enqueueCmd(chan: Int, addr: Int, size: Int, isWr: Int, data: List[Int] = List[Int]()) {
//    poke(c.io.interconnects(chan).vldIn, 1)
//    poke(c.io.interconnects(chan).addr(0), addr)
//    poke(c.io.interconnects(chan).size, size)
//
//    // If it is a write, enqueue first burst
//    val dataInBursts = getDataInBursts(data)
//    if (isWr > 0) {
//      poke(c.io.interconnects(chan).wdata, dataInBursts.dequeue)
//      poke(c.io.interconnects(chan).dataVldIn, 1)
//    }
//
//    observeChannel(chan)
//
//    poke(c.io.interconnects(chan).vldIn, 0)
//    if (isWr > 0) {
//      println("writing......")
//      while (!dataInBursts.isEmpty) {
//        poke(c.io.interconnects(chan).wdata, dataInBursts.dequeue)
//        observeChannel(chan)
//      }
//    }
//
//    poke(c.io.interconnects(chan).dataVldIn, 0)
//    updateExpected(chan, addr, size, isWr, data)
//  }

//  def enqueueBurstRead(chan: Int, addr: Int, size: Int) {
//    enqueueCmd(chan, addr, size, 0)
//  }
//
//  def enqueueBurstWrite(chan: Int, addr: Int, size: Int, data: List[Int]) {
//    enqueueCmd(chan, addr, size, 1, data)
//  }

  def peekOnChan(chan: Int) {
    val dramTagIn = peek(c.mus(chan).io.dram.tagIn).toInt
    val dramTagOut = peek(c.mus(chan).io.dram.tagOut).toInt
    val dramVldOut = peek(c.mus(chan).io.dram.vldOut).toInt
    val dramVldIn = peek(c.mus(chan).io.dram.vldIn).toInt
    val interconnectsVldIn = peek(c.io.interconnects(chan0).vldIn).toInt

    if (dramVldIn > 0) {
      numTransCompleted = numTransCompleted + 1
    }
  }

//  val numCommands = numCmds
  val numCommands = 500
  val writeMode = peek(c.mus(chan0).config.isWr).toInt
  if (writeMode > 0) {
    println("start testing writes")
    val waddrs = List.tabulate(numCommands) { i => addr1 + i * 0x40 }
    val wsizes = List.tabulate(numCommands) { i => burstSizeBytes }
    waddrs.zip(wsizes) foreach {
      case (waddr, wsize) => {
        poke(c.io.interconnects(chan0).vldIn, 1)
        poke(c.io.interconnects(chan0).addr(0), waddr)
        poke(c.io.interconnects(chan0).size, wsize)
        poke(c.io.interconnects(chan0).wdata, wdata)
        step(1)
        peekOnChan(chan0)
      }
    }

    poke(c.io.interconnects(chan0).vldIn, 0)
    poke(c.io.interconnects(chan0).addr(0), 0x0000)
    poke(c.io.interconnects(chan0).size, 0)

    while (numTransCompleted != numCommands) {
      println("numTransCompleted = " + numTransCompleted)
      step(1)
      peekOnChan(chan0)
    }

  } else {
    println("start testing writes")
    val raddrs = List.tabulate(numCommands) { i => addr1 + i * 0x40 }
    val rsizes = List.tabulate(numCommands) { i => burstSizeBytes }
    raddrs.zip(rsizes) foreach {
      case (raddr, rsize) => {
        poke(c.io.interconnects(chan0).vldIn, 1)
        poke(c.io.interconnects(chan0).addr(0), raddr)
        poke(c.io.interconnects(chan0).size, rsize)
        step(1)
        peekOnChan(chan0)
      }
    }

    poke(c.io.interconnects(chan0).vldIn, 0)
    poke(c.io.interconnects(chan0).addr(0), 0x0000)
    poke(c.io.interconnects(chan0).size, 0)

    while (numTransCompleted != numCommands) {
      println("numTransCompleted = " + numTransCompleted)
      step(1)
      peekOnChan(chan0)
    }
  }
}

object MultiMemoryUnitTest {
  val w = 32
  val v = 16
  val d = 512
  val numOutstandingBursts = 1024
  val burstSizeBytes = 64
  val scatterGather = 0
  val isWr = 0
  val config = MemoryUnitConfig(scatterGather, isWr)
  def main(args: Array[String]): Unit = {
      chiselMainTest(args, () => Module(new MultiMemoryUnitTester(w, d, v, numOutstandingBursts, burstSizeBytes, config))) {
        c => { new MultiMemoryUnitTests(c) }
    }
  }
}
