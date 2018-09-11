package org.web3j.protocol.geth;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ScheduledExecutorService;

import rx.Observable;

import org.web3j.protocol.Web3jService;
import org.web3j.protocol.admin.JsonRpc2_0Admin;
import org.web3j.protocol.admin.methods.response.BooleanResponse;
import org.web3j.protocol.admin.methods.response.PersonalSign;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthSubscribe;
import org.web3j.protocol.core.methods.response.MinerStartResponse;
import org.web3j.protocol.geth.response.PersonalEcRecover;
import org.web3j.protocol.geth.response.PersonalImportRawKey;
import org.web3j.protocol.websocket.events.PendingTransactionNotification;
import org.web3j.protocol.websocket.events.SyncingNotfication;

/**
 * JSON-RPC 2.0 factory implementation for Geth.
 */
public class JsonRpc2_0Geth extends JsonRpc2_0Admin implements Geth {

    public JsonRpc2_0Geth(Web3jService... web3jService) {
        super(web3jService);
    }

    public JsonRpc2_0Geth(Web3jService[] web3jServices, long pollingInterval,
                            ScheduledExecutorService scheduledExecutorService) {
        super(web3jServices, pollingInterval, scheduledExecutorService);
    }
    
    @Override
    public Request<?, PersonalImportRawKey> personalImportRawKey(
            String keydata, String password) {
        return new Request<>(
                "personal_importRawKey",
                Arrays.asList(keydata, password),
                web3jServices,
                PersonalImportRawKey.class);
    }

    @Override
    public Request<?, BooleanResponse> personalLockAccount(String accountId) {
        return new Request<>(
                "personal_lockAccount",
                Arrays.asList(accountId),
                web3jServices,
                BooleanResponse.class);
    }

    @Override
    public Request<?, PersonalSign> personalSign(
            String message, String accountId, String password) {
        return new Request<>(
                "personal_sign",
                Arrays.asList(message,accountId,password),
                web3jServices,
                PersonalSign.class);
    }

    @Override
    public Request<?, PersonalEcRecover> personalEcRecover(
            String hexMessage, String signedMessage) {
        return new Request<>(
                "personal_ecRecover",
                Arrays.asList(hexMessage,signedMessage),
                web3jServices,
                PersonalEcRecover.class);
    }

    @Override
    public Request<?, MinerStartResponse> minerStart(int threadCount) {
        return new Request<>(
                "miner_start",
                Arrays.asList(threadCount),
                web3jServices,
                MinerStartResponse.class);
    }

    @Override
    public Request<?, BooleanResponse> minerStop() {
        return new Request<>(
                "miner_stop",
                Collections.<String>emptyList(),
                web3jServices,
                BooleanResponse.class);
    }

    public Observable<PendingTransactionNotification> newPendingTransactionsNotifications() {
        return web3jServices[0].subscribe(
                new Request<>(
                        "eth_subscribe",
                        Arrays.asList("newPendingTransactions"),
                        web3jServices,
                        EthSubscribe.class),
                "eth_unsubscribe",
                PendingTransactionNotification.class
        );
    }

    @Override
    public Observable<SyncingNotfication> syncingStatusNotifications() {
        return web3jServices[0].subscribe(
                new Request<>(
                        "eth_subscribe",
                        Arrays.asList("syncing"),
                        web3jServices,
                        EthSubscribe.class),
                "eth_unsubscribe",
                SyncingNotfication.class
        );
    }
}
