package org.web3j.protocol.parity.methods.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.web3j.protocol.core.DefaultBlockParameter;

/**
 * TraceFilter used in trace_filter.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TraceFilter {

    private DefaultBlockParameter fromBlock;
    private DefaultBlockParameter toBlock;
    private List<String> fromAddress;
    private List<String> toAddress;

    public TraceFilter(DefaultBlockParameter fromBlock, DefaultBlockParameter toBlock,
            List<String> fromAddress, List<String> toAddress) {
        this.fromBlock = fromBlock;
        this.toBlock = toBlock;
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
    }

    public DefaultBlockParameter getFromBlock() {
        return fromBlock;
    }

    public DefaultBlockParameter getToBlock() {
        return toBlock;
    }

    public List<String> getFromAddress() {
        return fromAddress;
    }

    public List<String> getToAddress() {
        return toAddress;
    }
}
