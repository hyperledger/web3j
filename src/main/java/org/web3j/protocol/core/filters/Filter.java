package org.web3j.protocol.core.filters;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.Request;
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

    private ScheduledFuture<?> schedule;

    public Filter(Web3j web3j, Callback<T> callback) {
        this.web3j = web3j;
        this.callback = callback;
    }

    public void run(ScheduledExecutorService scheduledExecutorService, long blockTime) {
        try {
            EthFilter ethFilter = sendRequest();
            if (ethFilter.hasError()) {
                throwException(ethFilter.getError());
            }

            filterId = ethFilter.getFilterId();
            EthLog ethLogInit = this.createFilterRequest(this.filterId).send();
            process(ethLogInit.getLogs());

            schedule = scheduledExecutorService.scheduleAtFixedRate(() -> {
                EthLog ethLog = null;
                try {
                    ethLog = web3j.ethGetFilterChanges(filterId).send();
                } catch (IOException e) {
                    throwException(e);
                }
                if (ethLog.hasError()) {
                    throwException(ethFilter.getError());
                }

                process(ethLog.getLogs());
            }, 0, blockTime, TimeUnit.MILLISECONDS);


        } catch (IOException e) {
            throwException(e);
        }
    }

    abstract EthFilter sendRequest() throws IOException;

    abstract void process(List<EthLog.LogResult> logResults);

    public void cancel() {
        schedule.cancel(false);

        EthUninstallFilter ethUninstallFilter = null;
        try {
            ethUninstallFilter = web3j.ethUninstallFilter(filterId).send();
        } catch (IOException e) {
            throwException(e);
        }

        if (ethUninstallFilter.hasError()) {
            throwException(ethUninstallFilter.getError());
        }

        if (!ethUninstallFilter.isUninstalled()) {
            throwException(ethUninstallFilter.getError());
        }
    }

    protected abstract Request<?, EthLog> createFilterRequest(BigInteger filterId);

    void throwException(Response.Error error) {
        throw new FilterException("Invalid request: " + error.getMessage());
    }

    void throwException(Throwable cause) {
        throw new FilterException("Error sending request", cause);
    }
}

