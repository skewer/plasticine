#ifndef __PlasticineControllerTop__
#define __PlasticineControllerTop__

#include "emulator.h"

class PlasticineControllerTop_t : public mod_t {
 private:
  val_t __rand_seed;
  void __srand(val_t seed) { __rand_seed = seed; }
  val_t __rand_val() { return ::__rand_val(&__rand_seed); }
 public:
  dat_t<1> PlasticineControllerTop__io_start;
  dat_t<1> PlasticineControllerTop_controller__io_start;
  dat_t<1> PlasticineControllerTop_controller__io_tx_enq;
  dat_t<1> PlasticineControllerTop_dramsim__TX_ENQ;
  dat_t<1> PlasticineControllerTop_dramsim__TX_COMP;
  dat_t<1> PlasticineControllerTop_controller__io_tx_comp;
  dat_t<1> reset;
  dat_t<1> PlasticineControllerTop_controller__reset;
  dat_t<1> PlasticineControllerTop_controller__io_done;
  dat_t<1> PlasticineControllerTop__io_done;
  dat_t<1> PlasticineControllerTop__io_isWR;
  dat_t<1> PlasticineControllerTop_dramsim__IS_WR;
  dat_t<1> PlasticineControllerTop_dramsim__IS_WR_OUT;
  dat_t<1> PlasticineControllerTop__io_isWROut;
  dat_t<2> T25;
  dat_t<2> PlasticineControllerTop_controller__state;
  dat_t<64> PlasticineControllerTop__io_addr;
  dat_t<64> PlasticineControllerTop_dramsim__ADDR;
  dat_t<64> PlasticineControllerTop_dramsim__ADDR_OUT;
  dat_t<64> PlasticineControllerTop__io_addrOut;
  clk_t clk;

  void init ( val_t rand_init = 0 );
  void clock_lo ( dat_t<1> reset, bool assert_fire=true );
  void clock_hi ( dat_t<1> reset );
  int clock ( dat_t<1> reset );
  void print ( FILE* f );
  void print ( std::ostream& s );
  void dump ( FILE* f, val_t t, dat_t<1> reset=LIT<1>(0) );
  void dump_init ( FILE* f );

};

#include "emul_api.h"
class PlasticineControllerTop_api_t : public emul_api_t {
 public:
  PlasticineControllerTop_api_t(mod_t* m) : emul_api_t(m) { }
  void init_sim_data();
};

#endif
