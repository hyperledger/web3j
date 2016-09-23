package org.web3j.protocol.core;


import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.methods.request.*;
import org.web3j.protocol.core.methods.request.EthCall;
import org.web3j.protocol.core.methods.request.EthSendTransaction;
import org.web3j.protocol.core.methods.request.ShhPost;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.utils.Codec;

/**
 * JSON-RPC 2.0 factory implementation.
 */
public class JsonRpc2_0Web3j implements Web3j {

    protected static final long ID = 1;
    
    protected Web3jService web3jService;

    public JsonRpc2_0Web3j(Web3jService web3jService) {
        this.web3jService = web3jService;
    }

    @Override
    public Request<?, Web3ClientVersion> web3ClientVersion() {
        return new Request<>(
                "web3_clientVersion",
                Collections.<String>emptyList(),
                ID,
                web3jService,
                Web3ClientVersion.class);
    }

    @Override
    public Request<?, Web3Sha3> web3Sha3(String data) {
        return new Request<>(
                "web3_sha3",
                Arrays.asList(data),
                ID,
                web3jService,
                Web3Sha3.class);
    }

    @Override
    public Request<?, NetVersion> netVersion() {
        return new Request<>(
                "net_version",
                Collections.<String>emptyList(),
                ID,
                web3jService,
                NetVersion.class);
    }

    @Override
    public Request<?, NetListening> netListening() {
        return new Request<>(
                "net_listening",
                Collections.<String>emptyList(),
                ID,
                web3jService,
                NetListening.class);
    }

    @Override
    public Request<?, NetPeerCount> netPeerCount() {
        return new Request<>(
                "net_peerCount",
                Collections.<String>emptyList(),
                ID,
                web3jService,
                NetPeerCount.class);
    }

    @Override
    public Request<?, EthProtocolVersion> ethProtocolVersion() {
        return new Request<>(
                "eth_protocolVersion",
                Collections.<String>emptyList(),
                ID,
                web3jService,
                EthProtocolVersion.class);
    }

    @Override
    public Request<?, EthCoinbase> ethCoinbase() {
        return new Request<>(
                "eth_coinbase",
                Collections.<String>emptyList(),
                ID,
                web3jService,
                EthCoinbase.class);
    }

    @Override
    public Request<?, EthSyncing> ethSyncing() {
        return new Request<>(
                "eth_syncing",
                Collections.<String>emptyList(),
                ID,
                web3jService,
                EthSyncing.class);
    }

    @Override
    public Request<?, EthMining> ethMining() {
        return new Request<>(
                "eth_mining",
                Collections.<String>emptyList(),
                ID,
                web3jService,
                EthMining.class);
    }

    @Override
    public Request<?, EthHashrate> ethHashrate() {
        return new Request<>(
                "eth_hashrate",
                Collections.<String>emptyList(),
                ID,
                web3jService,
                EthHashrate.class);
    }

    @Override
    public Request<?, EthGasPrice> ethGasPrice() {
        return new Request<>(
                "eth_gasPrice",
                Collections.<String>emptyList(),
                ID,
                web3jService,
                EthGasPrice.class);
    }

    @Override
    public Request<?, EthAccounts> ethAccounts() {
        return new Request<>(
                "eth_accounts",
                Collections.<String>emptyList(),
                ID,
                web3jService,
                EthAccounts.class);
    }

    @Override
    public Request<?, EthBlockNumber> ethBlockNumber() {
        return new Request<>(
                "eth_blockNumber",
                Collections.<String>emptyList(),
                ID,
                web3jService,
                EthBlockNumber.class);
    }

    @Override
    public Request<?, EthGetBalance> ethGetBalance(
            String address, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "eth_getBalance",
                Arrays.asList(address, defaultBlockParameter.getValue()),
                ID,
                web3jService,
                EthGetBalance.class);
    }

    @Override
    public Request<?, EthGetStorageAt> ethGetStorageAt(
            String address, BigInteger position, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "eth_getStorageAt",
                Arrays.asList(
                        address,
                        Codec.encodeQuantity(position),
                        defaultBlockParameter.getValue()),
                ID,
                web3jService,
                EthGetStorageAt.class);
    }

    @Override
    public Request<?, EthGetTransactionCount> ethGetTransactionCount(
            String address, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "eth_getTransactionCount",
                Arrays.asList(address, defaultBlockParameter.getValue()),
                ID,
                web3jService,
                EthGetTransactionCount.class);
    }

    @Override
    public Request<?, EthGetBlockTransactionCountByHash> ethGetBlockTransactionCountByHash(
            String blockHash) {
        return new Request<>(
                "eth_getBlockTransactionCountByHash",
                Arrays.asList(blockHash),
                ID,
                web3jService,
                EthGetBlockTransactionCountByHash.class);
    }

    @Override
    public Request<?, EthGetBlockTransactionCountByNumber> ethGetBlockTransactionCountByNumber(
            DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "eth_getBlockTransactionCountByNumber",
                Arrays.asList(defaultBlockParameter.getValue()),
                ID,
                web3jService,
                EthGetBlockTransactionCountByNumber.class);
    }

    @Override
    public Request<?, EthGetUncleCountByBlockHash> ethGetUncleCountByBlockHash(String blockHash) {
        return new Request<>(
                "eth_getUncleCountByBlockHash",
                Arrays.asList(blockHash),
                ID,
                web3jService,
                EthGetUncleCountByBlockHash.class);
    }

    @Override
    public Request<?, EthGetUncleCountByBlockNumber> ethGetUncleCountByBlockNumber(
            DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "eth_getUncleCountByBlockNumber",
                Arrays.asList(defaultBlockParameter.getValue()),
                ID,
                web3jService,
                EthGetUncleCountByBlockNumber.class);
    }

    @Override
    public Request<?, EthGetCode> ethGetCode(
            String address, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "eth_getCode",
                Arrays.asList(address, defaultBlockParameter.getValue()),
                ID,
                web3jService,
                EthGetCode.class);
    }

    @Override
    public Request<?, EthSign> ethSign(String address, String sha3HashOfDataToSign) {
        return new Request<>(
                "eth_sign",
                Arrays.asList(address, sha3HashOfDataToSign),
                ID,
                web3jService,
                EthSign.class);
    }

    @Override
    public Request<?, org.web3j.protocol.core.methods.response.EthSendTransaction> ethSendTransaction(
            EthSendTransaction transaction) {
        return new Request<>(
                "eth_sendTransaction",
                Arrays.asList(transaction),
                ID,
                web3jService,
                org.web3j.protocol.core.methods.response.EthSendTransaction.class);
    }

    @Override
    public Request<?, org.web3j.protocol.core.methods.response.EthSendTransaction> ethSendRawTransaction(
            String signedTransactionData) {
        return new Request<>(
                "eth_sendRawTransaction",
                Arrays.asList(signedTransactionData),
                ID,
                web3jService,
                org.web3j.protocol.core.methods.response.EthSendTransaction.class);
    }

    @Override
    public Request<?, org.web3j.protocol.core.methods.response.EthCall> ethCall(
            EthCall ethCall, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "eth_call",
                Arrays.asList(ethCall, defaultBlockParameter),
                ID,
                web3jService,
                org.web3j.protocol.core.methods.response.EthCall.class);
    }

    @Override
    public Request<?, EthEstimateGas> ethEstimateGas(EthCall ethCall) {
        return new Request<>(
                "eth_estimateGas",
                Arrays.asList(ethCall),
                ID,
                web3jService,
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
                ID,
                web3jService,
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
                ID,
                web3jService,
                EthBlock.class);
    }

    @Override
    public Request<?, EthTransaction> ethGetTransactionByHash(String transactionHash) {
        return new Request<>(
                "eth_getTransactionByHash",
                Arrays.asList(transactionHash),
                ID,
                web3jService,
                EthTransaction.class);
    }

    @Override
    public Request<?, EthTransaction> ethGetTransactionByBlockHashAndIndex(
            String blockHash, BigInteger transactionIndex) {
        return new Request<>(
                "eth_getTransactionByBlockHashAndIndex",
                Arrays.asList(
                        blockHash,
                        Codec.encodeQuantity(transactionIndex)),
                ID,
                web3jService,
                EthTransaction.class);
    }

    @Override
    public Request<?, EthTransaction> ethGetTransactionByBlockNumberAndIndex(
            DefaultBlockParameter defaultBlockParameter, BigInteger transactionIndex) {
        return new Request<>(
                "eth_getTransactionByBlockNumberAndIndex",
                Arrays.asList(
                        defaultBlockParameter.getValue(),
                        Codec.encodeQuantity(transactionIndex)),
                ID,
                web3jService,
                EthTransaction.class);
    }

    @Override
    public Request<?, EthGetTransactionReceipt> ethGetTransactionReceipt(String transactionHash) {
        return new Request<>(
                "eth_getTransactionReceipt",
                Arrays.asList(transactionHash),
                ID,
                web3jService,
                EthGetTransactionReceipt.class);
    }

    @Override
    public Request<?, EthBlock> ethGetUncleByBlockHashAndIndex(
            String blockHash, BigInteger transactionIndex) {
        return new Request<>(
                "eth_getUncleByBlockHashAndIndex",
                Arrays.asList(
                        blockHash,
                        Codec.encodeQuantity(transactionIndex)),
                ID,
                web3jService,
                EthBlock.class);
    }

    @Override
    public Request<?, EthBlock> ethGetUncleByBlockNumberAndIndex(
            DefaultBlockParameter defaultBlockParameter, BigInteger uncleIndex) {
        return new Request<>(
                "eth_getUncleByBlockNumberAndIndex",
                Arrays.asList(
                        defaultBlockParameter.getValue(),
                        Codec.encodeQuantity(uncleIndex)),
                ID,
                web3jService,
                EthBlock.class);
    }

    @Override
    public Request<?, EthGetCompilers> ethGetCompilers() {
        return new Request<>(
                "eth_getCompilers",
                Collections.<String>emptyList(),
                ID,
                web3jService,
                EthGetCompilers.class);
    }

    @Override
    public Request<?, EthCompileLLL> ethCompileLLL(String sourceCode) {
        return new Request<>(
                "eth_compileLLL",
                Arrays.asList(sourceCode),
                ID,
                web3jService,
                EthCompileLLL.class);
    }

    @Override
    public Request<?, EthCompileSolidity> ethCompileSolidity(String sourceCode) {
        return new Request<>(
                "eth_compileSolidity",
                Arrays.asList(sourceCode),
                ID,
                web3jService,
                EthCompileSolidity.class);
    }

    @Override
    public Request<?, EthCompileSerpent> ethCompileSerpent(String sourceCode) {
        return new Request<>(
                "eth_compileSerpent",
                Arrays.asList(sourceCode),
                ID,
                web3jService,
                EthCompileSerpent.class);
    }

    @Override
    public Request<?, EthNewFilter> ethNewFilter(EthFilter ethFilter) {
        return new Request<>(
                "eth_newFilter",
                Arrays.asList(ethFilter),
                ID,
                web3jService,
                EthNewFilter.class);
    }

    @Override
    public Request<?, EthNewBlockFilter> ethNewBlockFilter() {
        return new Request<>(
                "eth_newBlockFilter",
                Collections.<String>emptyList(),
                ID,
                web3jService,
                EthNewBlockFilter.class);
    }

    @Override
    public Request<?, EthNewPendingTransactionFilter> ethNewPendingTransactionFilter() {
        return new Request<>(
                "eth_newPendingTransactionFilter",
                Collections.<String>emptyList(),
                ID,
                web3jService,
                EthNewPendingTransactionFilter.class);
    }

    @Override
    public Request<?, EthUninstallFilter> ethUninstallFilter(BigInteger filterId) {
        return new Request<>(
                "eth_uninstallFilter",
                Arrays.asList(Codec.encodeQuantity(filterId)),
                ID,
                web3jService,
                EthUninstallFilter.class);
    }

    @Override
    public Request<?, EthLog> ethGetFilterChanges(BigInteger filterId) {
        return new Request<>(
                "eth_getFilterChanges",
                Arrays.asList(Codec.encodeQuantity(filterId)),
                ID,
                web3jService,
                EthLog.class);
    }

    @Override
    public Request<?, EthLog> ethGetFilterLogs(BigInteger filterId) {
        return new Request<>(
                "eth_getFilterLogs",
                Arrays.asList(Codec.encodeQuantity(filterId)),
                ID,
                web3jService,
                EthLog.class);
    }

    @Override
    public Request<?, EthLog> ethGetLogs(EthFilter ethFilter) {
        return new Request<>(
                "eth_getLogs",
                Arrays.asList(ethFilter),
                ID,
                web3jService,
                EthLog.class);
    }

    @Override
    public Request<?, EthGetWork> ethGetWork() {
        return new Request<>(
                "eth_getWork",
                Collections.<String>emptyList(),
                ID,
                web3jService,
                EthGetWork.class);
    }

    @Override
    public Request<?, EthSubmitWork> ethSubmitWork(String nonce, String headerPowHash, String mixDigest) {
        return new Request<>(
                "eth_submitWork",
                Arrays.asList(nonce, headerPowHash, mixDigest),
                ID,
                web3jService,
                EthSubmitWork.class);
    }

    @Override
    public Request<?, EthSubmitHashrate> ethSubmitHashrate(String hashrate, String clientId) {
        return new Request<>(
                "eth_submitHashrate",
                Arrays.asList(hashrate, clientId),
                ID,
                web3jService,
                EthSubmitHashrate.class);
    }

    @Override
    public Request<?, DbPutString> dbPutString(
            String databaseName, String keyName, String stringToStore) {
        return new Request<>(
                "db_putString",
                Arrays.asList(databaseName, keyName, stringToStore),
                ID,
                web3jService,
                DbPutString.class);
    }

    @Override
    public Request<?, DbGetString> dbGetString(String databaseName, String keyName) {
        return new Request<>(
                "db_getString",
                Arrays.asList(databaseName, keyName),
                ID,
                web3jService,
                DbGetString.class);
    }

    @Override
    public Request<?, DbPutHex> dbPutHex(String databaseName, String keyName, String dataToStore) {
        return new Request<>(
                "db_putHex",
                Arrays.asList(databaseName, keyName, dataToStore),
                ID,
                web3jService,
                DbPutHex.class);
    }

    @Override
    public Request<?, DbGetHex> dbGetHex(String databaseName, String keyName) {
        return new Request<>(
                "db_getHex",
                Arrays.asList(databaseName, keyName),
                ID,
                web3jService,
                DbGetHex.class);
    }

    @Override
    public Request<?, org.web3j.protocol.core.methods.response.ShhPost> shhPost(ShhPost shhPost) {
        return new Request<>(
                "shh_post",
                Arrays.asList(shhPost),
                ID,
                web3jService,
                org.web3j.protocol.core.methods.response.ShhPost.class);
    }

    @Override
    public Request<?, ShhVersion> shhVersion() {
        return new Request<>(
                "shh_version",
                Collections.<String>emptyList(),
                ID,
                web3jService,
                ShhVersion.class);
    }

    @Override
    public Request<?, ShhNewIdentity> shhNewIdentity() {
        return new Request<>(
                "shh_newIdentity",
                Collections.<String>emptyList(),
                ID,
                web3jService,
                ShhNewIdentity.class);
    }

    @Override
    public Request<?, ShhHasIdentity> shhHasIdentity(String identityAddress) {
        return new Request<>(
                "shh_hasIdentity",
                Arrays.asList(identityAddress),
                ID,
                web3jService,
                ShhHasIdentity.class);
    }

    @Override
    public Request<?, ShhNewGroup> shhNewGroup() {
        return new Request<>(
                "shh_newGroup",
                Collections.<String>emptyList(),
                ID,
                web3jService,
                ShhNewGroup.class);
    }

    @Override
    public Request<?, ShhAddToGroup> shhAddToGroup(String identityAddress) {
        return new Request<>(
                "shh_addToGroup",
                Arrays.asList(identityAddress),
                ID,
                web3jService,
                ShhAddToGroup.class);
    }

    @Override
    public Request<?, ShhNewFilter> shhNewFilter(ShhFilter shhFilter) {
        return new Request<>(
                "shh_newFilter",
                Arrays.asList(shhFilter),
                ID,
                web3jService,
                ShhNewFilter.class);
    }

    @Override
    public Request<?, ShhUninstallFilter> shhUninstallFilter(BigInteger filterId) {
        return new Request<>(
                "shh_uninstallFilter",
                Arrays.asList(filterId),
                ID,
                web3jService,
                ShhUninstallFilter.class);
    }

    @Override
    public Request<?, ShhMessages> shhGetFilterChanges(BigInteger filterId) {
        return new Request<>(
                "shh_getFilterChanges",
                Arrays.asList(filterId),
                ID,
                web3jService,
                ShhMessages.class);
    }

    @Override
    public Request<?, ShhMessages> shhGetMessages(BigInteger filterId) {
        return new Request<>(
                "shh_getFilterChanges",
                Arrays.asList(filterId),
                ID,
                web3jService,
                ShhMessages.class);
    }
}
