package org.web3j.protocol.core.methods.request;

/**
 * Filter implementation as per <a href="https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_newfilter">docs</a>
 */
public class ShhFilter extends Filter<ShhFilter> {
    private String to;

    public ShhFilter(String to) {
        super();
        this.to = to;
    }

    public String getTo() {
        return to;
    }

    @Override
    Filter getThis() {
        return this;
    }
}
