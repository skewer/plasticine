package plasticine.arch
import chisel3._
import chisel3.util._
import templates.MuxN
import scala.language.reflectiveCalls
import scala.collection.mutable.ListBuffer

trait PlasticineArch extends PlasticineArch1 with PlasticineArch2 with PlasticineArch3 with PlasticineArch4 with PlasticineArch5 with PlasticineArch6 with PlasticineArch7 with PlasticineArch8 with PlasticineArch9 {
  self:PlasticineArch with Plasticine =>
  def connect(io:PlasticineIO, argOutMuxIns:Array[Array[DecoupledIO[UInt]]], doneOuts:Array[Bool], cus:Array[Array[CU]], scus:Array[Array[ScalarCU]] , mcs:Array[Array[MemoryChannel]], vsbs:Array[Array[VectorSwitch]], ssbs:Array[Array[ScalarSwitch]], csbs:Array[Array[ControlSwitch]], lcus:Array[Array[SwitchCU]]):Unit =  {
    connect1(io, argOutMuxIns, doneOuts, cus, scus, mcs, vsbs, ssbs, csbs, lcus)
    connect2(io, argOutMuxIns, doneOuts, cus, scus, mcs, vsbs, ssbs, csbs, lcus)
    connect3(io, argOutMuxIns, doneOuts, cus, scus, mcs, vsbs, ssbs, csbs, lcus)
    connect4(io, argOutMuxIns, doneOuts, cus, scus, mcs, vsbs, ssbs, csbs, lcus)
    connect5(io, argOutMuxIns, doneOuts, cus, scus, mcs, vsbs, ssbs, csbs, lcus)
    connect6(io, argOutMuxIns, doneOuts, cus, scus, mcs, vsbs, ssbs, csbs, lcus)
    connect7(io, argOutMuxIns, doneOuts, cus, scus, mcs, vsbs, ssbs, csbs, lcus)
    connect8(io, argOutMuxIns, doneOuts, cus, scus, mcs, vsbs, ssbs, csbs, lcus)
    connect9(io, argOutMuxIns, doneOuts, cus, scus, mcs, vsbs, ssbs, csbs, lcus)
  }
}
