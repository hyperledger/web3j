package org.web3j.protocol.jsonrpc20;

import java.io.IOException;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import org.web3j.Web3jService;
import org.web3j.methods.response.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * JSON-RPC 2.0 Integration Tests.
 */
public class ProtocolIT {

    private Web3jService web3jService;

    public ProtocolIT() {
        System.setProperty("org.apache.commons.logging.Log","org.apache.commons.logging.impl.SimpleLog");
        System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.wire", "DEBUG");
    }

    @Before
    public void setUp() {
        this.web3jService = new Web3jService();
    }

    @Test
    public void testWeb3ClientVersion() throws IOException {
        Web3ClientVersion web3ClientVersion = web3jService.web3ClientVersion();
        assertNotNull(web3ClientVersion.getJsonrpc());
    }

    @Test
    public void testWeb3Sha3() throws IOException {
        Web3Sha3 web3Sha3 = web3jService.web3Sha3("0x68656c6c6f20776f726c64");
        assertThat(web3Sha3.getResult(),
                is("0x47173285a8d7341e5e972fc677286384f802f8ef42a5ec5f03bbfa254cb01fad"));
    }

    @Test
    public void testNetVersion() throws IOException {
        NetVersion netVersion = web3jService.netVersion();
        assertNotNull(netVersion.getJsonrpc());
    }

    @Test
    public void testNetListening() throws IOException {
        NetListening netListening = web3jService.netListening();
        assertNotNull(netListening.isListening());
    }

    @Test
    public void testNetPeerCount() throws IOException {
        NetPeerCount netPeerCount = web3jService.netPeerCount();
        assertNotNull(netPeerCount.getQuantity());
    }

    @Test
    public void testEthProtocolVersion() throws IOException {
        EthProtocolVersion ethProtocolVersion = web3jService.ethProtocolVersion();
        assertNotNull(ethProtocolVersion.getProtocolVersion());
    }

    @Test
    public void testEthSyncing() throws IOException {
        EthSyncing ethSyncing = web3jService.ethSyncing();
        assertNotNull(ethSyncing.getResult());
    }

    @Test
    public void testEthCoinbase() throws IOException {
        EthCoinbase ethCoinbase = web3jService.ethCoinbase();
        assertNotNull(ethCoinbase.getAddress());
    }

    @Test
    public void testEthMining() throws IOException {
        EthMining ethMining = web3jService.ethMining();
        assertNotNull(ethMining.isMining());
    }

    @Test
    public void testEthHashrate() throws IOException {
        EthHashrate ethHashrate = web3jService.ethHashrate();
        assertNotNull(ethHashrate.getHashrate());
    }

    @Test
    public void testEthGasPrice() throws IOException {
        EthGasPrice ethGasPrice = web3jService.ethGasPrice();
        assertNotNull(ethGasPrice.getGasPrice());
    }

    @Test
    public void testEthAccounts() throws IOException {
        EthAccounts ethAccounts = web3jService.ethAccounts();
        assertNotNull(ethAccounts.getAccounts());
    }

    @Test
    public void testEthBlockNumber() throws IOException {
        EthBlockNumber ethBlockNumber = web3jService.ethBlockNumber();
        assertNotNull(ethBlockNumber.getBlockNumber());
    }

    @Test
    public void testEthGetBalance() throws IOException {
        EthGetBalance ethGetBalance = web3jService.ethGetBalance(
                "0x407d73d8a49eeb85d32cf465507dd71d507100c1", DefaultBlockParameter.valueOf("latest"));
        assertThat(ethGetBalance.getBalance(), equalTo(BigInteger.valueOf(0)));
    }

    @Test
    public void testEthGetStorageAt() throws IOException {
        EthGetStorageAt ethGetStorageAt = web3jService.ethGetStorageAt(
                "0x407d73d8a49eeb85d32cf465507dd71d507100c1",
                BigInteger.valueOf(0),
                DefaultBlockParameter.valueOf("latest"));
        assertNotNull(ethGetStorageAt.getData());
    }

    @Test
    public void testEthGetTransactionCount() throws IOException {
        EthGetTransactionCount ethGetTransactionCount = web3jService.ethGetTransactionCount(
                "0x407d73d8a49eeb85d32cf465507dd71d507100c1",
                DefaultBlockParameter.valueOf("latest"));
        assertNotNull(ethGetTransactionCount.getTransactionCount());
    }

    @Test
    public void testEthGetBlockTransactionCountByHash() throws IOException {
    
    }

    @Test
    public void testEthGetBlockTransactionCountByNumber() throws IOException {
    
    }

    @Test
    public void testEthGetUncleCountByBlockHash() throws IOException {
    
    }

    @Test
    public void testEthGetUncleCountByBlockNumber() throws IOException {
    
    }

    @Test
    public void testEthGetCode() throws IOException {
    
    }

    @Test
    public void testEthSign() throws IOException {
    
    }

    @Test
    public void testEthSendTransaction() throws IOException {
    
    }

    @Test
    public void testEthSendRawTransaction() throws IOException {
    
    }

    @Test
    public void testEthCall() throws IOException {
    
    }

    @Test
    public void testEthEstimateGas() throws IOException {
    
    }

    @Test
    public void testEthGetBlockByHash() throws IOException {
    
    }

    @Test
    public void testEthGetBlockByNumber() throws IOException {
        
    }

    @Test
    public void testEthGetTransactionByHash() throws IOException {
    
    }

    @Test
    public void testEthGetTransactionByBlockHashAndIndex() throws IOException {
    
    }

    @Test
    public void testEthGetTransactionByBlockNumberAndIndex() {
        
    }

    @Test
    public void testEthGetTransactionReceipt() throws IOException {
    
    }

    @Test
    public void testEthGetUncleByBlockHashAndIndex() throws IOException {
    
    }

    @Test
    public void testEthGetUncleByBlockNumberAndIndex() throws IOException {
        
    }

    @Test
    public void testEthGetCompilers() throws IOException {
        EthGetCompilers ethGetCompilers = web3jService.ethGetCompilers();
        assertNotNull(ethGetCompilers.getCompilers());
    }

    @Test
    public void testEthCompileLLL() throws IOException {
    
    }

    @Test
    public void testEthCompileSolidity() throws IOException {
    
    }

    @Test
    public void testEthCompileSerpent() throws IOException {
    
    }

    @Test
    public void testEthNewFilter() throws IOException {
    
    }

    @Test
    public void testEthNewBlockFilter() throws IOException {
        EthNewBlockFilter ethNewBlockFilter = web3jService.ethNewBlockFilter();
        assertNotNull(ethNewBlockFilter.getFilterId());
    }

    @Test
    public void testEthNewPendingTransactionFilter() throws IOException {
    
    }

    @Test
    public void testEthUninstallFilter() throws IOException {
    
    }

    @Test
    public void testEthGetFilterChanges() throws IOException {
    
    }

    @Test
    public void testEthGetFilterLogs() throws IOException {
    
    }

    @Test
    public void testEthGetLogs() throws IOException {
    
    }

    @Test
    public void testEthGetWork() throws IOException {
        EthGetWork ethGetWork = web3jService.ethGetWork();
        assertNotNull(ethGetWork.getResult());
    }

    @Test
    public void testEthSubmitWork() throws IOException {
    
    }

    @Test
    public void testEthSubmitHashrate() throws IOException {
    
    }

    @Test
    public void testDbPutString() throws IOException {
    
    }

    @Test
    public void testDbGetString() throws IOException {
    
    }

    @Test
    public void testDbPutHex() throws IOException {
    
    }

    @Test
    public void testDbGetHex() throws IOException {
    
    }

    @Test
    public void testShhPost() throws IOException {
    
    }

    @Ignore // {"jsonrpc":"2.0","id":67,"error":{"code":-32601,"message":"The method shh_version does not exist/is not available"}}
    @Test
    public void testShhVersion() throws IOException {
        ShhVersion shhVersion = web3jService.shhVersion();
        assertNotNull(shhVersion.getVersion());
    }

    @Ignore  // {"jsonrpc":"2.0","id":73,"error":{"code":-32601,"message":"The method shh_newIdentity does not exist/is not available"}}
    @Test
    public void testShhNewIdentity() throws IOException {
        ShhNewIdentity shhNewIdentity = web3jService.shhNewIdentity();
        assertNotNull(shhNewIdentity.getAddress());
    }

    @Test
    public void testShhHasIdentity() throws IOException {
    
    }

    @Ignore  // {"jsonrpc":"2.0","id":73,"error":{"code":-32601,"message":"The method shh_newIdentity does not exist/is not available"}}
    @Test
    public void testShhNewGroup() throws IOException {
        ShhNewGroup shhNewGroup = web3jService.shhNewGroup();
        assertNotNull(shhNewGroup.getAddress());
    }

    @Ignore  // {"jsonrpc":"2.0","id":73,"error":{"code":-32601,"message":"The method shh_addToGroup does not exist/is not available"}}
    @Test
    public void testShhAddToGroup() throws IOException {

    }

    @Test
    public void testShhNewFilter() throws IOException {
    
    }

    @Test
    public void testShhUninstallFilter() throws IOException {
    
    }

    @Test
    public void testShhGetFilterChanges() throws IOException {
    
    }

    @Test
    public void testShhGetMessages() throws IOException {
    
    }
}
