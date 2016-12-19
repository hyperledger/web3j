package org.web3j.protocol.core.rx;

import java.util.List;
import java.util.concurrent.ExecutorService;

import rx.Observable;
import rx.Subscriber;
import rx.subscriptions.Subscriptions;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.filters.BlockFilter;
import org.web3j.protocol.core.filters.LogFilter;
import org.web3j.protocol.core.filters.PendingTransactionFilter;
import org.web3j.protocol.core.methods.response.EthBlock;
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

    public Observable<String> ethBlockHashObservable(long pollingInterval) {
        return Observable.create(subscriber -> {
            BlockFilter blockFilter = new BlockFilter(
                    web3j, value -> subscriber.onNext(value));
            run(blockFilter, subscriber, pollingInterval);
        });
    }

    public Observable<String> ethPendingTransactionHashObservable(long pollingInterval) {
        return Observable.create(subscriber -> {
            PendingTransactionFilter pendingTransactionFilter = new PendingTransactionFilter(
                    web3j, value -> subscriber.onNext(value));

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

        executorService.submit(() -> filter.run(pollingInterval));
        subscriber.add(Subscriptions.create(filter::cancel));
    }

    public Observable<Transaction>  transactionObservable(long pollingInterval) {
        return blockObservable(true, pollingInterval)
                .flatMapIterable(ethBlock -> (List) ethBlock.getBlock().get().getTransactions());
    }

    public Observable<Transaction> pendingTransactionObservable(long pollingInterval) {
        return ethPendingTransactionHashObservable(pollingInterval)
                .flatMap(transactionHash ->
                        web3j.ethGetTransactionByHash(transactionHash).observable())
                .filter(ethTransaction -> ethTransaction.getTransaction().isPresent())
                .map(ethTransaction -> ethTransaction.getTransaction().get());
    }

    public Observable<EthBlock> blockObservable(
            boolean fullTransactionObjects, long pollingInterval) {
        return this.ethBlockHashObservable(pollingInterval)
                .flatMap(blockHash ->
                        web3j.ethGetBlockByHash(blockHash, fullTransactionObjects).observable());
    }
}
