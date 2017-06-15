package plasticine.arch
import chisel3._
import chisel3.util._
import plasticine.templates.MuxN
import scala.language.reflectiveCalls
import scala.collection.mutable.ListBuffer

trait PlasticineArch7 extends PlasticineArch6  {
  def connect7(io:PlasticineIO, argOutMuxIns:Array[Array[DecoupledIO[UInt]]], doneOuts:Array[Bool], cus:Array[Array[CU]], scus:Array[Array[ScalarCU]] , mcs:Array[Array[MemoryChannel]], vsbs:Array[Array[VectorSwitch]], ssbs:Array[Array[ScalarSwitch]], csbs:Array[Array[ControlSwitch]], lcus:Array[Array[SwitchCU]]):Unit =  {
    mcs(1)(2).io.plasticine.controlOut(0) <> csbs(2)(2).io.ins(11)
    mcs(1)(2).io.plasticine.controlOut(1) <> scus(1)(2).io.controlIn(4)
    mcs(1)(2).io.plasticine.controlOut(2) <> scus(1)(2).io.controlIn(5)
    csbs(0)(0).io.outs(0) <> scus(0)(0).io.controlIn(0)
    csbs(0)(0).io.outs(1) <> scus(0)(0).io.controlIn(1)
    csbs(0)(0).io.outs(2) <> scus(0)(0).io.controlIn(2)
    csbs(0)(0).io.outs(3) <> scus(0)(0).io.controlIn(3)
    csbs(0)(0).io.outs(4) <> mcs(0)(0).io.plasticine.controlIn(0)
    csbs(0)(0).io.outs(5) <> csbs(0)(1).io.ins(21)
    csbs(0)(0).io.outs(6) <> csbs(0)(1).io.ins(22)
    csbs(0)(0).io.outs(7) <> csbs(0)(1).io.ins(23)
    csbs(0)(0).io.outs(8) <> csbs(0)(1).io.ins(24)
    csbs(0)(0).io.outs(9) <> csbs(0)(1).io.ins(25)
    csbs(0)(0).io.outs(10) <> csbs(0)(1).io.ins(26)
    csbs(0)(0).io.outs(11) <> csbs(0)(1).io.ins(27)
    csbs(0)(0).io.outs(12) <> csbs(0)(1).io.ins(28)
    csbs(0)(0).io.outs(13) <> cus(0)(0).io.controlIn(3)
    csbs(0)(0).io.outs(14) <> csbs(1)(0).io.ins(0)
    csbs(0)(0).io.outs(15) <> csbs(1)(0).io.ins(1)
    csbs(0)(0).io.outs(16) <> csbs(1)(0).io.ins(2)
    csbs(0)(0).io.outs(17) <> csbs(1)(0).io.ins(3)
    csbs(0)(0).io.outs(18) <> lcus(0)(0).io.controlIn(0)
    csbs(0)(0).io.outs(19) <> lcus(0)(0).io.controlIn(1)
    csbs(0)(0).io.outs(20) <> lcus(0)(0).io.controlIn(2)
    csbs(0)(0).io.outs(21) <> lcus(0)(0).io.controlIn(3)
    csbs(0)(0).io.outs(22) <> doneOuts(0)
    csbs(0)(1).io.outs(0) <> scus(0)(1).io.controlIn(0)
    csbs(0)(1).io.outs(1) <> scus(0)(1).io.controlIn(1)
    csbs(0)(1).io.outs(2) <> scus(0)(1).io.controlIn(2)
    csbs(0)(1).io.outs(3) <> scus(0)(1).io.controlIn(3)
    csbs(0)(1).io.outs(4) <> mcs(0)(1).io.plasticine.controlIn(0)
    csbs(0)(1).io.outs(5) <> csbs(0)(2).io.ins(13)
    csbs(0)(1).io.outs(6) <> csbs(0)(2).io.ins(14)
    csbs(0)(1).io.outs(7) <> csbs(0)(2).io.ins(15)
    csbs(0)(1).io.outs(8) <> csbs(0)(2).io.ins(16)
    csbs(0)(1).io.outs(9) <> csbs(0)(2).io.ins(17)
    csbs(0)(1).io.outs(10) <> csbs(0)(2).io.ins(18)
    csbs(0)(1).io.outs(11) <> csbs(0)(2).io.ins(19)
    csbs(0)(1).io.outs(12) <> csbs(0)(2).io.ins(20)
    csbs(0)(1).io.outs(13) <> cus(0)(1).io.controlIn(3)
    csbs(0)(1).io.outs(14) <> csbs(1)(1).io.ins(0)
    csbs(0)(1).io.outs(15) <> csbs(1)(1).io.ins(1)
    csbs(0)(1).io.outs(16) <> csbs(1)(1).io.ins(2)
    csbs(0)(1).io.outs(17) <> csbs(1)(1).io.ins(3)
    csbs(0)(1).io.outs(18) <> lcus(0)(1).io.controlIn(0)
    csbs(0)(1).io.outs(19) <> lcus(0)(1).io.controlIn(1)
    csbs(0)(1).io.outs(20) <> lcus(0)(1).io.controlIn(2)
    csbs(0)(1).io.outs(21) <> lcus(0)(1).io.controlIn(3)
    csbs(0)(1).io.outs(22) <> cus(0)(0).io.controlIn(0)
    csbs(0)(1).io.outs(23) <> csbs(0)(0).io.ins(5)
    csbs(0)(1).io.outs(24) <> csbs(0)(0).io.ins(6)
    csbs(0)(1).io.outs(25) <> csbs(0)(0).io.ins(7)
    csbs(0)(1).io.outs(26) <> csbs(0)(0).io.ins(8)
    csbs(0)(1).io.outs(27) <> csbs(0)(0).io.ins(9)
    csbs(0)(1).io.outs(28) <> csbs(0)(0).io.ins(10)
    csbs(0)(1).io.outs(29) <> csbs(0)(0).io.ins(11)
    csbs(0)(1).io.outs(30) <> csbs(0)(0).io.ins(12)
    csbs(0)(2).io.outs(0) <> scus(0)(2).io.controlIn(0)
    csbs(0)(2).io.outs(1) <> scus(0)(2).io.controlIn(1)
    csbs(0)(2).io.outs(2) <> scus(0)(2).io.controlIn(2)
    csbs(0)(2).io.outs(3) <> scus(0)(2).io.controlIn(3)
    csbs(0)(2).io.outs(4) <> mcs(0)(2).io.plasticine.controlIn(0)
    csbs(0)(2).io.outs(5) <> doneOuts(3)
    csbs(0)(2).io.outs(6) <> csbs(1)(2).io.ins(0)
    csbs(0)(2).io.outs(7) <> csbs(1)(2).io.ins(1)
    csbs(0)(2).io.outs(8) <> csbs(1)(2).io.ins(2)
    csbs(0)(2).io.outs(9) <> csbs(1)(2).io.ins(3)
    csbs(0)(2).io.outs(10) <> lcus(0)(2).io.controlIn(0)
    csbs(0)(2).io.outs(11) <> lcus(0)(2).io.controlIn(1)
    csbs(0)(2).io.outs(12) <> lcus(0)(2).io.controlIn(2)
    csbs(0)(2).io.outs(13) <> lcus(0)(2).io.controlIn(3)
    csbs(0)(2).io.outs(14) <> cus(0)(1).io.controlIn(0)
    csbs(0)(2).io.outs(15) <> csbs(0)(1).io.ins(5)
    csbs(0)(2).io.outs(16) <> csbs(0)(1).io.ins(6)
    csbs(0)(2).io.outs(17) <> csbs(0)(1).io.ins(7)
    csbs(0)(2).io.outs(18) <> csbs(0)(1).io.ins(8)
    csbs(0)(2).io.outs(19) <> csbs(0)(1).io.ins(9)
    csbs(0)(2).io.outs(20) <> csbs(0)(1).io.ins(10)
    csbs(0)(2).io.outs(21) <> csbs(0)(1).io.ins(11)
    csbs(0)(2).io.outs(22) <> csbs(0)(1).io.ins(12)
    csbs(1)(0).io.outs(0) <> csbs(0)(0).io.ins(14)
    csbs(1)(0).io.outs(1) <> csbs(0)(0).io.ins(15)
    csbs(1)(0).io.outs(2) <> csbs(0)(0).io.ins(16)
    csbs(1)(0).io.outs(3) <> csbs(0)(0).io.ins(17)
    csbs(1)(0).io.outs(4) <> cus(0)(0).io.controlIn(2)
    csbs(1)(0).io.outs(5) <> csbs(1)(1).io.ins(17)
    csbs(1)(0).io.outs(6) <> csbs(1)(1).io.ins(18)
    csbs(1)(0).io.outs(7) <> csbs(1)(1).io.ins(19)
    csbs(1)(0).io.outs(8) <> csbs(1)(1).io.ins(20)
    csbs(1)(0).io.outs(9) <> cus(1)(0).io.controlIn(3)
    csbs(1)(0).io.outs(10) <> csbs(2)(0).io.ins(0)
    csbs(1)(0).io.outs(11) <> csbs(2)(0).io.ins(1)
  }
}
