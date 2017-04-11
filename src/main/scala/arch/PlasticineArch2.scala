package plasticine.arch
import chisel3._
import chisel3.util._
import scala.collection.mutable.ListBuffer

trait PlasticineArch2{
  self:PlasticineArch with Plasticine =>
  vsbs(2)(2).io.outs(0) <> vsbs(1)(2).io.ins(4)
  vsbs(2)(2).io.outs(1) <> vsbs(1)(2).io.ins(5)
  vsbs(2)(2).io.outs(2) <> vsbs(1)(2).io.ins(6)
  vsbs(2)(2).io.outs(3) <> vsbs(1)(2).io.ins(7)
  vsbs(2)(2).io.outs(4) <> vsbs(2)(1).io.ins(5)
  vsbs(2)(2).io.outs(5) <> vsbs(2)(1).io.ins(6)
  vsbs(2)(2).io.outs(6) <> vsbs(2)(1).io.ins(7)
  vsbs(2)(2).io.outs(7) <> vsbs(2)(1).io.ins(8)
  vsbs(2)(2).io.outs(8) <> cus(1)(1).io.vecIn(1)
  // ScalarNetwork Connection
  self.io.argIns(0) <> ssbs(0)(0).io.ins(11)
  self.io.argIns(1) <> ssbs(1)(0).io.ins(16)
  self.io.argIns(2) <> ssbs(2)(0).io.ins(11)
  self.io.argIns(3) <> ssbs(0)(2).io.ins(0)
  self.io.argIns(4) <> ssbs(1)(2).io.ins(4)
  self.io.argIns(5) <> ssbs(2)(2).io.ins(4)
  cus(0)(0).io.scalarOut(0) <> ssbs(0)(1).io.ins(11)
  cus(0)(0).io.scalarOut(1) <> ssbs(1)(1).io.ins(21)
  cus(0)(0).io.scalarOut(2) <> ssbs(1)(0).io.ins(4)
  cus(0)(0).io.scalarOut(3) <> ssbs(0)(0).io.ins(4)
  cus(1)(1).io.scalarOut(0) <> ssbs(1)(2).io.ins(11)
  cus(1)(1).io.scalarOut(1) <> ssbs(2)(2).io.ins(11)
  cus(1)(1).io.scalarOut(2) <> ssbs(2)(1).io.ins(4)
  cus(1)(1).io.scalarOut(3) <> ssbs(1)(1).io.ins(9)
  cus(0)(1).io.scalarOut(0) <> ssbs(0)(2).io.ins(7)
  cus(0)(1).io.scalarOut(1) <> ssbs(1)(2).io.ins(16)
  cus(0)(1).io.scalarOut(2) <> ssbs(1)(1).io.ins(4)
  cus(0)(1).io.scalarOut(3) <> ssbs(0)(1).io.ins(4)
  cus(1)(0).io.scalarOut(0) <> ssbs(1)(1).io.ins(16)
  cus(1)(0).io.scalarOut(1) <> ssbs(2)(1).io.ins(15)
  cus(1)(0).io.scalarOut(2) <> ssbs(2)(0).io.ins(4)
  cus(1)(0).io.scalarOut(3) <> ssbs(1)(0).io.ins(9)
  cus(0)(0).io.scalarOut(0) <> ssbs(0)(0).io.ins(9)
  cus(0)(0).io.scalarOut(1) <> ssbs(0)(0).io.ins(10)
  cus(0)(1).io.scalarOut(0) <> ssbs(0)(1).io.ins(9)
  cus(0)(1).io.scalarOut(1) <> ssbs(0)(1).io.ins(10)
  cus(0)(2).io.scalarOut(0) <> ssbs(0)(2).io.ins(5)
  cus(0)(2).io.scalarOut(1) <> ssbs(0)(2).io.ins(6)
  cus(1)(0).io.scalarOut(0) <> ssbs(1)(0).io.ins(14)
  cus(1)(0).io.scalarOut(1) <> ssbs(1)(0).io.ins(15)
  cus(1)(1).io.scalarOut(0) <> ssbs(1)(1).io.ins(14)
  cus(1)(1).io.scalarOut(1) <> ssbs(1)(1).io.ins(15)
  cus(1)(2).io.scalarOut(0) <> ssbs(1)(2).io.ins(9)
  cus(1)(2).io.scalarOut(1) <> ssbs(1)(2).io.ins(10)
  cus(2)(0).io.scalarOut(0) <> ssbs(2)(0).io.ins(9)
  cus(2)(0).io.scalarOut(1) <> ssbs(2)(0).io.ins(10)
  cus(2)(1).io.scalarOut(0) <> ssbs(2)(1).io.ins(9)
  cus(2)(1).io.scalarOut(1) <> ssbs(2)(1).io.ins(10)
  cus(2)(2).io.scalarOut(0) <> ssbs(2)(2).io.ins(5)
  cus(2)(2).io.scalarOut(1) <> ssbs(2)(2).io.ins(6)
  ssbs(0)(0).io.outs(0) <> ssbs(0)(1).io.ins(12)
  ssbs(0)(0).io.outs(1) <> ssbs(0)(1).io.ins(13)
  ssbs(0)(0).io.outs(2) <> ssbs(0)(1).io.ins(14)
  ssbs(0)(0).io.outs(3) <> ssbs(0)(1).io.ins(15)
  ssbs(0)(0).io.outs(4) <> cus(0)(0).io.scalarIn(3)
  ssbs(0)(0).io.outs(5) <> ssbs(1)(0).io.ins(0)
  ssbs(0)(0).io.outs(6) <> ssbs(1)(0).io.ins(1)
  ssbs(0)(0).io.outs(7) <> ssbs(1)(0).io.ins(2)
  ssbs(0)(0).io.outs(8) <> ssbs(1)(0).io.ins(3)
  ssbs(0)(0).io.outs(9) <> cus(0)(0).io.scalarIn(0)
  ssbs(0)(0).io.outs(10) <> cus(0)(0).io.scalarIn(1)
  ssbs(0)(0).io.outs(11) <> cus(0)(0).io.scalarIn(2)
  ssbs(0)(0).io.outs(12) <> cus(0)(0).io.scalarIn(3)
  ssbs(0)(0).io.outs(13) <> self.io.argOuts(0)
  ssbs(0)(1).io.outs(0) <> ssbs(0)(2).io.ins(8)
  ssbs(0)(1).io.outs(1) <> ssbs(0)(2).io.ins(9)
  ssbs(0)(1).io.outs(2) <> ssbs(0)(2).io.ins(10)
  ssbs(0)(1).io.outs(3) <> ssbs(0)(2).io.ins(11)
  ssbs(0)(1).io.outs(4) <> cus(0)(1).io.scalarIn(3)
  ssbs(0)(1).io.outs(5) <> ssbs(1)(1).io.ins(0)
  ssbs(0)(1).io.outs(6) <> ssbs(1)(1).io.ins(1)
  ssbs(0)(1).io.outs(7) <> ssbs(1)(1).io.ins(2)
  ssbs(0)(1).io.outs(8) <> ssbs(1)(1).io.ins(3)
  ssbs(0)(1).io.outs(9) <> cus(0)(1).io.scalarIn(0)
  ssbs(0)(1).io.outs(10) <> cus(0)(1).io.scalarIn(1)
  ssbs(0)(1).io.outs(11) <> cus(0)(1).io.scalarIn(2)
  ssbs(0)(1).io.outs(12) <> cus(0)(1).io.scalarIn(3)
  ssbs(0)(1).io.outs(13) <> cus(0)(0).io.scalarIn(0)
  ssbs(0)(1).io.outs(14) <> ssbs(0)(0).io.ins(0)
  ssbs(0)(1).io.outs(15) <> ssbs(0)(0).io.ins(1)
  ssbs(0)(1).io.outs(16) <> ssbs(0)(0).io.ins(2)
  ssbs(0)(1).io.outs(17) <> ssbs(0)(0).io.ins(3)
  ssbs(0)(2).io.outs(0) <> self.io.argOuts(3)
  ssbs(0)(2).io.outs(1) <> ssbs(1)(2).io.ins(0)
  ssbs(0)(2).io.outs(2) <> ssbs(1)(2).io.ins(1)
  ssbs(0)(2).io.outs(3) <> ssbs(1)(2).io.ins(2)
  ssbs(0)(2).io.outs(4) <> ssbs(1)(2).io.ins(3)
  ssbs(0)(2).io.outs(5) <> cus(0)(2).io.scalarIn(0)
  ssbs(0)(2).io.outs(6) <> cus(0)(2).io.scalarIn(1)
  ssbs(0)(2).io.outs(7) <> cus(0)(2).io.scalarIn(2)
  ssbs(0)(2).io.outs(8) <> cus(0)(2).io.scalarIn(3)
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
}
