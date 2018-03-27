package org.web3j.protocol.rx;

import java.util.List;

import rx.Observable;

import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.websocket.events.LogNotification;
import org.web3j.protocol.websocket.events.NewHeadsNotification;
import org.web3j.protocol.websocket.events.PendingTransactionNotification;
import org.web3j.protocol.websocket.events.SyncingNotfication;

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
     *         blockchain
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
     * @param startBlock block number to commence with
     * @param endBlock block number to finish with
     * @param fullTransactionObjects if true, provides transactions embedded in blocks, otherwise
     *                               transaction hashes
     * @return Observable to emit these blocks
     */
    Observable<EthBlock> replayBlocksObservable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock,
            boolean fullTransactionObjects);

    /**
     * Create an Observable that emits all blocks from the blockchain contained within the
     * requested range.
     *
     * @param startBlock block number to commence with
     * @param endBlock block number to finish with
     * @param fullTransactionObjects if true, provides transactions embedded in blocks, otherwise
     *                               transaction hashes
     * @param ascending if true, emits blocks in ascending order between range, otherwise
     *                  in descending order
     * @return Observable to emit these blocks
     */
    Observable<EthBlock> replayBlocksObservable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock,
            boolean fullTransactionObjects, boolean ascending);

    /**
     * Create an Observable that emits all transactions from the blockchain contained within the
     * requested range.
     *
     * @param startBlock block number to commence with
     * @param endBlock block number to finish with
     * @return Observable to emit these transactions in the order they appear in the blocks
     */
    Observable<Transaction> replayTransactionsObservable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock);

    /**
     * Create an Observable that emits all transactions from the blockchain starting with a
     * provided block number. Once it has replayed up to the most current block, the provided
     * Observable is invoked.
     *
     * <p>To automatically subscribe to new blocks, use
     * {@link #catchUpToLatestAndSubscribeToNewBlocksObservable(DefaultBlockParameter, boolean)}.
     *
     * @param startBlock the block number we wish to request from
     * @param fullTransactionObjects if we require full {@link Transaction} objects to be provided
     *                              in the {@link EthBlock} responses
     * @param onCompleteObservable a subsequent Observable that we wish to run once we are caught
     *                             up with the latest block
     * @return Observable to emit all requested blocks
     */
    Observable<EthBlock> catchUpToLatestBlockObservable(
            DefaultBlockParameter startBlock, boolean fullTransactionObjects,
            Observable<EthBlock> onCompleteObservable);

    /**
     * Creates an Observable that emits all blocks from the requested block number to the most
     * current. Once it has emitted the most current block, onComplete is called.
     *
     * @param startBlock the block number we wish to request from
     * @param fullTransactionObjects if we require full {@link Transaction} objects to be provided
     *                               in the {@link EthBlock} responses
     * @return Observable to emit all requested blocks
     */
    Observable<EthBlock> catchUpToLatestBlockObservable(
            DefaultBlockParameter startBlock, boolean fullTransactionObjects);

    /**
     * Creates an Observable that emits all transactions from the requested block number to the most
     * current. Once it has emitted the most current block's transactions, onComplete is called.
     *
     * @param startBlock the block number we wish to request from
     * @return Observable to emit all requested transactions
     */
    Observable<Transaction> catchUpToLatestTransactionObservable(
            DefaultBlockParameter startBlock);

    /**
     * Creates an Observable that emits all blocks from the requested block number to the most
     * current. Once it has emitted the most current block, it starts emitting new blocks as they
     * are created.
     *
     * @param startBlock the block number we wish to request from
     * @param fullTransactionObjects if we require full {@link Transaction} objects to be provided
     *                               in the {@link EthBlock} responses
     * @return Observable to emit all requested blocks and future
     */
    Observable<EthBlock> catchUpToLatestAndSubscribeToNewBlocksObservable(
            DefaultBlockParameter startBlock, boolean fullTransactionObjects);

    /**
     * As per
     * {@link #catchUpToLatestAndSubscribeToNewBlocksObservable(DefaultBlockParameter, boolean)},
     * except that all transactions contained within the blocks are emitted.
     *
     * @param startBlock the block number we wish to request from
     * @return Observable to emit all requested transactions and future
     */
    Observable<Transaction> catchUpToLatestAndSubscribeToNewTransactionsObservable(
            DefaultBlockParameter startBlock);

    /**
     * Creates an observable that emits a notification when a new header is appended to a chain,
     * including chain reorganizations.
     *
     * @return Observable that emits a notification for every new header
     */
    Observable<NewHeadsNotification> newHeadsNotifications();

    /**
     * Creates an observable that emits notifications for logs included in new imported blocks.
     *
     * @param addresses only return logs from this list of address. Return logs from all addresses
     *                  if the list is empty
     * @param topics only return logs that match specified topics. Returns logs for all topics if
     *               the list is empty
     * @return Observable that emits logs included in new blocks
     */
    Observable<LogNotification> logsNotifications(List<String> addresses, List<String> topics);

    /**
     * Creates an observable that emits a notification when a new transaction is added
     * to the pending state and is signed with a key that is available in the node.
     *
     * @return Observable that emits a notification when a new transaction is added
     *         to the pending state
     */
    Observable<PendingTransactionNotification> newPendingTransactionsNotifications();

    /**
     * Creates an observable that emits a notification when a node starts or stops syncing.
     * @return Observalbe that emits changes to syncing status
     */
    Observable<SyncingNotfication> syncingStatusNotifications();
}
