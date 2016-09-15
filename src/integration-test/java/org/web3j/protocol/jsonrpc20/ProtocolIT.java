package org.web3j.protocol.jsonrpc20;

import java.math.BigInteger;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import org.web3j.protocol.http.HttpService;
import org.web3j.methods.response.*;
import org.web3j.protocol.Web3j;

import static junit.framework.TestCase.assertFalse;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * JSON-RPC 2.0 Integration Tests.
 */
public class ProtocolIT {

    private Web3j web3J;

    private IntegrationTestConfig config = new MordenTestnetConfig();

    public ProtocolIT() {
        System.setProperty("org.apache.commons.logging.Log","org.apache.commons.logging.impl.SimpleLog");
        System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.wire", "DEBUG");
    }

    @Before
    public void setUp() {
        this.web3J = Web3j.build(new HttpService());
    }

    @Test
    public void testWeb3ClientVersion() throws Exception {
        Web3ClientVersion web3ClientVersion = web3J.web3ClientVersion().sendRequest();
        assertFalse(web3ClientVersion.getWeb3ClientVersion().isEmpty());
    }

    @Test
    public void testWeb3Sha3() throws Exception {
        Web3Sha3 web3Sha3 = web3J.web3Sha3("0x68656c6c6f20776f726c64").sendRequest();
        assertThat(web3Sha3.getResult(),
                is("0x47173285a8d7341e5e972fc677286384f802f8ef42a5ec5f03bbfa254cb01fad"));
    }

    @Test
    public void testNetVersion() throws Exception {
        NetVersion netVersion = web3J.netVersion().sendRequest();
        assertFalse(netVersion.getNetVersion().isEmpty());
    }

    @Test
    public void testNetListening() throws Exception {
        NetListening netListening = web3J.netListening().sendRequest();
        assertTrue(netListening.isListening());
    }

    @Test
    public void testNetPeerCount() throws Exception {
        NetPeerCount netPeerCount = web3J.netPeerCount().sendRequest();
        assertTrue(netPeerCount.getQuantity().signum() == 1);
    }

    @Test
    public void testEthProtocolVersion() throws Exception {
        EthProtocolVersion ethProtocolVersion = web3J.ethProtocolVersion().sendRequest();
        assertFalse(ethProtocolVersion.getProtocolVersion().isEmpty());
    }

    @Test
    public void testEthSyncing() throws Exception {
        EthSyncing ethSyncing = web3J.ethSyncing().sendRequest();
        assertNotNull(ethSyncing.getResult());
    }

    @Test
    public void testEthCoinbase() throws Exception {
        EthCoinbase ethCoinbase = web3J.ethCoinbase().sendRequest();
        assertNotNull(ethCoinbase.getAddress());
    }

    @Test
    public void testEthMining() throws Exception {
        EthMining ethMining = web3J.ethMining().sendRequest();
        assertTrue(ethMining.isMining());
    }

    @Test
    public void testEthHashrate() throws Exception {
        EthHashrate ethHashrate = web3J.ethHashrate().sendRequest();
        assertThat(ethHashrate.getHashrate(), is(BigInteger.ZERO));
    }

    @Test
    public void testEthGasPrice() throws Exception {
        EthGasPrice ethGasPrice = web3J.ethGasPrice().sendRequest();
        assertTrue(ethGasPrice.getGasPrice().signum() == 1);
    }

    @Test
    public void testEthAccounts() throws Exception {
        EthAccounts ethAccounts = web3J.ethAccounts().sendRequest();
        assertFalse(ethAccounts.getAccounts().isEmpty());
    }

    @Test
    public void testEthBlockNumber() throws Exception {
        EthBlockNumber ethBlockNumber = web3J.ethBlockNumber().sendRequest();
        assertTrue(ethBlockNumber.getBlockNumber().signum() == 1);
    }

    @Test
    public void testEthGetBalance() throws Exception {
        EthGetBalance ethGetBalance = web3J.ethGetBalance(
                config.validAccount(), DefaultBlockParameter.valueOf("latest")).sendRequest();
        assertTrue(ethGetBalance.getBalance().signum() == 1);
    }

    @Test
    public void testEthGetStorageAt() throws Exception {
        EthGetStorageAt ethGetStorageAt = web3J.ethGetStorageAt(
                config.validContractAddress(),
                BigInteger.valueOf(0),
                DefaultBlockParameter.valueOf("latest")).sendRequest();
        assertThat(ethGetStorageAt.getData(), is(config.validContractAddressPositionZero()));
    }

    @Test
    public void testEthGetTransactionCount() throws Exception {
        EthGetTransactionCount ethGetTransactionCount = web3J.ethGetTransactionCount(
                config.validAccount(),
                DefaultBlockParameter.valueOf("latest")).sendRequest();
        assertTrue(ethGetTransactionCount.getTransactionCount().signum() == 1);
    }

    @Test
    public void testEthGetBlockTransactionCountByHash() throws Exception {
        EthGetBlockTransactionCountByHash ethGetBlockTransactionCountByHash =
                web3J.ethGetBlockTransactionCountByHash(
                        config.validBlockHash()).sendRequest();
        assertThat(ethGetBlockTransactionCountByHash.getTransactionCount(),
                equalTo(config.validBlockTransactionCount()));
    }

    @Test
    public void testEthGetBlockTransactionCountByNumber() throws Exception {
        EthGetBlockTransactionCountByNumber ethGetBlockTransactionCountByNumber =
                web3J.ethGetBlockTransactionCountByNumber(
                        DefaultBlockParameter.valueOf(config.validBlock())).sendRequest();
        assertThat(ethGetBlockTransactionCountByNumber.getTransactionCount(),
                equalTo(config.validBlockTransactionCount()));
    }

    @Test
    public void testEthGetUncleCountByBlockHash() throws Exception {
        EthGetUncleCountByBlockHash ethGetUncleCountByBlockHash =
                web3J.ethGetUncleCountByBlockHash(config.validBlockHash()).sendRequest();
        assertThat(ethGetUncleCountByBlockHash.getUncleCount(),
                equalTo(config.validBlockUncleCount()));
    }

    @Test
    public void testEthGetUncleCountByBlockNumber() throws Exception {
        EthGetUncleCountByBlockNumber ethGetUncleCountByBlockNumber =
                web3J.ethGetUncleCountByBlockNumber(
                        DefaultBlockParameter.valueOf("latest")).sendRequest();
        assertThat(ethGetUncleCountByBlockNumber.getUncleCount(),
                equalTo(config.validBlockUncleCount()));
    }

    @Test
    public void testEthGetCode() throws Exception {
        EthGetCode ethGetCode = web3J.ethGetCode(config.validContractAddress(),
                DefaultBlockParameter.valueOf(config.validBlock())).sendRequest();
        assertThat(ethGetCode.getCode(), is(config.validContractCode()));
    }

    @Ignore  // TODO: Once account unlock functionality is available
    @Test
    public void testEthSign() throws Exception {

    }

    @Ignore  // TODO: Once account unlock functionality is available
    @Test
    public void testEthSendTransaction() throws Exception {
        EthSendTransaction ethSendTransaction = web3J.ethSendTransaction(
                config.ethSendTransaction()).sendRequest();
        assertThat(ethSendTransaction.getTransactionHash(), is(""));
    }

    @Ignore  // TODO: Once account unlock functionality is available
    @Test
    public void testEthSendRawTransaction() throws Exception {

    }

    @Ignore  // TODO: Complete
    @Test
    public void testEthCall() throws Exception {
        EthCall ethCall = web3J.ethCall(config.ethCall(),
                DefaultBlockParameter.valueOf(config.validBlock())).sendRequest();
        assertThat(ethCall.getValue(), is(""));

    }

    @Test
    public void testEthEstimateGas() throws Exception {
        EthEstimateGas ethEstimateGas = web3J.ethEstimateGas(config.ethCall())
                .sendRequest();
        assertThat(ethEstimateGas.getAmountUsed(), equalTo(BigInteger.valueOf(50000000)));
    }

    @Test
    public void testEthGetBlockByHashReturnHashObjects() throws Exception {
        EthBlock ethBlock = web3J.ethGetBlockByHash(config.validBlockHash(), false)
                .sendRequest();

        assertTrue(ethBlock.getBlock().isPresent());
        EthBlock.Block block = ethBlock.getBlock().get();
        assertThat(block.getNumber(), equalTo(config.validBlock()));
        assertThat(block.getTransactions().size(),
                is(config.validBlockTransactionCount().intValue()));
    }

    @Test
    public void testEthGetBlockByHashReturnFullTransactionObjects() throws Exception {
        EthBlock ethBlock = web3J.ethGetBlockByHash(config.validBlockHash(), true)
                .sendRequest();

        assertTrue(ethBlock.getBlock().isPresent());
        EthBlock.Block block = ethBlock.getBlock().get();
        assertThat(block.getNumber(), equalTo(config.validBlock()));
        assertThat(block.getTransactions().size(),
                equalTo(config.validBlockTransactionCount().intValue()));
    }

    @Test
    public void testEthGetBlockByNumberReturnHashObjects() throws Exception {
        EthBlock ethBlock = web3J.ethGetBlockByNumber(
                DefaultBlockParameter.valueOf(config.validBlock()), false).sendRequest();

        assertTrue(ethBlock.getBlock().isPresent());
        EthBlock.Block block = ethBlock.getBlock().get();
        assertThat(block.getNumber(), equalTo(config.validBlock()));
        assertThat(block.getTransactions().size(),
                equalTo(config.validBlockTransactionCount().intValue()));
    }

    @Test
    public void testEthGetBlockByNumberReturnTransactionObjects() throws Exception {
        EthBlock ethBlock = web3J.ethGetBlockByNumber(
                DefaultBlockParameter.valueOf(config.validBlock()), true).sendRequest();

        assertTrue(ethBlock.getBlock().isPresent());
        EthBlock.Block block = ethBlock.getBlock().get();
        assertThat(block.getNumber(), equalTo(config.validBlock()));
        assertThat(block.getTransactions().size(),
                equalTo(config.validBlockTransactionCount().intValue()));
    }

    @Test
    public void testEthGetTransactionByHash() throws Exception {
        EthTransaction ethTransaction = web3J.ethGetTransactionByHash(
                config.validTransactionHash()).sendRequest();
        assertTrue(ethTransaction.getTransaction().isPresent());
        Transaction transaction = ethTransaction.getTransaction().get();
        assertThat(transaction.getBlockHash(), is(config.validBlockHash()));
    }

    @Test
    public void testEthGetTransactionByBlockHashAndIndex() throws Exception {
        BigInteger index = BigInteger.ONE;

        EthTransaction ethTransaction = web3J.ethGetTransactionByBlockHashAndIndex(
                config.validBlockHash(), index).sendRequest();
        assertTrue(ethTransaction.getTransaction().isPresent());
        Transaction transaction = ethTransaction.getTransaction().get();
        assertThat(transaction.getBlockHash(), is(config.validBlockHash()));
        assertThat(transaction.getTransactionIndex(), equalTo(index));
    }

    @Test
    public void testEthGetTransactionByBlockNumberAndIndex() throws Exception {
        BigInteger index = BigInteger.ONE;

        EthTransaction ethTransaction = web3J.ethGetTransactionByBlockNumberAndIndex(
                DefaultBlockParameter.valueOf(config.validBlock()), index).sendRequest();
        assertTrue(ethTransaction.getTransaction().isPresent());
        Transaction transaction = ethTransaction.getTransaction().get();
        assertThat(transaction.getBlockHash(), is(config.validBlockHash()));
        assertThat(transaction.getTransactionIndex(), equalTo(index));
    }

    @Test
    public void testEthGetTransactionReceipt() throws Exception {
        EthGetTransactionReceipt ethGetTransactionReceipt = web3J.ethGetTransactionReceipt(
                config.validTransactionHash()).sendRequest();
        assertTrue(ethGetTransactionReceipt.getTransactionReceipt().isPresent());
        EthGetTransactionReceipt.TransactionReceipt transactionReceipt =
                ethGetTransactionReceipt.getTransactionReceipt().get();
        assertThat(transactionReceipt.getTransactionHash(), is(config.validTransactionHash()));
    }

    @Test
    public void testEthGetUncleByBlockHashAndIndex() throws Exception {
        EthBlock ethBlock = web3J.ethGetUncleByBlockHashAndIndex(
                config.validUncleBlockHash(), BigInteger.ZERO).sendRequest();
        assertTrue(ethBlock.getBlock().isPresent());
    }

    @Test
    public void testEthGetUncleByBlockNumberAndIndex() throws Exception {
        EthBlock ethBlock = web3J.ethGetUncleByBlockNumberAndIndex(
                DefaultBlockParameter.valueOf(config.validUncleBlock()), BigInteger.ZERO)
                .sendRequest();
        assertTrue(ethBlock.getBlock().isPresent());
    }

    @Test
    public void testEthGetCompilers() throws Exception {
        EthGetCompilers ethGetCompilers = web3J.ethGetCompilers().sendRequest();
        assertNotNull(ethGetCompilers.getCompilers());
    }

    @Ignore  // The method eth_compileLLL does not exist/is not available
    @Test
    public void testEthCompileLLL() throws Exception {
        EthCompileLLL ethCompileLLL = web3J.ethCompileLLL(
                "(returnlll (suicide (caller)))").sendRequest();
        assertFalse(ethCompileLLL.getCompiledSourceCode().isEmpty());
    }

    @Test
    public void testEthCompileSolidity() throws Exception {
        String sourceCode = "contract test { function multiply(uint a) returns(uint d) {   return a * 7;   } }";
        EthCompileSolidity ethCompileSolidity = web3J.ethCompileSolidity(sourceCode)
                .sendRequest();
        assertNotNull(ethCompileSolidity.getCompiledSolidity());
        assertThat(ethCompileSolidity.getCompiledSolidity().getTest().getInfo().getSource(), is(sourceCode));
    }

    @Ignore  // The method eth_compileSerpent does not exist/is not available
    @Test
    public void testEthCompileSerpent() throws Exception {
        EthCompileSerpent ethCompileSerpent = web3J.ethCompileSerpent(
                "/* some serpent */").sendRequest();
        assertFalse(ethCompileSerpent.getCompiledSourceCode().isEmpty());
    }

    @Test
    public void testEthNewFilter() throws Exception {

    }

    @Test
    public void testEthNewBlockFilter() throws Exception {
        EthNewBlockFilter ethNewBlockFilter = web3J.ethNewBlockFilter().sendRequest();
        assertNotNull(ethNewBlockFilter.getFilterId());
    }

    @Test
    public void testEthNewPendingTransactionFilter() throws Exception {
    
    }

    @Test
    public void testEthUninstallFilter() throws Exception {
    
    }

    @Test
    public void testEthGetFilterChanges() throws Exception {
    
    }

    @Test
    public void testEthGetFilterLogs() throws Exception {
    
    }

    @Test
    public void testEthGetLogs() throws Exception {
    
    }

//    @Test
//    public void testEthGetWork() throws Exception {
//        EthGetWork ethGetWork = requestFactory.ethGetWork();
//        assertNotNull(ethGetWork.getResult());
//    }

    @Test
    public void testEthSubmitWork() throws Exception {
    
    }

    @Test
    public void testEthSubmitHashrate() throws Exception {
    
    }

    @Test
    public void testDbPutString() throws Exception {
    
    }

    @Test
    public void testDbGetString() throws Exception {
    
    }

    @Test
    public void testDbPutHex() throws Exception {
    
    }

    @Test
    public void testDbGetHex() throws Exception {
    
    }

    @Test
    public void testShhPost() throws Exception {
    
    }

    @Ignore // The method shh_version does not exist/is not available
    @Test
    public void testShhVersion() throws Exception {
        ShhVersion shhVersion = web3J.shhVersion().sendRequest();
        assertNotNull(shhVersion.getVersion());
    }

    @Ignore  // The method shh_newIdentity does not exist/is not available
    @Test
    public void testShhNewIdentity() throws Exception {
        ShhNewIdentity shhNewIdentity = web3J.shhNewIdentity().sendRequest();
        assertNotNull(shhNewIdentity.getAddress());
    }

    @Test
    public void testShhHasIdentity() throws Exception {
    
    }

    @Ignore  // The method shh_newIdentity does not exist/is not available
    @Test
    public void testShhNewGroup() throws Exception {
        ShhNewGroup shhNewGroup = web3J.shhNewGroup().sendRequest();
        assertNotNull(shhNewGroup.getAddress());
    }

    @Ignore  // The method shh_addToGroup does not exist/is not available
    @Test
    public void testShhAddToGroup() throws Exception {

    }

    @Test
    public void testShhNewFilter() throws Exception {
    
    }

    @Test
    public void testShhUninstallFilter() throws Exception {
    
    }

    @Test
    public void testShhGetFilterChanges() throws Exception {
    
    }

    @Test
    public void testShhGetMessages() throws Exception {
    
    }
}
