package fringe

import chisel3._
import chisel3.util._
import plasticine.templates.Utils.log2Up
import plasticine.templates.RegFile
import plasticine.templates.{Pulser, Depulser}
import plasticine.templates.MAGCore
import scala.language.reflectiveCalls

/**
 * Fringe: Top module for FPGA shell
 * @param w: Word width
 * @param numArgIns: Number of input scalar arguments
 * @param numArgOuts: Number of output scalar arguments
 */
class FringeLite(
  val w: Int,
  val numArgIns: Int,
  val numArgOuts: Int
) extends Module {
  val numRegs = numArgIns + numArgOuts + 2  // (command, status registers)
  val addrWidth = log2Up(numRegs)

  val commandReg = 0  // TODO: These vals are used in test only, logic below does not use them.
  val statusReg = 1   //       Changing these values alone has no effect on the logic below.

  // Some constants (mostly MAG-related) that will later become module parameters
  val v = 16 // Number of words in the same stream
  val d = 512 // FIFO depth: Controls FIFO sizes for address, size, and wdata. Rdata is not buffered
  val regWidth = 64 // Force 64-bit registers

  val io = IO(new Bundle {
    // Host scalar interface
    val raddr = Input(UInt(addrWidth.W))
    val wen  = Input(Bool())
    val waddr = Input(UInt(addrWidth.W))
    val wdata = Input(Bits(regWidth.W))
    val rdata = Output(Bits(regWidth.W))

    // Accel Control IO
    val enable = Output(Bool())
    val done = Input(Bool())

    // Accel Scalar IO
    val argIns = Output(Vec(numArgIns, UInt(regWidth.W)))
    val argOuts = Vec(numArgOuts, Flipped(Decoupled((UInt(regWidth.W)))))
  })

  // Scalar, command, and status register file
  val regs = Module(new RegFile(regWidth, numRegs, numArgIns+2, numArgOuts+1))
  regs.io.raddr := io.raddr
  regs.io.waddr := io.waddr
  regs.io.wen := io.wen
  regs.io.wdata := io.wdata
  io.rdata := regs.io.rdata

  val command = regs.io.argIns(0)   // commandReg = first argIn
  val curStatus = regs.io.argIns(1) // current status

  val enablePulser = Module(new Pulser())
  enablePulser.io.in := command(0) & ~curStatus(0)          // enable = LSB of first argIn
  io.enable := enablePulser.io.out
  io.argIns := regs.io.argIns.drop(2) // Accel argIns: Everything except first argIn

  val depulser = Module(new Depulser())
  depulser.io.in := io.done
  depulser.io.rst := ~command
  val status = Wire(EnqIO(UInt(regWidth.W)))
  status.bits := command & depulser.io.out.asUInt
  status.valid := depulser.io.out
  regs.io.argOuts.zipWithIndex.foreach { case (argOutReg, i) =>
    // Manually assign bits and valid, because direct assignment with :=
    // is causing issues with chisel compilation due to the 'ready' signal
    // which we do not care about
    if (i == 0) { // statusReg: First argOut
      argOutReg.bits := status.bits
      argOutReg.valid := status.valid
    } else {
      argOutReg.bits := io.argOuts(i-1).bits
      argOutReg.valid := io.argOuts(i-1).valid
    }
  }
}
