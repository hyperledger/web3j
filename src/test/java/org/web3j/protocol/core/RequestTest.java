package org.web3j.protocol.core;


import org.junit.Test;

import org.web3j.protocol.core.methods.request.EthCall;
import org.web3j.protocol.RequestTester;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;


public class RequestTest extends RequestTester {

    private Web3j web3j;

    @Override
    protected void initWeb3Client(HttpService httpService) {
        web3j = Web3j.build(httpService);
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
    public void testEthCall() throws Exception {
        web3j.ethCall(new EthCall("0x52b93c80364dc2dd4444c146d73b9836bbbb2b3f", "0x0"),
                DefaultBlockParameter.valueOf("latest")).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eth_call\",\"params\":[{\"to\":\"0x52b93c80364dc2dd4444c146d73b9836bbbb2b3f\",\"data\":\"0x0\"},\"latest\"],\"id\":1}");
    }
}
