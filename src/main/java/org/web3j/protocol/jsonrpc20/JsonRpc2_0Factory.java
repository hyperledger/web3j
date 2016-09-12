package org.web3j.protocol.jsonrpc20;


import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

import org.web3j.methods.request.*;
import org.web3j.protocol.RequestFactory;
import org.web3j.protocol.Utils;

/**
 * JSON-RPC 2.0 factory implementation.
 */
public class JsonRpc2_0Factory implements RequestFactory {

    @Override
    public Request web3ClientVersion() {
        return new Request<>(
                "web3_clientVersion",
                Collections.<String>emptyList(),
                1
        );
    }

    @Override
    public Request web3Sha3(String data) {
        return new Request<>(
                "web3_sha3",
                Arrays.asList(data),
                1
        );
    }

    @Override
    public Request netVersion() {
        return new Request<>(
                "net_version",
                Collections.<String>emptyList(),
                1
        );
    }

    @Override
    public Request netListening() {
        return new Request<>(
                "net_listening",
                Collections.<String>emptyList(),
                1
        );
    }

    @Override
    public Request netPeerCount() {
        return new Request<>(
                "net_peerCount",
                Collections.<String>emptyList(),
                1
        );
    }

    @Override
    public Request ethProtocolVersion() {
        return new Request<>(
                "eth_protocolVersion",
                Collections.<String>emptyList(),
                1
        );
    }

    @Override
    public Request ethCoinbase() {
        return new Request<>(
                "eth_coinbase",
                Collections.<String>emptyList(),
                1
        );
    }

    @Override
    public Request ethSyncing() {
        return new Request<>(
                "eth_syncing",
                Collections.<String>emptyList(),
                1
        );
    }

    @Override
    public Request ethMining() {
        return new Request<>(
                "eth_mining",
                Collections.<String>emptyList(),
                1
        );
    }

    @Override
    public Request ethHashrate() {
        return new Request<>(
                "eth_hashrate",
                Collections.<String>emptyList(),
                1
        );
    }

    @Override
    public Request ethGasPrice() {
        return new Request<>(
                "eth_gasPrice",
                Collections.<String>emptyList(),
                1
        );
    }

    @Override
    public Request ethAccounts() {
        return new Request<>(
                "eth_accounts",
                Collections.<String>emptyList(),
                1
        );
    }

    @Override
    public Request ethBlockNumber() {
        return new Request<>(
                "eth_blockNumber",
                Collections.<String>emptyList(),
                1
        );
    }

    @Override
    public Request ethGetBalance(String address, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "eth_syncing",
                Arrays.asList(address, defaultBlockParameter.getValue()),
                1
        );
    }

    @Override
    public Request ethGetStorageAt(String address, BigInteger position,
                                   DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "eth_getStorageAt",
                Arrays.asList(
                        address,
                        Utils.encodeQuantity(position),
                        defaultBlockParameter.getValue()),
                1
        );
    }

    @Override
    public Request ethGetTransactionCount(String address,
                                          DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "eth_getTransactionCount",
                Arrays.asList(address, defaultBlockParameter.getValue()),
                1
        );
    }

    @Override
    public Request ethGetBlockTransactionCountByHash(String blockHash) {
        return new Request<>(
                "eth_getBlockTransactionCountByHash",
                Arrays.asList(blockHash),
                1
        );
    }

    @Override
    public Request ethGetBlockTransactionCountByNumber(DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "eth_getBlockTransactionCountByNumber",
                Arrays.asList(defaultBlockParameter.getValue()),
                1
        );
    }

    @Override
    public Request ethGetUncleCountByBlockHash(String blockHash) {
        return new Request<>(
                "eth_getUncleCountByBlockHash",
                Arrays.asList(blockHash),
                1
        );
    }

    @Override
    public Request ethGetUncleCountByBlockNumber(DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "eth_getUncleCountByBlockNumber",
                Arrays.asList(defaultBlockParameter.getValue()),
                1
        );
    }

    @Override
    public Request ethGetCode(String address, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "eth_getCode",
                Arrays.asList(address, defaultBlockParameter.getValue()),
                1
        );
    }

    @Override
    public Request ethSign(String address, String sha3HashOfDataToSign) {
        return new Request<>(
                "eth_sign",
                Arrays.asList(address, sha3HashOfDataToSign),
                1
        );
    }

    @Override
    public Request ethSendTransaction(EthSendTransaction transaction) {
        return new Request<>(
                "eth_sendTransaction",
                Arrays.asList(transaction),
                1
        );
    }

    @Override
    public Request ethSendRawTransaction(String signedTransactionData) {
        return new Request<>(
                "eth_sendRawTransaction",
                Arrays.asList(signedTransactionData),
                1
        );
    }

    @Override
    public Request ethCall(EthCall ethCall, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "eth_call",
                Arrays.asList(ethCall, defaultBlockParameter),
                1
        );
    }

    @Override
    public Request ethEstimateGas(EthCall ethCall, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "eth_estimateGas",
                Arrays.asList(ethCall, defaultBlockParameter),
                1
        );
    }

    @Override
    public Request ethGetBlockByHash(
            String blockHash, boolean returnFullTransactionObjects) {
        return new Request<>(
                "eth_getBlockByHash",
                Arrays.asList(
                        blockHash,
                        Boolean.toString(returnFullTransactionObjects)),
                1
        );
    }

    @Override
    public Request ethGetBlockByNumber(
            DefaultBlockParameter defaultBlockParameter,
            boolean returnFullTransactionObjects) {
        return new Request<>(
                "eth_getBlockByNumber",
                Arrays.asList(
                        defaultBlockParameter.getValue(),
                        Boolean.toString(returnFullTransactionObjects)),
                1
        );
    }

    @Override
    public Request ethGetTransactionByHash(String transactionHash) {
        return new Request<>(
                "eth_getTransactionByHash",
                Arrays.asList(transactionHash),
                1
        );
    }

    @Override
    public Request ethGetTransactionByBlockHashAndIndex(String blockHash, BigInteger transactionIndex) {
        return new Request<>(
                "eth_getTransactionByBlockHashAndIndex",
                Arrays.asList(
                        blockHash,
                        Utils.encodeQuantity(transactionIndex)),
                1
        );
    }

    @Override
    public Request ethGetTransactionByBlockNumberAndIndex(
            DefaultBlockParameter defaultBlockParameter, BigInteger transactionIndex) {
        return new Request<>(
                "eth_getTransactionByBlockNumberAndIndex",
                Arrays.asList(
                        defaultBlockParameter.getValue(),
                        Utils.encodeQuantity(transactionIndex)),
                1
        );
    }

    @Override
    public Request ethGetTransactionReceipt(String transactionHash) {
        return new Request<>(
                "eth_getTransactionReceipt",
                Arrays.asList(transactionHash),
                1
        );
    }

    @Override
    public Request ethGetUncleByBlockHashAndIndex(String blockHash, BigInteger transactionIndex) {
        return new Request<>(
                "eth_getUncleByBlockHashAndIndex",
                Arrays.asList(
                        blockHash,
                        Utils.encodeQuantity(transactionIndex)),
                1
        );
    }

    @Override
    public Request ethGetUncleByBlockNumberAndIndex(
            DefaultBlockParameter defaultBlockParameter, BigInteger uncleIndex) {
        return new Request<>(
                "eth_getUncleByBlockNumberAndIndex",
                Arrays.asList(
                        defaultBlockParameter.getValue(),
                        Utils.encodeQuantity(uncleIndex)),
                1
        );
    }

    @Override
    public Request ethGetCompilers() {
        return new Request<>(
                "eth_getCompilers",
                Collections.<String>emptyList(),
                1
        );
    }

    @Override
    public Request ethCompileLLL(String sourceCode) {
        return new Request<>(
                "eth_compileLLL",
                Arrays.asList(sourceCode),
                1
        );
    }

    @Override
    public Request ethCompileSolidity(String sourceCode) {
        return new Request<>(
                "eth_compileSolidity",
                Arrays.asList(sourceCode),
                1
        );
    }

    @Override
    public Request ethCompileSerpent(String sourceCode) {
        return new Request<>(
                "eth_compileSerpent",
                Arrays.asList(sourceCode),
                1
        );
    }

    @Override
    public Request ethNewFilter(EthFilter ethFilter) {
        return new Request<>(
                "eth_newFilter",
                Arrays.asList(ethFilter),
                1
        );
    }

    @Override
    public Request ethNewBlockFilter() {
        return new Request<>(
                "eth_newBlockFilter",
                Collections.<String>emptyList(),
                1
        );
    }

    @Override
    public Request ethNewPendingTransactionFilter() {
        return new Request<>(
                "eth_newPendingTransactionFilter",
                Collections.<String>emptyList(),
                1
        );
    }

    @Override
    public Request ethUninstallFilter(BigInteger filterId) {
        return new Request<>(
                "eth_uninstallFilter",
                Arrays.asList(Utils.encodeQuantity(filterId)),
                1
        );
    }

    @Override
    public Request ethGetFilterChanges(BigInteger filterId) {
        return new Request<>(
                "eth_getFilterChanges",
                Arrays.asList(Utils.encodeQuantity(filterId)),
                1
        );
    }

    @Override
    public Request ethGetFilterLogs(BigInteger filterId) {
        return new Request<>(
                "eth_getFilterLogs",
                Arrays.asList(Utils.encodeQuantity(filterId)),
                1
        );
    }

    @Override
    public Request ethGetLogs(EthFilter ethFilter) {
        return new Request<>(
                "eth_getLogs",
                Arrays.asList(ethFilter),
                1
        );
    }

    @Override
    public Request ethGetWork() {
        return new Request<>(
                "eth_getWork",
                Collections.<String>emptyList(),
                1
        );
    }

    @Override
    public Request ethSubmitWork(String nonce, String headerPowHash, String mixDigest) {
        return new Request<>(
                "eth_submitWork",
                Arrays.asList(nonce, headerPowHash, mixDigest),
                1
        );
    }

    @Override
    public Request ethSubmitHashrate(String hashrate, String clientId) {
        return new Request<>(
                "eth_submitHashrate",
                Arrays.asList(hashrate, clientId),
                1
        );
    }

    @Override
    public Request dbPutString(String databaseName, String keyName, String stringToStore) {
        return new Request<>(
                "db_putString",
                Arrays.asList(databaseName, keyName, stringToStore),
                1
        );
    }

    @Override
    public Request dbGetString(String databaseName, String keyName) {
        return new Request<>(
                "db_getString",
                Arrays.asList(databaseName, keyName),
                1
        );
    }

    @Override
    public Request dbPutHex(String databaseName, String keyName, String dataToStore) {
        return new Request<>(
                "db_putHex",
                Arrays.asList(databaseName, keyName, dataToStore),
                1
        );
    }

    @Override
    public Request dbGetHex(String databaseName, String keyName) {
        return new Request<>(
                "db_getHex",
                Arrays.asList(databaseName, keyName),
                1
        );
    }

    @Override
    public Request shhPost(ShhPost shhPost) {
        return new Request<>(
                "shh_post",
                Arrays.asList(shhPost),
                1
        );
    }

    @Override
    public Request shhVersion() {
        return new Request<>(
                "shh_version",
                Collections.<String>emptyList(),
                1
        );
    }

    @Override
    public Request shhNewIdentity() {
        return new Request<>(
                "shh_newIdentity",
                Collections.<String>emptyList(),
                1
        );
    }

    @Override
    public Request shhHasIdentity(String identityAddress) {
        return new Request<>(
                "shh_hasIdentity",
                Arrays.asList(identityAddress),
                1
        );
    }

    @Override
    public Request shhNewGroup() {
        return new Request<>(
                "shh_newGroup",
                Collections.<String>emptyList(),
                1
        );
    }

    @Override
    public Request shhAddToGroup(String identityAddress) {
        return new Request<>(
                "shh_addToGroup",
                Arrays.asList(identityAddress),
                1
        );
    }

    @Override
    public Request shhNewFilter(ShhFilter shhFilter) {
        return new Request<>(
                "shh_newFilter",
                Arrays.asList(shhFilter),
                1
        );
    }

    @Override
    public Request shhUninstallFilter(BigInteger filterId) {
        return new Request<>(
                "shh_uninstallFilter",
                Arrays.asList(filterId),
                1
        );
    }

    @Override
    public Request shhGetFilterChanges(BigInteger filterId) {
        return new Request<>(
                "shh_getFilterChanges",
                Arrays.asList(filterId),
                1
        );
    }

    @Override
    public Request shhGetMessages(BigInteger filterId) {
        return new Request<>(
                "shh_getFilterChanges",
                Arrays.asList(filterId),
                1
        );
    }
}
