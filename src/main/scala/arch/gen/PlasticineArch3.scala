package plasticine.arch
import chisel3._
import chisel3.util._
import plasticine.templates.MuxN
import scala.language.reflectiveCalls
import scala.collection.mutable.ListBuffer

trait PlasticineArch3 extends PlasticineArch2 {
  def connect3(io:PlasticineIO, argOutMuxIns:Array[Array[DecoupledIO[UInt]]], doneOuts:Array[Bool], cus:Array[Array[CU]], scus:Array[Array[ScalarCU]] , vsbs:Array[Array[VectorSwitch]], ssbs:Array[Array[ScalarSwitch]], csbs:Array[Array[ControlSwitch]], lcus:Array[Array[SwitchCU]]):Unit = {
    lcus(2)(2).io.scalarOut(0) <> ssbs(2)(2).io.ins(7)
    lcus(2)(2).io.scalarOut(1) <> ssbs(2)(2).io.ins(8)
    ssbs(0)(0).io.outs(0) <> ssbs(0)(1).io.ins(12)
    ssbs(0)(0).io.outs(1) <> ssbs(0)(1).io.ins(13)
    ssbs(0)(0).io.outs(2) <> ssbs(0)(1).io.ins(14)
    ssbs(0)(0).io.outs(3) <> ssbs(0)(1).io.ins(15)
    ssbs(0)(0).io.outs(4) <> cus(0)(0).io.scalarIn(3)
    ssbs(0)(0).io.outs(5) <> ssbs(1)(0).io.ins(0)
    ssbs(0)(0).io.outs(6) <> ssbs(1)(0).io.ins(1)
    ssbs(0)(0).io.outs(7) <> ssbs(1)(0).io.ins(2)
    ssbs(0)(0).io.outs(8) <> ssbs(1)(0).io.ins(3)
    ssbs(0)(0).io.outs(9) <> lcus(0)(0).io.scalarIn(0)
    ssbs(0)(0).io.outs(10) <> lcus(0)(0).io.scalarIn(1)
    ssbs(0)(0).io.outs(11) <> lcus(0)(0).io.scalarIn(2)
    ssbs(0)(0).io.outs(12) <> lcus(0)(0).io.scalarIn(3)
    ssbs(0)(0).io.outs(13) <> argOutMuxIns(0)(0)
    ssbs(0)(0).io.outs(13) <> argOutMuxIns(1)(0)
    ssbs(0)(0).io.outs(13) <> argOutMuxIns(2)(0)
    ssbs(0)(1).io.outs(0) <> ssbs(0)(2).io.ins(10)
    ssbs(0)(1).io.outs(1) <> ssbs(0)(2).io.ins(11)
    ssbs(0)(1).io.outs(2) <> ssbs(0)(2).io.ins(12)
    ssbs(0)(1).io.outs(3) <> ssbs(0)(2).io.ins(13)
    ssbs(0)(1).io.outs(4) <> cus(0)(1).io.scalarIn(3)
    ssbs(0)(1).io.outs(5) <> ssbs(1)(1).io.ins(0)
    ssbs(0)(1).io.outs(6) <> ssbs(1)(1).io.ins(1)
    ssbs(0)(1).io.outs(7) <> ssbs(1)(1).io.ins(2)
    ssbs(0)(1).io.outs(8) <> ssbs(1)(1).io.ins(3)
    ssbs(0)(1).io.outs(9) <> lcus(0)(1).io.scalarIn(0)
    ssbs(0)(1).io.outs(10) <> lcus(0)(1).io.scalarIn(1)
    ssbs(0)(1).io.outs(11) <> lcus(0)(1).io.scalarIn(2)
    ssbs(0)(1).io.outs(12) <> lcus(0)(1).io.scalarIn(3)
    ssbs(0)(1).io.outs(13) <> cus(0)(0).io.scalarIn(0)
    ssbs(0)(1).io.outs(14) <> ssbs(0)(0).io.ins(0)
    ssbs(0)(1).io.outs(15) <> ssbs(0)(0).io.ins(1)
    ssbs(0)(1).io.outs(16) <> ssbs(0)(0).io.ins(2)
    ssbs(0)(1).io.outs(17) <> ssbs(0)(0).io.ins(3)
    ssbs(0)(2).io.outs(0) <> argOutMuxIns(0)(3)
    ssbs(0)(2).io.outs(0) <> argOutMuxIns(1)(3)
    ssbs(0)(2).io.outs(0) <> argOutMuxIns(2)(3)
    ssbs(0)(2).io.outs(1) <> ssbs(1)(2).io.ins(0)
    ssbs(0)(2).io.outs(2) <> ssbs(1)(2).io.ins(1)
    ssbs(0)(2).io.outs(3) <> ssbs(1)(2).io.ins(2)
    ssbs(0)(2).io.outs(4) <> ssbs(1)(2).io.ins(3)
    ssbs(0)(2).io.outs(5) <> lcus(0)(2).io.scalarIn(0)
    ssbs(0)(2).io.outs(6) <> lcus(0)(2).io.scalarIn(1)
    ssbs(0)(2).io.outs(7) <> lcus(0)(2).io.scalarIn(2)
    ssbs(0)(2).io.outs(8) <> lcus(0)(2).io.scalarIn(3)
    ssbs(0)(2).io.outs(9) <> cus(0)(1).io.scalarIn(0)
    ssbs(0)(2).io.outs(10) <> ssbs(0)(1).io.ins(0)
    ssbs(0)(2).io.outs(11) <> ssbs(0)(1).io.ins(1)
    ssbs(0)(2).io.outs(12) <> ssbs(0)(1).io.ins(2)
    ssbs(0)(2).io.outs(13) <> ssbs(0)(1).io.ins(3)
    ssbs(1)(0).io.outs(0) <> ssbs(0)(0).io.ins(5)
    ssbs(1)(0).io.outs(1) <> ssbs(0)(0).io.ins(6)
    ssbs(1)(0).io.outs(2) <> ssbs(0)(0).io.ins(7)
    ssbs(1)(0).io.outs(3) <> ssbs(0)(0).io.ins(8)
    ssbs(1)(0).io.outs(4) <> cus(0)(0).io.scalarIn(2)
    ssbs(1)(0).io.outs(5) <> ssbs(1)(1).io.ins(17)
    ssbs(1)(0).io.outs(6) <> ssbs(1)(1).io.ins(18)
    ssbs(1)(0).io.outs(7) <> ssbs(1)(1).io.ins(19)
    ssbs(1)(0).io.outs(8) <> ssbs(1)(1).io.ins(20)
    ssbs(1)(0).io.outs(9) <> cus(1)(0).io.scalarIn(3)
    ssbs(1)(0).io.outs(10) <> ssbs(2)(0).io.ins(0)
    ssbs(1)(0).io.outs(11) <> ssbs(2)(0).io.ins(1)
    ssbs(1)(0).io.outs(12) <> ssbs(2)(0).io.ins(2)
    ssbs(1)(0).io.outs(13) <> ssbs(2)(0).io.ins(3)
    ssbs(1)(0).io.outs(14) <> lcus(1)(0).io.scalarIn(0)
    ssbs(1)(0).io.outs(15) <> lcus(1)(0).io.scalarIn(1)
    ssbs(1)(0).io.outs(16) <> lcus(1)(0).io.scalarIn(2)
    ssbs(1)(0).io.outs(17) <> lcus(1)(0).io.scalarIn(3)
    ssbs(1)(0).io.outs(18) <> argOutMuxIns(0)(1)
    ssbs(1)(0).io.outs(18) <> argOutMuxIns(1)(1)
    ssbs(1)(0).io.outs(18) <> argOutMuxIns(2)(1)
    ssbs(1)(1).io.outs(0) <> ssbs(0)(1).io.ins(5)
    ssbs(1)(1).io.outs(1) <> ssbs(0)(1).io.ins(6)
    ssbs(1)(1).io.outs(2) <> ssbs(0)(1).io.ins(7)
    ssbs(1)(1).io.outs(3) <> ssbs(0)(1).io.ins(8)
    ssbs(1)(1).io.outs(4) <> cus(0)(1).io.scalarIn(2)
    ssbs(1)(1).io.outs(5) <> ssbs(1)(2).io.ins(14)
    ssbs(1)(1).io.outs(6) <> ssbs(1)(2).io.ins(15)
    ssbs(1)(1).io.outs(7) <> ssbs(1)(2).io.ins(16)
    ssbs(1)(1).io.outs(8) <> ssbs(1)(2).io.ins(17)
    ssbs(1)(1).io.outs(9) <> cus(1)(1).io.scalarIn(3)
    ssbs(1)(1).io.outs(10) <> ssbs(2)(1).io.ins(0)
    ssbs(1)(1).io.outs(11) <> ssbs(2)(1).io.ins(1)
    ssbs(1)(1).io.outs(12) <> ssbs(2)(1).io.ins(2)
    ssbs(1)(1).io.outs(13) <> ssbs(2)(1).io.ins(3)
    ssbs(1)(1).io.outs(14) <> lcus(1)(1).io.scalarIn(0)
    ssbs(1)(1).io.outs(15) <> lcus(1)(1).io.scalarIn(1)
    ssbs(1)(1).io.outs(16) <> lcus(1)(1).io.scalarIn(2)
  }
}
