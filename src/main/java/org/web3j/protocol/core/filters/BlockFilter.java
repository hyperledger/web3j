package org.web3j.protocol.core.filters;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthFilter;
import org.web3j.protocol.core.methods.response.EthLog;

/**
 * Handler for working with block filter requests.
 */
public class BlockFilter extends Filter<String> {

    public BlockFilter(Web3j web3j, Callback<String> callback) {
        super(web3j, callback);
    }

    @Override
    EthFilter sendRequest() throws IOException {
        return web3j.ethNewBlockFilter().send();
    }

    @Override
    void process(List<EthLog.LogResult> logResults) {
        for (EthLog.LogResult logResult : logResults) {
            if (logResult instanceof EthLog.Hash) {
                String blockHash = ((EthLog.Hash) logResult).get();
                callback.onEvent(blockHash);
            } else {
                throw new FilterException(
                        "Unexpected result type: " + logResult.get() + ", required Hash");
            }
        }
    }
}

