package plasticine.arch
import chisel3._
import chisel3.util._
import plasticine.templates.MuxN
import scala.language.reflectiveCalls
import scala.collection.mutable.ListBuffer

trait PlasticineArch5 extends PlasticineArch4 {
  def connect5(io:PlasticineIO, argOutMuxIns:Array[Array[DecoupledIO[UInt]]], doneOuts:Array[Bool], cus:Array[Array[CU]], vsbs:Array[Array[VectorSwitch]], ssbs:Array[Array[ScalarSwitch]], csbs:Array[Array[ControlSwitch]], lcus:Array[Array[SwitchCU]]):Unit = {
    cus(1)(1).io.controlOut(1) <> csbs(2)(2).io.ins(13)
    cus(1)(1).io.controlOut(2) <> csbs(2)(1).io.ins(4)
    cus(1)(1).io.controlOut(3) <> csbs(1)(1).io.ins(9)
    cus(0)(1).io.controlOut(0) <> csbs(0)(2).io.ins(9)
    cus(0)(1).io.controlOut(1) <> csbs(1)(2).io.ins(18)
    cus(0)(1).io.controlOut(2) <> csbs(1)(1).io.ins(4)
    cus(0)(1).io.controlOut(3) <> csbs(0)(1).io.ins(4)
    cus(1)(0).io.controlOut(0) <> csbs(1)(1).io.ins(18)
    cus(1)(0).io.controlOut(1) <> csbs(2)(1).io.ins(17)
    cus(1)(0).io.controlOut(2) <> csbs(2)(0).io.ins(4)
    cus(1)(0).io.controlOut(3) <> csbs(1)(0).io.ins(9)
    lcus(0)(0).io.controlOut(0) <> csbs(0)(0).io.ins(9)
    lcus(0)(0).io.controlOut(1) <> csbs(0)(0).io.ins(10)
    lcus(0)(0).io.controlOut(2) <> csbs(0)(0).io.ins(11)
    lcus(0)(0).io.controlOut(3) <> csbs(0)(0).io.ins(12)
    lcus(0)(1).io.controlOut(0) <> csbs(0)(1).io.ins(9)
    lcus(0)(1).io.controlOut(1) <> csbs(0)(1).io.ins(10)
    lcus(0)(1).io.controlOut(2) <> csbs(0)(1).io.ins(11)
    lcus(0)(1).io.controlOut(3) <> csbs(0)(1).io.ins(12)
    lcus(0)(2).io.controlOut(0) <> csbs(0)(2).io.ins(5)
    lcus(0)(2).io.controlOut(1) <> csbs(0)(2).io.ins(6)
    lcus(0)(2).io.controlOut(2) <> csbs(0)(2).io.ins(7)
    lcus(0)(2).io.controlOut(3) <> csbs(0)(2).io.ins(8)
    lcus(1)(0).io.controlOut(0) <> csbs(1)(0).io.ins(14)
    lcus(1)(0).io.controlOut(1) <> csbs(1)(0).io.ins(15)
    lcus(1)(0).io.controlOut(2) <> csbs(1)(0).io.ins(16)
    lcus(1)(0).io.controlOut(3) <> csbs(1)(0).io.ins(17)
    lcus(1)(1).io.controlOut(0) <> csbs(1)(1).io.ins(14)
    lcus(1)(1).io.controlOut(1) <> csbs(1)(1).io.ins(15)
    lcus(1)(1).io.controlOut(2) <> csbs(1)(1).io.ins(16)
    lcus(1)(1).io.controlOut(3) <> csbs(1)(1).io.ins(17)
    lcus(1)(2).io.controlOut(0) <> csbs(1)(2).io.ins(9)
    lcus(1)(2).io.controlOut(1) <> csbs(1)(2).io.ins(10)
    lcus(1)(2).io.controlOut(2) <> csbs(1)(2).io.ins(11)
    lcus(1)(2).io.controlOut(3) <> csbs(1)(2).io.ins(12)
    lcus(2)(0).io.controlOut(0) <> csbs(2)(0).io.ins(9)
    lcus(2)(0).io.controlOut(1) <> csbs(2)(0).io.ins(10)
    lcus(2)(0).io.controlOut(2) <> csbs(2)(0).io.ins(11)
    lcus(2)(0).io.controlOut(3) <> csbs(2)(0).io.ins(12)
    lcus(2)(1).io.controlOut(0) <> csbs(2)(1).io.ins(9)
    lcus(2)(1).io.controlOut(1) <> csbs(2)(1).io.ins(10)
    lcus(2)(1).io.controlOut(2) <> csbs(2)(1).io.ins(11)
    lcus(2)(1).io.controlOut(3) <> csbs(2)(1).io.ins(12)
    lcus(2)(2).io.controlOut(0) <> csbs(2)(2).io.ins(5)
    lcus(2)(2).io.controlOut(1) <> csbs(2)(2).io.ins(6)
    lcus(2)(2).io.controlOut(2) <> csbs(2)(2).io.ins(7)
    lcus(2)(2).io.controlOut(3) <> csbs(2)(2).io.ins(8)
    csbs(0)(0).io.outs(0) <> csbs(0)(1).io.ins(14)
    csbs(0)(0).io.outs(1) <> csbs(0)(1).io.ins(15)
    csbs(0)(0).io.outs(2) <> csbs(0)(1).io.ins(16)
    csbs(0)(0).io.outs(3) <> csbs(0)(1).io.ins(17)
    csbs(0)(0).io.outs(4) <> cus(0)(0).io.controlIn(6)
    csbs(0)(0).io.outs(5) <> cus(0)(0).io.controlIn(7)
    csbs(0)(0).io.outs(6) <> csbs(1)(0).io.ins(0)
    csbs(0)(0).io.outs(7) <> csbs(1)(0).io.ins(1)
    csbs(0)(0).io.outs(8) <> csbs(1)(0).io.ins(2)
    csbs(0)(0).io.outs(9) <> csbs(1)(0).io.ins(3)
    csbs(0)(0).io.outs(10) <> lcus(0)(0).io.controlIn(0)
    csbs(0)(0).io.outs(11) <> lcus(0)(0).io.controlIn(1)
    csbs(0)(0).io.outs(12) <> lcus(0)(0).io.controlIn(2)
    csbs(0)(0).io.outs(13) <> lcus(0)(0).io.controlIn(3)
    csbs(0)(0).io.outs(14) <> lcus(0)(0).io.controlIn(4)
    csbs(0)(0).io.outs(15) <> lcus(0)(0).io.controlIn(5)
    csbs(0)(0).io.outs(16) <> lcus(0)(0).io.controlIn(6)
    csbs(0)(0).io.outs(17) <> lcus(0)(0).io.controlIn(7)
    csbs(0)(0).io.outs(18) <> doneOuts(0)
    csbs(0)(1).io.outs(0) <> csbs(0)(2).io.ins(10)
    csbs(0)(1).io.outs(1) <> csbs(0)(2).io.ins(11)
    csbs(0)(1).io.outs(2) <> csbs(0)(2).io.ins(12)
    csbs(0)(1).io.outs(3) <> csbs(0)(2).io.ins(13)
    csbs(0)(1).io.outs(4) <> cus(0)(1).io.controlIn(6)
    csbs(0)(1).io.outs(5) <> cus(0)(1).io.controlIn(7)
    csbs(0)(1).io.outs(6) <> csbs(1)(1).io.ins(0)
    csbs(0)(1).io.outs(7) <> csbs(1)(1).io.ins(1)
    csbs(0)(1).io.outs(8) <> csbs(1)(1).io.ins(2)
    csbs(0)(1).io.outs(9) <> csbs(1)(1).io.ins(3)
    csbs(0)(1).io.outs(10) <> lcus(0)(1).io.controlIn(0)
    csbs(0)(1).io.outs(11) <> lcus(0)(1).io.controlIn(1)
    csbs(0)(1).io.outs(12) <> lcus(0)(1).io.controlIn(2)
    csbs(0)(1).io.outs(13) <> lcus(0)(1).io.controlIn(3)
    csbs(0)(1).io.outs(14) <> lcus(0)(1).io.controlIn(4)
    csbs(0)(1).io.outs(15) <> lcus(0)(1).io.controlIn(5)
    csbs(0)(1).io.outs(16) <> lcus(0)(1).io.controlIn(6)
    csbs(0)(1).io.outs(17) <> lcus(0)(1).io.controlIn(7)
    csbs(0)(1).io.outs(18) <> cus(0)(0).io.controlIn(0)
    csbs(0)(1).io.outs(19) <> cus(0)(0).io.controlIn(1)
    csbs(0)(1).io.outs(20) <> csbs(0)(0).io.ins(0)
    csbs(0)(1).io.outs(21) <> csbs(0)(0).io.ins(1)
    csbs(0)(1).io.outs(22) <> csbs(0)(0).io.ins(2)
    csbs(0)(1).io.outs(23) <> csbs(0)(0).io.ins(3)
  }
}
