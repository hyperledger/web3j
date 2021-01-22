/*
 * Copyright 2019 Web3 Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.web3j.protocol.core;

import java.math.BigInteger;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.web3j.EVMTest;
import org.web3j.NodeType;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthCoinbase;
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
import org.web3j.protocol.core.methods.response.EthGetWork;
import org.web3j.protocol.core.methods.response.EthHashrate;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.core.methods.response.EthMining;
import org.web3j.protocol.core.methods.response.EthProtocolVersion;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.EthSign;
import org.web3j.protocol.core.methods.response.EthSubmitHashrate;
import org.web3j.protocol.core.methods.response.EthSubmitWork;
import org.web3j.protocol.core.methods.response.EthSyncing;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.core.methods.response.EthUninstallFilter;
import org.web3j.protocol.core.methods.response.NetListening;
import org.web3j.protocol.core.methods.response.NetPeerCount;
import org.web3j.protocol.core.methods.response.NetVersion;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.core.methods.response.Web3Sha3;
import org.web3j.test.contract.Fibonacci;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Numeric;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** JSON-RPC 2.0 Integration Tests. */
@EVMTest(type = NodeType.BESU)
public class CoreIT {

    private static Web3j web3j;

    private static IntegrationTestConfig config;

    public CoreIT() {}

    @BeforeAll
    public static void setUp(
            Web3j web3j, TransactionManager transactionManager, ContractGasProvider gasProvider)
            throws Exception {
        CoreIT.web3j = web3j;
        CoreIT.config = new TestnetConfig(web3j, transactionManager, gasProvider);
    }

    @Disabled // Method does not exist
    @Test
    public void testWeb3ClientVersion() throws Exception {
        Web3ClientVersion web3ClientVersion = web3j.web3ClientVersion().send();
        String clientVersion = web3ClientVersion.getWeb3ClientVersion();
        assertFalse(clientVersion.isEmpty());
    }

    @Disabled // Method Does not Exist
    @Test
    public void testWeb3Sha3() throws Exception {
        Web3Sha3 web3Sha3 = web3j.web3Sha3("0x68656c6c6f20776f726c64").send();
        assertEquals(
                web3Sha3.getResult(),
                ("0x47173285a8d7341e5e972fc677286384f802f8ef42a5ec5f03bbfa254cb01fad"));
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
        assertTrue(netPeerCount.getQuantity().signum() == 0);
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
        assertEquals(1, ethHashrate.getHashrate().compareTo(BigInteger.ONE));
    }

    @Test
    public void testEthGasPrice(Web3j web3j) throws Exception {
        EthGasPrice ethGasPrice = web3j.ethGasPrice().send();
        assertTrue(ethGasPrice.getGasPrice().signum() == 1);
    }

    @Test
    public void testEthAccounts(Web3j web3j) throws Exception {
        EthAccounts ethAccounts = web3j.ethAccounts().send();
        assertNotNull(ethAccounts.getAccounts());
    }

    @Test
    public void testEthBlockNumber(Web3j web3j) throws Exception {
        EthBlockNumber ethBlockNumber = web3j.ethBlockNumber().send();
        assertEquals(1, ethBlockNumber.getBlockNumber().signum());
    }

    @Test
    public void testEthGetBalance(Web3j web3j) throws Exception {
        EthGetBalance ethGetBalance =
                web3j.ethGetBalance(config.validAccount(), DefaultBlockParameter.valueOf("latest"))
                        .send();
        assertEquals(ethGetBalance.getBalance().signum(), 1);
    }

    @Test
    public void testEthGetStorageAt(Web3j web3j) throws Exception {
        EthGetStorageAt ethGetStorageAt =
                web3j.ethGetStorageAt(
                                config.validContractAddress(),
                                BigInteger.valueOf(0),
                                DefaultBlockParameter.valueOf("latest"))
                        .send();
        assertEquals(ethGetStorageAt.getData(), (config.validContractAddressPositionZero()));
    }

    @Test
    public void testEthGetTransactionCount() throws Exception {
        EthGetTransactionCount ethGetTransactionCount =
                web3j.ethGetTransactionCount(
                                config.validAccount(), DefaultBlockParameter.valueOf("latest"))
                        .send();
        assertTrue(ethGetTransactionCount.getTransactionCount().signum() == 1);
    }

    @Test
    public void testEthGetBlockTransactionCountByHash() throws Exception {

        EthGetBlockTransactionCountByHash ethGetBlockTransactionCountByHash =
                web3j.ethGetBlockTransactionCountByHash(config.validBlockHash()).send();
        assertEquals(BigInteger.ONE, ethGetBlockTransactionCountByHash.getTransactionCount());
    }

    @Test
    public void testEthGetBlockTransactionCountByNumber(Web3j web3j) throws Exception {
        EthGetBlockTransactionCountByNumber ethGetBlockTransactionCountByNumber =
                web3j.ethGetBlockTransactionCountByNumber(
                                DefaultBlockParameter.valueOf(BigInteger.ZERO))
                        .send();
        assertEquals(BigInteger.ZERO, ethGetBlockTransactionCountByNumber.getTransactionCount());
    }

    @Test
    public void testEthGetUncleCountByBlockHash(Web3j web3j) throws Exception {
        EthGetUncleCountByBlockHash ethGetUncleCountByBlockHash =
                web3j.ethGetUncleCountByBlockHash(config.validBlockHash()).send();
        assertEquals(ethGetUncleCountByBlockHash.getUncleCount(), (config.validBlockUncleCount()));
    }

    @Test
    public void testEthGetUncleCountByBlockNumber(Web3j web3j) throws Exception {
        EthGetUncleCountByBlockNumber ethGetUncleCountByBlockNumber =
                web3j.ethGetUncleCountByBlockNumber(DefaultBlockParameter.valueOf("latest")).send();
        assertEquals(
                ethGetUncleCountByBlockNumber.getUncleCount(), (config.validBlockUncleCount()));
    }

    @Test
    public void testEthGetCode() throws Exception {
        EthGetCode ethGetCode =
                web3j.ethGetCode(config.validContractAddress(), DefaultBlockParameterName.LATEST)
                        .send();
        assertEquals(
                "0x60806040526004361061004b5763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416633c7fdc70811461005057806361047ff41461007a575b600080fd5b34801561005c57600080fd5b50610068600435610092565b60408051918252519081900360200190f35b34801561008657600080fd5b506100686004356100e0565b600061009d826100e0565b604080518481526020810183905281519293507f71e71a8458267085d5ab16980fd5f114d2d37f232479c245d523ce8d23ca40ed929081900390910190a1919050565b60008115156100f15750600061011e565b81600114156101025750600161011e565b61010e600283036100e0565b61011a600184036100e0565b0190505b9190505600a165627a7a723058201b9d0941154b95636fb5e4225fefd5c2c460060efa5f5e40c9826dce08814af80029",
                ethGetCode.getCode());
    }

    @Test
    public void testEthSign() throws Exception {
        EthSign ethSign = web3j.ethSign(config.validAccount(), "Apples").send();
        ethSign.getSignature();
        assertNotNull(ethSign);
    }

    @Test
    @Disabled("Enable in the next release when geth is fixed")
    public void testEthSendTransaction(Web3j web3j, ContractGasProvider gasProvider)
            throws Exception {
        EthSendTransaction ethSendTransaction =
                web3j.ethSendTransaction(config.buildTransaction(web3j, gasProvider)).send();
        assertFalse(ethSendTransaction.getTransactionHash().isEmpty());
    }

    @Test
    public void testEthSendRawTransaction() throws Exception {
        EthGetTransactionCount transactionCount =
                web3j.ethGetTransactionCount(
                                config.validAccount(), DefaultBlockParameterName.LATEST)
                        .send();
        DefaultGasProvider gasProvider = new DefaultGasProvider();
        RawTransaction rawTransaction =
                RawTransaction.createEtherTransaction(
                        transactionCount.getTransactionCount(),
                        gasProvider.getGasPrice(),
                        gasProvider.getGasLimit(),
                        "0x627306090abaB3A6e1400e9345bC60c78a8BEf57",
                        BigInteger.ONE);
        byte[] signedMessage =
                TransactionEncoder.signMessage(
                        rawTransaction, Credentials.create(config.validPrivateKey()));
        String hexValue = Numeric.toHexString(signedMessage);
        EthSendTransaction ethSendTransaction =
                web3j.ethSendRawTransaction(hexValue).sendAsync().get();
        String transactionHash = ethSendTransaction.getTransactionHash();
        assertNotNull(transactionHash);
    }

    @Test
    @Disabled("This should be enabled in the next release when geth is fixed")
    public void testEthCall(Web3j web3j, ContractGasProvider gasProvider) throws Exception {

        EthCall ethCall =
                web3j.ethCall(
                                config.buildTransaction(web3j, gasProvider),
                                DefaultBlockParameterName.LATEST)
                        .send();

        assertEquals(
                "0x60806040526004361061004b5763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416633c7fdc70811461005057806361047ff41461007a575b600080fd5b34801561005c57600080fd5b50610068600435610092565b60408051918252519081900360200190f35b34801561008657600080fd5b506100686004356100e0565b600061009d826100e0565b604080518481526020810183905281519293507f71e71a8458267085d5ab16980fd5f114d2d37f232479c245d523ce8d23ca40ed929081900390910190a1919050565b60008115156100f15750600061011e565b81600114156101025750600161011e565b61010e600283036100e0565b61011a600184036100e0565b0190505b9190505600a165627a7a723058201b9d0941154b95636fb5e4225fefd5c2c460060efa5f5e40c9826dce08814af80029",
                ethCall.getValue());
    }

    @Test
    @Disabled("Enable in the next release when geth is fixed")
    public void testEthEstimateGas(Web3j web3j, ContractGasProvider gasProvider) throws Exception {
        org.web3j.protocol.core.methods.request.Transaction transaction =
                org.web3j.protocol.core.methods.request.Transaction.createContractTransaction(
                        config.validAccount(),
                        BigInteger.ZERO, // nonce
                        gasProvider.getGasPrice(),
                        config.validContractCode());
        EthEstimateGas ethEstimateGas = web3j.ethEstimateGas(transaction).send();
        assertEquals(ethEstimateGas.getAmountUsed().signum(), 1);
    }

    @Test
    public void testEthGetBlockByHashReturnHashObjects(Web3j web3j) throws Exception {
        EthBlock ethBlock = web3j.ethGetBlockByHash(config.validBlockHash(), false).send();

        EthBlock.Block block = ethBlock.getBlock();
        assertNotNull(ethBlock.getBlock());
        assertEquals(config.validBlockNumber(), block.getNumber());
        assertEquals(1, block.getTransactions().size());
    }

    @Test
    public void testEthGetBlockByHashReturnFullTransactionObjects(Web3j web3j) throws Exception {
        EthBlock ethBlock = web3j.ethGetBlockByHash(config.validBlockHash(), true).send();

        EthBlock.Block block = ethBlock.getBlock();
        assertNotNull(ethBlock.getBlock());
        assertEquals(config.validBlockNumber(), block.getNumber());
        assertEquals(1, block.getTransactions().size());
    }

    @Test
    public void testEthGetBlockByNumberReturnHashObjects(Web3j web3j) throws Exception {
        EthBlock ethBlock =
                web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(BigInteger.ZERO), false)
                        .send();

        EthBlock.Block block = ethBlock.getBlock();
        assertNotNull(ethBlock.getBlock());
        assertEquals(BigInteger.ZERO, block.getNumber());
        assertEquals(0, block.getTransactions().size());
    }

    @Test
    public void testEthGetBlockByNumberReturnTransactionObjects(Web3j web3j) throws Exception {
        EthBlock ethBlock =
                web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(BigInteger.ZERO), true)
                        .send();

        EthBlock.Block block = ethBlock.getBlock();
        assertNotNull(ethBlock.getBlock());
        assertEquals(BigInteger.ZERO, block.getNumber());
        assertEquals(0, block.getTransactions().size());
    }

    @Test
    public void testEthGetTransactionByHash() throws Exception {
        String transactionHash = config.transferEth(web3j).getTransactionHash();
        EthTransaction ethTransaction = web3j.ethGetTransactionByHash(transactionHash).send();
        assertTrue(ethTransaction.getTransaction().isPresent());
    }

    @Test
    public void testEthGetTransactionByBlockHashAndIndex() throws Exception {

        TransactionReceipt txReceipt = config.transferEth(web3j);
        String blockHash = txReceipt.getBlockHash();
        BigInteger txIndex = txReceipt.getTransactionIndex();

        EthTransaction ethTransaction =
                web3j.ethGetTransactionByBlockHashAndIndex(blockHash, txIndex).send();
        assertTrue(ethTransaction.getTransaction().isPresent());
        Transaction transaction = ethTransaction.getTransaction().get();
        assertEquals(transaction.getBlockHash(), blockHash);
        assertEquals(transaction.getTransactionIndex(), txIndex);
    }

    @Test
    public void testEthGetTransactionByBlockNumberAndIndex() throws Exception {

        EthTransaction ethTransaction =
                web3j.ethGetTransactionByBlockNumberAndIndex(
                                DefaultBlockParameter.valueOf(config.validBlockNumber()),
                                config.validTransactionIndex())
                        .send();
        assertTrue(ethTransaction.getTransaction().isPresent());
        Transaction transaction = ethTransaction.getTransaction().get();
        assertEquals(transaction.getBlockHash(), config.validBlockHash());
        assertEquals(transaction.getTransactionIndex(), BigInteger.valueOf(0));
    }

    @Test
    public void testEthGetTransactionReceipt() throws Exception {

        String txHash = config.transferEth(web3j).getTransactionHash();
        EthGetTransactionReceipt ethGetTransactionReceipt =
                web3j.ethGetTransactionReceipt(txHash).send();
        assertTrue(ethGetTransactionReceipt.getTransactionReceipt().isPresent());
    }

    @Test
    @Disabled
    public void testEthGetUncleByBlockHashAndIndex() throws Exception {
        EthBlock ethBlock =
                web3j.ethGetUncleByBlockHashAndIndex(config.validUncleBlockHash(), BigInteger.ZERO)
                        .send();
        assertNotNull(ethBlock.getBlock());
    }

    @Test
    @Disabled
    public void testEthGetUncleByBlockNumberAndIndex(Web3j web3j) throws Exception {
        EthBlock ethBlock =
                web3j.ethGetUncleByBlockNumberAndIndex(
                                DefaultBlockParameter.valueOf(config.validUncleBlock()),
                                BigInteger.ZERO)
                        .send();
        assertNotNull(ethBlock.getBlock());
    }

    @Test
    @Disabled
    public void testEthCompileSolidity() throws Exception {
        String sourceCode =
                "pragma solidity ^0.4.0;"
                        + "\ncontract test { function multiply(uint a) returns(uint d) {"
                        + "   return a * 7;   } }"
                        + "\ncontract test2 { function multiply2(uint a) returns(uint d) {"
                        + "   return a * 7;   } }";
        EthCompileSolidity ethCompileSolidity = web3j.ethCompileSolidity(sourceCode).send();
        // This methods does not exist
        //  assertNotNull(ethCompileSolidity.getCompiledSolidity());
        assertEquals(
                ethCompileSolidity.getCompiledSolidity().get("test2").getInfo().getSource(),
                (sourceCode));
    }

    @Test
    public void testFiltersByFilterId() throws Exception {
        org.web3j.protocol.core.methods.request.EthFilter ethFilter =
                new org.web3j.protocol.core.methods.request.EthFilter(
                        DefaultBlockParameterName.EARLIEST,
                        DefaultBlockParameterName.LATEST,
                        config.validContractAddress());

        String eventSignature = config.encodedEvent();
        ethFilter.addSingleTopic(eventSignature);
        Fibonacci fib = config.getValidDeployedContract();
        fib.fibonacciNotify(BigInteger.valueOf(2)).send();

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
        EthFilter ethNewBlockFilter = web3j.ethNewBlockFilter().send();
        assertNotNull(ethNewBlockFilter.getFilterId());
    }

    @Test
    public void testEthNewPendingTransactionFilter() throws Exception {
        EthFilter ethNewPendingTransactionFilter = web3j.ethNewPendingTransactionFilter().send();
        assertNotNull(ethNewPendingTransactionFilter.getFilterId());
    }

    @Test
    public void testEthGetLogs() throws Exception {
        Fibonacci fib = config.getValidDeployedContract();
        fib.fibonacciNotify(BigInteger.valueOf(2)).send();

        fib.fibonacci(BigInteger.ONE).send();
        org.web3j.protocol.core.methods.request.EthFilter ethFilter =
                new org.web3j.protocol.core.methods.request.EthFilter(
                        DefaultBlockParameterName.EARLIEST,
                        DefaultBlockParameterName.LATEST,
                        config.validContractAddress());

        ethFilter.addSingleTopic(config.encodedEvent());

        EthLog ethLog = web3j.ethGetLogs(ethFilter).send();
        List<EthLog.LogResult> logs = ethLog.getLogs();
        assertFalse(logs.isEmpty());
    }

    @Disabled // Not available
    @Test
    public void testEthGetWork() throws Exception {
        EthGetWork ethGetWork = web3j.ethGetWork().send();
        assertNotNull(ethGetWork.getResult());
    }

    @Disabled // Not available
    @Test
    public void testEthGetCompilers(Web3j web3j) throws Exception {
        EthGetCompilers ethGetCompilers = web3j.ethGetCompilers().send();
        assertNotNull(ethGetCompilers.getCompilers());
    }

    @Test
    public void testEthSubmitWork() throws Exception {
        EthSubmitWork ethSubmitWork =
                web3j.ethSubmitWork(
                                "0x0000000000000001",
                                "0x1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef",
                                "0xD1FE5700000000000000000000000000D1FE5700000000000000000000000000")
                        .send();
        assertNotNull(ethSubmitWork);
    }

    @Test
    public void testEthSubmitHashrate() throws Exception {
        EthSubmitHashrate ethHashRate =
                web3j.ethSubmitHashrate(
                                "0x0000000000000000000000000000000000000000000000000000000000500000",
                                "0x59daa26581d0acd1fce254fb7e85952f4c09d0915afd33d3886cd914bc7d283c")
                        .send();
        assertNotNull(ethHashRate);
    }
}
