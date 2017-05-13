package org.web3j.protocol.rx;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

import rx.Observable;
import rx.Subscriber;
import rx.subscriptions.Subscriptions;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.filters.BlockFilter;
import org.web3j.protocol.core.filters.LogFilter;
import org.web3j.protocol.core.filters.PendingTransactionFilter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.utils.Observables;

/**
 * web3j reactive API implementation.
 */
public class JsonRpc2_0Rx {

    private final Web3j web3j;
    private final ScheduledExecutorService scheduledExecutorService;

    public JsonRpc2_0Rx(Web3j web3j, ScheduledExecutorService scheduledExecutorService) {
        this.web3j = web3j;
        this.scheduledExecutorService = scheduledExecutorService;
    }

    public Observable<String> ethBlockHashObservable(long pollingInterval) {
        return Observable.create(subscriber -> {
            BlockFilter blockFilter = new BlockFilter(
                    web3j, subscriber::onNext);
            run(blockFilter, subscriber, pollingInterval);
        });
    }

    public Observable<String> ethPendingTransactionHashObservable(long pollingInterval) {
        return Observable.create(subscriber -> {
            PendingTransactionFilter pendingTransactionFilter = new PendingTransactionFilter(
                    web3j, subscriber::onNext);

            run(pendingTransactionFilter, subscriber, pollingInterval);
        });
    }

    public Observable<Log> ethLogObservable(
            org.web3j.protocol.core.methods.request.EthFilter ethFilter, long pollingInterval) {
        return Observable.create((Subscriber<? super Log> subscriber) -> {
            LogFilter logFilter = new LogFilter(
                    web3j, subscriber::onNext, ethFilter);

            run(logFilter, subscriber, pollingInterval);
        });
    }

    private <T> void run(
            org.web3j.protocol.core.filters.Filter<T> filter, Subscriber<? super T> subscriber,
            long pollingInterval) {

        filter.run(scheduledExecutorService, pollingInterval);
        subscriber.add(Subscriptions.create(filter::cancel));
    }

    public Observable<Transaction> transactionObservable(long pollingInterval) {
        return blockObservable(true, pollingInterval)
                .flatMapIterable(JsonRpc2_0Rx::toTransactions);
    }

    public Observable<Transaction> pendingTransactionObservable(long pollingInterval) {
        return ethPendingTransactionHashObservable(pollingInterval)
                .flatMap(transactionHash ->
                        web3j.ethGetTransactionByHash(transactionHash).observable())
                .map(ethTransaction -> ethTransaction.getTransaction().get());
    }

    public Observable<EthBlock> blockObservable(
            boolean fullTransactionObjects, long pollingInterval) {
        return ethBlockHashObservable(pollingInterval)
                .flatMap(blockHash ->
                        web3j.ethGetBlockByHash(blockHash, fullTransactionObjects).observable());
    }

    public Observable<EthBlock> replayBlocksObservable(
            BigInteger startBlockNumber, BigInteger endBlockNumber,
            boolean fullTransactionObjects) {

        return Observables.range(startBlockNumber, endBlockNumber)
                .flatMap(i -> web3j.ethGetBlockByNumber(
                        new DefaultBlockParameterNumber(i), fullTransactionObjects).observable());
    }

    public Observable<Transaction> replayTransactionsObservable(
            BigInteger startBlockNumber, BigInteger endBlockNumber) {
        return replayBlocksObservable(startBlockNumber, endBlockNumber, true)
                .flatMapIterable(JsonRpc2_0Rx::toTransactions);
    }

    public Observable<EthBlock> catchUpToLatestBlockObservable(
        BigInteger startBlockNumber, boolean fullTransactionObjects,
        Observable<EthBlock> onCompleteObservable) {

        BigInteger latestBlockNumber;
        try {
            latestBlockNumber = getLatestBlockNumber();
        } catch (IOException e) {
            return Observable.error(e);
        }

        if (startBlockNumber.compareTo(latestBlockNumber) > -1) {
            return onCompleteObservable;
        } else {
            return Observable.concat(
                    replayBlocksObservable(
                            startBlockNumber, latestBlockNumber,
                            fullTransactionObjects),
                    Observable.defer(() -> catchUpToLatestBlockObservable(
                            latestBlockNumber.add(BigInteger.ONE), fullTransactionObjects,
                            onCompleteObservable)));
        }
    }

    public Observable<EthBlock> catchUpToLatestAndSubscribeToNewBlocksObservable(
            BigInteger startBlockNumber, boolean fullTransactionObjects, long pollingInterval) {
        return catchUpToLatestBlockObservable(
                startBlockNumber, fullTransactionObjects,
                blockObservable(fullTransactionObjects, pollingInterval));
    }

    public Observable<Transaction> catchUpToLatestAndSubscribeToNewTransactionsObservable(
            BigInteger startBlockNumber, long pollingInterval) {
        return catchUpToLatestAndSubscribeToNewBlocksObservable(
                startBlockNumber, true, pollingInterval)
                .flatMapIterable(JsonRpc2_0Rx::toTransactions);
    }

    private BigInteger getLatestBlockNumber() throws IOException {
        EthBlock latestEthBlock = web3j.ethGetBlockByNumber(
                DefaultBlockParameterName.LATEST, false).send();
        return latestEthBlock.getBlock().getNumber();
    }

    private static List<Transaction> toTransactions(EthBlock ethBlock) {
        return ethBlock.getBlock().getTransactions().stream()
                .map(transactionResult -> (Transaction) transactionResult.get())
                .collect(Collectors.toList());
    }
}
