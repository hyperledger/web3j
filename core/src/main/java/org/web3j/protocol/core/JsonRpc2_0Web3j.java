package org.web3j.protocol.core;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.concurrent.ScheduledExecutorService;

import rx.Observable;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.methods.request.ShhFilter;
import org.web3j.protocol.core.methods.request.ShhPost;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.DbGetHex;
import org.web3j.protocol.core.methods.response.DbGetString;
import org.web3j.protocol.core.methods.response.DbPutHex;
import org.web3j.protocol.core.methods.response.DbPutString;
import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthCoinbase;
import org.web3j.protocol.core.methods.response.EthCompileLLL;
import org.web3j.protocol.core.methods.response.EthCompileSerpent;
import org.web3j.protocol.core.methods.response.EthCompileSolidity;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.protocol.core.methods.response.EthFilter;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetBlockTransactionCountByHash;
import org.web3j.protocol.core.methods.response.EthGetBlockTransactionCountByNumber;
import org.web3j.protocol.core.methods.response.EthGetCode;
import org.web3j.protocol.core.methods.response.EthGetCompilers;
import org.web3j.protocol.core.methods.response.EthGetStorageAt;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthGetUncleCountByBlockHash;
import org.web3j.protocol.core.methods.response.EthGetUncleCountByBlockNumber;
import org.web3j.protocol.core.methods.response.EthGetWork;
import org.web3j.protocol.core.methods.response.EthHashrate;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.core.methods.response.EthMining;
import org.web3j.protocol.core.methods.response.EthProtocolVersion;
import org.web3j.protocol.core.methods.response.EthSign;
import org.web3j.protocol.core.methods.response.EthSubmitHashrate;
import org.web3j.protocol.core.methods.response.EthSubmitWork;
import org.web3j.protocol.core.methods.response.EthSubscribe;
import org.web3j.protocol.core.methods.response.EthSyncing;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.core.methods.response.EthUninstallFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.NetListening;
import org.web3j.protocol.core.methods.response.NetPeerCount;
import org.web3j.protocol.core.methods.response.NetVersion;
import org.web3j.protocol.core.methods.response.ShhAddToGroup;
import org.web3j.protocol.core.methods.response.ShhHasIdentity;
import org.web3j.protocol.core.methods.response.ShhMessages;
import org.web3j.protocol.core.methods.response.ShhNewFilter;
import org.web3j.protocol.core.methods.response.ShhNewGroup;
import org.web3j.protocol.core.methods.response.ShhNewIdentity;
import org.web3j.protocol.core.methods.response.ShhUninstallFilter;
import org.web3j.protocol.core.methods.response.ShhVersion;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.core.methods.response.Web3Sha3;
import org.web3j.protocol.rx.JsonRpc2_0Rx;
import org.web3j.protocol.websocket.events.LogNotification;
import org.web3j.protocol.websocket.events.NewHeadsNotification;
import org.web3j.utils.Async;
import org.web3j.utils.Numeric;

/**
 * JSON-RPC 2.0 factory implementation.
 */
public class JsonRpc2_0Web3j implements Web3j {

    public static final int DEFAULT_BLOCK_TIME = 15 * 1000;

    protected final Web3jService[] web3jServices;
    private final JsonRpc2_0Rx web3jRx;
    private final long blockTime;
    private final ScheduledExecutorService scheduledExecutorService;


    public JsonRpc2_0Web3j(Web3jService[] web3jServices) {
        this(web3jServices, DEFAULT_BLOCK_TIME, Async.defaultExecutorService());
    }

    public JsonRpc2_0Web3j(
            Web3jService[] web3jServices, long pollingInterval,
            ScheduledExecutorService scheduledExecutorService) {

        this.web3jServices = web3jServices;
        this.web3jRx = new JsonRpc2_0Rx(this, scheduledExecutorService);
        this.blockTime = pollingInterval;
        this.scheduledExecutorService = scheduledExecutorService;
    }

    @Override
    public Request<?, Web3ClientVersion> web3ClientVersion() {
        return new Request<>(
                "web3_clientVersion",
                Collections.<String>emptyList(),
                web3jServices,
                Web3ClientVersion.class);
    }

    @Override
    public Request<?, Web3Sha3> web3Sha3(String data) {
        return new Request<>(
                "web3_sha3",
                Arrays.asList(data),
                web3jServices,
                Web3Sha3.class);
    }

    @Override
    public Request<?, NetVersion> netVersion() {
        return new Request<>(
                "net_version",
                Collections.<String>emptyList(),
                web3jServices,
                NetVersion.class);
    }

    @Override
    public Request<?, NetListening> netListening() {
        return new Request<>(
                "net_listening",
                Collections.<String>emptyList(),
                web3jServices,
                NetListening.class);
    }

    @Override
    public Request<?, NetPeerCount> netPeerCount() {
        return new Request<>(
                "net_peerCount",
                Collections.<String>emptyList(),
                web3jServices,
                NetPeerCount.class);
    }

    @Override
    public Request<?, EthProtocolVersion> ethProtocolVersion() {
        return new Request<>(
                "eth_protocolVersion",
                Collections.<String>emptyList(),
                web3jServices,
                EthProtocolVersion.class);
    }

    @Override
    public Request<?, EthCoinbase> ethCoinbase() {
        return new Request<>(
                "eth_coinbase",
                Collections.<String>emptyList(),
                web3jServices,
                EthCoinbase.class);
    }

    @Override
    public Request<?, EthSyncing> ethSyncing() {
        return new Request<>(
                "eth_syncing",
                Collections.<String>emptyList(),
                web3jServices,
                EthSyncing.class);
    }

    @Override
    public Request<?, EthMining> ethMining() {
        return new Request<>(
                "eth_mining",
                Collections.<String>emptyList(),
                web3jServices,
                EthMining.class);
    }

    @Override
    public Request<?, EthHashrate> ethHashrate() {
        return new Request<>(
                "eth_hashrate",
                Collections.<String>emptyList(),
                web3jServices,
                EthHashrate.class);
    }

    @Override
    public Request<?, EthGasPrice> ethGasPrice() {
        return new Request<>(
                "eth_gasPrice",
                Collections.<String>emptyList(),
                web3jServices,
                EthGasPrice.class);
    }

    @Override
    public Request<?, EthAccounts> ethAccounts() {
        return new Request<>(
                "eth_accounts",
                Collections.<String>emptyList(),
                web3jServices,
                EthAccounts.class);
    }

    @Override
    public Request<?, EthBlockNumber> ethBlockNumber() {
        return new Request<>(
                "eth_blockNumber",
                Collections.<String>emptyList(),
                web3jServices,
                EthBlockNumber.class);
    }

    @Override
    public Request<?, EthGetBalance> ethGetBalance(
            String address, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "eth_getBalance",
                Arrays.asList(address, defaultBlockParameter.getValue()),
                web3jServices,
                EthGetBalance.class);
    }

    @Override
    public Request<?, EthGetStorageAt> ethGetStorageAt(
            String address, BigInteger position, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "eth_getStorageAt",
                Arrays.asList(
                        address,
                        Numeric.encodeQuantity(position),
                        defaultBlockParameter.getValue()),
                web3jServices,
                EthGetStorageAt.class);
    }

    @Override
    public Request<?, EthGetTransactionCount> ethGetTransactionCount(
            String address, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "eth_getTransactionCount",
                Arrays.asList(address, defaultBlockParameter.getValue()),
                web3jServices,
                EthGetTransactionCount.class);
    }

    @Override
    public Request<?, EthGetBlockTransactionCountByHash> ethGetBlockTransactionCountByHash(
            String blockHash) {
        return new Request<>(
                "eth_getBlockTransactionCountByHash",
                Arrays.asList(blockHash),
                web3jServices,
                EthGetBlockTransactionCountByHash.class);
    }

    @Override
    public Request<?, EthGetBlockTransactionCountByNumber> ethGetBlockTransactionCountByNumber(
            DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "eth_getBlockTransactionCountByNumber",
                Arrays.asList(defaultBlockParameter.getValue()),
                web3jServices,
                EthGetBlockTransactionCountByNumber.class);
    }

    @Override
    public Request<?, EthGetUncleCountByBlockHash> ethGetUncleCountByBlockHash(String blockHash) {
        return new Request<>(
                "eth_getUncleCountByBlockHash",
                Arrays.asList(blockHash),
                web3jServices,
                EthGetUncleCountByBlockHash.class);
    }

    @Override
    public Request<?, EthGetUncleCountByBlockNumber> ethGetUncleCountByBlockNumber(
            DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "eth_getUncleCountByBlockNumber",
                Arrays.asList(defaultBlockParameter.getValue()),
                web3jServices,
                EthGetUncleCountByBlockNumber.class);
    }

    @Override
    public Request<?, EthGetCode> ethGetCode(
            String address, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "eth_getCode",
                Arrays.asList(address, defaultBlockParameter.getValue()),
                web3jServices,
                EthGetCode.class);
    }

    @Override
    public Request<?, EthSign> ethSign(String address, String sha3HashOfDataToSign) {
        return new Request<>(
                "eth_sign",
                Arrays.asList(address, sha3HashOfDataToSign),
                web3jServices,
                EthSign.class);
    }

    @Override
    public Request<?, org.web3j.protocol.core.methods.response.EthSendTransaction>
            ethSendTransaction(
            Transaction transaction) {
        return new Request<>(
                "eth_sendTransaction",
                Arrays.asList(transaction),
                web3jServices,
                org.web3j.protocol.core.methods.response.EthSendTransaction.class);
    }

    @Override
    public Request<?, org.web3j.protocol.core.methods.response.EthSendTransaction>
            ethSendRawTransaction(
            String signedTransactionData) {
        return new Request<>(
                "eth_sendRawTransaction",
                Arrays.asList(signedTransactionData),
                web3jServices,
                org.web3j.protocol.core.methods.response.EthSendTransaction.class);
    }

    @Override
    public Request<?, org.web3j.protocol.core.methods.response.EthCall> ethCall(
            Transaction transaction, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "eth_call",
                Arrays.asList(transaction, defaultBlockParameter),
                web3jServices,
                org.web3j.protocol.core.methods.response.EthCall.class);
    }

    @Override
    public Request<?, EthEstimateGas> ethEstimateGas(Transaction transaction) {
        return new Request<>(
                "eth_estimateGas",
                Arrays.asList(transaction),
                web3jServices,
                EthEstimateGas.class);
    }

    @Override
    public Request<?, EthBlock> ethGetBlockByHash(
            String blockHash, boolean returnFullTransactionObjects) {
        return new Request<>(
                "eth_getBlockByHash",
                Arrays.asList(
                        blockHash,
                        returnFullTransactionObjects),
                web3jServices,
                EthBlock.class);
    }

    @Override
    public Request<?, EthBlock> ethGetBlockByNumber(
            DefaultBlockParameter defaultBlockParameter,
            boolean returnFullTransactionObjects) {
        return new Request<>(
                "eth_getBlockByNumber",
                Arrays.asList(
                        defaultBlockParameter.getValue(),
                        returnFullTransactionObjects),
                web3jServices,
                EthBlock.class);
    }

    @Override
    public Request<?, EthTransaction> ethGetTransactionByHash(String transactionHash) {
        return new Request<>(
                "eth_getTransactionByHash",
                Arrays.asList(transactionHash),
                web3jServices,
                EthTransaction.class);
    }

    @Override
    public Request<?, EthTransaction> ethGetTransactionByBlockHashAndIndex(
            String blockHash, BigInteger transactionIndex) {
        return new Request<>(
                "eth_getTransactionByBlockHashAndIndex",
                Arrays.asList(
                        blockHash,
                        Numeric.encodeQuantity(transactionIndex)),
                web3jServices,
                EthTransaction.class);
    }

    @Override
    public Request<?, EthTransaction> ethGetTransactionByBlockNumberAndIndex(
            DefaultBlockParameter defaultBlockParameter, BigInteger transactionIndex) {
        return new Request<>(
                "eth_getTransactionByBlockNumberAndIndex",
                Arrays.asList(
                        defaultBlockParameter.getValue(),
                        Numeric.encodeQuantity(transactionIndex)),
                web3jServices,
                EthTransaction.class);
    }

    @Override
    public Request<?, EthGetTransactionReceipt> ethGetTransactionReceipt(String transactionHash) {
        return new Request<>(
                "eth_getTransactionReceipt",
                Arrays.asList(transactionHash),
                web3jServices,
                EthGetTransactionReceipt.class);
    }

    @Override
    public Request<?, EthBlock> ethGetUncleByBlockHashAndIndex(
            String blockHash, BigInteger transactionIndex) {
        return new Request<>(
                "eth_getUncleByBlockHashAndIndex",
                Arrays.asList(
                        blockHash,
                        Numeric.encodeQuantity(transactionIndex)),
                web3jServices,
                EthBlock.class);
    }

    @Override
    public Request<?, EthBlock> ethGetUncleByBlockNumberAndIndex(
            DefaultBlockParameter defaultBlockParameter, BigInteger uncleIndex) {
        return new Request<>(
                "eth_getUncleByBlockNumberAndIndex",
                Arrays.asList(
                        defaultBlockParameter.getValue(),
                        Numeric.encodeQuantity(uncleIndex)),
                web3jServices,
                EthBlock.class);
    }

    @Override
    public Request<?, EthGetCompilers> ethGetCompilers() {
        return new Request<>(
                "eth_getCompilers",
                Collections.<String>emptyList(),
                web3jServices,
                EthGetCompilers.class);
    }

    @Override
    public Request<?, EthCompileLLL> ethCompileLLL(String sourceCode) {
        return new Request<>(
                "eth_compileLLL",
                Arrays.asList(sourceCode),
                web3jServices,
                EthCompileLLL.class);
    }

    @Override
    public Request<?, EthCompileSolidity> ethCompileSolidity(String sourceCode) {
        return new Request<>(
                "eth_compileSolidity",
                Arrays.asList(sourceCode),
                web3jServices,
                EthCompileSolidity.class);
    }

    @Override
    public Request<?, EthCompileSerpent> ethCompileSerpent(String sourceCode) {
        return new Request<>(
                "eth_compileSerpent",
                Arrays.asList(sourceCode),
                web3jServices,
                EthCompileSerpent.class);
    }

    @Override
    public Request<?, EthFilter> ethNewFilter(
            org.web3j.protocol.core.methods.request.EthFilter ethFilter) {
        return new Request<>(
                "eth_newFilter",
                Arrays.asList(ethFilter),
                web3jServices,
                EthFilter.class);
    }

    @Override
    public Request<?, EthFilter> ethNewBlockFilter() {
        return new Request<>(
                "eth_newBlockFilter",
                Collections.<String>emptyList(),
                web3jServices,
                EthFilter.class);
    }

    @Override
    public Request<?, EthFilter> ethNewPendingTransactionFilter() {
        return new Request<>(
                "eth_newPendingTransactionFilter",
                Collections.<String>emptyList(),
                web3jServices,
                EthFilter.class);
    }

    @Override
    public Request<?, EthUninstallFilter> ethUninstallFilter(BigInteger filterId) {
        return new Request<>(
                "eth_uninstallFilter",
                Arrays.asList(Numeric.toHexStringWithPrefixSafe(filterId)),
                web3jServices,
                EthUninstallFilter.class);
    }

    @Override
    public Request<?, EthLog> ethGetFilterChanges(BigInteger filterId) {
        return new Request<>(
                "eth_getFilterChanges",
                Arrays.asList(Numeric.toHexStringWithPrefixSafe(filterId)),
                web3jServices,
                EthLog.class);
    }

    @Override
    public Request<?, EthLog> ethGetFilterLogs(BigInteger filterId) {
        return new Request<>(
                "eth_getFilterLogs",
                Arrays.asList(Numeric.toHexStringWithPrefixSafe(filterId)),
                web3jServices,
                EthLog.class);
    }

    @Override
    public Request<?, EthLog> ethGetLogs(
            org.web3j.protocol.core.methods.request.EthFilter ethFilter) {
        return new Request<>(
                "eth_getLogs",
                Arrays.asList(ethFilter),
                web3jServices,
                EthLog.class);
    }

    @Override
    public Request<?, EthGetWork> ethGetWork() {
        return new Request<>(
                "eth_getWork",
                Collections.<String>emptyList(),
                web3jServices,
                EthGetWork.class);
    }

    @Override
    public Request<?, EthSubmitWork> ethSubmitWork(
            String nonce, String headerPowHash, String mixDigest) {
        return new Request<>(
                "eth_submitWork",
                Arrays.asList(nonce, headerPowHash, mixDigest),
                web3jServices,
                EthSubmitWork.class);
    }

    @Override
    public Request<?, EthSubmitHashrate> ethSubmitHashrate(String hashrate, String clientId) {
        return new Request<>(
                "eth_submitHashrate",
                Arrays.asList(hashrate, clientId),
                web3jServices,
                EthSubmitHashrate.class);
    }

    @Override
    public Request<?, DbPutString> dbPutString(
            String databaseName, String keyName, String stringToStore) {
        return new Request<>(
                "db_putString",
                Arrays.asList(databaseName, keyName, stringToStore),
                web3jServices,
                DbPutString.class);
    }

    @Override
    public Request<?, DbGetString> dbGetString(String databaseName, String keyName) {
        return new Request<>(
                "db_getString",
                Arrays.asList(databaseName, keyName),
                web3jServices,
                DbGetString.class);
    }

    @Override
    public Request<?, DbPutHex> dbPutHex(String databaseName, String keyName, String dataToStore) {
        return new Request<>(
                "db_putHex",
                Arrays.asList(databaseName, keyName, dataToStore),
                web3jServices,
                DbPutHex.class);
    }

    @Override
    public Request<?, DbGetHex> dbGetHex(String databaseName, String keyName) {
        return new Request<>(
                "db_getHex",
                Arrays.asList(databaseName, keyName),
                web3jServices,
                DbGetHex.class);
    }

    @Override
    public Request<?, org.web3j.protocol.core.methods.response.ShhPost> shhPost(ShhPost shhPost) {
        return new Request<>(
                "shh_post",
                Arrays.asList(shhPost),
                web3jServices,
                org.web3j.protocol.core.methods.response.ShhPost.class);
    }

    @Override
    public Request<?, ShhVersion> shhVersion() {
        return new Request<>(
                "shh_version",
                Collections.<String>emptyList(),
                web3jServices,
                ShhVersion.class);
    }

    @Override
    public Request<?, ShhNewIdentity> shhNewIdentity() {
        return new Request<>(
                "shh_newIdentity",
                Collections.<String>emptyList(),
                web3jServices,
                ShhNewIdentity.class);
    }

    @Override
    public Request<?, ShhHasIdentity> shhHasIdentity(String identityAddress) {
        return new Request<>(
                "shh_hasIdentity",
                Arrays.asList(identityAddress),
                web3jServices,
                ShhHasIdentity.class);
    }

    @Override
    public Request<?, ShhNewGroup> shhNewGroup() {
        return new Request<>(
                "shh_newGroup",
                Collections.<String>emptyList(),
                web3jServices,
                ShhNewGroup.class);
    }

    @Override
    public Request<?, ShhAddToGroup> shhAddToGroup(String identityAddress) {
        return new Request<>(
                "shh_addToGroup",
                Arrays.asList(identityAddress),
                web3jServices,
                ShhAddToGroup.class);
    }

    @Override
    public Request<?, ShhNewFilter> shhNewFilter(ShhFilter shhFilter) {
        return new Request<>(
                "shh_newFilter",
                Arrays.asList(shhFilter),
                web3jServices,
                ShhNewFilter.class);
    }

    @Override
    public Request<?, ShhUninstallFilter> shhUninstallFilter(BigInteger filterId) {
        return new Request<>(
                "shh_uninstallFilter",
                Arrays.asList(Numeric.toHexStringWithPrefixSafe(filterId)),
                web3jServices,
                ShhUninstallFilter.class);
    }

    @Override
    public Request<?, ShhMessages> shhGetFilterChanges(BigInteger filterId) {
        return new Request<>(
                "shh_getFilterChanges",
                Arrays.asList(Numeric.toHexStringWithPrefixSafe(filterId)),
                web3jServices,
                ShhMessages.class);
    }

    @Override
    public Request<?, ShhMessages> shhGetMessages(BigInteger filterId) {
        return new Request<>(
                "shh_getMessages",
                Arrays.asList(Numeric.toHexStringWithPrefixSafe(filterId)),
                web3jServices,
                ShhMessages.class);
    }

    @Override
    public Observable<NewHeadsNotification> newHeadsNotifications() {
        return web3jServices[0].subscribe(
                new Request<>(
                        "eth_subscribe",
                        Collections.singletonList("newHeads"),
                        web3jServices,
                        EthSubscribe.class),
                "eth_unsubscribe",
                NewHeadsNotification.class
        );
    }

    @Override
    public Observable<LogNotification> logsNotifications(
            List<String> addresses, List<String> topics) {

        Map<String, Object> params = createLogsParams(addresses, topics);

        return web3jServices[0].subscribe(
                new Request<>(
                        "eth_subscribe",
                        Arrays.asList("logs", params),
                        web3jServices,
                        EthSubscribe.class),
                "eth_unsubscribe",
                LogNotification.class
        );
    }

    private Map<String, Object> createLogsParams(List<String> addresses, List<String> topics) {
        Map<String, Object> params = new HashMap<>();
        if (!addresses.isEmpty()) {
            params.put("address", addresses);
        }
        if (!topics.isEmpty()) {
            params.put("topics", topics);
        }
        return params;
    }

    @Override
    public Observable<String> ethBlockHashObservable() {
        return web3jRx.ethBlockHashObservable(blockTime);
    }

    @Override
    public Observable<String> ethPendingTransactionHashObservable() {
        return web3jRx.ethPendingTransactionHashObservable(blockTime);
    }

    @Override
    public Observable<Log> ethLogObservable(
            org.web3j.protocol.core.methods.request.EthFilter ethFilter) {
        return web3jRx.ethLogObservable(ethFilter, blockTime);
    }

    @Override
    public Observable<org.web3j.protocol.core.methods.response.Transaction>
            transactionObservable() {
        return web3jRx.transactionObservable(blockTime);
    }

    @Override
    public Observable<org.web3j.protocol.core.methods.response.Transaction>
            pendingTransactionObservable() {
        return web3jRx.pendingTransactionObservable(blockTime);
    }

    @Override
    public Observable<EthBlock> blockObservable(boolean fullTransactionObjects) {
        return web3jRx.blockObservable(fullTransactionObjects, blockTime);
    }

    @Override
    public Observable<EthBlock> replayBlocksObservable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock,
            boolean fullTransactionObjects) {
        return web3jRx.replayBlocksObservable(startBlock, endBlock, fullTransactionObjects);
    }

    @Override
    public Observable<EthBlock> replayBlocksObservable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock,
            boolean fullTransactionObjects, boolean ascending) {
        return web3jRx.replayBlocksObservable(startBlock, endBlock,
                fullTransactionObjects, ascending);
    }

    @Override
    public Observable<org.web3j.protocol.core.methods.response.Transaction>
            replayTransactionsObservable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        return web3jRx.replayTransactionsObservable(startBlock, endBlock);
    }

    @Override
    public Observable<EthBlock> catchUpToLatestBlockObservable(
            DefaultBlockParameter startBlock, boolean fullTransactionObjects,
            Observable<EthBlock> onCompleteObservable) {
        return web3jRx.catchUpToLatestBlockObservable(
                startBlock, fullTransactionObjects, onCompleteObservable);
    }

    @Override
    public Observable<EthBlock> catchUpToLatestBlockObservable(
            DefaultBlockParameter startBlock, boolean fullTransactionObjects) {
        return web3jRx.catchUpToLatestBlockObservable(startBlock, fullTransactionObjects);
    }

    @Override
    public Observable<org.web3j.protocol.core.methods.response.Transaction>
            catchUpToLatestTransactionObservable(DefaultBlockParameter startBlock) {
        return web3jRx.catchUpToLatestTransactionObservable(startBlock);
    }

    @Override
    public Observable<EthBlock> catchUpToLatestAndSubscribeToNewBlocksObservable(
            DefaultBlockParameter startBlock, boolean fullTransactionObjects) {
        return web3jRx.catchUpToLatestAndSubscribeToNewBlocksObservable(
                startBlock, fullTransactionObjects, blockTime);
    }

    @Override
    public Observable<org.web3j.protocol.core.methods.response.Transaction>
            catchUpToLatestAndSubscribeToNewTransactionsObservable(
            DefaultBlockParameter startBlock) {
        return web3jRx.catchUpToLatestAndSubscribeToNewTransactionsObservable(
                startBlock, blockTime);
    }

    @Override
    public void shutdown() {
        scheduledExecutorService.shutdown();
        try {
            for (Web3jService service: web3jServices) {
                service.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to close web3j service", e);
        }
    }
}
