package plasticine.arch
import chisel3._
import chisel3.util._
import plasticine.templates.MuxN
import scala.language.reflectiveCalls
import scala.collection.mutable.ListBuffer

trait PlasticineArch6 extends PlasticineArch5 {
  def connect6(io:PlasticineIO, argOutMuxIns:Array[Array[DecoupledIO[UInt]]], doneOuts:Array[Bool], cus:Array[Array[CU]], scus:Array[Array[ScalarCU]] , vsbs:Array[Array[VectorSwitch]], ssbs:Array[Array[ScalarSwitch]], csbs:Array[Array[ControlSwitch]], lcus:Array[Array[SwitchCU]]):Unit = {
    csbs(0)(2).io.outs(0) <> doneOuts(3)
    csbs(0)(2).io.outs(1) <> csbs(1)(2).io.ins(0)
    csbs(0)(2).io.outs(2) <> csbs(1)(2).io.ins(1)
    csbs(0)(2).io.outs(3) <> csbs(1)(2).io.ins(2)
    csbs(0)(2).io.outs(4) <> csbs(1)(2).io.ins(3)
    csbs(0)(2).io.outs(5) <> lcus(0)(2).io.controlIn(0)
    csbs(0)(2).io.outs(6) <> lcus(0)(2).io.controlIn(1)
    csbs(0)(2).io.outs(7) <> lcus(0)(2).io.controlIn(2)
    csbs(0)(2).io.outs(8) <> lcus(0)(2).io.controlIn(3)
    csbs(0)(2).io.outs(9) <> lcus(0)(2).io.controlIn(4)
    csbs(0)(2).io.outs(10) <> lcus(0)(2).io.controlIn(5)
    csbs(0)(2).io.outs(11) <> lcus(0)(2).io.controlIn(6)
    csbs(0)(2).io.outs(12) <> lcus(0)(2).io.controlIn(7)
    csbs(0)(2).io.outs(13) <> cus(0)(1).io.controlIn(0)
    csbs(0)(2).io.outs(14) <> cus(0)(1).io.controlIn(1)
    csbs(0)(2).io.outs(15) <> csbs(0)(1).io.ins(0)
    csbs(0)(2).io.outs(16) <> csbs(0)(1).io.ins(1)
    csbs(0)(2).io.outs(17) <> csbs(0)(1).io.ins(2)
    csbs(0)(2).io.outs(18) <> csbs(0)(1).io.ins(3)
    csbs(1)(0).io.outs(0) <> csbs(0)(0).io.ins(5)
    csbs(1)(0).io.outs(1) <> csbs(0)(0).io.ins(6)
    csbs(1)(0).io.outs(2) <> csbs(0)(0).io.ins(7)
    csbs(1)(0).io.outs(3) <> csbs(0)(0).io.ins(8)
    csbs(1)(0).io.outs(4) <> cus(0)(0).io.controlIn(4)
    csbs(1)(0).io.outs(5) <> cus(0)(0).io.controlIn(5)
    csbs(1)(0).io.outs(6) <> csbs(1)(1).io.ins(19)
    csbs(1)(0).io.outs(7) <> csbs(1)(1).io.ins(20)
    csbs(1)(0).io.outs(8) <> csbs(1)(1).io.ins(21)
    csbs(1)(0).io.outs(9) <> csbs(1)(1).io.ins(22)
    csbs(1)(0).io.outs(10) <> cus(1)(0).io.controlIn(6)
    csbs(1)(0).io.outs(11) <> cus(1)(0).io.controlIn(7)
    csbs(1)(0).io.outs(12) <> csbs(2)(0).io.ins(0)
    csbs(1)(0).io.outs(13) <> csbs(2)(0).io.ins(1)
    csbs(1)(0).io.outs(14) <> csbs(2)(0).io.ins(2)
    csbs(1)(0).io.outs(15) <> csbs(2)(0).io.ins(3)
    csbs(1)(0).io.outs(16) <> lcus(1)(0).io.controlIn(0)
    csbs(1)(0).io.outs(17) <> lcus(1)(0).io.controlIn(1)
    csbs(1)(0).io.outs(18) <> lcus(1)(0).io.controlIn(2)
    csbs(1)(0).io.outs(19) <> lcus(1)(0).io.controlIn(3)
    csbs(1)(0).io.outs(20) <> lcus(1)(0).io.controlIn(4)
    csbs(1)(0).io.outs(21) <> lcus(1)(0).io.controlIn(5)
    csbs(1)(0).io.outs(22) <> lcus(1)(0).io.controlIn(6)
    csbs(1)(0).io.outs(23) <> lcus(1)(0).io.controlIn(7)
    csbs(1)(0).io.outs(24) <> doneOuts(1)
    csbs(1)(1).io.outs(0) <> csbs(0)(1).io.ins(5)
    csbs(1)(1).io.outs(1) <> csbs(0)(1).io.ins(6)
    csbs(1)(1).io.outs(2) <> csbs(0)(1).io.ins(7)
    csbs(1)(1).io.outs(3) <> csbs(0)(1).io.ins(8)
    csbs(1)(1).io.outs(4) <> cus(0)(1).io.controlIn(4)
    csbs(1)(1).io.outs(5) <> cus(0)(1).io.controlIn(5)
    csbs(1)(1).io.outs(6) <> csbs(1)(2).io.ins(14)
    csbs(1)(1).io.outs(7) <> csbs(1)(2).io.ins(15)
    csbs(1)(1).io.outs(8) <> csbs(1)(2).io.ins(16)
    csbs(1)(1).io.outs(9) <> csbs(1)(2).io.ins(17)
    csbs(1)(1).io.outs(10) <> cus(1)(1).io.controlIn(6)
    csbs(1)(1).io.outs(11) <> cus(1)(1).io.controlIn(7)
    csbs(1)(1).io.outs(12) <> csbs(2)(1).io.ins(0)
    csbs(1)(1).io.outs(13) <> csbs(2)(1).io.ins(1)
    csbs(1)(1).io.outs(14) <> csbs(2)(1).io.ins(2)
    csbs(1)(1).io.outs(15) <> csbs(2)(1).io.ins(3)
    csbs(1)(1).io.outs(16) <> lcus(1)(1).io.controlIn(0)
    csbs(1)(1).io.outs(17) <> lcus(1)(1).io.controlIn(1)
    csbs(1)(1).io.outs(18) <> lcus(1)(1).io.controlIn(2)
    csbs(1)(1).io.outs(19) <> lcus(1)(1).io.controlIn(3)
    csbs(1)(1).io.outs(20) <> lcus(1)(1).io.controlIn(4)
    csbs(1)(1).io.outs(21) <> lcus(1)(1).io.controlIn(5)
    csbs(1)(1).io.outs(22) <> lcus(1)(1).io.controlIn(6)
    csbs(1)(1).io.outs(23) <> lcus(1)(1).io.controlIn(7)
    csbs(1)(1).io.outs(24) <> cus(1)(0).io.controlIn(0)
    csbs(1)(1).io.outs(25) <> cus(1)(0).io.controlIn(1)
    csbs(1)(1).io.outs(26) <> csbs(1)(0).io.ins(5)
    csbs(1)(1).io.outs(27) <> csbs(1)(0).io.ins(6)
    csbs(1)(1).io.outs(28) <> csbs(1)(0).io.ins(7)
    csbs(1)(1).io.outs(29) <> csbs(1)(0).io.ins(8)
    csbs(1)(1).io.outs(30) <> cus(0)(0).io.controlIn(2)
    csbs(1)(1).io.outs(31) <> cus(0)(0).io.controlIn(3)
    csbs(1)(2).io.outs(0) <> csbs(0)(2).io.ins(1)
    csbs(1)(2).io.outs(1) <> csbs(0)(2).io.ins(2)
    csbs(1)(2).io.outs(2) <> csbs(0)(2).io.ins(3)
    csbs(1)(2).io.outs(3) <> csbs(0)(2).io.ins(4)
    csbs(1)(2).io.outs(4) <> doneOuts(4)
    csbs(1)(2).io.outs(5) <> csbs(2)(2).io.ins(0)
    csbs(1)(2).io.outs(6) <> csbs(2)(2).io.ins(1)
    csbs(1)(2).io.outs(7) <> csbs(2)(2).io.ins(2)
    csbs(1)(2).io.outs(8) <> csbs(2)(2).io.ins(3)
    csbs(1)(2).io.outs(9) <> lcus(1)(2).io.controlIn(0)
    csbs(1)(2).io.outs(10) <> lcus(1)(2).io.controlIn(1)
    csbs(1)(2).io.outs(11) <> lcus(1)(2).io.controlIn(2)
    csbs(1)(2).io.outs(12) <> lcus(1)(2).io.controlIn(3)
    csbs(1)(2).io.outs(13) <> lcus(1)(2).io.controlIn(4)
  }
}
