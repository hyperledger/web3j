package org.web3j.protocol.rx;

import java.util.List;
import java.util.concurrent.ExecutorService;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.subscriptions.Subscriptions;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.filters.BlockFilter;
import org.web3j.protocol.core.filters.Callback;
import org.web3j.protocol.core.filters.LogFilter;
import org.web3j.protocol.core.filters.PendingTransactionFilter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.Transaction;

/**
 * web3j reactive API implementation.
 */
public class JsonRpc2_0Rx {

    private final Web3j web3j;
    private final ExecutorService executorService;

    public JsonRpc2_0Rx(Web3j web3j, ExecutorService executorService) {
        this.web3j = web3j;
        this.executorService = executorService;
    }

    public Observable<String> ethBlockHashObservable(final long pollingInterval) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                BlockFilter blockFilter = new BlockFilter(
                        web3j, new Callback<String>() {
                    @Override
                    public void onEvent(final String value) {
                        subscriber.onNext(value);
                    }
                });
                JsonRpc2_0Rx.this.run(blockFilter, subscriber, pollingInterval);
            }
        });
    }

    public Observable<String> ethPendingTransactionHashObservable(final long pollingInterval) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                PendingTransactionFilter pendingTransactionFilter = new PendingTransactionFilter(
                        web3j, new Callback<String>() {
                    @Override
                    public void onEvent(final String value) {
                        subscriber.onNext(value);
                    }
                });

                JsonRpc2_0Rx.this.run(pendingTransactionFilter, subscriber, pollingInterval);
            }
        });
    }

    public Observable<Log> ethLogObservable(
            final org.web3j.protocol.core.methods.request.EthFilter ethFilter,
            final long pollingInterval) {
        return Observable.create(new Observable.OnSubscribe<Log>() {
            @Override
            public void call(final Subscriber<? super Log> subscriber) {
                LogFilter logFilter = new LogFilter(
                        web3j, new Callback<Log>() {
                    @Override
                    public void onEvent(final Log t) {
                        subscriber.onNext(t);
                    }
                }, ethFilter);

                JsonRpc2_0Rx.this.run(logFilter, subscriber, pollingInterval);
            }
        });
    }

    private <T> void run(
            final org.web3j.protocol.core.filters.Filter<T> filter, Subscriber<? super T> subscriber,
            final long pollingInterval) {

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                filter.run(pollingInterval);
            }
        });
        subscriber.add(Subscriptions.create(new Action0() {
            @Override
            public void call() {
                filter.cancel();
            }
        }));
    }

    public Observable<Transaction>  transactionObservable(final long pollingInterval) {
        return blockObservable(true, pollingInterval)
                .flatMapIterable(new Func1<EthBlock, Iterable<? extends Transaction>>() {
                    @Override
                    public Iterable<? extends Transaction> call(final EthBlock ethBlock) {
                        return (List) ethBlock.getBlock().getTransactions();
                    }
                });
    }

    public Observable<Transaction> pendingTransactionObservable(final long pollingInterval) {
        return ethPendingTransactionHashObservable(pollingInterval)
                .flatMap(new Func1<String, Observable<EthTransaction>>() {
                    @Override
                    public Observable<EthTransaction> call(final String transactionHash) {
                        return web3j.ethGetTransactionByHash(transactionHash).observable();
                    }
                })
                .map(new Func1<EthTransaction, Transaction>() {
                    @Override
                    public Transaction call(final EthTransaction ethTransaction) {
                        return ethTransaction.getTransaction();
                    }
                });
    }

    public Observable<EthBlock> blockObservable(
            final boolean fullTransactionObjects, final long pollingInterval) {
        return this.ethBlockHashObservable(pollingInterval)
                .flatMap(new Func1<String, Observable<? extends EthBlock>>() {
                    @Override
                    public Observable<? extends EthBlock> call(final String blockHash) {
                        return web3j.ethGetBlockByHash(blockHash,
                                fullTransactionObjects).observable();
                    }
                });
    }
}
