package org.web3j.protocol.core;


import java.math.BigInteger;
import java.util.Arrays;

import org.junit.Test;

import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.request.*;
import org.web3j.protocol.RequestTester;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;


public class RequestTest extends RequestTester {

    private Web3j web3j;

    @Override
    protected void initWeb3Client(HttpService httpService) {
        web3j = Web3jFactory.build(httpService);
    }

    @Test
    public void testWeb3ClientVersion() throws Exception {
        web3j.web3ClientVersion().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"web3_clientVersion\",\"params\":[],\"id\":1}");
    }

    @Test
    public void testWeb3Sha3() throws Exception {
        web3j.web3Sha3("0x68656c6c6f20776f726c64").send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"web3_sha3\",\"params\":[\"0x68656c6c6f20776f726c64\"],\"id\":1}");
    }

    @Test
    public void testNetVersion() throws Exception {
        web3j.netVersion().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"net_version\",\"params\":[],\"id\":1}");
    }

    @Test
    public void testNetListening() throws Exception {
        web3j.netListening().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"net_listening\",\"params\":[],\"id\":1}");
    }

    @Test
    public void testNetPeerCount() throws Exception {
        web3j.netPeerCount().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"net_peerCount\",\"params\":[],\"id\":1}");
    }

    @Test
    public void testEthProtocolVersion() throws Exception {
        web3j.ethProtocolVersion().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eth_protocolVersion\",\"params\":[],\"id\":1}");
    }

    @Test
    public void testEthSyncing() throws Exception {
        web3j.ethSyncing().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eth_syncing\",\"params\":[],\"id\":1}");
    }

    @Test
    public void testEthCoinbase() throws Exception {
        web3j.ethCoinbase().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eth_coinbase\",\"params\":[],\"id\":1}");
    }

    @Test
    public void testEthMining() throws Exception {
        web3j.ethMining().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eth_mining\",\"params\":[],\"id\":1}");
    }

    @Test
    public void testEthHashrate() throws Exception {
        web3j.ethHashrate().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eth_hashrate\",\"params\":[],\"id\":1}");
    }

    @Test
    public void testEthGasPrice() throws Exception {
        web3j.ethGasPrice().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eth_gasPrice\",\"params\":[],\"id\":1}");
    }

    @Test
    public void testEthAccounts() throws Exception {
        web3j.ethAccounts().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eth_accounts\",\"params\":[],\"id\":1}");
    }

    @Test
    public void testEthBlockNumber() throws Exception {
        web3j.ethBlockNumber().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eth_blockNumber\",\"params\":[],\"id\":1}");
    }

    @Test
    public void testEthGetBalance() throws Exception {
        web3j.ethGetBalance("0x407d73d8a49eeb85d32cf465507dd71d507100c1",
                DefaultBlockParameterName.LATEST).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eth_getBalance\",\"params\":[\"0x407d73d8a49eeb85d32cf465507dd71d507100c1\",\"latest\"],\"id\":1}");
    }

    @Test
    public void testEthGetStorageAt() throws Exception {
        web3j.ethGetStorageAt("0x295a70b2de5e3953354a6a8344e616ed314d7251", BigInteger.ZERO,
                DefaultBlockParameterName.LATEST).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eth_getStorageAt\",\"params\":[\"0x295a70b2de5e3953354a6a8344e616ed314d7251\",\"0x0\",\"latest\"],\"id\":1}");
    }

    @Test
    public void testEthGetTransactionCount() throws Exception {
        web3j.ethGetTransactionCount("0x407d73d8a49eeb85d32cf465507dd71d507100c1",
                DefaultBlockParameterName.LATEST).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eth_getTransactionCount\",\"params\":[\"0x407d73d8a49eeb85d32cf465507dd71d507100c1\",\"latest\"],\"id\":1}");
    }

    @Test
    public void testEthGetBlockTransactionCountByHash() throws Exception {
        web3j.ethGetBlockTransactionCountByHash(
                "0xb903239f8543d04b5dc1ba6579132b143087c68db1b2168786408fcbce568238").send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eth_getBlockTransactionCountByHash\",\"params\":[\"0xb903239f8543d04b5dc1ba6579132b143087c68db1b2168786408fcbce568238\"],\"id\":1}");
    }

    @Test
    public void testEthGetBlockTransactionCountByNumber() throws Exception {
        web3j.ethGetBlockTransactionCountByNumber(DefaultBlockParameterNumber.valueOf(Numeric.toBigInt("0xe8"))).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eth_getBlockTransactionCountByNumber\",\"params\":[\"0xe8\"],\"id\":1}");
    }

    @Test
    public void testEthGetUncleCountByBlockHash() throws Exception {
        web3j.ethGetUncleCountByBlockHash("0xb903239f8543d04b5dc1ba6579132b143087c68db1b2168786408fcbce568238").send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eth_getUncleCountByBlockHash\",\"params\":[\"0xb903239f8543d04b5dc1ba6579132b143087c68db1b2168786408fcbce568238\"],\"id\":1}");
    }

    @Test
    public void testEthGetUncleCountByBlockNumber() throws Exception {
        web3j.ethGetUncleCountByBlockNumber(DefaultBlockParameterNumber.valueOf(Numeric.toBigInt("0xe8"))).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eth_getUncleCountByBlockNumber\",\"params\":[\"0xe8\"],\"id\":1}");
    }

    @Test
    public void testEthGetCode() throws Exception {
        web3j.ethGetCode("0xa94f5374fce5edbc8e2a8697c15331677e6ebf0b",
                DefaultBlockParameterNumber.valueOf(Numeric.toBigInt("0x2"))).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eth_getCode\",\"params\":[\"0xa94f5374fce5edbc8e2a8697c15331677e6ebf0b\",\"0x2\"],\"id\":1}");
    }

    @Test
    public void testEthSign() throws Exception {
        web3j.ethSign("0x8a3106a3e50576d4b6794a0e74d3bb5f8c9acaab", "0xc5d2460186f7233c927e7db2dcc703c0e500b653ca82273b7bfad8045d85a470").send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eth_sign\",\"params\":[\"0x8a3106a3e50576d4b6794a0e74d3bb5f8c9acaab\",\"0xc5d2460186f7233c927e7db2dcc703c0e500b653ca82273b7bfad8045d85a470\"],\"id\":1}");
    }

    @Test
    public void testEthSendTransaction() throws Exception {
        web3j.ethSendTransaction(new Transaction(
                "0xb60e8dd61c5d32be8058bb8eb970870f07233155",
                BigInteger.ONE,
                Numeric.toBigInt("0x9184e72a000"),
                Numeric.toBigInt("0x76c0"),
                "0xb60e8dd61c5d32be8058bb8eb970870f07233155",
                Numeric.toBigInt("0x9184e72a"),
                "0xd46e8dd67c5d32be8d46e8dd67c5d32be8058bb8eb970870f072445675058bb8eb970870f072445675")).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eth_sendTransaction\",\"params\":[{\"from\":\"0xb60e8dd61c5d32be8058bb8eb970870f07233155\",\"to\":\"0xb60e8dd61c5d32be8058bb8eb970870f07233155\",\"gas\":\"0x76c0\",\"gasPrice\":\"0x9184e72a000\",\"value\":\"0x9184e72a\",\"data\":\"0xd46e8dd67c5d32be8d46e8dd67c5d32be8058bb8eb970870f072445675058bb8eb970870f072445675\",\"nonce\":\"0x1\"}],\"id\":1}");
    }

    @Test
    public void testEthSendRawTransaction() throws Exception {
        web3j.ethSendRawTransaction("0xd46e8dd67c5d32be8d46e8dd67c5d32be8058bb8eb970870f072445675058bb8eb970870f072445675").send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eth_sendRawTransaction\",\"params\":[\"0xd46e8dd67c5d32be8d46e8dd67c5d32be8058bb8eb970870f072445675058bb8eb970870f072445675\"],\"id\":1}");
    }


    @Test
    public void testEthCall() throws Exception {
        web3j.ethCall(Transaction.createEthCallTransaction("0xb60e8dd61c5d32be8058bb8eb970870f07233155",
                        "0x0"),
                DefaultBlockParameterName.fromString("latest")).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eth_call\",\"params\":[{\"to\":\"0xb60e8dd61c5d32be8058bb8eb970870f07233155\",\"data\":\"0x0\"},\"latest\"],\"id\":1}");
    }

    @Test
    public void testEthEstimateGas() throws Exception {
        web3j.ethEstimateGas(
                Transaction.createEthCallTransaction("0x52b93c80364dc2dd4444c146d73b9836bbbb2b3f", "0x0")).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eth_estimateGas\",\"params\":[{\"to\":\"0x52b93c80364dc2dd4444c146d73b9836bbbb2b3f\",\"data\":\"0x0\"}],\"id\":1}");
    }

    @Test
    public void testEthGetBlockByHash() throws Exception {
        web3j.ethGetBlockByHash(
                "0xe670ec64341771606e55d6b4ca35a1a6b75ee3d5145a99d05921026d1527331", true).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eth_getBlockByHash\",\"params\":[\"0xe670ec64341771606e55d6b4ca35a1a6b75ee3d5145a99d05921026d1527331\",true],\"id\":1}");
    }

    @Test
    public void testEthGetBlockByNumber() throws Exception {
        web3j.ethGetBlockByNumber(
                DefaultBlockParameterNumber.valueOf(Numeric.toBigInt("0x1b4")), true).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eth_getBlockByNumber\",\"params\":[\"0x1b4\",true],\"id\":1}");
    }

    @Test
    public void testEthGetTransactionByHash() throws Exception {
        web3j.ethGetTransactionByHash(
                "0xb903239f8543d04b5dc1ba6579132b143087c68db1b2168786408fcbce568238").send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eth_getTransactionByHash\",\"params\":[\"0xb903239f8543d04b5dc1ba6579132b143087c68db1b2168786408fcbce568238\"],\"id\":1}");
    }

    @Test
    public void testEthGetTransactionByBlockHashAndIndex() throws Exception {
        web3j.ethGetTransactionByBlockHashAndIndex(
                "0xe670ec64341771606e55d6b4ca35a1a6b75ee3d5145a99d05921026d1527331",
                BigInteger.ZERO).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eth_getTransactionByBlockHashAndIndex\",\"params\":[\"0xe670ec64341771606e55d6b4ca35a1a6b75ee3d5145a99d05921026d1527331\",\"0x0\"],\"id\":1}");
    }

    @Test
    public void testEthGetTransactionByBlockNumberAndIndex() throws Exception {
        web3j.ethGetTransactionByBlockNumberAndIndex(
                DefaultBlockParameterNumber.valueOf(Numeric.toBigInt("0x29c")), BigInteger.ZERO).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eth_getTransactionByBlockNumberAndIndex\",\"params\":[\"0x29c\",\"0x0\"],\"id\":1}");
    }

    @Test
    public void testEthGetTransactionReceipt() throws Exception {
        web3j.ethGetTransactionReceipt("0xb903239f8543d04b5dc1ba6579132b143087c68db1b2168786408fcbce568238").send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eth_getTransactionReceipt\",\"params\":[\"0xb903239f8543d04b5dc1ba6579132b143087c68db1b2168786408fcbce568238\"],\"id\":1}");
    }

    @Test
    public void testEthGetUncleByBlockHashAndIndex() throws Exception {
        web3j.ethGetUncleByBlockHashAndIndex(
                "0xc6ef2fc5426d6ad6fd9e2a26abeab0aa2411b7ab17f30a99d3cb96aed1d1055b",
                BigInteger.ZERO).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eth_getUncleByBlockHashAndIndex\",\"params\":[\"0xc6ef2fc5426d6ad6fd9e2a26abeab0aa2411b7ab17f30a99d3cb96aed1d1055b\",\"0x0\"],\"id\":1}");
    }

    @Test
    public void testEthGetUncleByBlockNumberAndIndex() throws Exception {
        web3j.ethGetUncleByBlockNumberAndIndex(
                DefaultBlockParameterNumber.valueOf(Numeric.toBigInt("0x29c")), BigInteger.ZERO).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eth_getUncleByBlockNumberAndIndex\",\"params\":[\"0x29c\",\"0x0\"],\"id\":1}");
    }

    @Test
    public void testEthGetCompilers() throws Exception {
        web3j.ethGetCompilers().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eth_getCompilers\",\"params\":[],\"id\":1}");
    }

    @Test
    public void testEthCompileSolidity() throws Exception {
        web3j.ethCompileSolidity(
                "contract test { function multiply(uint a) returns(uint d) {   return a * 7;   } }")
                .send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eth_compileSolidity\",\"params\":[\"contract test { function multiply(uint a) returns(uint d) {   return a * 7;   } }\"],\"id\":1}");
    }

    @Test
    public void testEthCompileLLL() throws Exception {
        web3j.ethCompileLLL("(returnlll (suicide (caller)))").send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eth_compileLLL\",\"params\":[\"(returnlll (suicide (caller)))\"],\"id\":1}");
    }

    @Test
    public void testEthCompileSerpent() throws Exception {
        web3j.ethCompileSerpent("/* some serpent */").send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eth_compileSerpent\",\"params\":[\"/* some serpent */\"],\"id\":1}");
    }

    @Test
    public void testEthNewFilter() throws Exception {
        EthFilter ethFilter = new EthFilter()
                .addSingleTopic("0x12341234");

        web3j.ethNewFilter(ethFilter).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eth_newFilter\",\"params\":[{\"topics\":[\"0x12341234\"]}],\"id\":1}");
    }

    @Test
    public void testEthNewBlockFilter() throws Exception {
        web3j.ethNewBlockFilter().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eth_newBlockFilter\",\"params\":[],\"id\":1}");
    }

    @Test
    public void testEthNewPendingTransactionFilter() throws Exception {
        web3j.ethNewPendingTransactionFilter().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eth_newPendingTransactionFilter\",\"params\":[],\"id\":1}");
    }

    @Test
    public void testEthUninstallFilter() throws Exception {
        web3j.ethUninstallFilter(Numeric.toBigInt("0xb")).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eth_uninstallFilter\",\"params\":[\"0xb\"],\"id\":1}");
    }

    @Test
    public void testEthGetFilterChanges() throws Exception {
        web3j.ethGetFilterChanges(Numeric.toBigInt("0x16")).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eth_getFilterChanges\",\"params\":[\"0x16\"],\"id\":1}");
    }

    @Test
    public void testEthGetFilterLogs() throws Exception {
        web3j.ethGetFilterLogs(Numeric.toBigInt("0x16")).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eth_getFilterLogs\",\"params\":[\"0x16\"],\"id\":1}");
    }

    @Test
    public void testEthGetLogs() throws Exception {
        web3j.ethGetLogs(new EthFilter().addSingleTopic("0x000000000000000000000000a94f5374fce5edbc8e2a8697c15331677e6ebf0b"))
                .send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eth_getLogs\",\"params\":[{\"topics\":[\"0x000000000000000000000000a94f5374fce5edbc8e2a8697c15331677e6ebf0b\"]}],\"id\":1}");
    }

    @Test
    public void testEthGetWork() throws Exception {
        web3j.ethGetWork().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eth_getWork\",\"params\":[],\"id\":1}");
    }

    @Test
    public void testEthSubmitWork() throws Exception {
        web3j.ethSubmitWork("0x0000000000000001",
                "0x1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef",
                "0xD1FE5700000000000000000000000000D1FE5700000000000000000000000000").send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eth_submitWork\",\"params\":[\"0x0000000000000001\",\"0x1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef\",\"0xD1FE5700000000000000000000000000D1FE5700000000000000000000000000\"],\"id\":1}");
    }

    @Test
    public void testEthSubmitHashRate() throws Exception {
        web3j.ethSubmitHashrate(
                "0x0000000000000000000000000000000000000000000000000000000000500000",
                "0x59daa26581d0acd1fce254fb7e85952f4c09d0915afd33d3886cd914bc7d283c").send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eth_submitHashrate\",\"params\":[\"0x0000000000000000000000000000000000000000000000000000000000500000\",\"0x59daa26581d0acd1fce254fb7e85952f4c09d0915afd33d3886cd914bc7d283c\"],\"id\":1}");
    }

    @Test
    public void testDbPutString() throws Exception {
        web3j.dbPutString("testDB", "myKey", "myString").send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"db_putString\",\"params\":[\"testDB\",\"myKey\",\"myString\"],\"id\":1}");
    }

    @Test
    public void testDbGetString() throws Exception {
        web3j.dbGetString("testDB", "myKey").send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"db_getString\",\"params\":[\"testDB\",\"myKey\"],\"id\":1}");
    }

    @Test
    public void testDbPutHex() throws Exception {
        web3j.dbPutHex("testDB", "myKey", "0x68656c6c6f20776f726c64").send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"db_putHex\",\"params\":[\"testDB\",\"myKey\",\"0x68656c6c6f20776f726c64\"],\"id\":1}");
    }

    @Test
    public void testDbGetHex() throws Exception {
        web3j.dbGetHex("testDB", "myKey").send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"db_getHex\",\"params\":[\"testDB\",\"myKey\"],\"id\":1}");
    }

    @Test
    public void testShhVersion() throws Exception {
        web3j.shhVersion().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"shh_version\",\"params\":[],\"id\":1}");
    }

    @Test
    public void testShhPost() throws Exception {
        web3j.shhPost(new ShhPost(
                "0x04f96a5e25610293e42a73908e93ccc8c4d4dc0edcfa9fa872f50cb214e08ebf61a03e245533f97284d442460f2998cd41858798ddfd4d661997d3940272b717b1",
                "0x3e245533f97284d442460f2998cd41858798ddf04f96a5e25610293e42a73908e93ccc8c4d4dc0edcfa9fa872f50cb214e08ebf61a0d4d661997d3940272b717b1",
                Arrays.asList("0x776869737065722d636861742d636c69656e74", "0x4d5a695276454c39425154466b61693532"),
                "0x7b2274797065223a226d6",
                Numeric.toBigInt("0x64"),
                Numeric.toBigInt("0x64"))).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"shh_post\",\"params\":[{\"from\":\"0x04f96a5e25610293e42a73908e93ccc8c4d4dc0edcfa9fa872f50cb214e08ebf61a03e245533f97284d442460f2998cd41858798ddfd4d661997d3940272b717b1\",\"to\":\"0x3e245533f97284d442460f2998cd41858798ddf04f96a5e25610293e42a73908e93ccc8c4d4dc0edcfa9fa872f50cb214e08ebf61a0d4d661997d3940272b717b1\",\"topics\":[\"0x776869737065722d636861742d636c69656e74\",\"0x4d5a695276454c39425154466b61693532\"],\"payload\":\"0x7b2274797065223a226d6\",\"priority\":\"0x64\",\"ttl\":\"0x64\"}],\"id\":1}");
    }

    @Test
    public void testShhNewIdentity() throws Exception {
        web3j.shhNewIdentity().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"shh_newIdentity\",\"params\":[],\"id\":1}");
    }

    @Test
    public void testShhHasIdentity() throws Exception {
        web3j.shhHasIdentity("0x04f96a5e25610293e42a73908e93ccc8c4d4dc0edcfa9fa872f50cb214e08ebf61a03e245533f97284d442460f2998cd41858798ddfd4d661997d3940272b717b1").send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"shh_hasIdentity\",\"params\":[\"0x04f96a5e25610293e42a73908e93ccc8c4d4dc0edcfa9fa872f50cb214e08ebf61a03e245533f97284d442460f2998cd41858798ddfd4d661997d3940272b717b1\"],\"id\":1}");
    }

    @Test
    public void testShhNewGroup() throws Exception {
        web3j.shhNewGroup().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"shh_newGroup\",\"params\":[],\"id\":1}");
    }

    @Test
    public void testShhAddToGroup() throws Exception {
        web3j.shhAddToGroup("0x04f96a5e25610293e42a73908e93ccc8c4d4dc0edcfa9fa872f50cb214e08ebf61a03e245533f97284d442460f2998cd41858798ddfd4d661997d3940272b717b1").send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"shh_addToGroup\",\"params\":[\"0x04f96a5e25610293e42a73908e93ccc8c4d4dc0edcfa9fa872f50cb214e08ebf61a03e245533f97284d442460f2998cd41858798ddfd4d661997d3940272b717b1\"],\"id\":1}");
    }

    @Test
    public void testShhNewFilter() throws Exception {
        web3j.shhNewFilter(
                new ShhFilter("0x04f96a5e25610293e42a73908e93ccc8c4d4dc0edcfa9fa872f50cb214e08ebf61a03e245533f97284d442460f2998cd41858798ddfd4d661997d3940272b717b1")
                        .addSingleTopic("0x12341234bf4b564f")).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"shh_newFilter\",\"params\":[{\"topics\":[\"0x12341234bf4b564f\"],\"to\":\"0x04f96a5e25610293e42a73908e93ccc8c4d4dc0edcfa9fa872f50cb214e08ebf61a03e245533f97284d442460f2998cd41858798ddfd4d661997d3940272b717b1\"}],\"id\":1}");
    }

    @Test
    public void testShhUninstallFilter() throws Exception {
        web3j.shhUninstallFilter(Numeric.toBigInt("0x7")).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"shh_uninstallFilter\",\"params\":[\"0x7\"],\"id\":1}");
    }

    @Test
    public void testShhGetFilterChanges() throws Exception {
        web3j.shhGetFilterChanges(Numeric.toBigInt("0x7")).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"shh_getFilterChanges\",\"params\":[\"0x7\"],\"id\":1}");
    }

    @Test
    public void testShhGetMessages() throws Exception {
        web3j.shhGetMessages(Numeric.toBigInt("0x7")).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"shh_getMessages\",\"params\":[\"0x7\"],\"id\":1}");
    }

}
