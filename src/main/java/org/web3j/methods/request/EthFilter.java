package org.web3j.methods.request;

import java.util.Arrays;
import java.util.List;

import org.web3j.protocol.jsonrpc20.DefaultBlockParameter;

/**
 * Filter implementation as per <a href="https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_newfilter">docs</a>
 */
public class EthFilter extends Filter {
    private DefaultBlockParameter fromBlock;
    private DefaultBlockParameter toBlock;
    private List<String> address;  // specification implies this can be single address as string or list

    public EthFilter() {
        super();
        fromBlock = DefaultBlockParameter.valueOf("latest");
        toBlock = DefaultBlockParameter.valueOf("latest");
    }

    public EthFilter(DefaultBlockParameter fromBlock, DefaultBlockParameter toBlock,
                     List<String> address) {
        super();
        this.fromBlock = fromBlock;
        this.toBlock = toBlock;
        this.address = address;
    }

    public EthFilter(DefaultBlockParameter fromBlock, DefaultBlockParameter toBlock,
                     String address) {
        this(fromBlock, toBlock, Arrays.asList(address));
    }

    public DefaultBlockParameter getFromBlock() {
        return fromBlock;
    }

    public DefaultBlockParameter getToBlock() {
        return toBlock;
    }

    public List<String> getAddress() {
        return address;
    }
}
