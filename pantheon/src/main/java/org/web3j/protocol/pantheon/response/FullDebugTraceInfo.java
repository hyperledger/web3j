/*
 * Copyright 2019 Web3 Labs LTD.
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
package org.web3j.protocol.pantheon.response;

import java.util.List;

public class FullDebugTraceInfo {

    private int gas;
    private boolean failed;
    private String returnValue;
    private List<StructLogs> structLogs;

    public FullDebugTraceInfo(
            int gas, boolean failed, String returnValue, List<StructLogs> structLogs) {
        this.gas = gas;
        this.failed = failed;
        this.returnValue = returnValue;
        this.structLogs = structLogs;
    }

    public int getGas() {
        return gas;
    }

    public void setGas() {
        this.gas = gas;
    }

    public boolean getFailed() {
        return failed;
    }

    public void setFailed() {
        this.failed = failed;
    }

    public String getReturnValue() {
        return returnValue;
    }

    public void serReturnValye() {
        this.returnValue = returnValue;
    }

    public List<StructLogs> getStructLogs() {
        return structLogs;
    }

    public void setStructLogs() {
        this.structLogs = structLogs;
    }
}
