package plasticine.pisa.ir

import java.io.File
import _root_.scala.util.parsing.json.JSON
import scala.collection.immutable.Map
import plasticine.pisa.parser._
import plasticine.templates.Opcodes

object Config {
  def apply(path: String) = Parser(path)
}

class Config[T<:AbstractConfig](val config: T) {
  private var _v: Double = 0
  def v = _v
  def v_=(x: Double) = { _v = x }

  override def toString = {
s"""PISA version: $v
config: $config"""
  }
}

/**
 * Base class for all Config nodes
 */
abstract class AbstractConfig {
  protected def encodeOneHot(x: Int) = 1 << x
  protected def getRegNum(s: String) = if (s.size <= 1) 0 else s.drop(1).toInt
}

/**
 * Parsed config information for a single counter
 */
case class CounterRCConfig(config: Map[Any, Any]) extends AbstractConfig {
  /** Counter max */
  private var _max: Int = Parser.getFieldInt(config, "max")
  def max = _max
  def max_=(x: Int) { _max = x }

  /** Counter stride */
  private var _stride: Int = Parser.getFieldInt(config, "stride")
  def stride = _stride
  def stride_=(x: Int) { _stride = x }

  /** Is max const */
  private var _maxConst: Int = Parser.getFieldInt(config, "maxConst")
  def maxConst = _maxConst
  def maxConst_=(x: Int) { _maxConst = x }

  /** Is stride const */
  private var _strideConst: Int = Parser.getFieldInt(config, "strideConst")
  def strideConst = _strideConst
  def strideConst_=(x: Int) { _strideConst = x }

  /** Delay the start of counter */
  private var _startDelay: Int = 1 + Parser.getFieldInt(config, "startDelay")
  def startDelay = _startDelay
  def startDelay_=(x: Int) { _startDelay = x }

  /** Delay raising the counter 'done' signal */
  private var _endDelay: Int = 1 + Parser.getFieldInt(config, "endDelay")
  def endDelay = _endDelay
  def endDelay_=(x: Int) { _endDelay = x }
}


/**
 * CounterChain config information
 */
case class CounterChainConfig(config: Map[Any, Any]) extends AbstractConfig {
  // To chain or not?
  private var _chain: List[Int] = Parser.getFieldList(config, "chain")
                                        .asInstanceOf[List[Double]]
                                        .map { i => i.toInt }

  def chain = _chain
  def chain_=(x: List[Int]) { _chain = x }

  // Configuration for individual counters
  private var _counters: Seq[CounterRCConfig] = Parser.getFieldListOfMaps(config, "counters")
                                                      .map { h => new CounterRCConfig(h) }
  def counters = _counters
  def counters_=(x: Seq[CounterRCConfig]) { _counters = x }
}


/**
 * Parsed configuration information for ComputeUnit
 */
case class OperandConfig(config: String) extends AbstractConfig {
  private def getDataSrc = config(0) match {
    case 'l' => 0 // Local register
    case 'r' => 1 // Remote register
    case 'c' => 2 // Constant
    case 'i' => 3 // Iterator / counter
    case 'm' => 4 // Memory
    case _ => throw new Exception(s"Unknown data source '${config(0)}'. Must be l, r, c, i, or m")
  }
  private var _dataSrc = getDataSrc
  def dataSrc() = _dataSrc
  def dataSrc_=(x: Int) { _dataSrc = x }

  private var _value = getRegNum(config)
  def value() = _value
  def value_=(x: Int) { _value = x }

  override def toString = {
    s"dataSrc: $dataSrc\n" +
    s"value: $value\n"
  }
}

case class PipeStageConfig(config: Map[Any, Any]) extends AbstractConfig {
  private var _opA = new OperandConfig(Parser.getFieldString(config, "opA"))
  def opA() = _opA
  def opA_=(x: OperandConfig) { _opA = x }

  private var _opB = new OperandConfig(Parser.getFieldString(config, "opB"))
  def opB() = _opB
  def opB_=(x: OperandConfig) { _opB = x }

  private var _opcode = Opcodes.getCode(Parser.getFieldString(config, "opcode"))
  def opcode() = _opcode
  def opcode_=(x: Int) { _opcode = x }

  private var _result = encodeOneHot(getRegNum(Parser.getFieldString(config, "result")))
  def result() = _result
  def result_=(x: Int) { _result = x }

  // TODO: Remove hardcoded constant!
  val r = 4
  private var _fwd: List[Int] = {
    val m = Parser.getFieldMap(config, "fwd")
    List.tabulate(r) { i =>
      val regName = s"r$i"
      if (m.contains(regName)) {
        m(regName) match {
          case "counter" => 1
          case "memory" => 1
          case _ => throw new Exception(s"Unknown forward source location ${m(regName)}. It must either be 'counter' or 'memory'")
        }
      } else 0
    }
  }
  def fwd = _fwd
  def fwd_=(x: List[Int]) { _fwd = x }


  override def toString = {
    s"opA:\n ${opA.toString}" + "\n" +
    s"opB:\n ${opB.toString}" + "\n" +
    s"opcode: $opcode" + "\n" +
    s"result: $result"
  }
}

/**
 * Simple container to hold two ints: src and value. Used
 * to hold scratchpad config info.
 * TODO: Use this to hold operand info as well
 */
case class SrcValueTuple(val src: Int, val value: Int)

case class ScratchpadConfig(config: Map[Any, Any]) extends AbstractConfig {
  // Banking stride
  private def parseAddrSource(x: String) = {
    val src = x(0) match {
      case 'x' => 0  // Don't care
      case 's' => 0  // Stage
      case 'i' => 1  // Iterator
      case 'l' => 2  // Last stage - only for write addr. TODO: Error out for reads
      case _ => throw new Exception(s"Unknwon address source ${x(0)}; must be one of s, i, or l")
   }
   SrcValueTuple(src, if (x == "x") 0 else x.drop(1).toInt)
  }

  private var _wa = parseAddrSource(Parser.getFieldString(config, "wa"))
  def wa = _wa
  def wa_=(x: SrcValueTuple) { _wa = x }

  private var _ra = parseAddrSource(Parser.getFieldString(config, "ra"))
  def ra = _ra
  def ra_=(x: SrcValueTuple) { _ra = x }

  private var _wd = Parser.getFieldString(config, "wd") match {
    case "x" => 0 // Don't care
    case "local" => 0
    case "remote" => 1
    case _ => throw new Exception(s"Unknown write data source; must be either local or remote")
  }
  def wd = _wd
  def wd_=(x: Int) { _wd = x }

  private var _wen = Parser.getFieldString(config, "wen") match {
    case "x" => 0
    case n@_ => n.drop(1).toInt + 1
  }
  def wen = _wen
  def wen_=(x: Int) { _wen = x }
}

case class ComputeUnitConfig(config: Map[Any, Any]) extends AbstractConfig {
  /* CounterChain config */
  private var _counterChain = new CounterChainConfig(
    Parser.getFieldMap(config, "counterChain"))
  def counterChain() = _counterChain
  def counterChain_=(x: CounterChainConfig) { _counterChain = x }


  private var _scratchpads =  Parser.getFieldListOfMaps(config, "scratchpads")
                                    .map { ScratchpadConfig(_) }
  def scratchpads = _scratchpads
  def scratchpads_=(x: List[ScratchpadConfig]) { _scratchpads = x }

  /* Pipe stages config */
  private var _pipeStage = Parser.getFieldListOfMaps(config, "pipeStage")
                            .map { h => new PipeStageConfig(h) }
  def pipeStage = _pipeStage
  def pipeStage_=(x: List[PipeStageConfig]) { _pipeStage = x }

  private var _control = CUControlBoxConfig(Parser.getFieldMap(config, "control"))
  def control = _control
  def control_=(x: CUControlBoxConfig) { _control = x }


  override def toString = {
    s"pipeStage:\n" +
    pipeStage.map { _.toString }.reduce {_+_}
  }
}

/**
 * CUControlBox config information
 */
case class CUControlBoxConfig(config: Map[Any, Any]) extends AbstractConfig {
  private var _tokenOutLUT: List[LUTConfig] = Parser.getFieldListOfMaps(config, "tokenOutLUT")
                                                    .map { LUTConfig(_) }
  def tokenOutLUT = _tokenOutLUT
  def tokenOutLUT_=(x: List[LUTConfig]) { _tokenOutLUT = x }

  private var _enableLUT: List[LUTConfig] = Parser.getFieldListOfMaps(config, "enableLUT")
                                                    .map { LUTConfig(_) }
  def enableLUT = _enableLUT
  def enableLUT_=(x: List[LUTConfig]) { _enableLUT = x }

  private var _udcInit: List[Int] = Parser.getFieldList(config, "udcInit")
                                        .asInstanceOf[List[Double]]
                                        .map { _.toInt }
  def udcInit = _udcInit
  def udcInit_=(x: List[Int]) { _udcInit = x }

  private var _decXbar: CrossbarConfig  = CrossbarConfig(Parser.getFieldMap(config, "decXbar"))
  def decXbar = _decXbar
  def decXbar_=(x: CrossbarConfig) { _decXbar = x }

  private var _incXbar: CrossbarConfig  = CrossbarConfig(Parser.getFieldMap(config, "incXbar"))
  def incXbar = _incXbar
  def incXbar_=(x: CrossbarConfig) { _incXbar = x }
}

/**
 * Crossbar config information
 */
case class CrossbarConfig(config: Map[Any, Any]) extends AbstractConfig {
  private var _outSelect: List[Int] = Parser.getFieldList(config, "outSelect")
                                        .asInstanceOf[List[Double]]
                                        .map { _.toInt }

  def outSelect = _outSelect
  def outSelect_=(x: List[Int]) { _outSelect = x }
}

/**
 * LUT config information
 */
case class LUTConfig(config: Map[Any, Any]) extends AbstractConfig {
  private var _table: List[Int] = Parser.getFieldList(config, "table")
                                        .asInstanceOf[List[Double]]
                                        .map { _.toInt }

  def table = _table
  def table_=(x: List[Int]) { _table = x }
}

/**
 * Plasticine config information
 */
case class PlasticineConfig(config: Map[Any, Any]) extends AbstractConfig {
  private var _cu: List[ComputeUnitConfig] = Parser.getFieldListOfMaps(config, "cu")
                                        .map { ComputeUnitConfig(_) }

  def cu = _cu
  def cu_=(x: List[ComputeUnitConfig]) { _cu = x }
}
