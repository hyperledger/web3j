package org.web3j.protocol.pantheon.response;

import java.util.List;

public class FullDebugTraceInfo {

    private int gas;
    private boolean failed;
    private String returnValue;
    private List<StructLogs> structLogs;

    public FullDebugTraceInfo(int gas, boolean failed, String returnValue, List<StructLogs> structLogs) {
        this.gas = gas;
        this.failed = failed;
        this.returnValue = returnValue;
        this.structLogs = structLogs;
    }

    public int getGas() {
        return gas;
    }

    public void setGas() { this.gas = gas; }

    public boolean getFailed() {
        return failed;
    }

    public void setFailed() {
        this.failed = failed;
    }

    public String getReturnValue() { return returnValue; }

    public void serReturnValye() { this.returnValue = returnValue; }

    public List<StructLogs> getStructLogs() {
        return structLogs;
    }

    public void setStructLogs() { this.structLogs = structLogs; }
}