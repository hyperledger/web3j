package org.web3j.protocol.core.filters;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.response.EthFilter;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.core.methods.response.EthUninstallFilter;


/**
 * Class for creating managed filter requests with callbacks.
 */
public abstract class Filter<T> {

    final Web3j web3j;
    final Callback<T> callback;

    private volatile BigInteger filterId;
    private volatile boolean canceled = false;

    public Filter(Web3j web3j, Callback<T> callback) {
        this.web3j = web3j;
        this.callback = callback;
    }

    public void run(long blockTime) {
        try {
            EthFilter ethFilter = sendRequest();
            if (ethFilter.hasError()) {
                throwException(ethFilter.getError());
            }

            filterId = ethFilter.getFilterId();

            while (!canceled) {
                EthLog ethLog = web3j.ethGetFilterChanges(filterId).sendAsync().get();
                if (ethLog.hasError()) {
                    throwException(ethFilter.getError());
                }

                process(ethLog.getLogs());

                Thread.sleep(blockTime);
            }
        } catch (InterruptedException | ExecutionException e) {
            throwException(e);
        }
    }

    abstract EthFilter sendRequest() throws ExecutionException, InterruptedException;

    abstract void process(List<EthLog.LogResult> logResults);

    public void cancel() {
        canceled = true;

        EthUninstallFilter ethUninstallFilter = null;
        try {
            ethUninstallFilter = web3j.ethUninstallFilter(filterId).sendAsync().get();
        } catch (InterruptedException | ExecutionException e) {
            throwException(e);
        }

        if (ethUninstallFilter.hasError()) {
            throwException(ethUninstallFilter.getError());
        }

        if (!ethUninstallFilter.isUninstalled()) {
            throwException(ethUninstallFilter.getError());
        }
    }

    void throwException(Response.Error error) {
        throw new FilterException("Invalid request: " + error.getMessage());
    }

    void throwException(Throwable cause) {
        throw new FilterException("Error sending request", cause);
    }
}

