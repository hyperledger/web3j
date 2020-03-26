/*
 * Copyright 2019 Web3 Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.web3j.protocol.besu.response;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public class StructLogs {

    private int pc;
    private String op;
    private int gas;
    private int gasCost;
    private int depth;
    private List<String> stack;
    private List<String> memory;
    private Map<BigInteger, String> storage;

    public StructLogs(
            final int pc,
            final String op,
            final int gas,
            final int gasCost,
            final int depth,
            final List<String> stack,
            final List<String> memory,
            final Map<BigInteger, String> storage) {
        this.pc = pc;
        this.op = op;
        this.gas = gas;
        this.gasCost = gasCost;
        this.depth = depth;
        this.stack = stack;
        this.memory = memory;
        this.storage = storage;
    }

    public void setPc(final int pc) {
        this.pc = pc;
    }

    public void setOp(final String op) {
        this.op = op;
    }

    public void setGas(final int gas) {
        this.gas = gas;
    }

    public void setGasCost(final int gasCost) {
        this.gasCost = gasCost;
    }

    public void setDepth(final int depth) {
        this.depth = depth;
    }

    public void setStack(final List<String> stack) {
        this.stack = stack;
    }

    public void setMemory(final List<String> memory) {
        this.memory = memory;
    }

    public void setStorage(final Map<BigInteger, String> storage) {
        this.storage = storage;
    }

    public int getPc() {
        return pc;
    }

    public String getOp() {
        return op;
    }

    public int getGas() {
        return gas;
    }

    public int getGasCost() {
        return gasCost;
    }

    public int getDepth() {
        return depth;
    }

    public List<String> getStack() {
        return stack;
    }

    public List<String> getMemory() {
        return memory;
    }

    public Map<BigInteger, String> getStorage() {
        return storage;
    }
}
