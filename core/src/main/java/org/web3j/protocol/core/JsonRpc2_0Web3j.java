/*
 * Copyright 2019 Web3 Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.web3j.protocol.core;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

import io.reactivex.Flowable;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.methods.request.ShhFilter;
import org.web3j.protocol.core.methods.request.ShhPost;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.BooleanResponse;
import org.web3j.protocol.core.methods.response.DbGetHex;
import org.web3j.protocol.core.methods.response.DbGetString;
import org.web3j.protocol.core.methods.response.DbPutHex;
import org.web3j.protocol.core.methods.response.DbPutString;
import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthChainId;
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
import org.web3j.protocol.core.methods.response.TxPoolStatus;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.core.methods.response.Web3Sha3;
import org.web3j.protocol.core.methods.response.admin.AdminDataDir;
import org.web3j.protocol.core.methods.response.admin.AdminNodeInfo;
import org.web3j.protocol.core.methods.response.admin.AdminPeers;
import org.web3j.protocol.rx.JsonRpc2_0Rx;
import org.web3j.protocol.websocket.events.LogNotification;
import org.web3j.protocol.websocket.events.NewHeadsNotification;
import org.web3j.utils.Async;
import org.web3j.utils.Numeric;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

/** JSON-RPC 2.0 factory implementation. */
public class JsonRpc2_0Web3j implements Web3j {

    public static final int DEFAULT_BLOCK_TIME = 15 * 1000;

    protected final Web3jService web3jService;
    private final JsonRpc2_0Rx web3jRx;
    private final long blockTime;
    private final ScheduledExecutorService scheduledExecutorService;

    public JsonRpc2_0Web3j(final Web3jService web3jService) {
        this(web3jService, DEFAULT_BLOCK_TIME, Async.defaultExecutorService());
    }

    public JsonRpc2_0Web3j(
            final Web3jService web3jService,
            final long pollingInterval,
            final ScheduledExecutorService scheduledExecutorService) {
        this.web3jService = web3jService;
        this.web3jRx = new JsonRpc2_0Rx(this, scheduledExecutorService);
        this.blockTime = pollingInterval;
        this.scheduledExecutorService = scheduledExecutorService;
    }

    @Override
    public Request<?, Web3ClientVersion> web3ClientVersion() {
        return new Request<>(
                "web3_clientVersion",
                Collections.<String>emptyList(),
                web3jService,
                Web3ClientVersion.class);
    }

    @Override
    public Request<?, Web3Sha3> web3Sha3(final String data) {
        return new Request<>("web3_sha3", singletonList(data), web3jService, Web3Sha3.class);
    }

    @Override
    public Request<?, NetVersion> netVersion() {
        return new Request<>(
                "net_version", Collections.<String>emptyList(), web3jService, NetVersion.class);
    }

    @Override
    public Request<?, NetListening> netListening() {
        return new Request<>(
                "net_listening", Collections.<String>emptyList(), web3jService, NetListening.class);
    }

    @Override
    public Request<?, NetPeerCount> netPeerCount() {
        return new Request<>(
                "net_peerCount", Collections.<String>emptyList(), web3jService, NetPeerCount.class);
    }

    @Override
    public Request<?, AdminNodeInfo> adminNodeInfo() {
        return new Request<>(
                "admin_nodeInfo", Collections.emptyList(), web3jService, AdminNodeInfo.class);
    }

    @Override
    public Request<?, AdminPeers> adminPeers() {
        return new Request<>(
                "admin_peers", Collections.emptyList(), web3jService, AdminPeers.class);
    }

    @Override
    public Request<?, BooleanResponse> adminAddPeer(String url) {
        return new Request<>(
                "admin_addPeer", Arrays.asList(url), web3jService, BooleanResponse.class);
    }

    @Override
    public Request<?, BooleanResponse> adminRemovePeer(String url) {
        return new Request<>(
                "admin_removePeer", Arrays.asList(url), web3jService, BooleanResponse.class);
    }

    @Override
    public Request<?, AdminDataDir> adminDataDir() {
        return new Request<>(
                "admin_datadir", Collections.emptyList(), web3jService, AdminDataDir.class);
    }

    @Override
    public Request<?, EthProtocolVersion> ethProtocolVersion() {
        return new Request<>(
                "eth_protocolVersion",
                Collections.<String>emptyList(),
                web3jService,
                EthProtocolVersion.class);
    }

    @Override
    public Request<?, EthChainId> ethChainId() {
        return new Request<>(
                "eth_chainId", Collections.<String>emptyList(), web3jService, EthChainId.class);
    }

    @Override
    public Request<?, EthCoinbase> ethCoinbase() {
        return new Request<>(
                "eth_coinbase", Collections.<String>emptyList(), web3jService, EthCoinbase.class);
    }

    @Override
    public Request<?, EthSyncing> ethSyncing() {
        return new Request<>(
                "eth_syncing", Collections.<String>emptyList(), web3jService, EthSyncing.class);
    }

    @Override
    public Request<?, EthMining> ethMining() {
        return new Request<>(
                "eth_mining", Collections.<String>emptyList(), web3jService, EthMining.class);
    }

    @Override
    public Request<?, EthHashrate> ethHashrate() {
        return new Request<>(
                "eth_hashrate", Collections.<String>emptyList(), web3jService, EthHashrate.class);
    }

    @Override
    public Request<?, EthGasPrice> ethGasPrice() {
        return new Request<>(
                "eth_gasPrice", Collections.<String>emptyList(), web3jService, EthGasPrice.class);
    }

    @Override
    public Request<?, EthAccounts> ethAccounts() {
        return new Request<>(
                "eth_accounts", Collections.<String>emptyList(), web3jService, EthAccounts.class);
    }

    @Override
    public Request<?, EthBlockNumber> ethBlockNumber() {
        return new Request<>(
                "eth_blockNumber",
                Collections.<String>emptyList(),
                web3jService,
                EthBlockNumber.class);
    }

    @Override
    public Request<?, EthGetBalance> ethGetBalance(
            final String address, final DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "eth_getBalance",
                asList(address, defaultBlockParameter.getValue()),
                web3jService,
                EthGetBalance.class);
    }

    @Override
    public Request<?, EthGetStorageAt> ethGetStorageAt(
            final String address,
            final BigInteger position,
            final DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "eth_getStorageAt",
                asList(address, Numeric.encodeQuantity(position), defaultBlockParameter.getValue()),
                web3jService,
                EthGetStorageAt.class);
    }

    @Override
    public Request<?, EthGetTransactionCount> ethGetTransactionCount(
            final String address, final DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "eth_getTransactionCount",
                asList(address, defaultBlockParameter.getValue()),
                web3jService,
                EthGetTransactionCount.class);
    }

    @Override
    public Request<?, EthGetBlockTransactionCountByHash> ethGetBlockTransactionCountByHash(
            final String blockHash) {
        return new Request<>(
                "eth_getBlockTransactionCountByHash",
                singletonList(blockHash),
                web3jService,
                EthGetBlockTransactionCountByHash.class);
    }

    @Override
    public Request<?, EthGetBlockTransactionCountByNumber> ethGetBlockTransactionCountByNumber(
            final DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "eth_getBlockTransactionCountByNumber",
                singletonList(defaultBlockParameter.getValue()),
                web3jService,
                EthGetBlockTransactionCountByNumber.class);
    }

    @Override
    public Request<?, EthGetUncleCountByBlockHash> ethGetUncleCountByBlockHash(
            final String blockHash) {
        return new Request<>(
                "eth_getUncleCountByBlockHash",
                singletonList(blockHash),
                web3jService,
                EthGetUncleCountByBlockHash.class);
    }

    @Override
    public Request<?, EthGetUncleCountByBlockNumber> ethGetUncleCountByBlockNumber(
            final DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "eth_getUncleCountByBlockNumber",
                singletonList(defaultBlockParameter.getValue()),
                web3jService,
                EthGetUncleCountByBlockNumber.class);
    }

    @Override
    public Request<?, EthGetCode> ethGetCode(
            final String address, final DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "eth_getCode",
                asList(address, defaultBlockParameter.getValue()),
                web3jService,
                EthGetCode.class);
    }

    @Override
    public Request<?, EthSign> ethSign(final String address, final String sha3HashOfDataToSign) {
        return new Request<>(
                "eth_sign", asList(address, sha3HashOfDataToSign), web3jService, EthSign.class);
    }

    @Override
    public Request<?, org.web3j.protocol.core.methods.response.EthSendTransaction>
            ethSendTransaction(final Transaction transaction) {
        return new Request<>(
                "eth_sendTransaction",
                singletonList(transaction),
                web3jService,
                org.web3j.protocol.core.methods.response.EthSendTransaction.class);
    }

    @Override
    public Request<?, org.web3j.protocol.core.methods.response.EthSendTransaction>
            ethSendRawTransaction(final String signedTransactionData) {
        return new Request<>(
                "eth_sendRawTransaction",
                singletonList(signedTransactionData),
                web3jService,
                org.web3j.protocol.core.methods.response.EthSendTransaction.class);
    }

    @Override
    public Request<?, org.web3j.protocol.core.methods.response.EthCall> ethCall(
            final Transaction transaction, final DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "eth_call",
                asList(transaction, defaultBlockParameter),
                web3jService,
                org.web3j.protocol.core.methods.response.EthCall.class);
    }

    @Override
    public Request<?, EthEstimateGas> ethEstimateGas(final Transaction transaction) {
        return new Request<>(
                "eth_estimateGas", singletonList(transaction), web3jService, EthEstimateGas.class);
    }

    @Override
    public Request<?, EthBlock> ethGetBlockByHash(
            final String blockHash, final boolean returnFullTransactionObjects) {
        return new Request<>(
                "eth_getBlockByHash",
                asList(blockHash, returnFullTransactionObjects),
                web3jService,
                EthBlock.class);
    }

    @Override
    public Request<?, EthBlock> ethGetBlockByNumber(
            final DefaultBlockParameter defaultBlockParameter,
            final boolean returnFullTransactionObjects) {
        return new Request<>(
                "eth_getBlockByNumber",
                asList(defaultBlockParameter.getValue(), returnFullTransactionObjects),
                web3jService,
                EthBlock.class);
    }

    @Override
    public Request<?, EthTransaction> ethGetTransactionByHash(final String transactionHash) {
        return new Request<>(
                "eth_getTransactionByHash",
                singletonList(transactionHash),
                web3jService,
                EthTransaction.class);
    }

    @Override
    public Request<?, EthTransaction> ethGetTransactionByBlockHashAndIndex(
            final String blockHash, final BigInteger transactionIndex) {
        return new Request<>(
                "eth_getTransactionByBlockHashAndIndex",
                asList(blockHash, Numeric.encodeQuantity(transactionIndex)),
                web3jService,
                EthTransaction.class);
    }

    @Override
    public Request<?, EthTransaction> ethGetTransactionByBlockNumberAndIndex(
            final DefaultBlockParameter defaultBlockParameter, final BigInteger transactionIndex) {
        return new Request<>(
                "eth_getTransactionByBlockNumberAndIndex",
                asList(defaultBlockParameter.getValue(), Numeric.encodeQuantity(transactionIndex)),
                web3jService,
                EthTransaction.class);
    }

    @Override
    public Request<?, EthGetTransactionReceipt> ethGetTransactionReceipt(
            final String transactionHash) {
        return new Request<>(
                "eth_getTransactionReceipt",
                singletonList(transactionHash),
                web3jService,
                EthGetTransactionReceipt.class);
    }

    @Override
    public Request<?, EthBlock> ethGetUncleByBlockHashAndIndex(
            final String blockHash, final BigInteger transactionIndex) {
        return new Request<>(
                "eth_getUncleByBlockHashAndIndex",
                asList(blockHash, Numeric.encodeQuantity(transactionIndex)),
                web3jService,
                EthBlock.class);
    }

    @Override
    public Request<?, EthBlock> ethGetUncleByBlockNumberAndIndex(
            final DefaultBlockParameter defaultBlockParameter, final BigInteger uncleIndex) {
        return new Request<>(
                "eth_getUncleByBlockNumberAndIndex",
                asList(defaultBlockParameter.getValue(), Numeric.encodeQuantity(uncleIndex)),
                web3jService,
                EthBlock.class);
    }

    @Override
    public Request<?, EthGetCompilers> ethGetCompilers() {
        return new Request<>(
                "eth_getCompilers",
                Collections.<String>emptyList(),
                web3jService,
                EthGetCompilers.class);
    }

    @Override
    public Request<?, EthCompileLLL> ethCompileLLL(final String sourceCode) {
        return new Request<>(
                "eth_compileLLL", singletonList(sourceCode), web3jService, EthCompileLLL.class);
    }

    @Override
    public Request<?, EthCompileSolidity> ethCompileSolidity(final String sourceCode) {
        return new Request<>(
                "eth_compileSolidity",
                singletonList(sourceCode),
                web3jService,
                EthCompileSolidity.class);
    }

    @Override
    public Request<?, EthCompileSerpent> ethCompileSerpent(final String sourceCode) {
        return new Request<>(
                "eth_compileSerpent",
                singletonList(sourceCode),
                web3jService,
                EthCompileSerpent.class);
    }

    @Override
    public Request<?, EthFilter> ethNewFilter(
            final org.web3j.protocol.core.methods.request.EthFilter ethFilter) {
        return new Request<>(
                "eth_newFilter", singletonList(ethFilter), web3jService, EthFilter.class);
    }

    @Override
    public Request<?, EthFilter> ethNewBlockFilter() {
        return new Request<>(
                "eth_newBlockFilter",
                Collections.<String>emptyList(),
                web3jService,
                EthFilter.class);
    }

    @Override
    public Request<?, EthFilter> ethNewPendingTransactionFilter() {
        return new Request<>(
                "eth_newPendingTransactionFilter",
                Collections.<String>emptyList(),
                web3jService,
                EthFilter.class);
    }

    @Override
    public Request<?, EthUninstallFilter> ethUninstallFilter(final BigInteger filterId) {
        return new Request<>(
                "eth_uninstallFilter",
                singletonList(Numeric.toHexStringWithPrefixSafe(filterId)),
                web3jService,
                EthUninstallFilter.class);
    }

    @Override
    public Request<?, EthLog> ethGetFilterChanges(final BigInteger filterId) {
        return new Request<>(
                "eth_getFilterChanges",
                singletonList(Numeric.toHexStringWithPrefixSafe(filterId)),
                web3jService,
                EthLog.class);
    }

    @Override
    public Request<?, EthLog> ethGetFilterLogs(final BigInteger filterId) {
        return new Request<>(
                "eth_getFilterLogs",
                singletonList(Numeric.toHexStringWithPrefixSafe(filterId)),
                web3jService,
                EthLog.class);
    }

    @Override
    public Request<?, EthLog> ethGetLogs(
            final org.web3j.protocol.core.methods.request.EthFilter ethFilter) {
        return new Request<>("eth_getLogs", singletonList(ethFilter), web3jService, EthLog.class);
    }

    @Override
    public Request<?, EthGetWork> ethGetWork() {
        return new Request<>(
                "eth_getWork", Collections.<String>emptyList(), web3jService, EthGetWork.class);
    }

    @Override
    public Request<?, EthSubmitWork> ethSubmitWork(
            final String nonce, final String headerPowHash, final String mixDigest) {
        return new Request<>(
                "eth_submitWork",
                asList(nonce, headerPowHash, mixDigest),
                web3jService,
                EthSubmitWork.class);
    }

    @Override
    public Request<?, EthSubmitHashrate> ethSubmitHashrate(
            final String hashrate, final String clientId) {
        return new Request<>(
                "eth_submitHashrate",
                asList(hashrate, clientId),
                web3jService,
                EthSubmitHashrate.class);
    }

    @Override
    public Request<?, DbPutString> dbPutString(
            final String databaseName, final String keyName, final String stringToStore) {
        return new Request<>(
                "db_putString",
                asList(databaseName, keyName, stringToStore),
                web3jService,
                DbPutString.class);
    }

    @Override
    public Request<?, DbGetString> dbGetString(final String databaseName, final String keyName) {
        return new Request<>(
                "db_getString", asList(databaseName, keyName), web3jService, DbGetString.class);
    }

    @Override
    public Request<?, DbPutHex> dbPutHex(
            final String databaseName, final String keyName, final String dataToStore) {
        return new Request<>(
                "db_putHex",
                asList(databaseName, keyName, dataToStore),
                web3jService,
                DbPutHex.class);
    }

    @Override
    public Request<?, DbGetHex> dbGetHex(final String databaseName, final String keyName) {
        return new Request<>(
                "db_getHex", asList(databaseName, keyName), web3jService, DbGetHex.class);
    }

    @Override
    public Request<?, org.web3j.protocol.core.methods.response.ShhPost> shhPost(
            final ShhPost shhPost) {
        return new Request<>(
                "shh_post",
                singletonList(shhPost),
                web3jService,
                org.web3j.protocol.core.methods.response.ShhPost.class);
    }

    @Override
    public Request<?, ShhVersion> shhVersion() {
        return new Request<>(
                "shh_version", Collections.<String>emptyList(), web3jService, ShhVersion.class);
    }

    @Override
    public Request<?, ShhNewIdentity> shhNewIdentity() {
        return new Request<>(
                "shh_newIdentity",
                Collections.<String>emptyList(),
                web3jService,
                ShhNewIdentity.class);
    }

    @Override
    public Request<?, ShhHasIdentity> shhHasIdentity(final String identityAddress) {
        return new Request<>(
                "shh_hasIdentity",
                singletonList(identityAddress),
                web3jService,
                ShhHasIdentity.class);
    }

    @Override
    public Request<?, ShhNewGroup> shhNewGroup() {
        return new Request<>(
                "shh_newGroup", Collections.<String>emptyList(), web3jService, ShhNewGroup.class);
    }

    @Override
    public Request<?, ShhAddToGroup> shhAddToGroup(final String identityAddress) {
        return new Request<>(
                "shh_addToGroup",
                singletonList(identityAddress),
                web3jService,
                ShhAddToGroup.class);
    }

    @Override
    public Request<?, ShhNewFilter> shhNewFilter(final ShhFilter shhFilter) {
        return new Request<>(
                "shh_newFilter", singletonList(shhFilter), web3jService, ShhNewFilter.class);
    }

    @Override
    public Request<?, ShhUninstallFilter> shhUninstallFilter(final BigInteger filterId) {
        return new Request<>(
                "shh_uninstallFilter",
                singletonList(Numeric.toHexStringWithPrefixSafe(filterId)),
                web3jService,
                ShhUninstallFilter.class);
    }

    @Override
    public Request<?, ShhMessages> shhGetFilterChanges(final BigInteger filterId) {
        return new Request<>(
                "shh_getFilterChanges",
                singletonList(Numeric.toHexStringWithPrefixSafe(filterId)),
                web3jService,
                ShhMessages.class);
    }

    @Override
    public Request<?, ShhMessages> shhGetMessages(final BigInteger filterId) {
        return new Request<>(
                "shh_getMessages",
                singletonList(Numeric.toHexStringWithPrefixSafe(filterId)),
                web3jService,
                ShhMessages.class);
    }

    @Override
    public Request<?, TxPoolStatus> txPoolStatus() {
        return new Request<>(
                "txpool_status", Collections.<String>emptyList(), web3jService, TxPoolStatus.class);
    }

    @Override
    public Flowable<NewHeadsNotification> newHeadsNotifications() {
        return web3jService.subscribe(
                new Request<>(
                        "eth_subscribe",
                        singletonList("newHeads"),
                        web3jService,
                        EthSubscribe.class),
                "eth_unsubscribe",
                NewHeadsNotification.class);
    }

    @Override
    public Flowable<LogNotification> logsNotifications(
            final List<String> addresses, final List<String> topics) {

        final Map<String, Object> params = createLogsParams(addresses, topics);

        return web3jService.subscribe(
                new Request<>(
                        "eth_subscribe", asList("logs", params), web3jService, EthSubscribe.class),
                "eth_unsubscribe",
                LogNotification.class);
    }

    private Map<String, Object> createLogsParams(
            final List<String> addresses, final List<String> topics) {
        final Map<String, Object> params = new HashMap<>();
        if (!addresses.isEmpty()) {
            params.put("address", addresses);
        }
        if (!topics.isEmpty()) {
            params.put("topics", topics);
        }
        return params;
    }

    @Override
    public Flowable<String> ethBlockHashFlowable() {
        return web3jRx.ethBlockHashFlowable(blockTime);
    }

    @Override
    public Flowable<String> ethPendingTransactionHashFlowable() {
        return web3jRx.ethPendingTransactionHashFlowable(blockTime);
    }

    @Override
    public Flowable<Log> ethLogFlowable(
            final org.web3j.protocol.core.methods.request.EthFilter ethFilter) {
        return web3jRx.ethLogFlowable(ethFilter, blockTime);
    }

    @Override
    public Flowable<org.web3j.protocol.core.methods.response.Transaction> transactionFlowable() {
        return web3jRx.transactionFlowable(blockTime);
    }

    @Override
    public Flowable<org.web3j.protocol.core.methods.response.Transaction>
            pendingTransactionFlowable() {
        return web3jRx.pendingTransactionFlowable(blockTime);
    }

    @Override
    public Flowable<EthBlock> blockFlowable(final boolean fullTransactionObjects) {
        return web3jRx.blockFlowable(fullTransactionObjects, blockTime);
    }

    @Override
    public Flowable<EthBlock> replayPastBlocksFlowable(
            final DefaultBlockParameter startBlock,
            final DefaultBlockParameter endBlock,
            final boolean fullTransactionObjects) {
        return web3jRx.replayBlocksFlowable(startBlock, endBlock, fullTransactionObjects);
    }

    @Override
    public Flowable<EthBlock> replayPastBlocksFlowable(
            final DefaultBlockParameter startBlock,
            final DefaultBlockParameter endBlock,
            final boolean fullTransactionObjects,
            final boolean ascending) {
        return web3jRx.replayBlocksFlowable(
                startBlock, endBlock, fullTransactionObjects, ascending);
    }

    @Override
    public Flowable<EthBlock> replayPastBlocksFlowable(
            final DefaultBlockParameter startBlock,
            final boolean fullTransactionObjects,
            final Flowable<EthBlock> onCompleteFlowable) {
        return web3jRx.replayPastBlocksFlowable(
                startBlock, fullTransactionObjects, onCompleteFlowable);
    }

    @Override
    public Flowable<EthBlock> replayPastBlocksFlowable(
            final DefaultBlockParameter startBlock, final boolean fullTransactionObjects) {
        return web3jRx.replayPastBlocksFlowable(startBlock, fullTransactionObjects);
    }

    @Override
    public Flowable<org.web3j.protocol.core.methods.response.Transaction>
            replayPastTransactionsFlowable(
                    final DefaultBlockParameter startBlock, final DefaultBlockParameter endBlock) {
        return web3jRx.replayTransactionsFlowable(startBlock, endBlock);
    }

    @Override
    public Flowable<org.web3j.protocol.core.methods.response.Transaction>
            replayPastTransactionsFlowable(final DefaultBlockParameter startBlock) {
        return web3jRx.replayPastTransactionsFlowable(startBlock);
    }

    @Override
    public Flowable<EthBlock> replayPastAndFutureBlocksFlowable(
            final DefaultBlockParameter startBlock, final boolean fullTransactionObjects) {
        return web3jRx.replayPastAndFutureBlocksFlowable(
                startBlock, fullTransactionObjects, blockTime);
    }

    @Override
    public Flowable<org.web3j.protocol.core.methods.response.Transaction>
            replayPastAndFutureTransactionsFlowable(final DefaultBlockParameter startBlock) {
        return web3jRx.replayPastAndFutureTransactionsFlowable(startBlock, blockTime);
    }

    @Override
    public void shutdown() {
        scheduledExecutorService.shutdown();
        try {
            web3jService.close();
        } catch (final IOException e) {
            throw new RuntimeException("Failed to close web3j service", e);
        }
    }

    @Override
    public BatchRequest newBatch() {
        return new BatchRequest(web3jService);
    }
}
