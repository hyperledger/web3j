package org.web3j.protocol;

import org.web3j.methods.request.*;

import java.math.BigInteger;

import org.web3j.protocol.jsonrpc20.DefaultBlockParameter;
import org.web3j.protocol.jsonrpc20.Request;

/**
 * JSON-RPC Request Object building factory.
 */
public interface RequestFactory {
    Request web3ClientVersion();
    Request web3Sha3(String data);
    Request netVersion();
    Request netListening();
    Request netPeerCount();
    Request ethProtocolVersion();
    Request ethCoinbase();
    Request ethSyncing();

    Request ethMining();
    Request ethHashrate();
    Request ethGasPrice();
    Request ethAccounts();
    Request ethBlockNumber();
    Request ethGetBalance(String address, DefaultBlockParameter defaultBlockParameter);
    Request ethGetStorageAt(String address, BigInteger position,
                            DefaultBlockParameter defaultBlockParameter);
    Request ethGetTransactionCount(String address, DefaultBlockParameter defaultBlockParameter);
    Request ethGetBlockTransactionCountByHash(String blockHash);
    Request ethGetBlockTransactionCountByNumber(DefaultBlockParameter defaultBlockParameter);
    Request ethGetUncleCountByBlockHash(String blockHash);
    Request ethGetUncleCountByBlockNumber(DefaultBlockParameter defaultBlockParameter);
    Request ethGetCode(String address, DefaultBlockParameter defaultBlockParameter);
    Request ethSign(String address, String sha3HashOfDataToSign);
    Request ethSendTransaction(EthSendTransaction transaction);
    Request ethSendRawTransaction(String signedTransactionData);
    Request ethCall(EthCall ethCall, DefaultBlockParameter defaultBlockParameter);
    Request ethEstimateGas(EthCall ethCall, DefaultBlockParameter defaultBlockParameter);
    Request ethGetBlockByHash(String blockHash, boolean returnFullTransactionObjects);
    Request ethGetBlockByNumber(
            DefaultBlockParameter defaultBlockParameter,
            boolean returnFullTransactionObjects);
    Request ethGetTransactionByHash(String transactionHash);
    Request ethGetTransactionByBlockHashAndIndex(String blockHash, BigInteger transactionIndex);
    Request ethGetTransactionByBlockNumberAndIndex(DefaultBlockParameter defaultBlockParameter,
                                                   BigInteger transactionIndex);
    Request ethGetTransactionReceipt(String transactionHash);
    Request ethGetUncleByBlockHashAndIndex(String blockHash, BigInteger transactionIndex);
    Request ethGetUncleByBlockNumberAndIndex(DefaultBlockParameter defaultBlockParameter,
                                             BigInteger transactionIndex);
    Request ethGetCompilers();
    Request ethCompileLLL(String sourceCode);
    Request ethCompileSolidity(String sourceCode);
    Request ethCompileSerpent(String sourceCode);
    Request ethNewFilter(EthFilter ethFilter);
    Request ethNewBlockFilter();
    Request ethNewPendingTransactionFilter();
    Request ethUninstallFilter(BigInteger filterId);
    Request ethGetFilterChanges(BigInteger filterId);
    Request ethGetFilterLogs(BigInteger filterId);
    Request ethGetLogs(EthFilter ethFilter);
    Request ethGetWork();
    Request ethSubmitWork(String nonce, String headerPowHash, String mixDigest);
    Request ethSubmitHashrate(String hashrate, String clientId);
    Request dbPutString(String databaseName, String keyName, String stringToStore);
    Request dbGetString(String databaseName, String keyName);
    Request dbPutHex(String databaseName, String keyName, String dataToStore);
    Request dbGetHex(String databaseName, String keyName);
    Request shhPost(SshPost sshPost);
    Request shhVersion();
    Request shhNewIdentity();
    Request shhHasIdentity(String identityAddress);
    Request shhNewGroup();
    Request shhAddToGroup(String identityAddress);
    Request shhNewFilter(SshFilter sshFilter);
    Request shhUninstallFilter(BigInteger filterId);
    Request shhGetFilterChanges(BigInteger filterId);
    Request shhGetMessages(BigInteger filterId);
}
