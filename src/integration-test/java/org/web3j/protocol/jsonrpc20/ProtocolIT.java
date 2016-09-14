package org.web3j.protocol.jsonrpc20;

import java.io.IOException;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import org.web3j.Web3jService;
import org.web3j.methods.response.*;

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

    private Web3jService web3jService;

    private IntegrationTestConfig config = new MordenTestnetConfig();

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
        assertFalse(web3ClientVersion.getWeb3ClientVersion().isEmpty());
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
        assertFalse(netVersion.getNetVersion().isEmpty());
    }

    @Test
    public void testNetListening() throws IOException {
        NetListening netListening = web3jService.netListening();
        assertTrue(netListening.isListening());
    }

    @Test
    public void testNetPeerCount() throws IOException {
        NetPeerCount netPeerCount = web3jService.netPeerCount();
        assertTrue(netPeerCount.getQuantity().signum() == 1);
    }

    @Test
    public void testEthProtocolVersion() throws IOException {
        EthProtocolVersion ethProtocolVersion = web3jService.ethProtocolVersion();
        assertFalse(ethProtocolVersion.getProtocolVersion().isEmpty());
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
        assertTrue(ethMining.isMining());
    }

    @Test
    public void testEthHashrate() throws IOException {
        EthHashrate ethHashrate = web3jService.ethHashrate();
        assertThat(ethHashrate.getHashrate(), is(BigInteger.ZERO));
    }

    @Test
    public void testEthGasPrice() throws IOException {
        EthGasPrice ethGasPrice = web3jService.ethGasPrice();
        assertTrue(ethGasPrice.getGasPrice().signum() == 1);
    }

    @Test
    public void testEthAccounts() throws IOException {
        EthAccounts ethAccounts = web3jService.ethAccounts();
        assertFalse(ethAccounts.getAccounts().isEmpty());
    }

    @Test
    public void testEthBlockNumber() throws IOException {
        EthBlockNumber ethBlockNumber = web3jService.ethBlockNumber();
        assertTrue(ethBlockNumber.getBlockNumber().signum() == 1);
    }

    @Test
    public void testEthGetBalance() throws IOException {
        EthGetBalance ethGetBalance = web3jService.ethGetBalance(
                config.validAccount(), DefaultBlockParameter.valueOf("latest"));
        assertTrue(ethGetBalance.getBalance().signum() == 1);
    }

    @Test
    public void testEthGetStorageAt() throws IOException {
        EthGetStorageAt ethGetStorageAt = web3jService.ethGetStorageAt(
                config.validContractAddress(),
                BigInteger.valueOf(0),
                DefaultBlockParameter.valueOf("latest"));
        assertThat(ethGetStorageAt.getData(), is(config.validContractAddressPositionZero()));
    }

    @Test
    public void testEthGetTransactionCount() throws IOException {
        EthGetTransactionCount ethGetTransactionCount = web3jService.ethGetTransactionCount(
                config.validAccount(),
                DefaultBlockParameter.valueOf("latest"));
        assertTrue(ethGetTransactionCount.getTransactionCount().signum() == 1);
    }

    @Test
    public void testEthGetBlockTransactionCountByHash() throws IOException {
        EthGetBlockTransactionCountByHash ethGetBlockTransactionCountByHash =
                web3jService.ethGetBlockTransactionCountByHash(
                        config.validBlockHash());
        assertThat(ethGetBlockTransactionCountByHash.getTransactionCount(),
                equalTo(config.validBlockTransactionCount()));
    }

    @Test
    public void testEthGetBlockTransactionCountByNumber() throws IOException {
        EthGetBlockTransactionCountByNumber ethGetBlockTransactionCountByNumber =
                web3jService.ethGetBlockTransactionCountByNumber(
                        DefaultBlockParameter.valueOf(config.validBlock()));
        assertThat(ethGetBlockTransactionCountByNumber.getTransactionCount(),
                equalTo(config.validBlockTransactionCount()));
    }

    @Test
    public void testEthGetUncleCountByBlockHash() throws IOException {
        EthGetUncleCountByBlockHash ethGetUncleCountByBlockHash =
                web3jService.ethGetUncleCountByBlockHash(config.validBlockHash());
        assertThat(ethGetUncleCountByBlockHash.getUncleCount(),
                equalTo(config.validBlockUncleCount()));
    }

    @Test
    public void testEthGetUncleCountByBlockNumber() throws IOException {
        EthGetUncleCountByBlockNumber ethGetUncleCountByBlockNumber =
                web3jService.ethGetUncleCountByBlockNumber(DefaultBlockParameter.valueOf("latest"));
        assertThat(ethGetUncleCountByBlockNumber.getUncleCount(),
                equalTo(config.validBlockUncleCount()));
    }

    @Test
    public void testEthGetCode() throws IOException {
        EthGetCode ethGetCode = web3jService.ethGetCode(config.validContractAddress(),
                DefaultBlockParameter.valueOf(config.validBlock()));
        assertThat(ethGetCode.getCode(), is(config.validContractCode()));
    }

    @Ignore  // TODO: Once account unlock functionality is available
    @Test
    public void testEthSign() throws IOException {

    }

    @Ignore  // TODO: Once account unlock functionality is available
    @Test
    public void testEthSendTransaction() throws IOException {
        EthSendTransaction ethSendTransaction = web3jService.ethSendTransaction(
                config.ethSendTransaction());
        assertThat(ethSendTransaction.getTransactionHash(), is(""));
    }

    @Ignore  // TODO: Once account unlock functionality is available
    @Test
    public void testEthSendRawTransaction() throws IOException {

    }

    @Ignore  // TODO: Complete
    @Test
    public void testEthCall() throws IOException {
        EthCall ethCall = web3jService.ethCall(config.ethCall(),
                DefaultBlockParameter.valueOf(config.validBlock()));
        assertThat(ethCall.getValue(), is(""));

    }

    @Test
    public void testEthEstimateGas() throws IOException {
        EthEstimateGas ethEstimateGas = web3jService.ethEstimateGas(config.ethCall());
        assertThat(ethEstimateGas.getAmountUsed(), equalTo(BigInteger.valueOf(50000000)));
    }

    @Test
    public void testEthGetBlockByHashReturnHashObjects() throws IOException {
        EthBlock ethBlock = web3jService.ethGetBlockByHash(config.validBlockHash(), false);

        assertTrue(ethBlock.getBlock().isPresent());
        EthBlock.Block block = ethBlock.getBlock().get();
        assertThat(block.getNumber(), equalTo(config.validBlock()));
        assertThat(block.getTransactions().size(),
                is(config.validBlockTransactionCount().intValue()));
    }

    @Test
    public void testEthGetBlockByHashReturnFullTransactionObjects() throws IOException {
        EthBlock ethBlock = web3jService.ethGetBlockByHash(config.validBlockHash(), true);

        assertTrue(ethBlock.getBlock().isPresent());
        EthBlock.Block block = ethBlock.getBlock().get();
        assertThat(block.getNumber(), equalTo(config.validBlock()));
        assertThat(block.getTransactions().size(),
                equalTo(config.validBlockTransactionCount().intValue()));
    }

    @Test
    public void testEthGetBlockByNumberReturnHashObjects() throws IOException {
        EthBlock ethBlock = web3jService.ethGetBlockByNumber(
                DefaultBlockParameter.valueOf(config.validBlock()), false);

        assertTrue(ethBlock.getBlock().isPresent());
        EthBlock.Block block = ethBlock.getBlock().get();
        assertThat(block.getNumber(), equalTo(config.validBlock()));
        assertThat(block.getTransactions().size(),
                equalTo(config.validBlockTransactionCount().intValue()));
    }

    @Test
    public void testEthGetBlockByNumberReturnTransactionObjects() throws IOException {
        EthBlock ethBlock = web3jService.ethGetBlockByNumber(
                DefaultBlockParameter.valueOf(config.validBlock()), true);

        assertTrue(ethBlock.getBlock().isPresent());
        EthBlock.Block block = ethBlock.getBlock().get();
        assertThat(block.getNumber(), equalTo(config.validBlock()));
        assertThat(block.getTransactions().size(),
                equalTo(config.validBlockTransactionCount().intValue()));
    }

    @Test
    public void testEthGetTransactionByHash() throws IOException {
        EthTransaction ethTransaction = web3jService.ethGetTransactionByHash(
                config.validTransactionHash());
        assertTrue(ethTransaction.getTransaction().isPresent());
        Transaction transaction = ethTransaction.getTransaction().get();
        assertThat(transaction.getBlockHash(), is(config.validBlockHash()));
    }

    @Test
    public void testEthGetTransactionByBlockHashAndIndex() throws IOException {
        BigInteger index = BigInteger.ONE;

        EthTransaction ethTransaction = web3jService.ethGetTransactionByBlockHashAndIndex(
                config.validBlockHash(), index);
        assertTrue(ethTransaction.getTransaction().isPresent());
        Transaction transaction = ethTransaction.getTransaction().get();
        assertThat(transaction.getBlockHash(), is(config.validBlockHash()));
        assertThat(transaction.getTransactionIndex(), equalTo(index));
    }

    @Test
    public void testEthGetTransactionByBlockNumberAndIndex() throws IOException {
        BigInteger index = BigInteger.ONE;

        EthTransaction ethTransaction = web3jService.ethGetTransactionByBlockNumberAndIndex(
                DefaultBlockParameter.valueOf(config.validBlock()), index);
        assertTrue(ethTransaction.getTransaction().isPresent());
        Transaction transaction = ethTransaction.getTransaction().get();
        assertThat(transaction.getBlockHash(), is(config.validBlockHash()));
        assertThat(transaction.getTransactionIndex(), equalTo(index));
    }

    @Test
    public void testEthGetTransactionReceipt() throws IOException {
        EthGetTransactionReceipt ethGetTransactionReceipt = web3jService.ethGetTransactionReceipt(
                config.validTransactionHash());
        assertTrue(ethGetTransactionReceipt.getTransactionReceipt().isPresent());
        EthGetTransactionReceipt.TransactionReceipt transactionReceipt =
                ethGetTransactionReceipt.getTransactionReceipt().get();
        assertThat(transactionReceipt.getTransactionHash(), is(config.validTransactionHash()));
    }

    @Test
    public void testEthGetUncleByBlockHashAndIndex() throws IOException {
        EthBlock ethBlock = web3jService.ethGetUncleByBlockHashAndIndex(
                config.validUncleBlockHash(), BigInteger.ZERO);
        assertTrue(ethBlock.getBlock().isPresent());
    }

    @Test
    public void testEthGetUncleByBlockNumberAndIndex() throws IOException {
        EthBlock ethBlock = web3jService.ethGetUncleByBlockNumberAndIndex(
                DefaultBlockParameter.valueOf(config.validUncleBlock()), BigInteger.ZERO);
        assertTrue(ethBlock.getBlock().isPresent());
    }

    @Test
    public void testEthGetCompilers() throws IOException {
        EthGetCompilers ethGetCompilers = web3jService.ethGetCompilers();
        assertNotNull(ethGetCompilers.getCompilers());
    }

    @Ignore  // The method eth_compileLLL does not exist/is not available
    @Test
    public void testEthCompileLLL() throws IOException {
        EthCompileLLL ethCompileLLL = web3jService.ethCompileLLL(
                "(returnlll (suicide (caller)))");
        assertFalse(ethCompileLLL.getCompiledSourceCode().isEmpty());
    }

    @Test
    public void testEthCompileSolidity() throws IOException {
        String sourceCode = "contract test { function multiply(uint a) returns(uint d) {   return a * 7;   } }";
        EthCompileSolidity ethCompileSolidity = web3jService.ethCompileSolidity(sourceCode);
        assertNotNull(ethCompileSolidity.getCompiledSolidity());
        assertThat(ethCompileSolidity.getCompiledSolidity().getTest().getInfo().getSource(), is(sourceCode));
    }

    @Ignore  // The method eth_compileSerpent does not exist/is not available
    @Test
    public void testEthCompileSerpent() throws IOException {
        EthCompileSerpent ethCompileSerpent = web3jService.ethCompileSerpent(
                "/* some serpent */");
        assertFalse(ethCompileSerpent.getCompiledSourceCode().isEmpty());
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

//    @Test
//    public void testEthGetWork() throws IOException {
//        EthGetWork ethGetWork = web3jService.ethGetWork();
//        assertNotNull(ethGetWork.getResult());
//    }

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

    @Ignore // The method shh_version does not exist/is not available
    @Test
    public void testShhVersion() throws IOException {
        ShhVersion shhVersion = web3jService.shhVersion();
        assertNotNull(shhVersion.getVersion());
    }

    @Ignore  // The method shh_newIdentity does not exist/is not available
    @Test
    public void testShhNewIdentity() throws IOException {
        ShhNewIdentity shhNewIdentity = web3jService.shhNewIdentity();
        assertNotNull(shhNewIdentity.getAddress());
    }

    @Test
    public void testShhHasIdentity() throws IOException {
    
    }

    @Ignore  // The method shh_newIdentity does not exist/is not available
    @Test
    public void testShhNewGroup() throws IOException {
        ShhNewGroup shhNewGroup = web3jService.shhNewGroup();
        assertNotNull(shhNewGroup.getAddress());
    }

    @Ignore  // The method shh_addToGroup does not exist/is not available
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
