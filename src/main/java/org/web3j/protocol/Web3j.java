package org.web3j.protocol;

import java.math.BigInteger;

import org.web3j.protocol.core.methods.request.*;
import org.web3j.protocol.core.methods.request.EthCall;
import org.web3j.protocol.core.methods.request.EthSendTransaction;
import org.web3j.protocol.core.methods.request.ShhPost;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.JsonRpc2_0Web3j;
import org.web3j.protocol.core.Request;

/**
 * JSON-RPC Request object building factory.
 */
public interface Web3j {
    static Web3j build(Web3jService web3jService) {
        return new JsonRpc2_0Web3j(web3jService);
    }

    Request<?, Web3ClientVersion> web3ClientVersion();

    Request<?, Web3Sha3> web3Sha3(String data);

    Request<?, NetVersion> netVersion();

    Request<?, NetListening> netListening();

    Request<?, NetPeerCount> netPeerCount();

    Request<?, EthProtocolVersion> ethProtocolVersion();

    Request<?, EthCoinbase> ethCoinbase();

    Request<?, EthSyncing> ethSyncing();

    Request<?, EthMining> ethMining();

    Request<?, EthHashrate> ethHashrate();

    Request<?, EthGasPrice> ethGasPrice();

    Request<?, EthAccounts> ethAccounts();

    Request<?, EthBlockNumber> ethBlockNumber();

    Request<?, EthGetBalance> ethGetBalance(String address, DefaultBlockParameter defaultBlockParameter);

    Request<?, EthGetStorageAt> ethGetStorageAt(String address, BigInteger position,
                            DefaultBlockParameter defaultBlockParameter);

    Request<?, EthGetTransactionCount> ethGetTransactionCount(String address, DefaultBlockParameter defaultBlockParameter);

    Request<?, EthGetBlockTransactionCountByHash> ethGetBlockTransactionCountByHash(String blockHash);

    Request<?, EthGetBlockTransactionCountByNumber> ethGetBlockTransactionCountByNumber(DefaultBlockParameter defaultBlockParameter);

    Request<?, EthGetUncleCountByBlockHash> ethGetUncleCountByBlockHash(String blockHash);

    Request<?, EthGetUncleCountByBlockNumber> ethGetUncleCountByBlockNumber(DefaultBlockParameter defaultBlockParameter);

    Request<?, EthGetCode> ethGetCode(String address, DefaultBlockParameter defaultBlockParameter);

    Request<?, EthSign> ethSign(String address, String sha3HashOfDataToSign);

    Request<?, org.web3j.protocol.core.methods.response.EthSendTransaction> ethSendTransaction(EthSendTransaction transaction);

    Request<?, org.web3j.protocol.core.methods.response.EthSendTransaction> ethSendRawTransaction(String signedTransactionData);

    Request<?, org.web3j.protocol.core.methods.response.EthCall> ethCall(EthCall ethCall, DefaultBlockParameter defaultBlockParameter);

    Request<?, EthEstimateGas> ethEstimateGas(EthCall ethCall);

    Request<?, EthBlock> ethGetBlockByHash(String blockHash, boolean returnFullTransactionObjects);

    Request<?, EthBlock> ethGetBlockByNumber(
            DefaultBlockParameter defaultBlockParameter,
            boolean returnFullTransactionObjects);

    Request<?, EthTransaction> ethGetTransactionByHash(String transactionHash);

    Request<?, EthTransaction> ethGetTransactionByBlockHashAndIndex(String blockHash, BigInteger transactionIndex);

    Request<?, EthTransaction> ethGetTransactionByBlockNumberAndIndex(DefaultBlockParameter defaultBlockParameter,
                                                   BigInteger transactionIndex);

    Request<?, EthGetTransactionReceipt> ethGetTransactionReceipt(String transactionHash);

    Request<?, EthBlock> ethGetUncleByBlockHashAndIndex(String blockHash, BigInteger transactionIndex);

    Request<?, EthBlock> ethGetUncleByBlockNumberAndIndex(DefaultBlockParameter defaultBlockParameter,
                                             BigInteger transactionIndex);

    Request<?, EthGetCompilers> ethGetCompilers();

    Request<?, EthCompileLLL> ethCompileLLL(String sourceCode);

    Request<?, EthCompileSolidity> ethCompileSolidity(String sourceCode);

    Request<?, EthCompileSerpent> ethCompileSerpent(String sourceCode);

    Request<?, EthNewFilter> ethNewFilter(EthFilter ethFilter);

    Request<?, EthNewBlockFilter> ethNewBlockFilter();

    Request<?, EthNewPendingTransactionFilter> ethNewPendingTransactionFilter();

    Request<?, EthUninstallFilter> ethUninstallFilter(BigInteger filterId);

    Request<?, EthLog> ethGetFilterChanges(BigInteger filterId);

    Request<?, EthLog> ethGetFilterLogs(BigInteger filterId);

    Request<?, EthLog> ethGetLogs(EthFilter ethFilter);

    Request<?, EthGetWork> ethGetWork();

    Request<?, EthSubmitWork> ethSubmitWork(String nonce, String headerPowHash, String mixDigest);

    Request<?, EthSubmitHashrate> ethSubmitHashrate(String hashrate, String clientId);

    Request<?, DbPutString> dbPutString(String databaseName, String keyName, String stringToStore);

    Request<?, DbGetString> dbGetString(String databaseName, String keyName);

    Request<?, DbPutHex> dbPutHex(String databaseName, String keyName, String dataToStore);

    Request<?, DbGetHex> dbGetHex(String databaseName, String keyName);

    Request<?, org.web3j.protocol.core.methods.response.ShhPost> shhPost(ShhPost shhPost);

    Request<?, ShhVersion> shhVersion();

    Request<?, ShhNewIdentity> shhNewIdentity();

    Request<?, ShhHasIdentity> shhHasIdentity(String identityAddress);

    Request<?, ShhNewGroup> shhNewGroup();

    Request<?, ShhAddToGroup> shhAddToGroup(String identityAddress);

    Request<?, ShhNewFilter> shhNewFilter(ShhFilter shhFilter);

    Request<?, ShhUninstallFilter> shhUninstallFilter(BigInteger filterId);

    Request<?, ShhMessages> shhGetFilterChanges(BigInteger filterId);

    Request<?, ShhMessages> shhGetMessages(BigInteger filterId);
}
