package plasticine.arch
import chisel3._
import chisel3.util._
import plasticine.templates.MuxN
import scala.language.reflectiveCalls
import scala.collection.mutable.ListBuffer

trait PlasticineArch1 {
  def connect1(io:PlasticineIO, argOutMuxIns:Array[Array[DecoupledIO[UInt]]], doneOuts:Array[Bool], cus:Array[Array[CU]], vsbs:Array[Array[VectorSwitch]], ssbs:Array[Array[ScalarSwitch]], csbs:Array[Array[ControlSwitch]], lcus:Array[Array[SwitchCU]]):Unit = {
    // VectorNetwork Connection
    cus(0)(0).io.vecOut(0) <> vsbs(0)(1).io.ins(9)
    cus(0)(0).io.vecOut(1) <> vsbs(1)(1).io.ins(19)
    cus(0)(0).io.vecOut(2) <> vsbs(1)(0).io.ins(4)
    cus(0)(0).io.vecOut(3) <> vsbs(0)(0).io.ins(4)
    cus(1)(1).io.vecOut(0) <> vsbs(1)(2).io.ins(8)
    cus(1)(1).io.vecOut(1) <> vsbs(2)(2).io.ins(8)
    cus(1)(1).io.vecOut(2) <> vsbs(2)(1).io.ins(4)
    cus(1)(1).io.vecOut(3) <> vsbs(1)(1).io.ins(9)
    cus(0)(1).io.vecOut(0) <> vsbs(0)(2).io.ins(4)
    cus(0)(1).io.vecOut(1) <> vsbs(1)(2).io.ins(13)
    cus(0)(1).io.vecOut(2) <> vsbs(1)(1).io.ins(4)
    cus(0)(1).io.vecOut(3) <> vsbs(0)(1).io.ins(4)
    cus(1)(0).io.vecOut(0) <> vsbs(1)(1).io.ins(14)
    cus(1)(0).io.vecOut(1) <> vsbs(2)(1).io.ins(13)
    cus(1)(0).io.vecOut(2) <> vsbs(2)(0).io.ins(4)
    cus(1)(0).io.vecOut(3) <> vsbs(1)(0).io.ins(9)
    vsbs(0)(0).io.outs(0) <> vsbs(0)(1).io.ins(10)
    vsbs(0)(0).io.outs(1) <> vsbs(0)(1).io.ins(11)
    vsbs(0)(0).io.outs(2) <> vsbs(0)(1).io.ins(12)
    vsbs(0)(0).io.outs(3) <> vsbs(0)(1).io.ins(13)
    vsbs(0)(0).io.outs(4) <> cus(0)(0).io.vecIn(3)
    vsbs(0)(0).io.outs(5) <> vsbs(1)(0).io.ins(0)
    vsbs(0)(0).io.outs(6) <> vsbs(1)(0).io.ins(1)
    vsbs(0)(0).io.outs(7) <> vsbs(1)(0).io.ins(2)
    vsbs(0)(0).io.outs(8) <> vsbs(1)(0).io.ins(3)
    vsbs(0)(1).io.outs(0) <> vsbs(0)(2).io.ins(5)
    vsbs(0)(1).io.outs(1) <> vsbs(0)(2).io.ins(6)
    vsbs(0)(1).io.outs(2) <> vsbs(0)(2).io.ins(7)
    vsbs(0)(1).io.outs(3) <> vsbs(0)(2).io.ins(8)
    vsbs(0)(1).io.outs(4) <> cus(0)(1).io.vecIn(3)
    vsbs(0)(1).io.outs(5) <> vsbs(1)(1).io.ins(0)
    vsbs(0)(1).io.outs(6) <> vsbs(1)(1).io.ins(1)
    vsbs(0)(1).io.outs(7) <> vsbs(1)(1).io.ins(2)
    vsbs(0)(1).io.outs(8) <> vsbs(1)(1).io.ins(3)
    vsbs(0)(1).io.outs(9) <> cus(0)(0).io.vecIn(0)
    vsbs(0)(1).io.outs(10) <> vsbs(0)(0).io.ins(0)
    vsbs(0)(1).io.outs(11) <> vsbs(0)(0).io.ins(1)
    vsbs(0)(1).io.outs(12) <> vsbs(0)(0).io.ins(2)
    vsbs(0)(1).io.outs(13) <> vsbs(0)(0).io.ins(3)
    vsbs(0)(2).io.outs(0) <> vsbs(1)(2).io.ins(0)
    vsbs(0)(2).io.outs(1) <> vsbs(1)(2).io.ins(1)
    vsbs(0)(2).io.outs(2) <> vsbs(1)(2).io.ins(2)
    vsbs(0)(2).io.outs(3) <> vsbs(1)(2).io.ins(3)
    vsbs(0)(2).io.outs(4) <> cus(0)(1).io.vecIn(0)
    vsbs(0)(2).io.outs(5) <> vsbs(0)(1).io.ins(0)
    vsbs(0)(2).io.outs(6) <> vsbs(0)(1).io.ins(1)
    vsbs(0)(2).io.outs(7) <> vsbs(0)(1).io.ins(2)
    vsbs(0)(2).io.outs(8) <> vsbs(0)(1).io.ins(3)
    vsbs(1)(0).io.outs(0) <> vsbs(0)(0).io.ins(5)
    vsbs(1)(0).io.outs(1) <> vsbs(0)(0).io.ins(6)
    vsbs(1)(0).io.outs(2) <> vsbs(0)(0).io.ins(7)
    vsbs(1)(0).io.outs(3) <> vsbs(0)(0).io.ins(8)
    vsbs(1)(0).io.outs(4) <> cus(0)(0).io.vecIn(2)
    vsbs(1)(0).io.outs(5) <> vsbs(1)(1).io.ins(15)
    vsbs(1)(0).io.outs(6) <> vsbs(1)(1).io.ins(16)
    vsbs(1)(0).io.outs(7) <> vsbs(1)(1).io.ins(17)
    vsbs(1)(0).io.outs(8) <> vsbs(1)(1).io.ins(18)
    vsbs(1)(0).io.outs(9) <> cus(1)(0).io.vecIn(3)
    vsbs(1)(0).io.outs(10) <> vsbs(2)(0).io.ins(0)
    vsbs(1)(0).io.outs(11) <> vsbs(2)(0).io.ins(1)
    vsbs(1)(0).io.outs(12) <> vsbs(2)(0).io.ins(2)
    vsbs(1)(0).io.outs(13) <> vsbs(2)(0).io.ins(3)
    vsbs(1)(1).io.outs(0) <> vsbs(0)(1).io.ins(5)
    vsbs(1)(1).io.outs(1) <> vsbs(0)(1).io.ins(6)
    vsbs(1)(1).io.outs(2) <> vsbs(0)(1).io.ins(7)
    vsbs(1)(1).io.outs(3) <> vsbs(0)(1).io.ins(8)
    vsbs(1)(1).io.outs(4) <> cus(0)(1).io.vecIn(2)
    vsbs(1)(1).io.outs(5) <> vsbs(1)(2).io.ins(9)
    vsbs(1)(1).io.outs(6) <> vsbs(1)(2).io.ins(10)
    vsbs(1)(1).io.outs(7) <> vsbs(1)(2).io.ins(11)
    vsbs(1)(1).io.outs(8) <> vsbs(1)(2).io.ins(12)
    vsbs(1)(1).io.outs(9) <> cus(1)(1).io.vecIn(3)
    vsbs(1)(1).io.outs(10) <> vsbs(2)(1).io.ins(0)
    vsbs(1)(1).io.outs(11) <> vsbs(2)(1).io.ins(1)
    vsbs(1)(1).io.outs(12) <> vsbs(2)(1).io.ins(2)
    vsbs(1)(1).io.outs(13) <> vsbs(2)(1).io.ins(3)
    vsbs(1)(1).io.outs(14) <> cus(1)(0).io.vecIn(0)
    vsbs(1)(1).io.outs(15) <> vsbs(1)(0).io.ins(5)
    vsbs(1)(1).io.outs(16) <> vsbs(1)(0).io.ins(6)
    vsbs(1)(1).io.outs(17) <> vsbs(1)(0).io.ins(7)
    vsbs(1)(1).io.outs(18) <> vsbs(1)(0).io.ins(8)
    vsbs(1)(1).io.outs(19) <> cus(0)(0).io.vecIn(1)
    vsbs(1)(2).io.outs(0) <> vsbs(0)(2).io.ins(0)
    vsbs(1)(2).io.outs(1) <> vsbs(0)(2).io.ins(1)
    vsbs(1)(2).io.outs(2) <> vsbs(0)(2).io.ins(2)
    vsbs(1)(2).io.outs(3) <> vsbs(0)(2).io.ins(3)
    vsbs(1)(2).io.outs(4) <> vsbs(2)(2).io.ins(0)
    vsbs(1)(2).io.outs(5) <> vsbs(2)(2).io.ins(1)
    vsbs(1)(2).io.outs(6) <> vsbs(2)(2).io.ins(2)
  }
}
