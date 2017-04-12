package plasticine.spade

import java.io.File

import scala.collection.immutable.Map
import plasticine.pisa.parser._
import plasticine.templates.Opcodes
import scala.collection.mutable.HashMap
import scala.util.Random

import scala.collection.mutable.ListBuffer
import fringe._

trait Params

trait CUParams extends Params {
  val w: Int = 32
  val v: Int = 16
  val numCounters: Int = 8
  val numUDCs: Int = 5
  val regColors = ListBuffer[List[RegColor]]()
  val d: Int = 8
  val r: Int = 16
  val numScalarIn: Int = 4
  val numScalarOut: Int = 4
  val numVectorIn: Int = 4
  val numVectorOut: Int = 4
  val numControlIn: Int = 8
  val numControlOut: Int = 8
}

trait PCUParams extends CUParams

trait PMUParams extends CUParams {
  val wd = 5
}

trait PlasticineParams extends Params {
  val w: Int = 32
  val numRows: Int = 2
  val numCols: Int = 2
  val cuParams: Array[Array[CUParams]]
  val vectorSwitchParams:Array[Array[VectorSwitchParams]] 
  val scalarSwitchParams:Array[Array[ScalarSwitchParams]] 
  val controlSwitchParams:Array[Array[ControlSwitchParams]] 
  val numArgOutSelections: List[Int]   //  = List(6,6,6)
}

trait FringeParams extends Params {
  val addrWidth: Int = 32
  val dataWidth: Int = 32
  val numArgIns: Int = 16
  val numArgOuts: Int = 8
  val loadStreamInfo = List[StreamParInfo]()
  val storeStreamInfo = List[StreamParInfo]()
}

trait TopParams {
  val plasticineParams: PlasticineParams
  val fringeParams: FringeParams
  val target:String = ""
}

abstract class SwitchParams(val numIns: Int, val numOuts: Int) extends Params

case class VectorSwitchParams(override val numIns:Int, override val numOuts:Int, w:Int, v:Int) extends SwitchParams(numIns, numOuts)
case class ScalarSwitchParams(override val numIns:Int, override val numOuts:Int, w:Int) extends SwitchParams(numIns, numOuts)
case class ControlSwitchParams(override val numIns:Int, override val numOuts:Int) extends SwitchParams(numIns, numOuts)

