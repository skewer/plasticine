#ifndef __FRINGE_CONTEXT_PLASTICINE_H__
#define __FRINGE_CONTEXT_PLASTICINE_H__

#include <spawn.h>
#include <poll.h>
#include <sys/wait.h>
#include <sys/types.h>
#include <iostream>
#include <fstream>
#include <vector>
#include <cstdlib>
#include <cstring>
#include <fcntl.h>
#include <errno.h>
#include <stdio.h>
#include <unistd.h>

#include "FringeContextBase.h"
#include "simAddrMap.h"
#include "simDefs.h"
#include "channel.h"
#include "generated.h"

//Source: http://stackoverflow.com/questions/13893085/posix-spawnp-and-piping-child-output-to-a-string
class FringeContextPlasticine : public FringeContextBase<void> {

  pid_t sim_pid;
  std::string configPath;
  std::string simPath;
  Channel *cmdChannel;
  Channel *respChannel;
  int initialCycles = -1;
  uint64_t numCycles = 0;
  uint32_t numArgIns = 0;
  uint32_t numArgInsId = 0;
  uint32_t numArgOuts = 0;
  uint32_t numArgIOs = 0;
  uint32_t numArgIOsId = 0;

  posix_spawn_file_actions_t action;
  int globalID = 1;

  const uint32_t burstSizeBytes = 64;
  const uint32_t commandReg = COMMAND_OFFSET;
  const uint32_t statusReg = STATUS_OFFSET;
  uint64_t maxCycles = 10000000000;
  uint64_t stepCount = 0;

  // Debug flags
  bool debugRegs = false;

  // Set of environment variables that should be set and visible to the simulator process
  // Each variable must be explicitly mentioned here
  // Each specified variable must be set (will trigger an assert otherwise)
  std::vector<std::string> envVariablesToSim = {
    "LD_LIBRARY_PATH",
    "SIM_PATH",
    "DRAMSIM_HOME",
    "USE_IDEAL_DRAM",
    "DRAM_DEBUG",
    "DRAM_NUM_OUTSTANDING_BURSTS",
    "VPD_ON",
    "VCD_ON"
  };

  char* checkAndGetEnvVar(std::string var) {
    const char *cvar = var.c_str();
    char *value = getenv(cvar);
    ASSERT(value != NULL, "%s is NULL\n", cvar);
    return value;
  }

  bool envToBool(std::string var) {
    const char *cvar = var.c_str();
    char *value = getenv(cvar);
    if (value != NULL) {
      if (value[0] != 0 && atoi(value) > 0) {
        return true;
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  long envToLong(std::string var) {
    const char *cvar = var.c_str();
    char *value = getenv(cvar);
    if (value != NULL) {
      if (value[0] != 0) {
        return atol(value);
      } else {
        return -1;
      }
    } else {
      return -1;
    }
  }

  int sendCmd(SIM_CMD cmd) {
    simCmd simCmd;
    simCmd.id = globalID++;
    simCmd.cmd = cmd;

    switch (cmd) {
      case RESET:
        simCmd.size = 0;
        break;
      case START:
        simCmd.size = 0;
        break;
      case STEP:
        simCmd.size = 0;
        break;
      case FIN:
        simCmd.size = 0;
        break;
      case READY:
        simCmd.size = 0;
        break;
      case GET_CYCLES:
        simCmd.size = 0;
        break;
      default:
        EPRINTF("Command %d not supported!\n", cmd);
        exit(-1);
    }

    cmdChannel->send(&simCmd);
    return simCmd.id;
  }

  simCmd* recvResp() {
    return (simCmd*) respChannel->recv();
  }

public:
  void step() {
    sendCmd(STEP);
    numCycles = getCycles();
    stepCount++;

    int printInterval = 10000;
    static uint64_t nextPrint = printInterval;

    if ((numCycles >= nextPrint)) {
      EPRINTF("\t%lu cycles elapsed\n", nextPrint);
      nextPrint += printInterval;
    }
  }

  void resetCycles() {
    initialCycles = -1;
    numCycles = 0;
  }

  uint64_t getCycles() {
    int id = sendCmd(GET_CYCLES);
    simCmd *resp = recvResp();
    ASSERT(id == resp->id, "GET_CYCLES resp->id does not match cmd.id!");
    ASSERT(GET_CYCLES == resp->cmd, "GET_CYCLES resp->cmd does not match cmd.cmd!");
    uint64_t cycles = *(uint64_t*)resp->data;
    if (initialCycles == -1) initialCycles = (int) cycles;
    return (cycles - initialCycles);
  }

  void finish() {
    int id = sendCmd(FIN);
    simCmd *resp = recvResp();
    ASSERT(id == resp->id, "FIN resp->id does not match cmd.id!");
    ASSERT(FIN == resp->cmd, "FIN resp->cmd does not match cmd.cmd!");
  }

  void reset() {
    sendCmd(RESET);
  }

  void start() {
    sendCmd(START);
  }

  virtual void writeReg(uint32_t reg, uint64_t data) {
    simCmd cmd;
    cmd.id = globalID++;
    cmd.cmd = WRITE_REG;
    uint32_t regWithBase = DEVICE_REG_BASE + reg;
    std::memcpy(cmd.data, &regWithBase, sizeof(uint32_t));
    std::memcpy(cmd.data+sizeof(uint32_t), &data, sizeof(uint64_t));
    cmd.size = sizeof(uint64_t);
    cmdChannel->send(&cmd);
  }

  virtual uint64_t readReg(uint32_t reg) {
    simCmd cmd;
    simCmd *resp = NULL;
    cmd.id = globalID++;
    cmd.cmd = READ_REG;
    cmd.size = 0;
    uint32_t regWithBase = DEVICE_REG_BASE + reg;
    std::memcpy(cmd.data, &regWithBase, sizeof(uint32_t));
    cmdChannel->send(&cmd);
    resp = recvResp();
    ASSERT(resp->cmd == READ_REG, "Response from Sim is not READ_REG");
    uint64_t rdata = *(uint64_t*)resp->data;
    return rdata;
  }

  virtual uint64_t malloc(size_t bytes) {
    simCmd cmd;
    cmd.id = globalID++;
    cmd.cmd = MALLOC;
    std::memcpy(cmd.data, &bytes, sizeof(size_t));
    cmd.size = sizeof(size_t);
    cmdChannel->send(&cmd);
    simCmd *resp = recvResp();
    ASSERT(cmd.id == resp->id, "malloc resp->id does not match cmd.id!");
    ASSERT(cmd.cmd == resp->cmd, "malloc resp->cmd does not match cmd.cmd!");
    return (uint64_t)(*(uint64_t*)resp->data);
  }

  virtual void free(uint64_t buf) {
    simCmd cmd;
    cmd.id = globalID++;
    cmd.cmd = FREE;
    std::memcpy(cmd.data, &buf, sizeof(uint64_t));
    cmd.size = sizeof(uint64_t);
    cmdChannel->send(&cmd);
  }

  virtual void memcpy(uint64_t dst, void *src, size_t bytes) {
    simCmd cmd;
    cmd.id = globalID++;
    cmd.cmd = MEMCPY_H2D;
    uint64_t *data = (uint64_t*)cmd.data;
    data[0] = dst;
    data[1] = bytes;
    cmd.size = 2* sizeof(uint64_t);
    cmdChannel->send(&cmd);

    // Now send fixed 'bytes' from src
    cmdChannel->sendFixedBytes(src, bytes);

    // Wait for ack
    simCmd *resp = recvResp();
    ASSERT(cmd.id == resp->id, "memcpy resp->id does not match cmd.id!");
    ASSERT(cmd.cmd == resp->cmd, "memcpy resp->cmd does not match cmd.cmd!");
  }

  virtual void memcpy(void *dst, uint64_t src, size_t bytes) {
    simCmd cmd;
    cmd.id = globalID++;
    cmd.cmd = MEMCPY_D2H;
    uint64_t *data = (uint64_t*)cmd.data;
    data[0] = src;
    data[1] = bytes;
    cmd.size = 2* sizeof(uint64_t);
    cmdChannel->send(&cmd);

    // Now receive fixed 'bytes' from src
    respChannel->recvFixedBytes(dst, bytes);
  }

  void connect() {
    int id = sendCmd(READY);
    simCmd *cmd = recvResp();
    ASSERT(cmd->id == id, "Error: Received ID does not match sent ID\n");
    ASSERT(cmd->cmd == READY, "Error: Received cmd is not 'READY'\n");
    EPRINTF("Connection successful!\n");
  }

  FringeContextPlasticine(std::string path = "") : FringeContextBase(path) {
    configPath = path;
    cmdChannel = new Channel(sizeof(simCmd));
    respChannel = new Channel(sizeof(simCmd));

    simPath = std::string(checkAndGetEnvVar("SIM_PATH"));
    posix_spawn_file_actions_init(&action);

    // Create cmdPipe (read) handle at SIM_CMD_FD, respPipe (write) handle at SIM_RESP_FD
    // Close old descriptors after dup2
    posix_spawn_file_actions_addclose(&action, cmdChannel->writeFd());
    posix_spawn_file_actions_addclose(&action, respChannel->readFd());
    posix_spawn_file_actions_adddup2(&action, cmdChannel->readFd(), SIM_CMD_FD);
    posix_spawn_file_actions_adddup2(&action, respChannel->writeFd(), SIM_RESP_FD);

    std::string argsmem[] = {simPath};
    char *args[] = {&argsmem[0][0],nullptr};

    // Pass required environment variables to simulator
    // Required environment variables must be specified in "envVariablesToSim"
    char **envs = new char*[envVariablesToSim.size() + 1];
    std::string *valueStrs = new std::string[envVariablesToSim.size()];
    int i = 0;
    for (std::vector<std::string>::iterator it = envVariablesToSim.begin(); it != envVariablesToSim.end(); it++) {
      std::string var = *it;
      valueStrs[i] = var + "=" + std::string(checkAndGetEnvVar(var));
      envs[i] = &valueStrs[i][0];
      i++;
    }
    envs[envVariablesToSim.size()] = nullptr;

    if(posix_spawnp(&sim_pid, args[0], &action, NULL, &args[0], &envs[0]) != 0) {
      EPRINTF("posix_spawnp failed, error = %s\n", strerror(errno));
      exit(-1);
    }

    // Close Sim side of pipes
    close(cmdChannel->readFd());
    close(respChannel->writeFd());

    // Connect with simulator
    connect();

    // Configure settings from environment
    debugRegs = envToBool("DEBUG_REGS");
    long envCycles = atol("MAX_CYCLES");
    if (envCycles > 0) maxCycles = envCycles;
  }

  virtual void load() {
    // 0. Apply board reset for a few cycles
    for (int i=0; i<5; i++) {
      reset();
    }

    // Lower board reset
    start();

    // 1. Config file -> Host DRAM
    size_t size   = getFileSize(path.c_str());
    uint8_t *hostConfigMem  = (uint8_t*) std::malloc(size);
    int nbytes    = fileToBuf(hostConfigMem, configPath.c_str(), size);
    ASSERT(nbytes == size, "Bytes read (%d) does not match file size %lu!\n", nbytes, size);
    EPRINTF("[load] Configuration size: %d bytes\n", nbytes);

    // 2. Host DRAM -> Device DRAM
    uint64_t devConfigMem = malloc(size);
    memcpy(devConfigMem, hostConfigMem, size);

    // 3. Write address and size to config controller registers
    writeReg(CONFIG_BASE + CONFIG_ADDR_OFFSET, devConfigMem);
    writeReg(CONFIG_BASE + CONFIG_SIZE_OFFSET, size);

    // 4. Start config controller
    writeReg(CONFIG_BASE + COMMAND_OFFSET, 1);

    // 5. Poll on config controller's status register
    resetCycles();
    uint32_t status = readReg(CONFIG_BASE + STATUS_OFFSET);
    while (status == 0) {
      step();
      status = readReg(CONFIG_BASE + STATUS_OFFSET);
    }

    EPRINTF("[load] Configuration Complete, Time: %lu cycles\n", numCycles);
    writeReg(CONFIG_BASE + COMMAND_OFFSET, 0);
    while (status != 0) {
      step();
      status = readReg(CONFIG_BASE + STATUS_OFFSET);
    }
  }

  virtual void run() {
    // Current assumption is that the design sets arguments individually
    uint32_t status = 0;

    resetCycles();

    // Implement 4-way handshake
    writeReg(statusReg, 0);
    writeReg(commandReg, 1);

    while((status == 0) && (numCycles <= maxCycles)) {
      step();
      status = readReg(statusReg);
    }
    EPRINTF("Design ran for %lu cycles, status = %u\n", numCycles, status);
    if (status == 0) { // Design did not run to completion
      EPRINTF("=========================================\n");
      EPRINTF("ERROR: Simulation terminated after %lu cycles\n", numCycles);
      EPRINTF("=========================================\n");
    } else {  // Ran to completion, pull down command signal
      if (status & 0x2) { // Hardware timeout
        EPRINTF("=========================================\n");
        EPRINTF("Hardware timeout after %lu cycles\n", numCycles);
        EPRINTF("=========================================\n");
      }
      writeReg(commandReg, 0);
      while (status != 0) {
        step();
        status = readReg(statusReg);
      }
    }
  }

  virtual void setNumArgIns(uint32_t number) {
    numArgIns = number;
  }

  virtual void setNumArgIOs(uint32_t number) {
    numArgIOs = number;
  }

  virtual void setArg(uint32_t arg, uint64_t data, bool isIO) {
    writeReg(arg+2, data);
    numArgInsId++;
    if (isIO) numArgIOsId++;
  }

  virtual uint64_t getArg(uint32_t arg, bool isIO) {
    numArgOuts++;
    if (isIO) {
      return readReg(2+arg);
    } else {
      if (numArgIns == 0) {
        return readReg(1-numArgIOs+2+arg);
      } else {
        return readReg(numArgIns-numArgIOs+2+arg);
      }

    }
  }

  virtual uint64_t getArgIn(uint32_t arg, bool isIO) {
    return readReg(2+arg);
  }

  void dumpDebugRegs() {
    EPRINTF(" ******* Debug regs *******\n");
    int argInOffset = numArgIns == 0 ? 1 : numArgIns;
    int argOutOffset = numArgOuts == 0 ? 1 : numArgOuts;
    for (int i=0; i<NUM_DEBUG_SIGNALS; i++) {
      if (i % 16 == 0) EPRINTF("\n");
      uint64_t value = readReg(argInOffset + argOutOffset + 2 - numArgIOs + i);
      EPRINTF("\t%s: %08lx (%08lu)\n", signalLabels[i], value, value);
    }
    EPRINTF(" **************************\n");
  }

  ~FringeContextPlasticine() {
    if (debugRegs) {
      dumpDebugRegs();
    }
    finish();
  }
};

// Fringe Simulation APIs
void fringeInit(int argc, char **argv) {
}

#endif
