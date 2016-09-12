package org.web3j.methods.request;

/**
 * Filter implementation as per <a href="https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_newfilter">docs</a>
 */
public class ShhFilter extends Filter {
    private String toBlock;

    public ShhFilter(String toBlock) {
        super();
        this.toBlock = toBlock;
    }

    public String getToBlock() {
        return toBlock;
    }
}
