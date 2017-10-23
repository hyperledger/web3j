package org.web3j.protocol.rx;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.filters.BlockFilter;
import org.web3j.protocol.core.filters.Callback;
import org.web3j.protocol.core.filters.LogFilter;
import org.web3j.protocol.core.filters.PendingTransactionFilter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.utils.Collection;
import org.web3j.utils.Observables;

/**
 * web3j reactive API implementation.
 */
public class JsonRpc2_0Rx {

    private final Web3j web3j;
    private final ScheduledExecutorService scheduledExecutorService;
    private final Scheduler scheduler;

    public JsonRpc2_0Rx(Web3j web3j, ScheduledExecutorService scheduledExecutorService) {
        this.web3j = web3j;
        this.scheduledExecutorService = scheduledExecutorService;
        this.scheduler = Schedulers.from(scheduledExecutorService);
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
            final org.web3j.protocol.core.filters.Filter<T> filter,
            Subscriber<? super T> subscriber,
            final long pollingInterval) {

        filter.run(scheduledExecutorService, pollingInterval);
        subscriber.add(Subscriptions.create(new Action0() {
            @Override
            public void call() {
                filter.cancel();
            }
        }));
        scheduledExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                filter.run(scheduledExecutorService, pollingInterval);
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
                        return JsonRpc2_0Rx.this.toTransactions(ethBlock);
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

    public Observable<EthBlock> replayBlocksObservable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock,
            boolean fullTransactionObjects) {
        return replayBlocksObservable(startBlock, endBlock, fullTransactionObjects, true);
    }

    public Observable<EthBlock> replayBlocksObservable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock,
            boolean fullTransactionObjects, boolean ascending) {
        // We use a scheduler to ensure this Observable runs asynchronously for users to be
        // consistent with the other Observables
        return replayBlocksObservableSync(startBlock, endBlock, fullTransactionObjects, ascending)
                .subscribeOn(scheduler);
    }

    private Observable<EthBlock> replayBlocksObservableSync(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock,
            final boolean fullTransactionObjects) {
        return replayBlocksObservableSync(startBlock, endBlock, fullTransactionObjects, true);
    }

    private Observable<EthBlock> replayBlocksObservableSync(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock,
            final boolean fullTransactionObjects, boolean ascending) {

        BigInteger startBlockNumber = null;
        BigInteger endBlockNumber = null;
        try {
            startBlockNumber = getBlockNumber(startBlock);
            endBlockNumber = getBlockNumber(endBlock);
        } catch (IOException e) {
            Observable.error(e);
        }

        if (ascending) {
            return Observables.range(startBlockNumber, endBlockNumber)
                    .flatMap(new Func1<BigInteger, Observable<? extends EthBlock>>() {
                        @Override
                        public Observable<? extends EthBlock> call(BigInteger i) {
                            return web3j.ethGetBlockByNumber(
                                    new DefaultBlockParameterNumber(i),
                                    fullTransactionObjects).observable();
                        }
                    });
        } else {
            return Observables.range(startBlockNumber, endBlockNumber, false)
                    .flatMap(new Func1<BigInteger, Observable<? extends EthBlock>>() {
                        @Override
                        public Observable<? extends EthBlock> call(BigInteger i) {
                            return web3j.ethGetBlockByNumber(
                                    new DefaultBlockParameterNumber(i),
                                    fullTransactionObjects).observable();
                        }
                    });
        }


    }

    public Observable<Transaction> replayTransactionsObservable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        return replayBlocksObservable(startBlock, endBlock, true)
                .flatMapIterable(new Func1<EthBlock, Iterable<? extends Transaction>>() {
                    @Override
                    public Iterable<? extends Transaction> call(EthBlock ethBlock) {
                        return toTransactions(ethBlock);
                    }
                });
    }

    public Observable<EthBlock> catchUpToLatestBlockObservable(
            DefaultBlockParameter startBlock, boolean fullTransactionObjects,
            Observable<EthBlock> onCompleteObservable) {
        // We use a scheduler to ensure this Observable runs asynchronously for users to be
        // consistent with the other Observables
        return catchUpToLatestBlockObservableSync(
                startBlock, fullTransactionObjects, onCompleteObservable)
                .subscribeOn(scheduler);
    }

    public Observable<EthBlock> catchUpToLatestBlockObservable(
            DefaultBlockParameter startBlock, boolean fullTransactionObjects) {
        return catchUpToLatestBlockObservable(
                startBlock, fullTransactionObjects, Observable.<EthBlock>empty());
    }

    private Observable<EthBlock> catchUpToLatestBlockObservableSync(
            DefaultBlockParameter startBlock, final boolean fullTransactionObjects,
            final Observable<EthBlock> onCompleteObservable) {

        BigInteger startBlockNumber;
        final BigInteger latestBlockNumber;
        try {
            startBlockNumber = getBlockNumber(startBlock);
            latestBlockNumber = getLatestBlockNumber();
        } catch (IOException e) {
            return Observable.error(e);
        }

        if (startBlockNumber.compareTo(latestBlockNumber) > -1) {
            return onCompleteObservable;
        } else {
            return Observable.concat(
                    replayBlocksObservableSync(
                            new DefaultBlockParameterNumber(startBlockNumber),
                            new DefaultBlockParameterNumber(latestBlockNumber),
                            fullTransactionObjects),
                    Observable.defer(new Func0<Observable<EthBlock>>() {
                        @Override
                        public Observable<EthBlock> call() {
                            return JsonRpc2_0Rx.this.catchUpToLatestBlockObservableSync(
                                    new DefaultBlockParameterNumber(
                                            latestBlockNumber.add(BigInteger.ONE)),
                                    fullTransactionObjects,
                                    onCompleteObservable);
                        }
                    }));
        }
    }

    public Observable<Transaction> catchUpToLatestTransactionObservable(
            DefaultBlockParameter startBlock) {
        return catchUpToLatestBlockObservable(
                startBlock, true, Observable.<EthBlock>empty())
                .flatMapIterable(new Func1<EthBlock, Iterable<? extends Transaction>>() {
                    @Override
                    public Iterable<? extends Transaction> call(EthBlock ethBlock) {
                        return toTransactions(ethBlock);
                    }
                });
    }

    public Observable<EthBlock> catchUpToLatestAndSubscribeToNewBlocksObservable(
            DefaultBlockParameter startBlock, boolean fullTransactionObjects,
            long pollingInterval) {

        return catchUpToLatestBlockObservable(
                startBlock, fullTransactionObjects,
                blockObservable(fullTransactionObjects, pollingInterval));
    }

    public Observable<Transaction> catchUpToLatestAndSubscribeToNewTransactionsObservable(
            DefaultBlockParameter startBlock, long pollingInterval) {
        return catchUpToLatestAndSubscribeToNewBlocksObservable(
                startBlock, true, pollingInterval)
                .flatMapIterable(new Func1<EthBlock, Iterable<? extends Transaction>>() {
                    @Override
                    public Iterable<? extends Transaction> call(EthBlock ethBlock) {
                        return toTransactions(ethBlock);
                    }
                });
    }

    private BigInteger getLatestBlockNumber() throws IOException {
        return getBlockNumber(DefaultBlockParameterName.LATEST);
    }

    private BigInteger getBlockNumber(
            DefaultBlockParameter defaultBlockParameter) throws IOException {
        if (defaultBlockParameter instanceof DefaultBlockParameterNumber) {
            return ((DefaultBlockParameterNumber) defaultBlockParameter).getBlockNumber();
        } else {
            EthBlock latestEthBlock = web3j.ethGetBlockByNumber(
                    defaultBlockParameter, false).send();
            return latestEthBlock.getBlock().getNumber();
        }
    }

    private static List<Transaction> toTransactions(EthBlock ethBlock) {
        // If you ever see an exception thrown here, it's probably due to an incomplete chain in
        // Geth/Parity. You should resync to solve.
        List<EthBlock.TransactionResult> transactionResults = ethBlock.getBlock().getTransactions();
        List<Transaction> transactions = new ArrayList<Transaction>(transactionResults.size());

        for (EthBlock.TransactionResult transactionResult : transactionResults) {
            transactions.add((Transaction) transactionResult.get());
        }
        return transactions;
    }
}
