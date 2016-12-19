package org.web3j.protocol.core.rx;

import rx.Observable;

import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Log;

/**
 * The Observables JSON-RPC client event API.
 */
public interface Web3jRx {
    Observable<Log> ethLogObservable(org.web3j.protocol.core.methods.request.EthFilter ethFilter);

    Observable<String> ethBlockHashObservable();

    Observable<String> ethPendingTransactionHashObservable();

    Observable<org.web3j.protocol.core.methods.response.Transaction> transactionObservable();

    Observable<org.web3j.protocol.core.methods.response.Transaction> pendingTransactionObservable();

    Observable<EthBlock> blockObservable(boolean fullTransactionObjects);
}
