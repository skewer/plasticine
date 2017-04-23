package plasticine.arch
import chisel3._
import chisel3.util._
import plasticine.templates.MuxN
import scala.language.reflectiveCalls
import scala.collection.mutable.ListBuffer

trait PlasticineArch7 extends PlasticineArch6 {
  def connect7(io:PlasticineIO, argOutMuxIns:Array[Array[DecoupledIO[UInt]]], cus:Array[Array[CU]], vsbs:Array[Array[VectorSwitch]], ssbs:Array[Array[ScalarSwitch]], csbs:Array[Array[ControlSwitch]], lcus:Array[Array[SwitchCU]]):Unit = {
    csbs(1)(2).io.outs(23) <> cus(0)(1).io.controlIn(2)
    csbs(1)(2).io.outs(24) <> cus(0)(1).io.controlIn(3)
    csbs(2)(0).io.outs(0) <> csbs(1)(0).io.ins(10)
    csbs(2)(0).io.outs(1) <> csbs(1)(0).io.ins(11)
    csbs(2)(0).io.outs(2) <> csbs(1)(0).io.ins(12)
    csbs(2)(0).io.outs(3) <> csbs(1)(0).io.ins(13)
    csbs(2)(0).io.outs(4) <> cus(1)(0).io.controlIn(4)
    csbs(2)(0).io.outs(5) <> cus(1)(0).io.controlIn(5)
    csbs(2)(0).io.outs(6) <> csbs(2)(1).io.ins(12)
    csbs(2)(0).io.outs(7) <> csbs(2)(1).io.ins(13)
    csbs(2)(0).io.outs(8) <> csbs(2)(1).io.ins(14)
    csbs(2)(0).io.outs(9) <> csbs(2)(1).io.ins(15)
    csbs(2)(0).io.outs(10) <> lcus(2)(0).io.controlIn(0)
    csbs(2)(0).io.outs(11) <> lcus(2)(0).io.controlIn(1)
    csbs(2)(0).io.outs(12) <> lcus(2)(0).io.controlIn(2)
    csbs(2)(0).io.outs(13) <> lcus(2)(0).io.controlIn(3)
    csbs(2)(0).io.outs(14) <> lcus(2)(0).io.controlIn(4)
    csbs(2)(0).io.outs(15) <> lcus(2)(0).io.controlIn(5)
    csbs(2)(0).io.outs(16) <> lcus(2)(0).io.controlIn(6)
    csbs(2)(0).io.outs(17) <> lcus(2)(0).io.controlIn(7)
//    csbs(2)(0).io.outs(18) <> io.done
    csbs(2)(1).io.outs(0) <> csbs(1)(1).io.ins(10)
    csbs(2)(1).io.outs(1) <> csbs(1)(1).io.ins(11)
    csbs(2)(1).io.outs(2) <> csbs(1)(1).io.ins(12)
    csbs(2)(1).io.outs(3) <> csbs(1)(1).io.ins(13)
    csbs(2)(1).io.outs(4) <> cus(1)(1).io.controlIn(4)
    csbs(2)(1).io.outs(5) <> cus(1)(1).io.controlIn(5)
    csbs(2)(1).io.outs(6) <> csbs(2)(2).io.ins(8)
    csbs(2)(1).io.outs(7) <> csbs(2)(2).io.ins(9)
    csbs(2)(1).io.outs(8) <> csbs(2)(2).io.ins(10)
    csbs(2)(1).io.outs(9) <> csbs(2)(2).io.ins(11)
    csbs(2)(1).io.outs(10) <> lcus(2)(1).io.controlIn(0)
    csbs(2)(1).io.outs(11) <> lcus(2)(1).io.controlIn(1)
    csbs(2)(1).io.outs(12) <> lcus(2)(1).io.controlIn(2)
    csbs(2)(1).io.outs(13) <> lcus(2)(1).io.controlIn(3)
    csbs(2)(1).io.outs(14) <> lcus(2)(1).io.controlIn(4)
    csbs(2)(1).io.outs(15) <> lcus(2)(1).io.controlIn(5)
    csbs(2)(1).io.outs(16) <> lcus(2)(1).io.controlIn(6)
    csbs(2)(1).io.outs(17) <> lcus(2)(1).io.controlIn(7)
    csbs(2)(1).io.outs(18) <> csbs(2)(0).io.ins(5)
    csbs(2)(1).io.outs(19) <> csbs(2)(0).io.ins(6)
    csbs(2)(1).io.outs(20) <> csbs(2)(0).io.ins(7)
    csbs(2)(1).io.outs(21) <> csbs(2)(0).io.ins(8)
    csbs(2)(1).io.outs(22) <> cus(1)(0).io.controlIn(2)
    csbs(2)(1).io.outs(23) <> cus(1)(0).io.controlIn(3)
    csbs(2)(2).io.outs(0) <> csbs(1)(2).io.ins(5)
    csbs(2)(2).io.outs(1) <> csbs(1)(2).io.ins(6)
    csbs(2)(2).io.outs(2) <> csbs(1)(2).io.ins(7)
    csbs(2)(2).io.outs(3) <> csbs(1)(2).io.ins(8)
//    csbs(2)(2).io.outs(4) <> io.done
    csbs(2)(2).io.outs(5) <> lcus(2)(2).io.controlIn(0)
    csbs(2)(2).io.outs(6) <> lcus(2)(2).io.controlIn(1)
    csbs(2)(2).io.outs(7) <> lcus(2)(2).io.controlIn(2)
    csbs(2)(2).io.outs(8) <> lcus(2)(2).io.controlIn(3)
    csbs(2)(2).io.outs(9) <> lcus(2)(2).io.controlIn(4)
    csbs(2)(2).io.outs(10) <> lcus(2)(2).io.controlIn(5)
    csbs(2)(2).io.outs(11) <> lcus(2)(2).io.controlIn(6)
    csbs(2)(2).io.outs(12) <> lcus(2)(2).io.controlIn(7)
    csbs(2)(2).io.outs(13) <> csbs(2)(1).io.ins(5)
    csbs(2)(2).io.outs(14) <> csbs(2)(1).io.ins(6)
    csbs(2)(2).io.outs(15) <> csbs(2)(1).io.ins(7)
    csbs(2)(2).io.outs(16) <> csbs(2)(1).io.ins(8)
    csbs(2)(2).io.outs(17) <> cus(1)(1).io.controlIn(2)
    csbs(2)(2).io.outs(18) <> cus(1)(1).io.controlIn(3)
  }
}
