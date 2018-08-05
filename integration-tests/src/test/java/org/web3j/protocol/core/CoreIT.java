package org.web3j.protocol.core;

import java.math.BigInteger;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthCoinbase;
import org.web3j.protocol.core.methods.response.EthCompileLLL;
import org.web3j.protocol.core.methods.response.EthCompileSerpent;
import org.web3j.protocol.core.methods.response.EthCompileSolidity;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.protocol.core.methods.response.EthFilter;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetBlockTransactionCountByHash;
import org.web3j.protocol.core.methods.response.EthGetBlockTransactionCountByNumber;
import org.web3j.protocol.core.methods.response.EthGetCode;
import org.web3j.protocol.core.methods.response.EthGetCompilers;
import org.web3j.protocol.core.methods.response.EthGetStorageAt;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthGetUncleCountByBlockHash;
import org.web3j.protocol.core.methods.response.EthGetUncleCountByBlockNumber;
import org.web3j.protocol.core.methods.response.EthHashrate;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.core.methods.response.EthMining;
import org.web3j.protocol.core.methods.response.EthProtocolVersion;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.EthSyncing;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.core.methods.response.EthUninstallFilter;
import org.web3j.protocol.core.methods.response.NetListening;
import org.web3j.protocol.core.methods.response.NetPeerCount;
import org.web3j.protocol.core.methods.response.NetVersion;
import org.web3j.protocol.core.methods.response.ShhNewGroup;
import org.web3j.protocol.core.methods.response.ShhNewIdentity;
import org.web3j.protocol.core.methods.response.ShhVersion;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.core.methods.response.Web3Sha3;
import org.web3j.protocol.http.HttpService;

import static junit.framework.TestCase.assertFalse;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeThat;
import static org.web3j.protocol.core.TestParameters.isInfuraTestRinkebyUrl;

/**
 * JSON-RPC 2.0 Integration Tests.
 */
public class CoreIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(CoreIT.class);

    private static final int METHOD_NOT_FOUND = -32601;

    private Web3j web3j;

    private IntegrationTestConfig config = new TestnetConfig();
    private Boolean syncing;

    public CoreIT() { }

    @Before
    public void setUp() {
        this.web3j = Web3j.build(new HttpService(TestParameters.TEST_RINKEBY_URL));

        try {
            EthSyncing ethSyncing = web3j.ethSyncing().send();
            syncing = ethSyncing.isSyncing();
        } catch (Exception e) {
            LOGGER.warn("Unable to get ethSyncing", e);
        }
    }

    @Test
    public void testWeb3ClientVersion() throws Exception {
        Web3ClientVersion web3ClientVersion = web3j.web3ClientVersion().send();
        String clientVersion = web3ClientVersion.getWeb3ClientVersion();
        System.out.println("Ethereum client version: " + clientVersion);
        assertFalse(clientVersion.isEmpty());
    }

    @Test
    public void testWeb3Sha3() throws Exception {
        Web3Sha3 web3Sha3 = web3j.web3Sha3("0x68656c6c6f20776f726c64").send();
        assertThat(web3Sha3.getResult(),
                is("0x47173285a8d7341e5e972fc677286384f802f8ef42a5ec5f03bbfa254cb01fad"));
    }

    @Test
    public void testNetVersion() throws Exception {
        NetVersion netVersion = web3j.netVersion().send();
        assertFalse(netVersion.getNetVersion().isEmpty());
    }

    @Test
    public void testNetListening() throws Exception {
        NetListening netListening = web3j.netListening().send();
        assertTrue(netListening.isListening());
    }

    @Test
    public void testNetPeerCount() throws Exception {
        NetPeerCount netPeerCount = web3j.netPeerCount().send();
        assertTrue(netPeerCount.getQuantity().signum() == 1);
    }

    @Test
    public void testEthProtocolVersion() throws Exception {
        EthProtocolVersion ethProtocolVersion = web3j.ethProtocolVersion().send();
        assertFalse(ethProtocolVersion.getProtocolVersion().isEmpty());
    }

    @Test
    public void testEthSyncing() throws Exception {
        EthSyncing ethSyncing = web3j.ethSyncing().send();
        assertNotNull(ethSyncing.getResult());
    }

    @Test
    public void testEthCoinbase() throws Exception {
        assumeFalse("Infura does NOT support eth_coinbase - "
                + "https://github.com/INFURA/infura/wiki/FAQ#q-does-infura-support-all-rpc-methods",
                isInfuraTestRinkebyUrl());

        EthCoinbase ethCoinbase = web3j.ethCoinbase().send();
        assertNotNull(ethCoinbase.getAddress());
    }

    @Test
    public void testEthMining() throws Exception {
        EthMining ethMining = web3j.ethMining().send();
        assertNotNull(ethMining.getResult());
    }

    @Test
    public void testEthHashrate() throws Exception {
        EthHashrate ethHashrate = web3j.ethHashrate().send();
        assertThat(ethHashrate.getHashrate(), is(BigInteger.ZERO));
    }

    @Test
    public void testEthGasPrice() throws Exception {
        EthGasPrice ethGasPrice = web3j.ethGasPrice().send();
        assertTrue(ethGasPrice.getGasPrice().signum() == 1);
    }

    @Test
    public void testEthAccounts() throws Exception {
        EthAccounts ethAccounts = web3j.ethAccounts().send();
        assertNotNull(ethAccounts.getAccounts());
    }

    @Test
    public void testEthBlockNumber() throws Exception {
        assumeThat("Skipping testEthBlockNumber() because we are still syncing, which means we "
                + "will NOT be able to accurately get the block number - See "
                + "https://github.com/ethereum/go-ethereum/issues/14338",
                syncing,
                allOf(
                        notNullValue(),
                        is(false)
                ));

        EthBlockNumber ethBlockNumber = web3j.ethBlockNumber().send();
        assertTrue(ethBlockNumber.getBlockNumber().signum() == 1);
    }

    @Test
    public void testEthGetBalance() throws Exception {
        assumeThat("Skipping testEthGetBalance() because we are still syncing, which means we "
                + "will NOT be able to accurately do ethGetFilterLogs",
                syncing,
                allOf(
                        notNullValue(),
                        is(false)
                ));

        EthGetBalance ethGetBalance = web3j.ethGetBalance(
                config.validAccount(), DefaultBlockParameter.valueOf("latest")).send();
        assertTrue(ethGetBalance.getBalance().signum() == 1);
    }

    @Test
    public void testEthGetStorageAt() throws Exception {
        assumeThat("Skipping testEthGetStorageAt() because we are still syncing, which means we "
                + "will NOT be able to accurately do ethGetStorageAt",
                syncing,
                allOf(
                        notNullValue(),
                        is(false)
                ));

        EthGetStorageAt ethGetStorageAt = web3j.ethGetStorageAt(
                config.validContractAddress(),
                BigInteger.valueOf(0),
                DefaultBlockParameter.valueOf("latest")).send();
        assertThat(ethGetStorageAt.getData(), is(config.validContractAddressPositionZero()));
    }

    @Test
    public void testEthGetTransactionCount() throws Exception {
        assumeThat("Skipping testEthGetTransactionCount() because we are still syncing, which "
                + "means we will NOT be able to accurately do ethGetTransactionCount",
                syncing,
                allOf(
                        notNullValue(),
                        is(false)
                ));

        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
                config.validAccount(),
                DefaultBlockParameter.valueOf("latest")).send();
        assertTrue(ethGetTransactionCount.getTransactionCount().signum() == 1);
    }

    @Test
    public void testEthGetBlockTransactionCountByHash() throws Exception {
        EthGetBlockTransactionCountByHash ethGetBlockTransactionCountByHash =
                web3j.ethGetBlockTransactionCountByHash(
                        config.validBlockHash()).send();
        assertThat(ethGetBlockTransactionCountByHash.getTransactionCount(),
                equalTo(config.validBlockTransactionCount()));
    }

    @Test
    public void testEthGetBlockTransactionCountByNumber() throws Exception {
        EthGetBlockTransactionCountByNumber ethGetBlockTransactionCountByNumber =
                web3j.ethGetBlockTransactionCountByNumber(
                        DefaultBlockParameter.valueOf(config.validBlock())).send();
        assertThat(ethGetBlockTransactionCountByNumber.getTransactionCount(),
                equalTo(config.validBlockTransactionCount()));
    }

    @Test
    public void testEthGetUncleCountByBlockHash() throws Exception {
        EthGetUncleCountByBlockHash ethGetUncleCountByBlockHash =
                web3j.ethGetUncleCountByBlockHash(config.validBlockHash()).send();
        assertThat(ethGetUncleCountByBlockHash.getUncleCount(),
                equalTo(config.validBlockUncleCount()));
    }

    @Test
    public void testEthGetUncleCountByBlockNumber() throws Exception {
        EthGetUncleCountByBlockNumber ethGetUncleCountByBlockNumber =
                web3j.ethGetUncleCountByBlockNumber(
                        DefaultBlockParameter.valueOf(new BigInteger("232"))).send();
        assertThat(ethGetUncleCountByBlockNumber.getUncleCount(),
                equalTo(config.validBlockUncleCount()));
    }

    @Test
    public void testEthGetCode() throws Exception {
        assumeThat("Skipping testEthGetCode() because we are still syncing, which means we will "
                + "NOT be able to accurately do ethGetCode",
                syncing,
                allOf(
                        notNullValue(),
                        is(false)
                ));

        EthGetCode ethGetCode = web3j.ethGetCode(config.validContractAddress(),
                DefaultBlockParameter.valueOf(config.validBlock())).send();
        assertThat(ethGetCode.getCode(), is(config.validContractCode()));
    }

    @Ignore  // TODO: Once account unlock functionality is available
    @Test
    public void testEthSign() throws Exception {
        // EthSign ethSign = web3j.ethSign();
        assumeFalse("Infura does NOT support eth_sign - "
                + "https://github.com/INFURA/infura/wiki/FAQ"
                + "#q-does-infura-support-all-rpc-methods",
                isInfuraTestRinkebyUrl());
    }

    @Ignore  // TODO: Once account unlock functionality is available
    @Test
    public void testEthSendTransaction() throws Exception {
        assumeFalse("Infura does NOT support eth_sendTransaction - "
                + "https://github.com/INFURA/infura/wiki/FAQ"
                + "#q-does-infura-support-all-rpc-methods",
                isInfuraTestRinkebyUrl());

        EthSendTransaction ethSendTransaction = web3j.ethSendTransaction(
                config.buildTransaction()).send();
        assertFalse(ethSendTransaction.getTransactionHash().isEmpty());
    }

    @Ignore  // TODO: Once account unlock functionality is available
    @Test
    public void testEthSendRawTransaction() throws Exception {

    }

    @Test
    public void testEthCall() throws Exception {
        EthCall ethCall = web3j.ethCall(config.buildTransaction(),
                DefaultBlockParameter.valueOf("latest")).send();

        assertThat(DefaultBlockParameterName.LATEST.getValue(), is("latest"));
        assertThat(ethCall.getValue(), is("0x"));
    }

    @Test
    public void testEthEstimateGas() throws Exception {
        EthEstimateGas ethEstimateGas = web3j.ethEstimateGas(config.buildTransaction())
                .send();
        assertTrue(ethEstimateGas.getAmountUsed().signum() == 1);
    }

    @Test
    public void testEthGetBlockByHashReturnHashObjects() throws Exception {
        EthBlock ethBlock = web3j.ethGetBlockByHash(config.validBlockHash(), false)
                .send();

        EthBlock.Block block = ethBlock.getBlock();
        assertNotNull(ethBlock.getBlock());
        assertThat(block.getNumber(), equalTo(config.validBlock()));
        assertThat(block.getTransactions().size(),
                is(config.validBlockTransactionCount().intValue()));
    }

    @Test
    public void testEthGetBlockByHashReturnFullTransactionObjects() throws Exception {
        EthBlock ethBlock = web3j.ethGetBlockByHash(config.validBlockHash(), true)
                .send();

        EthBlock.Block block = ethBlock.getBlock();
        assertNotNull(ethBlock.getBlock());
        assertThat(block.getNumber(), equalTo(config.validBlock()));
        assertThat(block.getTransactions().size(),
                equalTo(config.validBlockTransactionCount().intValue()));
    }

    @Test
    public void testEthGetBlockByNumberReturnHashObjects() throws Exception {
        EthBlock ethBlock = web3j.ethGetBlockByNumber(
                DefaultBlockParameter.valueOf(config.validBlock()), false).send();

        EthBlock.Block block = ethBlock.getBlock();
        assertNotNull(ethBlock.getBlock());
        assertThat(block.getNumber(), equalTo(config.validBlock()));
        assertThat(block.getTransactions().size(),
                equalTo(config.validBlockTransactionCount().intValue()));
    }

    @Test
    public void testEthGetBlockByNumberReturnTransactionObjects() throws Exception {
        EthBlock ethBlock = web3j.ethGetBlockByNumber(
                DefaultBlockParameter.valueOf(config.validBlock()), true).send();

        EthBlock.Block block = ethBlock.getBlock();
        assertNotNull(ethBlock.getBlock());
        assertThat(block.getNumber(), equalTo(config.validBlock()));
        assertThat(block.getTransactions().size(),
                equalTo(config.validBlockTransactionCount().intValue()));
    }

    @Test
    public void testEthGetTransactionByHash() throws Exception {
        EthTransaction ethTransaction = web3j.ethGetTransactionByHash(
                config.validTransactionHash()).send();
        assertTrue("Transaction should be present",
                ethTransaction.getTransaction().isPresent());
        Transaction transaction = ethTransaction.getTransaction().get();
        assertThat("Transaction block hash should match",
                transaction.getBlockHash(), is(config.validBlockHash()));
    }

    @Test
    public void testEthGetTransactionByBlockHashAndIndex() throws Exception {
        BigInteger index = BigInteger.ONE;

        EthTransaction ethTransaction = web3j.ethGetTransactionByBlockHashAndIndex(
                config.validBlockHash(), index).send();
        assertTrue(ethTransaction.getTransaction().isPresent());
        Transaction transaction = ethTransaction.getTransaction().get();
        assertThat(transaction.getBlockHash(), is(config.validBlockHash()));
        assertThat(transaction.getTransactionIndex(), equalTo(index));
    }

    @Test
    public void testEthGetTransactionByBlockNumberAndIndex() throws Exception {
        BigInteger index = BigInteger.ONE;

        EthTransaction ethTransaction = web3j.ethGetTransactionByBlockNumberAndIndex(
                DefaultBlockParameter.valueOf(config.validBlock()), index).send();
        assertTrue(ethTransaction.getTransaction().isPresent());
        Transaction transaction = ethTransaction.getTransaction().get();
        assertThat(transaction.getBlockHash(), is(config.validBlockHash()));
        assertThat(transaction.getTransactionIndex(), equalTo(index));
    }

    @Test
    public void testEthGetTransactionReceipt() throws Exception {
        EthGetTransactionReceipt ethGetTransactionReceipt = web3j.ethGetTransactionReceipt(
                config.validTransactionHash()).send();
        assertTrue(ethGetTransactionReceipt.getTransactionReceipt().isPresent());
        TransactionReceipt transactionReceipt =
                ethGetTransactionReceipt.getTransactionReceipt().get();
        assertThat(transactionReceipt.getTransactionHash(), is(config.validTransactionHash()));
    }

    @Test
    public void testEthGetUncleByBlockHashAndIndex() throws Exception {
        EthBlock ethBlock = web3j.ethGetUncleByBlockHashAndIndex(
                config.validUncleBlockHash(), BigInteger.ZERO).send();
        assertNotNull(ethBlock.getBlock());
    }

    @Test
    public void testEthGetUncleByBlockNumberAndIndex() throws Exception {
        EthBlock ethBlock = web3j.ethGetUncleByBlockNumberAndIndex(
                DefaultBlockParameter.valueOf(config.validUncleBlock()), BigInteger.ZERO)
                .send();
        assertNotNull(ethBlock.getBlock());
    }

    @Test
    public void testEthGetCompilers() throws Exception {
        EthGetCompilers ethGetCompilers = web3j.ethGetCompilers().send();

        String assumptionMessage = "ethGetCompilers should have errors because it is no longer "
                + "supported. see - https://github.com/ethereum/EIPs/issues/209, and "
                + "https://github.com/ethereum/wiki/issues/403";
        assumeThat(assumptionMessage, ethGetCompilers.getError(), nullValue());
        assumeThat(
                assumptionMessage, ethGetCompilers.getError().getCode(),
                not(equalTo(METHOD_NOT_FOUND)));

        assertNotNull(ethGetCompilers.getCompilers());
    }

    @Ignore  // The method eth_compileLLL does not exist/is not available
    @Test
    public void testEthCompileLLL() throws Exception {
        EthCompileLLL ethCompileLLL = web3j.ethCompileLLL(
                "(returnlll (suicide (caller)))").send();
        assertFalse(ethCompileLLL.getCompiledSourceCode().isEmpty());
    }

    @Test
    public void testEthCompileSolidity() throws Exception {
        String sourceCode = "pragma solidity ^0.4.0;"
                + "\ncontract test { function multiply(uint a) returns(uint d) {"
                + "   return a * 7;   } }"
                + "\ncontract test2 { function multiply2(uint a) returns(uint d) {"
                + "   return a * 7;   } }";
        EthCompileSolidity ethCompileSolidity = web3j.ethCompileSolidity(sourceCode)
                .send();

        String assumptionMessage = "ethCompileSolidity should have errors because it is no longer "
                + "supported. see - https://github.com/ethereum/EIPs/issues/209, and "
                + "https://github.com/ethereum/wiki/issues/403";
        assumeThat(assumptionMessage, ethCompileSolidity.getError(), nullValue());
        assumeThat(
                assumptionMessage, ethCompileSolidity.getError().getCode(),
                not(equalTo(METHOD_NOT_FOUND)));

        assertNotNull(ethCompileSolidity.getCompiledSolidity());
        assertThat(
                ethCompileSolidity.getCompiledSolidity().get("test2").getInfo().getSource(),
                is(sourceCode));
    }

    @Ignore  // The method eth_compileSerpent does not exist/is not available
    @Test
    public void testEthCompileSerpent() throws Exception {
        EthCompileSerpent ethCompileSerpent = web3j.ethCompileSerpent(
                "/* some serpent */").send();
        assertFalse(ethCompileSerpent.getCompiledSourceCode().isEmpty());
    }

    @Test
    public void testFiltersByFilterId() throws Exception {
        assumeFalse("Infura does NOT support eth_newFilter, eth_getFilterLogs, "
                + "eth_getFilterChanges, and eth_uninstallFilter - "
                + "https://github.com/INFURA/infura/blob/master/docs/source/index.html.md"
                + "#supported-json-rpc-methods",
                isInfuraTestRinkebyUrl());

        assumeThat("Skipping testFiltersByFilterId() because we are still syncing, which means we "
                + "will NOT be able to accurately do ethGetFilterLogs",
                syncing,
                allOf(
                        notNullValue(),
                        is(false)
                ));

        org.web3j.protocol.core.methods.request.EthFilter ethFilter =
                new org.web3j.protocol.core.methods.request.EthFilter(
                DefaultBlockParameterName.EARLIEST,
                DefaultBlockParameterName.LATEST,
                config.validContractAddress());

        String eventSignature = config.encodedEvent();
        ethFilter.addSingleTopic(eventSignature);

        // eth_newFilter
        EthFilter ethNewFilter = web3j.ethNewFilter(ethFilter).send();
        BigInteger filterId = ethNewFilter.getFilterId();

        // eth_getFilterLogs
        EthLog ethFilterLogs = web3j.ethGetFilterLogs(filterId).send();
        List<EthLog.LogResult> filterLogs = ethFilterLogs.getLogs();
        assertFalse(filterLogs.isEmpty());

        // eth_getFilterChanges - nothing will have changed in this interval
        EthLog ethLog = web3j.ethGetFilterChanges(filterId).send();
        assertTrue(ethLog.getLogs().isEmpty());

        // eth_uninstallFilter
        EthUninstallFilter ethUninstallFilter = web3j.ethUninstallFilter(filterId).send();
        assertTrue(ethUninstallFilter.isUninstalled());
    }

    @Test
    public void testEthNewBlockFilter() throws Exception {
        assumeFalse("Infura does NOT support eth_newBlockFilter - "
                + "https://github.com/INFURA/infura/blob/master/docs/source/index.html.md"
                + "#supported-json-rpc-methods",
                isInfuraTestRinkebyUrl());

        EthFilter ethNewBlockFilter = web3j.ethNewBlockFilter().send();
        assertNotNull(ethNewBlockFilter.getFilterId());
    }

    @Test
    public void testEthNewPendingTransactionFilter() throws Exception {
        assumeFalse("Infura does NOT support eth_newPendingTransactionFilter - "
                + "https://github.com/INFURA/infura/blob/master/docs/source/index.html.md"
                + "#supported-json-rpc-methods",
                isInfuraTestRinkebyUrl());

        EthFilter ethNewPendingTransactionFilter =
                web3j.ethNewPendingTransactionFilter().send();
        assertNotNull(ethNewPendingTransactionFilter.getFilterId());
    }

    @Test
    public void testEthGetLogs() throws Exception {
        assumeThat("Skipping testEthGetLogs() because we are still syncing, which means we will "
                + "NOT be able to accurately do ethGetLogs",
                syncing,
                allOf(
                        notNullValue(),
                        is(false)
                ));

        org.web3j.protocol.core.methods.request.EthFilter ethFilter =
                new org.web3j.protocol.core.methods.request.EthFilter(
                DefaultBlockParameterName.EARLIEST,
                DefaultBlockParameterName.LATEST,
                config.validContractAddress()
        );

        ethFilter.addSingleTopic(config.encodedEvent());

        EthLog ethLog = web3j.ethGetLogs(ethFilter).send();
        List<EthLog.LogResult> logs = ethLog.getLogs();
        assertFalse(logs.isEmpty());
    }

    // @Test
    // public void testEthGetWork() throws Exception {
    //     EthGetWork ethGetWork = requestFactory.ethGetWork();
    //     assertNotNull(ethGetWork.getResult());
    // }

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
        ShhVersion shhVersion = web3j.shhVersion().send();
        assertNotNull(shhVersion.getVersion());
    }

    @Ignore  // The method shh_newIdentity does not exist/is not available
    @Test
    public void testShhNewIdentity() throws Exception {
        ShhNewIdentity shhNewIdentity = web3j.shhNewIdentity().send();
        assertNotNull(shhNewIdentity.getAddress());
    }

    @Test
    public void testShhHasIdentity() throws Exception {
    
    }

    @Ignore  // The method shh_newIdentity does not exist/is not available
    @Test
    public void testShhNewGroup() throws Exception {
        ShhNewGroup shhNewGroup = web3j.shhNewGroup().send();
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
