package org.web3j;


import java.io.IOException;
import java.math.BigInteger;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import org.web3j.methods.request.EthFilter;
import org.web3j.methods.request.ShhFilter;
import org.web3j.methods.response.*;
import org.web3j.protocol.RequestFactory;
import org.web3j.protocol.Utils;
import org.web3j.protocol.jsonrpc20.DefaultBlockParameter;
import org.web3j.protocol.jsonrpc20.JsonRpc2_0Factory;
import org.web3j.protocol.jsonrpc20.Request;
import org.web3j.protocol.jsonrpc20.Response;

/**
 * Service API.
 */
public class Web3jService {

    private RequestFactory requestFactory = new JsonRpc2_0Factory();
    private CloseableHttpClient httpclient = HttpClients.createDefault();
    private ObjectMapper objectMapper = Utils.getObjectMapper();

    private final String url;

    public Web3jService() {
        this.url = "http://localhost:8545/";
    }

    public Web3jService(String url) {
        this.url = url;
    }

    public Web3jService(String url, CloseableHttpClient httpClient) {
        this.url = url;
        this.httpclient = httpClient;
    }

    private <T extends Response> T sendRequest(
            Request jsonRpc20Request, Class<T> type) throws IOException {

        byte[] payload = objectMapper.writeValueAsBytes(jsonRpc20Request);

        HttpPost httpPost = new HttpPost(this.url);
        httpPost.setEntity(new ByteArrayEntity(payload));
        httpPost.setHeader("Content-Type", "application/json; charset=UTF-8");

        ResponseHandler<T> responseHandler = getResponseHandler(type);
        try {
            return httpclient.execute(httpPost, responseHandler);
        } finally {
            httpclient.close();
        }
    }

    public <T> ResponseHandler<T> getResponseHandler(Class<T> type) {
        return response -> {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    return objectMapper.readValue(response.getEntity().getContent(), type);
                } else {
                    return null;
                }
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        };
    }

    public Web3ClientVersion web3ClientVersion() throws IOException {
        Request jsonRpc20Request = requestFactory.web3ClientVersion();
        return sendRequest(jsonRpc20Request, Web3ClientVersion.class);
    }

    public Web3Sha3 web3Sha3(String data) throws IOException {
        Request request = requestFactory.web3Sha3(data);
        return sendRequest(request, Web3Sha3.class);
    }

    public NetVersion netVersion() throws IOException {
        Request request = requestFactory.netVersion();
        return sendRequest(request, NetVersion.class);
    }

    public NetListening netListening() throws IOException {
        Request request = requestFactory.netListening();
        return sendRequest(request, NetListening.class);
    }

    public NetPeerCount netPeerCount() throws IOException {
        Request request = requestFactory.netPeerCount();
        return sendRequest(request, NetPeerCount.class);
    }

    public EthProtocolVersion ethProtocolVersion() throws IOException {
        Request request = requestFactory.ethProtocolVersion();
        return sendRequest(request, EthProtocolVersion.class);
    }

    public EthSyncing ethSyncing() throws IOException {
        Request request = requestFactory.ethSyncing();
        return sendRequest(request, EthSyncing.class);
    }

    public EthCoinbase ethCoinbase() throws IOException {
        Request request = requestFactory.ethCoinbase();
        return sendRequest(request, EthCoinbase.class);
    }

    public EthMining ethMining() throws IOException {
        Request request = requestFactory.ethMining();
        return sendRequest(request, EthMining.class);
    }

    public EthHashrate ethHashrate() throws IOException {
        Request request = requestFactory.ethHashrate();
        return sendRequest(request, EthHashrate.class);
    }

    public EthGasPrice ethGasPrice() throws IOException {
        Request request = requestFactory.ethGasPrice();
        return sendRequest(request, EthGasPrice.class);
    }

    public EthAccounts ethAccounts() throws IOException {
        Request request = requestFactory.ethAccounts();
        return sendRequest(request, EthAccounts.class);
    }

    public EthBlockNumber ethBlockNumber() throws IOException {
        Request request = requestFactory.ethBlockNumber();
        return sendRequest(request, EthBlockNumber.class);
    }

    public EthGetBalance ethGetBalance(
            String address, DefaultBlockParameter defaultBlockParameter) throws IOException {
        Request request = requestFactory.ethGetBalance(address, defaultBlockParameter);
        return sendRequest(request, EthGetBalance.class);
    }

    public EthGetStorageAt ethGetStorageAt(
            String address, BigInteger position,
            DefaultBlockParameter defaultBlockParameter) throws IOException {
        Request request = requestFactory.ethGetStorageAt(address, position, defaultBlockParameter);
        return sendRequest(request, EthGetStorageAt.class);
    }

    public EthGetTransactionCount ethGetTransactionCount(
            String address, DefaultBlockParameter defaultBlockParameter) throws IOException {
        Request request = requestFactory.ethGetTransactionCount(address, defaultBlockParameter);
        return sendRequest(request, EthGetTransactionCount.class);
    }

    public EthGetBlockTransactionCountByHash ethGetBlockTransactionCountByHash(
            String blockHash) throws IOException {
        Request request = requestFactory.ethGetBlockTransactionCountByHash(blockHash);
        return sendRequest(request, EthGetBlockTransactionCountByHash.class);
    }

    public EthGetBlockTransactionCountByNumber ethGetBlockTransactionCountByNumber(
            DefaultBlockParameter defaultBlockParameter) throws IOException {
        Request request = requestFactory.ethGetBlockTransactionCountByNumber(defaultBlockParameter);
        return sendRequest(request, EthGetBlockTransactionCountByNumber.class);
    }

    public EthGetUncleCountByBlockHash ethGetUncleCountByBlockHash(
            String blockHash) throws IOException {
        Request request = requestFactory.ethGetUncleCountByBlockHash(blockHash);
        return sendRequest(request, EthGetUncleCountByBlockHash.class);
    }

    public EthGetUncleCountByBlockNumber ethGetUncleCountByBlockNumber(
            DefaultBlockParameter defaultBlockParameter) throws IOException {
        Request request = requestFactory.ethGetUncleCountByBlockNumber(defaultBlockParameter);
        return sendRequest(request, EthGetUncleCountByBlockNumber.class);
    }

    public EthGetCode ethGetCode(
            String address, DefaultBlockParameter defaultBlockParameter) throws IOException {
        Request request = requestFactory.ethGetCode(address, defaultBlockParameter);
        return sendRequest(request, EthGetCode.class);
    }

    public EthSign ethSign(String address, String sha3HashOfDataToSign) throws IOException {
        Request request = requestFactory.ethSign(address, sha3HashOfDataToSign);
        return sendRequest(request, EthSign.class);
    }

    public EthSendTransaction ethSendTransaction(
            org.web3j.methods.request.EthSendTransaction transaction) throws IOException {
        Request request = requestFactory.ethSendTransaction(transaction);
        return sendRequest(request, EthSendTransaction.class);
    }

    public EthSendRawTransaction ethSendRawTransaction(
            String signedTransactionData) throws IOException {
        Request request = requestFactory.ethSendRawTransaction(signedTransactionData);
        return sendRequest(request, EthSendRawTransaction.class);
    }

    public EthCall ethCall(
            org.web3j.methods.request.EthCall ethCall,
            DefaultBlockParameter defaultBlockParameter) throws IOException {
        Request request = requestFactory.ethCall(ethCall, defaultBlockParameter);
        return sendRequest(request, EthCall.class);
    }

    public EthEstimateGas ethEstimateGas(
            org.web3j.methods.request.EthCall ethCall,
            DefaultBlockParameter defaultBlockParameter) throws IOException {
        Request request = requestFactory.ethEstimateGas(ethCall, defaultBlockParameter);
        return sendRequest(request, EthEstimateGas.class);
    }

    public EthBlock ethGetBlockByHash(
            String blockHash, boolean returnFullTransactionObjects) throws IOException {
        Request request = requestFactory.ethGetBlockByHash(blockHash, returnFullTransactionObjects);
        return sendRequest(request, EthBlock.class);
    }

    public EthBlock ethGetBlockByNumber(
            DefaultBlockParameter defaultBlockParameter,
            boolean returnFullTransactionObjects) throws IOException {
        Request request = requestFactory.ethGetBlockByNumber(
                defaultBlockParameter, returnFullTransactionObjects);
        return sendRequest(request, EthBlock.class);
    }

    public EthTransaction ethGetTransactionByHash(String transactionHash) throws IOException {
        Request request = requestFactory.ethGetTransactionByHash(transactionHash);
        return sendRequest(request, EthTransaction.class);
    }

    public EthTransaction ethGetTransactionByBlockHashAndIndex(
            String blockHash, BigInteger transactionIndex) throws IOException {
        Request request = requestFactory.ethGetTransactionByBlockHashAndIndex(
                blockHash, transactionIndex);
        return sendRequest(request, EthTransaction.class);
    }

    public EthTransaction ethGetTransactionByBlockNumberAndIndex(
            DefaultBlockParameter defaultBlockParameter,
            BigInteger transactionIndex) throws IOException {
        Request request = requestFactory.ethGetTransactionByBlockNumberAndIndex(
                defaultBlockParameter, transactionIndex);
        return sendRequest(request, EthTransaction.class);
    }

    public EthGetTransactionReceipt ethGetTransactionReceipt(
            String transactionHash) throws IOException {
        Request request = requestFactory.ethGetTransactionReceipt(transactionHash);
        return sendRequest(request, EthGetTransactionReceipt.class);
    }

    public EthBlock ethGetUncleByBlockHashAndIndex(
            String blockHash, BigInteger transactionIndex) throws IOException {
        Request request = requestFactory.ethGetUncleByBlockHashAndIndex(
                blockHash, transactionIndex);
        return sendRequest(request, EthBlock.class);
    }

    public EthBlock ethGetUncleByBlockNumberAndIndex(
            DefaultBlockParameter defaultBlockParameter,
            BigInteger transactionIndex) throws IOException {
        Request request = requestFactory.ethGetUncleByBlockNumberAndIndex(
                defaultBlockParameter, transactionIndex);
        return sendRequest(request, EthBlock.class);
    }

    public EthGetCompilers ethGetCompilers() throws IOException {
        Request request = requestFactory.ethGetCompilers();
        return sendRequest(request, EthGetCompilers.class);
    }

    public EthCompileLLL ethCompileLLL(String sourceCode) throws IOException {
        Request request = requestFactory.ethCompileLLL(sourceCode);
        return sendRequest(request, EthCompileLLL.class);
    }

    public EthCompileSolidity ethCompileSolidity(String sourceCode) throws IOException {
        Request request = requestFactory.ethCompileSolidity(sourceCode);
        return sendRequest(request, EthCompileSolidity.class);
    }

    public EthCompileSerpent ethCompileSerpent(String sourceCode) throws IOException {
        Request request = requestFactory.ethCompileSerpent(sourceCode);
        return sendRequest(request, EthCompileSerpent.class);
    }

    public EthNewFilter ethNewFilter(EthFilter ethFilter) throws IOException {
        Request request = requestFactory.ethNewFilter(ethFilter);
        return sendRequest(request, EthNewFilter.class);
    }

    public EthNewBlockFilter ethNewBlockFilter() throws IOException {
        Request request = requestFactory.ethNewBlockFilter();
        return sendRequest(request, EthNewBlockFilter.class);
    }

    public EthNewPendingTransactionFilter ethNewPendingTransactionFilter() throws IOException {
        Request request = requestFactory.ethNewPendingTransactionFilter();
        return sendRequest(request, EthNewPendingTransactionFilter.class);
    }

    public EthUninstallFilter ethUninstallFilter(BigInteger filterId) throws IOException {
        Request request = requestFactory.ethUninstallFilter(filterId);
        return sendRequest(request, EthUninstallFilter.class);
    }

    public EthLog ethGetFilterChanges(BigInteger filterId) throws IOException {
        Request request = requestFactory.ethGetFilterChanges(filterId);
        return sendRequest(request, EthLog.class);
    }

    public EthLog ethGetFilterLogs(BigInteger filterId) throws IOException {
        Request request = requestFactory.ethGetFilterLogs(filterId);
        return sendRequest(request, EthLog.class);
    }

    public EthLog ethGetLogs(EthFilter ethFilter) throws IOException {
        Request request = requestFactory.ethGetLogs(ethFilter);
        return sendRequest(request, EthLog.class);
    }

    public EthGetWork ethGetWork() throws IOException {
        Request request = requestFactory.ethGetWork();
        return sendRequest(request, EthGetWork.class);
    }

    public EthSubmitWork ethSubmitWork(
            String nonce, String headerPowHash, String mixDigest) throws IOException {
        Request request = requestFactory.ethSubmitWork(nonce, headerPowHash, mixDigest);
        return sendRequest(request, EthSubmitWork.class);
    }

    public EthSubmitHashrate ethSubmitHashrate(
            String hashrate, String clientId) throws IOException {
        Request request = requestFactory.ethSubmitHashrate(hashrate, clientId);
        return sendRequest(request, EthSubmitHashrate.class);
    }

    public DbPutString dbPutString(
            String databaseName, String keyName, String stringToStore) throws IOException {
        Request request = requestFactory.dbPutString(databaseName, keyName, stringToStore);
        return sendRequest(request, DbPutString.class);
    }

    public DbGetString dbGetString(String databaseName, String keyName) throws IOException {
        Request request = requestFactory.dbGetString(databaseName, keyName);
        return sendRequest(request, DbGetString.class);
    }

    public DbPutHex dbPutHex(
            String databaseName, String keyName, String dataToStore) throws IOException {
        Request request = requestFactory.dbPutHex(databaseName, keyName, dataToStore);
        return sendRequest(request, DbPutHex.class);
    }

    public DbGetHex dbGetHex(String databaseName, String keyName) throws IOException {
        Request request = requestFactory.dbGetHex(databaseName, keyName);
        return sendRequest(request, DbGetHex.class);
    }

    public ShhPost shhPost(org.web3j.methods.request.ShhPost shhPost) throws IOException {
        Request request = requestFactory.shhPost(shhPost);
        return sendRequest(request, ShhPost.class);
    }

    public ShhVersion shhVersion() throws IOException {
        Request request = requestFactory.shhVersion();
        return sendRequest(request, ShhVersion.class);
    }

    public ShhNewIdentity shhNewIdentity() throws IOException {
        Request request = requestFactory.shhNewIdentity();
        return sendRequest(request, ShhNewIdentity.class);
    }

    public ShhHasIdentity shhHasIdentity(String identityAddress) throws IOException {
        Request request = requestFactory.shhHasIdentity(identityAddress);
        return sendRequest(request, ShhHasIdentity.class);
    }

    public ShhNewGroup shhNewGroup() throws IOException {
        Request request = requestFactory.shhNewGroup();
        return sendRequest(request, ShhNewGroup.class);
    }

    public ShhAddToGroup shhAddToGroup(String identityAddress) throws IOException {
        Request request = requestFactory.shhAddToGroup(identityAddress);
        return sendRequest(request, ShhAddToGroup.class);
    }

    public ShhNewFilter shhNewFilter(ShhFilter shhFilter) throws IOException {
        Request request = requestFactory.shhNewFilter(shhFilter);
        return sendRequest(request, ShhNewFilter.class);
    }

    public ShhUninstallFilter shhUninstallFilter(BigInteger filterId) throws IOException {
        Request request = requestFactory.shhUninstallFilter(filterId);
        return sendRequest(request, ShhUninstallFilter.class);
    }

    public ShhMessages shhGetFilterChanges(BigInteger filterId) throws IOException {
        Request request = requestFactory.shhGetFilterChanges(filterId);
        return sendRequest(request, ShhMessages.class);
    }

    public ShhMessages shhGetMessages(BigInteger filterId) throws IOException {
        Request request = requestFactory.shhGetMessages(filterId);
        return sendRequest(request, ShhMessages.class);
    }
}
