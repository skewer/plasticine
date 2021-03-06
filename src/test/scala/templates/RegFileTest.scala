// See LICENSE for license details.

package templates
//import templates.CommonMain

import chisel3.core.Module
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

import scala.collection.mutable.ListBuffer
import scala.language.reflectiveCalls

/**
 * RegFile test harness
 */
class RegFileUnitTester(c: RegFile)(implicit args: Array[String]) extends ArgsTester(c) {
  // Host interface test: write to all regs, then read from all regs
  println(s"Host interface test")
  val wdata = List.fill(c.d) { rnd.nextInt }
  poke(c.io.addrInterface.wen, 1)
  for (i <- 0 until c.d) {
    poke(c.io.addrInterface.waddr, i)
    poke(c.io.addrInterface.wdata, wdata(i))
    step(1)
  }

  poke(c.io.addrInterface.wen, 0)

  val rdata = ListBuffer[Int]()
  for (i <- 0 until c.d) {
    poke(c.io.addrInterface.raddr, i)
    step(1)
    rdata.append(peek(c.io.addrInterface.rdata).toInt)
  }

  wdata.zip(rdata) foreach { case (exp, obs) =>
    if (exp != obs) printFail(s"Expected $exp, observed $obs")
    else printPass(s"Expected $exp, observed $obs")
  }

  val expectedRegs = rdata.clone
  // ArgOut interface test: Set argOutEn, write, and check values
  println(s"ArgOut interface test")
  for (i <- 0 until c.numArgOuts) {
    val argOut = i
    val regIdx = c.argOut2RegIdx(i)
    poke(c.io.argOuts(i).bits, argOut)
    poke(c.io.argOuts(i).valid, 1)
    step(1)
    poke(c.io.argOuts(i).valid, 0)
    expectedRegs(regIdx) = argOut
  }

  for (i <- 0 until c.d) {
    poke(c.io.addrInterface.raddr, i)
    step(1)
    rdata(i) = peek(c.io.addrInterface.rdata).toInt
  }

  expectedRegs.zip(rdata) foreach { case (exp, obs) =>
    if (exp != obs) printFail(s"Expected $exp, observed $obs")
    else printPass(s"Expected $exp, observed $obs")
  }
}

object RegFileTest extends CommonMain {
  type DUTType = RegFile
  def dut = () => {
    val w = 32
    val d = 16
    val numArgIns = d/2
    val numArgOuts = d/2
    new RegFile(w, d, numArgIns, numArgOuts)
  }
  def tester = { c: DUTType => new RegFileUnitTester(c) }
}

