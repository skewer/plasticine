// See LICENSE for license details.

package templates

import chisel3.core._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}
import scala.language.reflectiveCalls

/**
 * FF test harness
 */
class MuxNUnitTester(c: MuxN[UInt])(implicit args: Array[String]) extends ArgsTester(c) {
    val ins = List.tabulate(c.numInputs) { i =>
      math.abs(scala.util.Random.nextInt % (1 << 4))
    }
    c.io.ins zip ins foreach { case (cin, in) => poke(cin, in) }

    (0 until c.numInputs) foreach { sel =>
      poke(c.io.sel, sel)
      step(1)
      val out = ins(sel)
      expect(c.io.out, out)
    }
}


object MuxNTest extends CommonMain {
  type DUTType = MuxN[UInt]
  def dut = () => new MuxN(UInt(args(1).toInt.W), args(0).toInt)
  def tester = { c: DUTType => new MuxNUnitTester(c) }
}

//class MuxNCharTests(c: MuxNReg)(implicit args: Array[String]) extends ArgsTester(c)
//
//object MuxNChar {
//  def main(args: Array[String]): Unit = {
//    val appArgs = args.take(args.indexOf("end"))
//    if (appArgs.size < 2) {
//      println("Usage: MuxNChar <wordWidth> <numInputs>")
//      sys.exit(-1)
//    }
//    val w = appArgs(0).toInt
//    val numInputs = appArgs(1).toInt
//    chiselMainTest(args, () => Module(new MuxNReg(numInputs, w))) {
//      c => new MuxNCharTests(c)
//    }
//  }
//}
//
//object MuxNAreaChar {
//  def main(args: Array[String]): Unit = {
//    val appArgs = args.take(args.indexOf("end"))
//    if (appArgs.size < 2) {
//      println("Usage: MuxNAreaChar <wordWidth> <numInputs>")
//      sys.exit(-1)
//    }
//    val w = appArgs(0).toInt
//    val numInputs = appArgs(1).toInt
//    chiselMainTest(args, () => Module(new MuxN(numInputs, w))) {
//      c => new MuxNTests(c)
//    }
//  }
//}
