package org.web3j.protocol.rx;

import rx.Observable;

import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.Transaction;

/**
 * The Observables JSON-RPC client event API.
 */
public interface Web3jRx {
    Observable<Log> ethLogObservable(EthFilter ethFilter);

    Observable<String> ethBlockHashObservable();

    Observable<String> ethPendingTransactionHashObservable();

    Observable<Transaction> transactionObservable();

    Observable<Transaction> pendingTransactionObservable();

    Observable<EthBlock> blockObservable(boolean fullTransactionObjects);
}
