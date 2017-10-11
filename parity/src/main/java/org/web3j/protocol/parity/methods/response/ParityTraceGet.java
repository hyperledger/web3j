package org.web3j.protocol.parity.methods.response;

import org.web3j.protocol.core.Response;

/**
 * trace_get.
 */
public class ParityTraceGet extends Response<Trace> {
    public Trace getTrace() {
        return getResult();
    }
}
