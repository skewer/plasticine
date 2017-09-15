package fringe

import util._
import chisel3._
import chisel3.util._
import templates.Utils.log2Up

/**
 * DRAM Memory Access Generator
 * MAG config register format
 */
case class MAGOpcode() extends Bundle {
  val scatterGather = Bool()
//  val isWr = Bool()

  override def cloneType(): this.type = {
    new MAGOpcode().asInstanceOf[this.type]
  }
}

class Command(val addrWidth: Int, val sizeWidth: Int) extends Bundle {
  val addr = UInt(addrWidth.W)
  val isWr = Bool()
  val isSparse = Bool()
  val size = UInt(sizeWidth.W)

  override def cloneType(): this.type = {
    new Command(addrWidth, sizeWidth).asInstanceOf[this.type]
  }
}

// Parallelization and word width information
case class StreamParInfo(w: Int, v: Int)

class MemoryStream(addrWidth: Int, sizeWidth: Int) extends Bundle {
  val cmd = Flipped(Decoupled(new Command(addrWidth, sizeWidth)))

  override def cloneType(): this.type = {
    new MemoryStream(addrWidth, sizeWidth).asInstanceOf[this.type]
  }
}

class LoadStream(p: StreamParInfo) extends MemoryStream(addrWidth = 64, sizeWidth = 16) {
  val rdata = Decoupled(Vec(p.v, UInt(p.w.W)))

  override def cloneType(): this.type = {
    new LoadStream(p).asInstanceOf[this.type]
  }
}

class StoreStream(p: StreamParInfo) extends MemoryStream(addrWidth = 64, sizeWidth = 16) {
  val wdata = Flipped(Decoupled(Vec(p.v, UInt(p.w.W))))
  val wresp = Decoupled(Bool())

  override def cloneType(): this.type = {
    new StoreStream(p).asInstanceOf[this.type]
  }

}

class AppStreams(loadPar: List[StreamParInfo], storePar: List[StreamParInfo]) extends Bundle {
  val loads = HVec.tabulate(loadPar.size) { i => new LoadStream(loadPar(i)) }
  val stores = HVec.tabulate(storePar.size) { i => new StoreStream(storePar(i)) }

  override def cloneType(): this.type = {
    new AppStreams(loadPar, storePar).asInstanceOf[this.type]
  }

}

class DRAMCommand(w: Int, v: Int) extends Bundle {
  val addr = UInt(64.W)
  val size = UInt(32.W)
  val rawAddr = UInt(64.W)
  val isWr = Bool() // 1
  val isSparse = Bool()
  val tag = UInt(w.W)
  val streamId = UInt(w.W)
  val dramReadySeen = Bool()
//  val wdata = Vec(v, UInt(w.W)) // v

  override def cloneType(): this.type = {
    new DRAMCommand(w, v).asInstanceOf[this.type]
  }

}

class DRAMWdata(w: Int, v: Int) extends Bundle {
  val wdata = Vec(v, UInt(w.W))
  val wlast = Bool()
  val streamId = UInt(w.W)

  override def cloneType(): this.type = {
    new DRAMWdata(w, v).asInstanceOf[this.type]
  }

}

class DRAMReadResponse(w: Int, v: Int) extends Bundle {
  val rdata = Vec(v, UInt(w.W)) // v
  val tag = UInt(w.W)
  val streamId = UInt(w.W)

  override def cloneType(): this.type = {
    new DRAMReadResponse(w, v).asInstanceOf[this.type]
  }
}

class DRAMWriteResponse(w: Int, v: Int) extends Bundle {
  val tag = UInt(w.W)
  val streamId = UInt(w.W)

  override def cloneType(): this.type = {
    new DRAMWriteResponse(w, v).asInstanceOf[this.type]
  }
}

class DRAMStream(w: Int, v: Int) extends Bundle {
  val cmd = Decoupled(new DRAMCommand(w, v))
  val wdata = Decoupled(new DRAMWdata(w, v))
  val rresp = Flipped(Decoupled(new DRAMReadResponse(w, v)))
  val wresp = Flipped(Decoupled(new DRAMWriteResponse(w, v)))

  override def cloneType(): this.type = {
    new DRAMStream(w, v).asInstanceOf[this.type]
  }
}

class GenericStreams(streamIns: List[StreamParInfo], streamOuts: List[StreamParInfo]) extends Bundle {
  val ins = HVec.tabulate(streamIns.size) { i => StreamIn(streamIns(i)) }
  val outs = HVec.tabulate(streamOuts.size) { i => StreamOut(streamOuts(i)) }

  override def cloneType(): this.type = {
    new GenericStreams(streamIns, streamOuts).asInstanceOf[this.type]
  }

}

class StreamIO(val p: StreamParInfo) extends Bundle {
  val data = UInt(p.w.W)
  val tag = UInt(p.w.W)
  val last = Bool()

  override def cloneType(): this.type = new StreamIO(p).asInstanceOf[this.type]
}

object StreamOut {
  def apply(p: StreamParInfo) = Decoupled(new StreamIO(p))
}

object StreamIn {
  def apply(p: StreamParInfo) = Flipped(Decoupled(new StreamIO(p)))
}

class DebugSignals extends Bundle {
  val num_enable = Output(UInt(32.W))
  val num_cmd_valid = Output(UInt(32.W))
  val num_cmd_valid_enable = Output(UInt(32.W))
  val num_cmd_ready = Output(UInt(32.W))
  val num_cmd_ready_enable = Output(UInt(32.W))
  val num_resp_valid = Output(UInt(32.W))
  val num_resp_valid_enable = Output(UInt(32.W))
  val num_rdata_enq = Output(UInt(32.W))
  val num_rdata_deq = Output(UInt(32.W))
  val num_app_rdata_ready = Output(UInt(32.W))

  // rdata enq
  val rdata_enq0_0 = Output(UInt(32.W))
  val rdata_enq0_1 = Output(UInt(32.W))
  val rdata_enq0_15 = Output(UInt(32.W))

  val rdata_enq1_0 = Output(UInt(32.W))
  val rdata_enq1_1 = Output(UInt(32.W))
  val rdata_enq1_15 = Output(UInt(32.W))

  // rdata deq
  val rdata_deq0_0 = Output(UInt(32.W))
  val rdata_deq0_1 = Output(UInt(32.W))
  val rdata_deq0_15 = Output(UInt(32.W))

  val rdata_deq1_0 = Output(UInt(32.W))
  val rdata_deq1_1 = Output(UInt(32.W))
  val rdata_deq1_15 = Output(UInt(32.W))

  // wdata enq
  val wdata_enq0_0 = Output(UInt(32.W))
  val wdata_enq0_1 = Output(UInt(32.W))
  val wdata_enq0_15 = Output(UInt(32.W))

  val wdata_enq1_0 = Output(UInt(32.W))
  val wdata_enq1_1 = Output(UInt(32.W))
  val wdata_enq1_15 = Output(UInt(32.W))

  // wdata deq
  val wdata_deq0_0 = Output(UInt(32.W))
  val wdata_deq0_1 = Output(UInt(32.W))
  val wdata_deq0_15 = Output(UInt(32.W))

  val wdata_deq1_0 = Output(UInt(32.W))
  val wdata_deq1_1 = Output(UInt(32.W))
  val wdata_deq1_15 = Output(UInt(32.W))

}

//class StreamOutAccel(p: StreamParInfo) extends Bundle {
//  val data = UInt(p.w.W)
//  val tag = UInt(p.w.W)
//  val last = Bool()
//
//  override def cloneType(): this.type = { new StreamOutAccel(p).asInstanceOf[this.type] }
//}
//
//class StreamInAccel(p: StreamParInfo) extends Bundle {
//  val data = UInt(p.w.W)
//  val tag = UInt(p.w.W)
//  val last = Bool()
//
//  override def cloneType(): this.type = { new StreamInAccel(p).asInstanceOf[this.type] }
//}
