package plasticine.arch
import chisel3._
import chisel3.util._
import templates.MuxN
import scala.language.reflectiveCalls
import scala.collection.mutable.ListBuffer

trait PlasticineArch10 extends PlasticineArch9 {
  def connect10(io:PlasticineIO, argOutMuxIns:Array[Array[DecoupledIO[UInt]]], doneOuts:Array[Bool], cus:Array[Array[CU]], scus:Array[Array[ScalarCU]] , mcs:Array[Array[MemoryChannel]], vsbs:Array[Array[VectorSwitch]], ssbs:Array[Array[ScalarSwitch]], csbs:Array[Array[ControlSwitch]], lcus:Array[Array[SwitchCU]]):Unit = {
    csbs(0)(1).io.outs(13) <> csbs(0)(2).io.ins(32)
    csbs(0)(1).io.outs(14) <> csbs(0)(2).io.ins(33)
    csbs(0)(1).io.outs(15) <> csbs(0)(2).io.ins(34)
    csbs(0)(1).io.outs(16) <> csbs(0)(2).io.ins(35)
    csbs(0)(1).io.outs(17) <> csbs(0)(2).io.ins(36)
    csbs(0)(1).io.outs(18) <> csbs(0)(2).io.ins(37)
    csbs(0)(1).io.outs(19) <> csbs(0)(2).io.ins(38)
    csbs(0)(1).io.outs(20) <> csbs(0)(2).io.ins(39)
    csbs(0)(1).io.outs(21) <> csbs(0)(2).io.ins(40)
    csbs(0)(1).io.outs(22) <> csbs(0)(2).io.ins(41)
    csbs(0)(1).io.outs(23) <> csbs(0)(2).io.ins(42)
    csbs(0)(1).io.outs(24) <> csbs(0)(2).io.ins(43)
    csbs(0)(1).io.outs(25) <> csbs(0)(2).io.ins(44)
    csbs(0)(1).io.outs(26) <> csbs(0)(2).io.ins(45)
    csbs(0)(1).io.outs(27) <> csbs(0)(2).io.ins(46)
    csbs(0)(1).io.outs(28) <> csbs(0)(2).io.ins(47)
    csbs(0)(1).io.outs(29) <> cus(0)(1).io.controlIn(12)
    csbs(0)(1).io.outs(30) <> cus(0)(1).io.controlIn(13)
    csbs(0)(1).io.outs(31) <> cus(0)(1).io.controlIn(14)
    csbs(0)(1).io.outs(32) <> cus(0)(1).io.controlIn(15)
    csbs(0)(1).io.outs(33) <> csbs(1)(1).io.ins(0)
    csbs(0)(1).io.outs(34) <> csbs(1)(1).io.ins(1)
    csbs(0)(1).io.outs(35) <> csbs(1)(1).io.ins(2)
    csbs(0)(1).io.outs(36) <> csbs(1)(1).io.ins(3)
    csbs(0)(1).io.outs(37) <> csbs(1)(1).io.ins(4)
    csbs(0)(1).io.outs(38) <> csbs(1)(1).io.ins(5)
    csbs(0)(1).io.outs(39) <> csbs(1)(1).io.ins(6)
    csbs(0)(1).io.outs(40) <> csbs(1)(1).io.ins(7)
    csbs(0)(1).io.outs(41) <> csbs(1)(1).io.ins(8)
    csbs(0)(1).io.outs(42) <> csbs(1)(1).io.ins(9)
    csbs(0)(1).io.outs(43) <> csbs(1)(1).io.ins(10)
    csbs(0)(1).io.outs(44) <> csbs(1)(1).io.ins(11)
    csbs(0)(1).io.outs(45) <> lcus(0)(1).io.controlIn(0)
    csbs(0)(1).io.outs(46) <> lcus(0)(1).io.controlIn(1)
    csbs(0)(1).io.outs(47) <> lcus(0)(1).io.controlIn(2)
    csbs(0)(1).io.outs(48) <> lcus(0)(1).io.controlIn(3)
    csbs(0)(1).io.outs(49) <> lcus(0)(1).io.controlIn(4)
    csbs(0)(1).io.outs(50) <> lcus(0)(1).io.controlIn(5)
    csbs(0)(1).io.outs(51) <> cus(0)(0).io.controlIn(0)
    csbs(0)(1).io.outs(52) <> cus(0)(0).io.controlIn(1)
    csbs(0)(1).io.outs(53) <> cus(0)(0).io.controlIn(2)
    csbs(0)(1).io.outs(54) <> cus(0)(0).io.controlIn(3)
    csbs(0)(1).io.outs(55) <> csbs(0)(0).io.ins(5)
    csbs(0)(1).io.outs(56) <> csbs(0)(0).io.ins(6)
    csbs(0)(1).io.outs(57) <> csbs(0)(0).io.ins(7)
    csbs(0)(1).io.outs(58) <> csbs(0)(0).io.ins(8)
    csbs(0)(1).io.outs(59) <> csbs(0)(0).io.ins(9)
    csbs(0)(1).io.outs(60) <> csbs(0)(0).io.ins(10)
    csbs(0)(1).io.outs(61) <> csbs(0)(0).io.ins(11)
    csbs(0)(1).io.outs(62) <> csbs(0)(0).io.ins(12)
    csbs(0)(1).io.outs(63) <> csbs(0)(0).io.ins(13)
    csbs(0)(1).io.outs(64) <> csbs(0)(0).io.ins(14)
    csbs(0)(1).io.outs(65) <> csbs(0)(0).io.ins(15)
    csbs(0)(1).io.outs(66) <> csbs(0)(0).io.ins(16)
    csbs(0)(1).io.outs(67) <> csbs(0)(0).io.ins(17)
    csbs(0)(1).io.outs(68) <> csbs(0)(0).io.ins(18)
    csbs(0)(1).io.outs(69) <> csbs(0)(0).io.ins(19)
    csbs(0)(1).io.outs(70) <> csbs(0)(0).io.ins(20)
    csbs(0)(1).io.outs(71) <> csbs(0)(0).io.ins(21)
    csbs(0)(1).io.outs(72) <> csbs(0)(0).io.ins(22)
    csbs(0)(1).io.outs(73) <> csbs(0)(0).io.ins(23)
    csbs(0)(1).io.outs(74) <> csbs(0)(0).io.ins(24)
    csbs(0)(1).io.outs(75) <> csbs(0)(0).io.ins(25)
    csbs(0)(1).io.outs(76) <> csbs(0)(0).io.ins(26)
    csbs(0)(1).io.outs(77) <> csbs(0)(0).io.ins(27)
    csbs(0)(1).io.outs(78) <> csbs(0)(0).io.ins(28)
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
    csbs(0)(2).io.outs(10) <> csbs(1)(2).io.ins(4)
    csbs(0)(2).io.outs(11) <> csbs(1)(2).io.ins(5)
    csbs(0)(2).io.outs(12) <> csbs(1)(2).io.ins(6)
    csbs(0)(2).io.outs(13) <> csbs(1)(2).io.ins(7)
    csbs(0)(2).io.outs(14) <> csbs(1)(2).io.ins(8)
    csbs(0)(2).io.outs(15) <> csbs(1)(2).io.ins(9)
    csbs(0)(2).io.outs(16) <> csbs(1)(2).io.ins(10)
    csbs(0)(2).io.outs(17) <> csbs(1)(2).io.ins(11)
    csbs(0)(2).io.outs(18) <> lcus(0)(2).io.controlIn(0)
    csbs(0)(2).io.outs(19) <> lcus(0)(2).io.controlIn(1)
    csbs(0)(2).io.outs(20) <> lcus(0)(2).io.controlIn(2)
    csbs(0)(2).io.outs(21) <> lcus(0)(2).io.controlIn(3)
    csbs(0)(2).io.outs(22) <> lcus(0)(2).io.controlIn(4)
    csbs(0)(2).io.outs(23) <> lcus(0)(2).io.controlIn(5)
  }
}
