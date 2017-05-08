package org.web3j.protocol.rx;

import java.math.BigInteger;

import rx.Observable;

import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.Transaction;

/**
 * The Observables JSON-RPC client event API.
 */
public interface Web3jRx {

    /**
     * Create an observable to filter for specific log events on the blockchain.
     *
     * @param ethFilter filter criteria
     * @return Observable that emits all Log events matching the filter
     */
    Observable<Log> ethLogObservable(EthFilter ethFilter);

    /**
     * Create an Observable to emit block hashes.
     *
     * @return Observable that emits all new block hashes as new blocks are created on the
     * blockchain
     */
    Observable<String> ethBlockHashObservable();

    /**
     * Create an Observable to emit pending transactions, i.e. those transactions that have been
     * submitted by a node, but don't yet form part of a block (haven't been mined yet).
     *
     * @return Observable to emit pending transaction hashes.
     */
    Observable<String> ethPendingTransactionHashObservable();

    /**
     * Create an Observable to emit all new transactions as they are confirmed on the blockchain.
     * i.e. they have been mined and are incorporated into a block.
     *
     * @return Observable to emit new transactions on the blockchain
     */
    Observable<Transaction> transactionObservable();

    /**
     * Create an Observable to emit all pending transactions that have yet to be placed into a
     * block on the blockchain.
     *
     * @return Observable to emit pending transactions
     */
    Observable<Transaction> pendingTransactionObservable();

    /**
     * Create an Observable that emits newly created blocks on the blockchain.
     *
     * @param fullTransactionObjects if true, provides transactions embedded in blocks, otherwise
     *                              transaction hashes
     * @return Observable that emits all new blocks as they are added to the blockchain
     */
    Observable<EthBlock> blockObservable(boolean fullTransactionObjects);

    /**
     * Create an Observable that emits all blocks from the blockchain contained within the
     * requested range.
     *
     * @param startBlockNumber block number to commence with
     * @param endBlockNumber block number to finish with
     * @return Observable to emit these blocks
     */
    Observable<EthBlock> replayBlocksObservable(
            BigInteger startBlockNumber, BigInteger endBlockNumber, boolean fullTransactionObjects);

    /**
     * Create an Observable that emits all transactions from the blockchain contained within the
     * requested range.
     *
     * @param startBlockNumber block number to commence with
     * @param endBlockNumber block number to finish with
     * @return Observable to emit these transactions in the order they appear in the blocks
     */
    Observable<Transaction> replayTransactionsObservable(
            BigInteger startBlockNumber, BigInteger endBlockNumber);

    /**
     * Create an Observable that emits all transactions from the blockchain starting with a
     * provided block number. Once it has replayed up to the most current block, the provided
     * Observable is invoked.
     *
     * <p>To automatically subscribe to new blocks, use
     * {@link #catchUpToLatestAndSubscribeToNewBlocksObservable(BigInteger, boolean)}.
     *
     * @param startBlockNumber the block number we wish to request from
     * @param fullTransactionObjects if we require full {@link Transaction} objects to be provided in
     *                               the {@link EthBlock} responses
     * @param onCompleteObservable a subsequent Observable that we wish to run once we are caught
     *                             up with the latest block
     * @return Observable to emit all requested blocks
     */
    Observable<EthBlock> catchUpToLatestBlockObservable(
            BigInteger startBlockNumber, boolean fullTransactionObjects,
            Observable<EthBlock> onCompleteObservable);

    /**
     * Creates an Observable that emits all blocks from the requested block number to the most
     * current. Once it has emitted the most current block, it starts emitting new blocks as they
     * are created.
     *
     * @param startBlockNumber the block number we wish to request from
     * @param fullTransactionObjects if we require full {@link Transaction} objects to be provided in
     *                               the {@link EthBlock} responses
     * @return Observable to emit all requested blocks and future
     */
    Observable<EthBlock> catchUpToLatestAndSubscribeToNewBlocksObservable(
            BigInteger startBlockNumber, boolean fullTransactionObjects);

    /**
     * As per {@link #catchUpToLatestAndSubscribeToNewBlocksObservable(BigInteger, boolean)},
     * except that all transactions contained within the blocks are emitted.
     *
     * @param startBlockNumber
     * @return Observable to emit all requested transactions and future
     */
    Observable<Transaction> catchUpToLatestAndSubscribeToNewTransactionsObservable(
            BigInteger startBlockNumber);
}
