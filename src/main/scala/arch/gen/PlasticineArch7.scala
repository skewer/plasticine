package plasticine.arch
import chisel3._
import chisel3.util._
import scala.collection.mutable.ListBuffer

trait PlasticineArch7 {
  self:PlasticineArch with Plasticine =>
  def connect7(cus:Array[Array[CU]], vsbs:Array[Array[VectorSwitch]], ssbs:Array[Array[ScalarSwitch]], csbs:Array[Array[ControlSwitch]]):Unit = {
    csbs(2)(0).io.outs(4) <> cus(1)(0).io.controlIn(4)
    csbs(2)(0).io.outs(5) <> cus(1)(0).io.controlIn(5)
    csbs(2)(0).io.outs(6) <> csbs(2)(1).io.ins(12)
    csbs(2)(0).io.outs(7) <> csbs(2)(1).io.ins(13)
    csbs(2)(0).io.outs(8) <> csbs(2)(1).io.ins(14)
    csbs(2)(0).io.outs(9) <> csbs(2)(1).io.ins(15)
    csbs(2)(0).io.outs(10) <> cus(2)(0).io.controlIn(0)
    csbs(2)(0).io.outs(11) <> cus(2)(0).io.controlIn(1)
    csbs(2)(0).io.outs(12) <> cus(2)(0).io.controlIn(2)
    csbs(2)(0).io.outs(13) <> cus(2)(0).io.controlIn(3)
    csbs(2)(0).io.outs(14) <> cus(2)(0).io.controlIn(4)
    csbs(2)(0).io.outs(15) <> cus(2)(0).io.controlIn(5)
    csbs(2)(0).io.outs(16) <> cus(2)(0).io.controlIn(6)
    csbs(2)(0).io.outs(17) <> cus(2)(0).io.controlIn(7)
    csbs(2)(0).io.outs(18) <> io.done
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
    csbs(2)(1).io.outs(10) <> cus(2)(1).io.controlIn(0)
    csbs(2)(1).io.outs(11) <> cus(2)(1).io.controlIn(1)
    csbs(2)(1).io.outs(12) <> cus(2)(1).io.controlIn(2)
    csbs(2)(1).io.outs(13) <> cus(2)(1).io.controlIn(3)
    csbs(2)(1).io.outs(14) <> cus(2)(1).io.controlIn(4)
    csbs(2)(1).io.outs(15) <> cus(2)(1).io.controlIn(5)
    csbs(2)(1).io.outs(16) <> cus(2)(1).io.controlIn(6)
    csbs(2)(1).io.outs(17) <> cus(2)(1).io.controlIn(7)
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
    csbs(2)(2).io.outs(4) <> io.done
    csbs(2)(2).io.outs(5) <> cus(2)(2).io.controlIn(0)
    csbs(2)(2).io.outs(6) <> cus(2)(2).io.controlIn(1)
    csbs(2)(2).io.outs(7) <> cus(2)(2).io.controlIn(2)
    csbs(2)(2).io.outs(8) <> cus(2)(2).io.controlIn(3)
    csbs(2)(2).io.outs(9) <> cus(2)(2).io.controlIn(4)
    csbs(2)(2).io.outs(10) <> cus(2)(2).io.controlIn(5)
    csbs(2)(2).io.outs(11) <> cus(2)(2).io.controlIn(6)
    csbs(2)(2).io.outs(12) <> cus(2)(2).io.controlIn(7)
    csbs(2)(2).io.outs(13) <> csbs(2)(1).io.ins(5)
    csbs(2)(2).io.outs(14) <> csbs(2)(1).io.ins(6)
    csbs(2)(2).io.outs(15) <> csbs(2)(1).io.ins(7)
    csbs(2)(2).io.outs(16) <> csbs(2)(1).io.ins(8)
    csbs(2)(2).io.outs(17) <> cus(1)(1).io.controlIn(2)
    csbs(2)(2).io.outs(18) <> cus(1)(1).io.controlIn(3)
  }
}
