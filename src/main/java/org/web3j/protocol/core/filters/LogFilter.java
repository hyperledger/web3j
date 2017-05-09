package org.web3j.protocol.core.filters;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthFilter;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.core.methods.response.Log;

/**
 * Log filter handler
 */
public class LogFilter extends Filter<Log> {

    private final org.web3j.protocol.core.methods.request.EthFilter ethFilter;

    public LogFilter(
            Web3j web3j, Callback<Log> callback,
            org.web3j.protocol.core.methods.request.EthFilter ethFilter) {
        super(web3j, callback);
        this.ethFilter = ethFilter;
    }

    @Override
    EthFilter sendRequest() throws IOException {
        return web3j.ethNewFilter(ethFilter).send();
    }

    @Override
    void process(List<EthLog.LogResult> logResults) {
        for (EthLog.LogResult logResult : logResults) {
            if (logResult instanceof EthLog.LogObject) {
                Log log = ((EthLog.LogObject) logResult).get();
                callback.onEvent(log);
            } else {
                throw new FilterException(
                        "Unexpected result type: " + logResult.get() + " required LogObject");
            }
        }
    }
}
