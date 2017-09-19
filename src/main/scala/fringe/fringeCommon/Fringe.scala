package fringe

import scala.language.reflectiveCalls
import chisel3._
import chisel3.util.{Cat, EnqIO, Decoupled}
import templates._
import templates.Utils.log2Up

/**
 * Fringe: Top module for FPGA shell
 * @param w: Word width
 * @param numArgIns: Number of input scalar arguments
 * @param numArgOuts: Number of output scalar arguments
 */
class Fringe(
  val w: Int,
  val numArgIns: Int,
  val numArgOuts: Int,
  val numArgIOs: Int,
  val numChannels: Int,
  val numArgInstrs: Int,
  val loadStreamInfo: List[StreamParInfo],
  val storeStreamInfo: List[StreamParInfo],
  val streamInsInfo: List[StreamParInfo],
  val streamOutsInfo: List[StreamParInfo],
  val blockingDRAMIssue: Boolean = false
) extends Module {
//  val numRegs = numArgIns + numArgOuts + 2 - numArgIOs // (command, status registers)
//  val addrWidth = log2Up(numRegs)
  val addrWidth = 32

  val commandReg = 0  // TODO: These vals are used in test only, logic below does not use them.
  val statusReg = 1   //       Changing these values alone has no effect on the logic below.

  // Some constants (mostly MAG-related) that will later become module parameters
  val v = 16 // Number of words in the same stream
  val numOutstandingBursts = 1024  // Picked arbitrarily
  val burstSizeBytes = 64
  val d = 512 // FIFO depth: Controls FIFO sizes for address, size, and wdata
  val regWidth = 64 // Force 64-bit registers

  val io = IO(new Bundle {
    // Host scalar interface
    val regIO = new RegFilePureInterface(addrWidth, regWidth)

    // Accel Control IO
    val enable = Output(Bool())
    val done = Input(Bool())

    // Accel Scalar IO
    val argIns = Output(Vec(numArgIns, UInt(regWidth.W)))
    val argOuts = Vec(numArgOuts, Flipped(Decoupled((UInt(regWidth.W)))))

    // Accel memory IO
    val memStreams = new AppStreams(loadStreamInfo, storeStreamInfo)
    val dram = Vec(numChannels, new DRAMStream(w, v))

    //Accel stream IO
//    val genericStreamsAccel = Flipped(new GenericStreams(streamInsInfo, streamOutsInfo))
//    val genericStreamOutTop = StreamOut(StreamParInfo(w, 1))
//    val genericStreamInTop = StreamIn(StreamParInfo(w, 1))

    // Debug
    val aws_top_enable = Input(Bool()) // For AWS, enable comes in as input to top module

    val dbg = new DebugSignals
  })

  // Divide up channels within memStreams into 'numChannels' in round-robin fashion
  // The method below returns the IDs of load and store streams for each channel
  def getChannelAssignment(numStreams: Int, numChannels: Int) = {
    List.tabulate(numChannels) { i => (i until numStreams by numChannels).toList }
  }

  def getAppStreamIDs(assignment: List[Int]) = {
    val (loads, absStores) = if (assignment.indexWhere { _ >= loadStreamInfo.size } == -1) (assignment, List[Int]())
                          else assignment.splitAt(assignment.indexWhere { _ >= loadStreamInfo.size })
    val stores = absStores.map { _ - loadStreamInfo.size }
    (loads, stores)
  }

    println(s"[Fringe] loadStreamInfo: $loadStreamInfo, storeStreamInfo: $storeStreamInfo")
  val numStreams = loadStreamInfo.size + storeStreamInfo.size
  val assignment = getChannelAssignment(numStreams, numChannels)
  val mags = List.tabulate(numChannels) { i =>
    val channelAssignment = assignment(i)
    val (loadStreamIDs, storeStreamIDs) = getAppStreamIDs(channelAssignment)

    println(s"[Fringe] Creating MAG $i, assignment: $channelAssignment, loadStreamIDs: $loadStreamIDs, storeStreamIDs: $storeStreamIDs")
    val linfo = if (loadStreamIDs.size == 0) List(StreamParInfo(w, 16)) else loadStreamIDs.map { loadStreamInfo(_) }
    val sinfo = if (storeStreamIDs.size == 0) List(StreamParInfo(w, 16)) else storeStreamIDs.map {storeStreamInfo(_) }
    val loadStreams = loadStreamIDs.map { io.memStreams.loads(_) }
    val storeStreams = storeStreamIDs.map { io.memStreams.stores(_) }

    val mag = Module(new MAGCore(w, d, v, linfo, sinfo, numOutstandingBursts, burstSizeBytes, blockingDRAMIssue))
    mag.io.app.loads.zip(loadStreams) foreach { case (l, ls) => l <> ls }
    mag.io.app.stores.zip(storeStreams) foreach { case (s, ss) => s <> ss }
    mag
  }

  val debugChannelID = 0

  val numDebugs = mags(debugChannelID).numDebugs
  val numRegs = numArgIns + numArgOuts + 2 - numArgIOs + numDebugs // (command, status registers)

  // Scalar, command, and status register file
  val regs = Module(new RegFile(regWidth, numRegs, numArgIns+2, numArgOuts+1+numDebugs, numArgIOs))
  regs.io.addrInterface <> io.regIO
//  regs.io.raddr := io.raddr
//  regs.io.waddr := io.waddr
//  regs.io.wen := io.wen
//  regs.io.wdata := io.wdata
//  io.rdata := regs.io.rdata

  val command = regs.io.argIns(0)   // commandReg = first argIn
  val curStatus = regs.io.argIns(1) // current status
  val localEnable = command(0) & ~curStatus(0)          // enable = LSB of first argIn
  io.enable := localEnable

  // Hardware time out (for debugging)
  val timeoutCycles = 12000000000L
  val timeoutCtr = Module(new Counter(40))
  timeoutCtr.io.reset := 0.U
  timeoutCtr.io.saturate := 1.U
  timeoutCtr.io.max := timeoutCycles.U
  timeoutCtr.io.stride := 1.U
  timeoutCtr.io.enable := localEnable

  io.argIns.zipWithIndex.foreach{case (p, i) => p := regs.io.argIns(i+2)}

  val depulser = Module(new Depulser())
  depulser.io.in := io.done | timeoutCtr.io.done
  depulser.io.rst := ~command
  val status = Wire(EnqIO(UInt(regWidth.W)))
  status.bits := Cat(command(0) & timeoutCtr.io.done, command(0) & depulser.io.out.asUInt)
  status.valid := depulser.io.out
  regs.io.argOuts.zipWithIndex.foreach { case (argOutReg, i) =>
    // Manually assign bits and valid, because direct assignment with :=
    // is causing issues with chisel compilation due to the 'ready' signal
    // which we do not care about
    if (i == 0) { // statusReg: First argOut
      argOutReg.bits := status.bits
      argOutReg.valid := status.valid
    } else if (i <= numArgOuts) {
      argOutReg.bits := io.argOuts(i-1).bits
      argOutReg.valid := io.argOuts(i-1).valid
    } else {
      argOutReg.bits := mags(debugChannelID).io.debugSignals(i-numArgOuts-1)
      argOutReg.valid := 1.U
    }
  }

  // Memory address generator
  val magConfig = Wire(new MAGOpcode())
  magConfig.scatterGather := false.B
  mags.foreach { _.io.config := magConfig }
  if ((FringeGlobals.target == "aws") | (FringeGlobals.target == "aws-sim")) {
    mags.foreach { _.io.enable := io.aws_top_enable }
  } else {
    mags.foreach { _.io.enable := localEnable }
  }

//  mag.io.app <> io.memStreams

  mags.zip(io.dram) foreach { case (mag, d) => mag.io.dram <> d }

  io.dbg <> mags(debugChannelID).io.dbg

  // In simulation, streams are just pass through
  // TODO: Make these connections with hashmap generated by codegen
//  if (io.genericStreamsAccel.outs.length == 1) {
//    io.genericStreamsAccel.ins(0) <> io.genericStreamInTop
//  }
//  if (io.genericStreamsAccel.ins.length == 1) {
//    io.genericStreamsAccel.outs(0) <> io.genericStreamOutTop
//  }
}
