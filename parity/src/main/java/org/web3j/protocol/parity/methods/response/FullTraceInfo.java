package org.web3j.protocol.parity.methods.response;

import java.util.List;
import java.util.Map;

/**
 * FullTraceInfo used in following methods.
 * <ol>
 *     <li>trace_call</li>
 *     <li>trace_rawTransaction</li>
 *     <li>trace_replayTransaction</li>
 * </ol>
 */
public class FullTraceInfo {

    private String output;
    private Map<String, StateDiff> stateDiff;
    private List<Trace> trace;
    private VMTrace vmTrace;

    public FullTraceInfo() {
    }

    public FullTraceInfo(String output, Map<String, StateDiff> stateDiff,
            List<Trace> trace, VMTrace vmTrace) {
        this.output = output;
        this.stateDiff = stateDiff;
        this.trace = trace;
        this.vmTrace = vmTrace;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public Map<String, StateDiff> getStateDiff() {
        return stateDiff;
    }

    public void setStateDiff(Map<String, StateDiff> stateDiff) {
        this.stateDiff = stateDiff;
    }

    public List<Trace> getTrace() {
        return trace;
    }

    public void setTrace(List<Trace> trace) {
        this.trace = trace;
    }

    public VMTrace getVmTrace() {
        return vmTrace;
    }

    public void setVmTrace(VMTrace vmTrace) {
        this.vmTrace = vmTrace;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof FullTraceInfo)) {
            return false;
        }

        FullTraceInfo that = (FullTraceInfo) o;

        if (getOutput() != null ? !getOutput().equals(that.getOutput())
                : that.getOutput() != null) {
            return false;
        }
        if (getStateDiff() != null ? !getStateDiff().equals(that.getStateDiff())
                : that.getStateDiff() != null) {
            return false;
        }
        if (getTrace() != null ? !getTrace().equals(that.getTrace())
                : that.getTrace() != null) {
            return false;
        }
        return getVmTrace() != null ? getVmTrace().equals(that.getVmTrace())
                : that.getVmTrace() == null;
    }

    @Override
    public int hashCode() {
        int result = getOutput() != null ? getOutput().hashCode() : 0;
        result = 31 * result + (getStateDiff() != null ? getStateDiff().hashCode() : 0);
        result = 31 * result + (getTrace() != null ? getTrace().hashCode() : 0);
        result = 31 * result + (getVmTrace() != null ? getVmTrace().hashCode() : 0);
        return result;
    }

}
