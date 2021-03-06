## set up synopsys requirements: 
# source /cad/modules/tcl/init/bash
# license
export SNPSLMD_LICENSE_FILE=27000@cadlic0.stanford.edu

# for dc_shell
export PATH="/cad/synopsys/dc_shell/latest/bin:$PATH"

# for icc_shell
export PATH="/cad/synopsys/icc/J-2014.09-SP3/bin:$PATH"

# for vcs-mx
export VCSVER="I-2014.03-2"
export VCS_HOME="/cad/synopsys/vcs-mx/$VCSVER"
export VIRSIMHOME="$VCS_HOME/gui/virsim"
export PATH="$VCS_HOME/bin:$PATH"
export PATH="$VCS_HOME/gui/dve/bin:$PATH"
export PATH="$VIRSIMHOME/bin:$PATH"

# for primetime shell
export PTSVER="H-2012.12-SP2"
export SYNOPSYS_PTS="/cad/synopsys/pts/$PTSVER" 
export PATH="$SYNOPSYS_PTS/bin:$PATH"
export PATH="$SYNOPSYS_PTS/amd64/syn/bin/:$PATH"

# for milkyway
export PATH="/cad/synopsys/mw/G-2012.06-SP5-4/bin/AMD.64:$PATH"


export ICC_EG="/home/tianzhao/plasticine_power_area_estimate/ICC_tutorial/ICC-RM_J-2014.09"

## set up TSMC 45 library
export TSMCHOME="/cad/synopsys_EDK2/TSMCHOME"

## verilog simulations libraries
export TSMC_VERILOG_DIR=$TSMCHOME/digital/Front_End/verilog
#main standard cell libraries
export TSMC_LIB_DIR=$TSMCHOME/digital/Front_End/timing_power_noise/NLDM

#Coarse grain libraries for Multi voltage flows
export TSMC_CGLIB_DIR=$TSMCHOME/digital/Front_End/timing_power

#Milkyway libraries for physical design
export TSMC_MWLIB_DIR=$TSMCHOME/digital/Back_End/milkyway


#First: Standard Vt libraries
export TCBN45GSNAME="tcbn45gsbwp_110a"

export TCBN45GS_SVT_WC_0V8=$TSMC_LIB_DIR/$TCBN45GSNAME/tcbn45gsbwpwc0d72.db
export TCBN45GS_SVT_BC_0V8=$TSMC_LIB_DIR/$TCBN45GSNAME/tcbn45gsbwpbc0d88.db
export TCBN45GS_SVT_WC_0V9=$TSMC_LIB_DIR/$TCBN45GSNAME/tcbn45gsbwpwc.db
export TCBN45GS_SVT_BC_0V9=$TSMC_LIB_DIR/$TCBN45GSNAME/tcbn45gsbwpbc.db
export TCBN45GS_SVT_WC_1V0=$TSMC_LIB_DIR/$TCBN45GSNAME/tcbn45gsbwpwc0d9.db
export TCBN45GS_SVT_BC_1V0=$TSMC_LIB_DIR/$TCBN45GSNAME/tcbn45gsbwpbc1d1.db

export TCBN45GS_SVT_MW=$TSMC_MWLIB_DIR/$TCBN45GSNAME/frame_only_VHV_0d5_0/tcbn45gsbwp

export TCBN45GS_VERILOG=$TSMC_VERILOG_DIR/tcbn45gsbwp_110b/tcbn45gsbwp.v


#Second: Low Vt libraries

export TCBN45GSLVTNAME=tcbn45gsbwplvt_110a

export TCBN45GS_LVT_WC_0V8=$TSMC_LIB_DIR/$TCBN45GSLVTNAME/tcbn45gsbwplvtwc0d72.db
export TCBN45GS_LVT_BC_0V8=$TSMC_LIB_DIR/$TCBN45GSLVTNAME/tcbn45gsbwplvtbc0d88.db
export TCBN45GS_LVT_WC_0V9=$TSMC_LIB_DIR/$TCBN45GSLVTNAME/tcbn45gsbwplvtwc.db
export TCBN45GS_LVT_BC_0V9=$TSMC_LIB_DIR/$TCBN45GSLVTNAME/tcbn45gsbwplvtbc.db
export TCBN45GS_LVT_WC_1V0=$TSMC_LIB_DIR/$TCBN45GSLVTNAME/tcbn45gsbwplvtwc0d9.db
export TCBN45GS_LVT_BC_1V0=$TSMC_LIB_DIR/$TCBN45GSLVTNAME/tcbn45gsbwplvtbc1d1.db
 
export TCBN45GSLVT_MW=$TSMC_MWLIB_DIR/$TCBN45GSLVTNAME/frame_only_VHV_0d5_0/tcbn45gsbwplvt

export TCBN45GSLVT_VERILOG=$TSMC_VERILOG_DIR/tcbn45gsbwplvt_110b/tcbn45gsbwplvt.v

#Third: High Vt libraries

export TCBN45GSHVTNAME=tcbn45gsbwphvt_110a

export TCBN45GS_HVT_WC_0V8=$TSMC_LIB_DIR/$TCBN45GSHVTNAME/tcbn45gsbwphvtwc0d72.db
export TCBN45GS_HVT_BC_0V8=$TSMC_LIB_DIR/$TCBN45GSHVTNAME/tcbn45gsbwphvtbc0d88.db
export TCBN45GS_HVT_WC_0V9=$TSMC_LIB_DIR/$TCBN45GSHVTNAME/tcbn45gsbwphvtwc.db
export TCBN45GS_HVT_BC_0V9=$TSMC_LIB_DIR/$TCBN45GSHVTNAME/tcbn45gsbwphvtbc.db
export TCBN45GS_HVT_WC_1V0=$TSMC_LIB_DIR/$TCBN45GSHVTNAME/tcbn45gsbwphvtwc0d9.db
export TCBN45GS_HVT_BC_1V0=$TSMC_LIB_DIR/$TCBN45GSHVTNAME/tcbn45gsbwphvtbc1d1.db

#main standard cell libraries
export TSMC_LIB_DIR=$TSMCHOME/digital/Front_End/timing_power_noise/NLDM

#Coarse grain libraries for Multi voltage flows
export TSMC_CGLIB_DIR=$TSMCHOME/digital/Front_End/timing_power

#Milkyway libraries for physical design
export TSMC_MWLIB_DIR=$TSMCHOME/digital/Back_End/milkyway


#First: Standard Vt libraries
export TCBN45GSNAME=tcbn45gsbwp_110a

export TCBN45GS_SVT_WC_0V8=$TSMC_LIB_DIR/$TCBN45GSNAME/tcbn45gsbwpwc0d72.db
export TCBN45GS_SVT_BC_0V8=$TSMC_LIB_DIR/$TCBN45GSNAME/tcbn45gsbwpbc0d88.db
export TCBN45GS_SVT_WC_0V9=$TSMC_LIB_DIR/$TCBN45GSNAME/tcbn45gsbwpwc.db
export TCBN45GS_SVT_BC_0V9=$TSMC_LIB_DIR/$TCBN45GSNAME/tcbn45gsbwpbc.db
export TCBN45GS_SVT_WC_1V0=$TSMC_LIB_DIR/$TCBN45GSNAME/tcbn45gsbwpwc0d9.db
export TCBN45GS_SVT_BC_1V0=$TSMC_LIB_DIR/$TCBN45GSNAME/tcbn45gsbwpbc1d1.db

export TCBN45GS_SVT_MW=$TSMC_MWLIB_DIR/$TCBN45GSNAME/frame_only_VHV_0d5_0/tcbn45gsbwp

export TCBN45GS_VERILOG=$TSMC_VERILOG_DIR/tcbn45gsbwp_110b/tcbn45gsbwp.v


#Second: Low Vt libraries

export TCBN45GSLVTNAME=tcbn45gsbwplvt_110a

export TCBN45GS_LVT_WC_0V8=$TSMC_LIB_DIR/$TCBN45GSLVTNAME/tcbn45gsbwplvtwc0d72.db
export TCBN45GS_LVT_BC_0V8=$TSMC_LIB_DIR/$TCBN45GSLVTNAME/tcbn45gsbwplvtbc0d88.db
export TCBN45GS_LVT_WC_0V9=$TSMC_LIB_DIR/$TCBN45GSLVTNAME/tcbn45gsbwplvtwc.db
export TCBN45GS_LVT_BC_0V9=$TSMC_LIB_DIR/$TCBN45GSLVTNAME/tcbn45gsbwplvtbc.db
export TCBN45GS_LVT_WC_1V0=$TSMC_LIB_DIR/$TCBN45GSLVTNAME/tcbn45gsbwplvtwc0d9.db
export TCBN45GS_LVT_BC_1V0=$TSMC_LIB_DIR/$TCBN45GSLVTNAME/tcbn45gsbwplvtbc1d1.db

export TCBN45GSLVT_MW=$TSMC_MWLIB_DIR/$TCBN45GSLVTNAME/frame_only_VHV_0d5_0/tcbn45gsbwplvt
export TCBN45GSLVT_VERILOG=$TSMC_VERILOG_DIR/tcbn45gsbwplvt_110b/tcbn45gsbwplvt.v

#Third: High Vt libraries

export TCBN45GSHVTNAME=tcbn45gsbwphvt_110a

export TCBN45GS_HVT_WC_0V8=$TSMC_LIB_DIR/$TCBN45GSHVTNAME/tcbn45gsbwphvtwc0d72.db
export TCBN45GS_HVT_BC_0V8=$TSMC_LIB_DIR/$TCBN45GSHVTNAME/tcbn45gsbwphvtbc0d88.db
export TCBN45GS_HVT_WC_0V9=$TSMC_LIB_DIR/$TCBN45GSHVTNAME/tcbn45gsbwphvtwc.db
export TCBN45GS_HVT_BC_0V9=$TSMC_LIB_DIR/$TCBN45GSHVTNAME/tcbn45gsbwphvtbc.db
export TCBN45GS_HVT_WC_1V0=$TSMC_LIB_DIR/$TCBN45GSHVTNAME/tcbn45gsbwphvtwc0d9.db
export TCBN45GS_HVT_BC_1V0=$TSMC_LIB_DIR/$TCBN45GSHVTNAME/tcbn45gsbwphvtbc1d1.db

export TCBN45GSHVT_MW=$TSMC_MWLIB_DIR/$TCBN45GSHVTNAME/frame_only_VHV_0d5_0/tcbn45gsbwphvt

export TCBN45GSHVT_VERILOG=$TSMC_VERILOG_DIR/tcbn45gsbwphvt_110b/tcbn45gsbwphvt.v
export TCBN45GSHVT_MW=$TSMC_MWLIB_DIR/$TCBN45GSHVTNAME/frame_only_VHV_0d5_0/tcbn45gsbwphvt

export TCBN45GSHVT_VERILOG=$TSMC_VERILOG_DIR/tcbn45gsbwphvt_110b/tcbn45gsbwphvt.v

