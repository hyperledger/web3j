package org.conor10.web3j.protocol;

import org.conor10.web3j.protocol.jsonrpc20.DefaultBlockParamTag;
import org.conor10.web3j.protocol.jsonrpc20.Request;

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
    Request ethGetBalance(String address, int blockNumber);
    Request ethGetBalance(String address, DefaultBlockParamTag defaultBlockParamTag);
    /*
    Request ethGetStorageAt();
    Request ethGetTransactionCount();
    Request ethGetBlockTransactionCountByHash();
    Request ethGetBlockTransactionCountByNumber();
    Request ethGetUncleCountByBlockHash();
    Request ethGetUncleCountByBlockNumber();
    Request ethGetCode();
    Request ethSign();
    Request ethSendTransaction();
    Request ethSendRawTransaction();
    Request ethCall();
    Request ethEstimateGas();
    Request ethGetBlockByHash();
    Request ethGetBlockByNumber();
    Request ethGetTransactionByHash();
    Request ethGetTransactionByBlockHashAndIndex();
    Request ethGetTransactionByBlockNumberAndIndex();
    Request ethGetTransactionReceipt();
    Request ethGetUncleByBlockHashAndIndex();
    Request ethGetUncleByBlockNumberAndIndex();
    Request ethGetCompilers();
    Request ethCompileLLL();
    Request ethCompileSolidity();
    Request ethCompileSerpent();
    Request ethNewFilter();
    Request ethNewBlockFilter();
    Request ethNewPendingTransactionFilter();
    Request ethUninstallFilter();
    Request ethGetFilterChanges();
    Request ethGetFilterLogs();
    Request ethGetLogs();
    Request ethGetWork();
    Request ethSubmitWork();
    Request ethSubmitHashrate();
    Request dbPutString();
    Request dbGetString();
    Request dbPutHex();
    Request dbGetHex();
    Request shhPost();
    Request shhVersion();
    Request shhNewIdentity();
    Request shhHasIdentity();
    Request shhNewGroup();
    Request shhAddToGroup();
    Request shhNewFilter();
    Request shhUninstallFilter();
    Request shhGetFilterChanges();
    Request shhGetMessages();
*/
}
