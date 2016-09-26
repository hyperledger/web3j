package org.web3j.protocol.core;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;

import org.web3j.protocol.ResponseTester;
import org.web3j.protocol.core.methods.response.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Core Protocol Response tests.
 */
public class ResponseTest extends ResponseTester {

    @Test
    public void testErrorResponse() {
        buildResponse(
                "{" +
                        "  \"jsonrpc\":\"2.0\"," +
                        "  \"id\":1," +
                        "  \"error\":{" +
                        "    \"code\":-32602," +
                        "    \"message\":\"Invalid address length, expected 40 got 64 bytes\"," +
                        "    \"data\":null" +
                        "  }" +
                        "}"
        );

        EthBlock ethBlock = deserialiseResponse(EthBlock.class);
        assertTrue(ethBlock.hasError());
        assertThat(ethBlock.getError(), equalTo(
                new Response.Error(-32602, "Invalid address length, expected 40 got 64 bytes")));
    }

    @Test
    public void testWeb3ClientVersion() {
        buildResponse(
                "{\n" +
                        "  \"id\":67,\n" +
                        "  \"jsonrpc\":\"2.0\",\n" +
                        "  \"result\": \"Mist/v0.9.3/darwin/go1.4.1\"\n" +
                        "}"
        );

        Web3ClientVersion web3ClientVersion = deserialiseResponse(Web3ClientVersion.class);
        assertThat(web3ClientVersion.getWeb3ClientVersion(), is("Mist/v0.9.3/darwin/go1.4.1"));
    }

    @Test
    public void testWeb3Sha3() throws IOException {
        buildResponse(
                "{\n" +
                        "  \"id\":64,\n" +
                        "  \"jsonrpc\": \"2.0\",\n" +
                        "  \"result\": \"0x47173285a8d7341e5e972fc677286384f802f8ef42a5ec5f03bbfa254cb01fad\"\n" +
                        "}"
        );

        Web3Sha3 web3Sha3 = deserialiseResponse(Web3Sha3.class);
        assertThat(web3Sha3.getResult(),
                is("0x47173285a8d7341e5e972fc677286384f802f8ef42a5ec5f03bbfa254cb01fad"));
    }

    @Test
    public void testNetVersion() throws IOException {
        buildResponse(
                "{\n" +
                        "  \"id\":67,\n" +
                        "  \"jsonrpc\": \"2.0\",\n" +
                        "  \"result\": \"59\"\n" +
                        "}"
        );

        NetVersion netVersion = deserialiseResponse(NetVersion.class);
        assertThat(netVersion.getNetVersion(), is("59"));
    }

    @Test
    public void testNetListening() throws IOException {
        buildResponse(
                "{\n" +
                        "  \"id\":67,\n" +
                        "  \"jsonrpc\":\"2.0\",\n" +
                        "  \"result\":true\n" +
                        "}"
        );

        NetListening netListening = deserialiseResponse(NetListening.class);
        assertThat(netListening.isListening(), is(true));
    }

    @Test
    public void testNetPeerCount() throws IOException {
        buildResponse(
                "{\n" +
                        "  \"id\":74,\n" +
                        "  \"jsonrpc\": \"2.0\",\n" +
                        "  \"result\": \"0x2\"\n" +
                        "}"
        );

        NetPeerCount netPeerCount = deserialiseResponse(NetPeerCount.class);
        assertThat(netPeerCount.getQuantity(), equalTo(BigInteger.valueOf(2L)));
    }

    @Test
    public void testEthProtocolVersion() throws IOException {
        buildResponse(
                "{\n" +
                        "  \"id\":67,\n" +
                        "  \"jsonrpc\": \"2.0\",\n" +
                        "  \"result\": \"54\"\n" +
                        "}"
        );

        EthProtocolVersion ethProtocolVersion = deserialiseResponse(EthProtocolVersion.class);
        assertThat(ethProtocolVersion.getProtocolVersion(), is("54"));
    }

    @Test
    public void testEthSyncingInProgress() {
        buildResponse(
                "{\n" +
                        "  \"id\":1,\n" +
                        "  \"jsonrpc\": \"2.0\",\n" +
                        "  \"result\": {\n" +
                        "  \"startingBlock\": \"0x384\",\n" +
                        "  \"currentBlock\": \"0x386\",\n" +
                        "  \"highestBlock\": \"0x454\"\n" +
                        "  }\n" +
                        "}"
        );

        // Response received from Geth node
        // "{\"jsonrpc\":\"2.0\",\"id\":1,\"result\":{\"currentBlock\":\"0x117a\",\"highestBlock\":\"0x21dab4\",\"knownStates\":\"0x0\",\"pulledStates\":\"0x0\",\"startingBlock\":\"0xa51\"}}"

        EthSyncing ethSyncing = deserialiseResponse(EthSyncing.class);

        assertThat(ethSyncing.getResult(), equalTo(new EthSyncing.Syncing("0x384", "0x386", "0x454", null, null)));
    }

    @Test
    public void testEthSyncing() {
        buildResponse(
                "{\n" +
                        "  \"id\":1,\n" +
                        "  \"jsonrpc\": \"2.0\",\n" +
                        "  \"result\": false\n" +
                        "}"
        );

        EthSyncing ethSyncing = deserialiseResponse(EthSyncing.class);
        assertThat(ethSyncing.isSyncing(), is(false));
    }

    @Test
    public void testEthMining() {
        buildResponse(
                "{\n" +
                        "  \"id\":71,\n" +
                        "  \"jsonrpc\": \"2.0\",\n" +
                        "  \"result\": true\n" +
                        "}"
        );

        EthMining ethMining = deserialiseResponse(EthMining.class);
        assertThat(ethMining.isMining(), is(true));
    }

    @Test
    public void testEthHashrate() {
        buildResponse(
                "{\n" +
                        "  \"id\":71,\n" +
                        "  \"jsonrpc\": \"2.0\",\n" +
                        "  \"result\": \"0x38a\"\n" +
                        "}"
        );

        EthHashrate ethHashrate = deserialiseResponse(EthHashrate.class);
        assertThat(ethHashrate.getHashrate(), equalTo(BigInteger.valueOf(906L)));
    }

    @Test
    public void testEthGasPrice() {
        buildResponse(
                "{\n" +
                        "  \"id\":73,\n" +
                        "  \"jsonrpc\": \"2.0\",\n" +
                        "  \"result\": \"0x9184e72a000\"\n" +
                        "}"
        );

        EthGasPrice ethGasPrice = deserialiseResponse(EthGasPrice.class);
        assertThat(ethGasPrice.getGasPrice(), equalTo(BigInteger.valueOf(10000000000000L)));
    }

    @Test
    public void testEthAccounts() {
        buildResponse(
                "{\n" +
                        "  \"id\":1,\n" +
                        "  \"jsonrpc\": \"2.0\",\n" +
                        "  \"result\": [\"0x407d73d8a49eeb85d32cf465507dd71d507100c1\"]\n" +
                        "}"
        );

        EthAccounts ethAccounts = deserialiseResponse(EthAccounts.class);
        assertThat(ethAccounts.getAccounts(), equalTo(Arrays.asList("0x407d73d8a49eeb85d32cf465507dd71d507100c1")));
    }

    @Test
    public void testEthBlockNumber() {
        buildResponse(
                "{\n" +
                        "  \"id\":83,\n" +
                        "  \"jsonrpc\": \"2.0\",\n" +
                        "  \"result\": \"0x4b7\"\n" +
                        "}"
        );

        EthBlockNumber ethBlockNumber = deserialiseResponse(EthBlockNumber.class);
        assertThat(ethBlockNumber.getBlockNumber(), equalTo(BigInteger.valueOf(1207L)));
    }

    @Test
    public void testEthGetBalance() {
        buildResponse(
                "{\n" +
                        "  \"id\":1,\n" +
                        "  \"jsonrpc\": \"2.0\",\n" +
                        "  \"result\": \"0x234c8a3397aab58\"\n" +
                        "}"
        );

        EthGetBalance ethGetBalance = deserialiseResponse(EthGetBalance.class);
        assertThat(ethGetBalance.getBalance(), equalTo(BigInteger.valueOf(158972490234375000L)));
    }

    @Test
    public void testEthStorageAt() {
        buildResponse(
                "{" +
                        "    \"jsonrpc\":\"2.0\"," +
                        "    \"id\":1," +
                        "    \"result\":\"0x000000000000000000000000000000000000000000000000000000000000162e\"" +
                        "}"
        );

        EthGetStorageAt ethGetStorageAt = deserialiseResponse(EthGetStorageAt.class);
        assertThat(ethGetStorageAt.getResult(), is("0x000000000000000000000000000000000000000000000000000000000000162e"));
    }

    @Test
    public void testEthGetTransactionCount() {
        buildResponse(
                "{\n" +
                        "  \"id\":1,\n" +
                        "  \"jsonrpc\": \"2.0\",\n" +
                        "  \"result\": \"0x1\"\n" +
                        "}"
        );

        EthGetTransactionCount ethGetTransactionCount = deserialiseResponse((EthGetTransactionCount.class));
        assertThat(ethGetTransactionCount.getTransactionCount(), equalTo(BigInteger.valueOf(1L)));
    }

    @Test
    public void testEthGetBlockTransactionCountByHash() {
        buildResponse(
                "{\n" +
                        "  \"id\":1,\n" +
                        "  \"jsonrpc\": \"2.0\",\n" +
                        "  \"result\": \"0xb\"\n" +
                        "}"
        );

        EthGetBlockTransactionCountByHash ethGetBlockTransactionCountByHash = deserialiseResponse(
                EthGetBlockTransactionCountByHash.class);
        assertThat(ethGetBlockTransactionCountByHash.getTransactionCount(),
                equalTo(BigInteger.valueOf(11)));
    }

    @Test
    public void testEthGetBlockTransactionCountByNumber() {
        buildResponse(
                "{\n" +
                        "  \"id\":1,\n" +
                        "  \"jsonrpc\": \"2.0\",\n" +
                        "  \"result\": \"0xa\"\n" +
                        "}"
        );

        EthGetBlockTransactionCountByNumber ethGetBlockTransactionCountByNumber = deserialiseResponse(
                EthGetBlockTransactionCountByNumber.class);
        assertThat(ethGetBlockTransactionCountByNumber.getTransactionCount(),
                equalTo(BigInteger.valueOf(10)));
    }

    @Test
    public void testEthGetUncleCountByBlockHash() {
        buildResponse(
                "{\n" +
                        "  \"id\":1,\n" +
                        "  \"jsonrpc\": \"2.0\",\n" +
                        "  \"result\": \"0x1\"\n" +
                        "}"
        );

        EthGetUncleCountByBlockHash ethGetUncleCountByBlockHash = deserialiseResponse(
                EthGetUncleCountByBlockHash.class);
        assertThat(ethGetUncleCountByBlockHash.getUncleCount(),
                equalTo(BigInteger.valueOf(1)));
    }

    @Test
    public void testEthGetUncleCountByBlockNumber() {
        buildResponse(
                "{\n" +
                        "  \"id\":1,\n" +
                        "  \"jsonrpc\": \"2.0\",\n" +
                        "  \"result\": \"0x1\"\n" +
                        "}"
        );

        EthGetUncleCountByBlockNumber ethGetUncleCountByBlockNumber = deserialiseResponse(
                EthGetUncleCountByBlockNumber.class);
        assertThat(ethGetUncleCountByBlockNumber.getUncleCount(),
                equalTo(BigInteger.valueOf(1)));
    }

    @Test
    public void testGetCode() {
        buildResponse(
                "{\n" +
                        "  \"id\":1,\n" +
                        "  \"jsonrpc\": \"2.0\",\n" +
                        "  \"result\": \"0x600160008035811a818181146012578301005b601b6001356025565b8060005260206000f25b600060078202905091905056\"\n" +
                        "}"
        );

        EthGetCode ethGetCode = deserialiseResponse(EthGetCode.class);
        assertThat(ethGetCode.getCode(), is("0x600160008035811a818181146012578301005b601b6001356025565b8060005260206000f25b600060078202905091905056"));
    }

    @Test
    public void testEthSign() {
        buildResponse(
                "{\n" +
                        "  \"id\":1,\n" +
                        "  \"jsonrpc\": \"2.0\",\n" +
                        "  \"result\": \"0xbd685c98ec39490f50d15c67ba2a8e9b5b1d6d7601fca80b295e7d717446bd8b7127ea4871e996cdc8cae7690408b4e800f60ddac49d2ad34180e68f1da0aaf001\"\n" +
                        "}"
        );

        EthSign ethSign = deserialiseResponse(EthSign.class);
        assertThat(ethSign.getSignature(), is("0xbd685c98ec39490f50d15c67ba2a8e9b5b1d6d7601fca80b295e7d717446bd8b7127ea4871e996cdc8cae7690408b4e800f60ddac49d2ad34180e68f1da0aaf001"));
    }

    @Test
    public void testEthSendTransaction() {
        buildResponse(
                "{\n" +
                        "  \"id\":1,\n" +
                        "  \"jsonrpc\": \"2.0\",\n" +
                        "  \"result\": \"0xe670ec64341771606e55d6b4ca35a1a6b75ee3d5145a99d05921026d1527331\"\n" +
                        "}"
        );

        EthSendTransaction ethSendTransaction = deserialiseResponse(EthSendTransaction.class);
        assertThat(ethSendTransaction.getTransactionHash(), is("0xe670ec64341771606e55d6b4ca35a1a6b75ee3d5145a99d05921026d1527331"));
    }

    @Test
    public void testEthSendRawTransaction() {
        buildResponse(
                "{\n" +
                        "  \"id\":1,\n" +
                        "  \"jsonrpc\": \"2.0\",\n" +
                        "  \"result\": \"0xe670ec64341771606e55d6b4ca35a1a6b75ee3d5145a99d05921026d1527331\"\n" +
                        "}"
        );

        EthSendRawTransaction ethSendRawTransaction = deserialiseResponse(EthSendRawTransaction.class);
        assertThat(ethSendRawTransaction.getTransactionHash(), is("0xe670ec64341771606e55d6b4ca35a1a6b75ee3d5145a99d05921026d1527331"));
    }

    @Test
    public void testEthCall() {
        buildResponse(
                "{\n" +
                        "  \"id\":1,\n" +
                        "  \"jsonrpc\": \"2.0\",\n" +
                        "  \"result\": \"0x\"\n" +
                        "}"
        );

        EthCall ethCall = deserialiseResponse(EthCall.class);
        assertThat(ethCall.getValue(), is("0x"));
    }

    @Test
    public void testEthEstimateGas() {
        buildResponse(
                "{\n" +
                        "  \"id\":1,\n" +
                        "  \"jsonrpc\": \"2.0\",\n" +
                        "  \"result\": \"0x5208\"\n" +
                        "}"
        );

        EthEstimateGas ethEstimateGas = deserialiseResponse(EthEstimateGas.class);
        assertThat(ethEstimateGas.getAmountUsed(), equalTo(BigInteger.valueOf(21000)));
    }

    @Test
    public void testEthBlockTransactionHashes() {
        buildResponse(
                "{\n" +
                        "\"id\":1,\n" +
                        "\"jsonrpc\":\"2.0\",\n" +
                        "\"result\": {\n" +
                        "    \"number\": \"0x1b4\",\n" +
                        "    \"hash\": \"0xe670ec64341771606e55d6b4ca35a1a6b75ee3d5145a99d05921026d1527331\",\n" +
                        "    \"parentHash\": \"0x9646252be9520f6e71339a8df9c55e4d7619deeb018d2a3f2d21fc165dde5eb5\",\n" +
                        "    \"nonce\": \"0xe04d296d2460cfb8472af2c5fd05b5a214109c25688d3704aed5484f9a7792f2\",\n" +
                        "    \"sha3Uncles\": \"0x1dcc4de8dec75d7aab85b567b6ccd41ad312451b948a7413f0a142fd40d49347\",\n" +
                        "    \"logsBloom\": \"0xe670ec64341771606e55d6b4ca35a1a6b75ee3d5145a99d05921026d1527331\",\n" +
                        "    \"transactionsRoot\": \"0x56e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421\",\n" +
                        "    \"stateRoot\": \"0xd5855eb08b3387c0af375e9cdb6acfc05eb8f519e419b874b6ff2ffda7ed1dff\",\n" +
                        "    \"receiptRoot\": \"0x56e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421\",\n" +
                        "    \"miner\": \"0x4e65fda2159562a496f9f3522f89122a3088497a\",\n" +
                        "    \"difficulty\": \"0x027f07\",\n" +
                        "    \"totalDifficulty\":  \"0x027f07\",\n" +
                        "    \"extraData\": \"0x0000000000000000000000000000000000000000000000000000000000000000\",\n" +
                        "    \"size\":  \"0x027f07\",\n" +
                        "    \"gasLimit\": \"0x9f759\",\n" +
                        "    \"gasUsed\": \"0x9f759\",\n" +
                        "    \"timestamp\": \"0x54e34e8e\",\n" +
                        "    \"transactions\": [" +
                        "        \"0xe670ec64341771606e55d6b4ca35a1a6b75ee3d5145a99d05921026d1527331\",\n" +
                        "        \"0xd5855eb08b3387c0af375e9cdb6acfc05eb8f519e419b874b6ff2ffda7ed1df\"\n" +
                        "    ], \n" +
                        "    \"uncles\": [\n" +
                        "       \"0x1dcc4de8dec75d7aab85b567b6ccd41ad312451b948a7413f0a142fd40d49347\",\n" +
                        "       \"0xd5855eb08b3387c0af375e9cdb6acfc05eb8f519e419b874b6ff2ffda7ed1dff\"\n" +
                        "    ]\n" +
                        "  }\n" +
                        "}"
        );

        EthBlock ethBlock = deserialiseResponse(EthBlock.class);
        EthBlock.Block block = new EthBlock.Block(
                "0x1b4",
                "0xe670ec64341771606e55d6b4ca35a1a6b75ee3d5145a99d05921026d1527331",
                "0x9646252be9520f6e71339a8df9c55e4d7619deeb018d2a3f2d21fc165dde5eb5",
                "0xe04d296d2460cfb8472af2c5fd05b5a214109c25688d3704aed5484f9a7792f2",
                "0x1dcc4de8dec75d7aab85b567b6ccd41ad312451b948a7413f0a142fd40d49347",
                "0xe670ec64341771606e55d6b4ca35a1a6b75ee3d5145a99d05921026d1527331",
                "0x56e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421",
                "0xd5855eb08b3387c0af375e9cdb6acfc05eb8f519e419b874b6ff2ffda7ed1dff",
                "0x56e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421",
                "0x4e65fda2159562a496f9f3522f89122a3088497a",
                "0x027f07",
                "0x027f07",
                "0x0000000000000000000000000000000000000000000000000000000000000000",
                "0x027f07",
                "0x9f759",
                "0x9f759",
                "0x54e34e8e",
                Arrays.asList(
                        new EthBlock.TransactionHash("0xe670ec64341771606e55d6b4ca35a1a6b75ee3d5145a99d05921026d1527331"),
                        new EthBlock.TransactionHash("0xd5855eb08b3387c0af375e9cdb6acfc05eb8f519e419b874b6ff2ffda7ed1df")
                ),
                Arrays.asList(
                        "0x1dcc4de8dec75d7aab85b567b6ccd41ad312451b948a7413f0a142fd40d49347",
                        "0xd5855eb08b3387c0af375e9cdb6acfc05eb8f519e419b874b6ff2ffda7ed1dff"
                )
        );
        assertThat(ethBlock.getBlock().get(),
                equalTo(block));
    }

    @Test
    public void testEthBlockFullTransactions() {
        buildResponse(
                "{\n" +
                        "\"id\":1,\n" +
                        "\"jsonrpc\":\"2.0\",\n" +
                        "\"result\": {\n" +
                        "    \"number\": \"0x1b4\",\n" +
                        "    \"hash\": \"0xe670ec64341771606e55d6b4ca35a1a6b75ee3d5145a99d05921026d1527331\",\n" +
                        "    \"parentHash\": \"0x9646252be9520f6e71339a8df9c55e4d7619deeb018d2a3f2d21fc165dde5eb5\",\n" +
                        "    \"nonce\": \"0xe04d296d2460cfb8472af2c5fd05b5a214109c25688d3704aed5484f9a7792f2\",\n" +
                        "    \"sha3Uncles\": \"0x1dcc4de8dec75d7aab85b567b6ccd41ad312451b948a7413f0a142fd40d49347\",\n" +
                        "    \"logsBloom\": \"0xe670ec64341771606e55d6b4ca35a1a6b75ee3d5145a99d05921026d1527331\",\n" +
                        "    \"transactionsRoot\": \"0x56e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421\",\n" +
                        "    \"stateRoot\": \"0xd5855eb08b3387c0af375e9cdb6acfc05eb8f519e419b874b6ff2ffda7ed1dff\",\n" +
                        "    \"receiptRoot\": \"0x56e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421\",\n" +
                        "    \"miner\": \"0x4e65fda2159562a496f9f3522f89122a3088497a\",\n" +
                        "    \"difficulty\": \"0x027f07\",\n" +
                        "    \"totalDifficulty\":  \"0x027f07\",\n" +
                        "    \"extraData\": \"0x0000000000000000000000000000000000000000000000000000000000000000\",\n" +
                        "    \"size\":  \"0x027f07\",\n" +
                        "    \"gasLimit\": \"0x9f759\",\n" +
                        "    \"gasUsed\": \"0x9f759\",\n" +
                        "    \"timestamp\": \"0x54e34e8e\",\n" +
                        "    \"transactions\": [{" +
                        "        \"hash\":\"0xc6ef2fc5426d6ad6fd9e2a26abeab0aa2411b7ab17f30a99d3cb96aed1d1055b\",\n" +
                        "        \"nonce\":\"0x\",\n" +
                        "        \"blockHash\": \"0xbeab0aa2411b7ab17f30a99d3cb9c6ef2fc5426d6ad6fd9e2a26a6aed1d1055b\",\n" +
                        "        \"blockNumber\": \"0x15df\",\n" +
                        "        \"transactionIndex\":  \"0x1\",\n" +
                        "        \"from\":\"0x407d73d8a49eeb85d32cf465507dd71d507100c1\",\n" +
                        "        \"to\":\"0x85h43d8a49eeb85d32cf465507dd71d507100c1\",\n" +
                        "        \"value\":\"0x7f110\",\n" +
                        "        \"gas\": \"0x7f110\",\n" +
                        "        \"gasPrice\":\"0x09184e72a000\",\n" +
                        "        \"input\":\"0x603880600c6000396000f300603880600c6000396000f3603880600c6000396000f360\"" +
                        "    }], \n" +
                        "    \"uncles\": [\n" +
                        "       \"0x1dcc4de8dec75d7aab85b567b6ccd41ad312451b948a7413f0a142fd40d49347\",\n" +
                        "       \"0xd5855eb08b3387c0af375e9cdb6acfc05eb8f519e419b874b6ff2ffda7ed1dff\"\n" +
                        "    ]\n" +
                        "  }\n" +
                        "}"
        );

        EthBlock ethBlock = deserialiseResponse(EthBlock.class);
        EthBlock.Block block = new EthBlock.Block(
                "0x1b4",
                "0xe670ec64341771606e55d6b4ca35a1a6b75ee3d5145a99d05921026d1527331",
                "0x9646252be9520f6e71339a8df9c55e4d7619deeb018d2a3f2d21fc165dde5eb5",
                "0xe04d296d2460cfb8472af2c5fd05b5a214109c25688d3704aed5484f9a7792f2",
                "0x1dcc4de8dec75d7aab85b567b6ccd41ad312451b948a7413f0a142fd40d49347",
                "0xe670ec64341771606e55d6b4ca35a1a6b75ee3d5145a99d05921026d1527331",
                "0x56e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421",
                "0xd5855eb08b3387c0af375e9cdb6acfc05eb8f519e419b874b6ff2ffda7ed1dff",
                "0x56e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421",
                "0x4e65fda2159562a496f9f3522f89122a3088497a",
                "0x027f07",
                "0x027f07",
                "0x0000000000000000000000000000000000000000000000000000000000000000",
                "0x027f07",
                "0x9f759",
                "0x9f759",
                "0x54e34e8e",
                Arrays.asList(new EthBlock.TransactionObject(
                                "0xc6ef2fc5426d6ad6fd9e2a26abeab0aa2411b7ab17f30a99d3cb96aed1d1055b",
                                "0x",
                                "0xbeab0aa2411b7ab17f30a99d3cb9c6ef2fc5426d6ad6fd9e2a26a6aed1d1055b",
                                "0x15df",
                                "0x1",
                                "0x407d73d8a49eeb85d32cf465507dd71d507100c1",
                                "0x85h43d8a49eeb85d32cf465507dd71d507100c1",
                                "0x7f110",
                                "0x7f110",
                                "0x09184e72a000",
                                "0x603880600c6000396000f300603880600c6000396000f3603880600c6000396000f360"
                        )
                ),
                Arrays.asList(
                        "0x1dcc4de8dec75d7aab85b567b6ccd41ad312451b948a7413f0a142fd40d49347",
                        "0xd5855eb08b3387c0af375e9cdb6acfc05eb8f519e419b874b6ff2ffda7ed1dff"
                )
        );
        assertThat(ethBlock.getBlock().get(),
                equalTo(block));
    }

    @Test
    public void testEthBlockNull() {
        buildResponse(
                "{\n" +
                        "  \"result\": null\n" +
                        "}"
        );

        EthBlock ethBlock = deserialiseResponse(EthBlock.class);
        assertThat(ethBlock.getBlock(), is(Optional.empty()));
    }

    @Test
    public void testEthTransaction() {
        buildResponse(
                "{\n" +
                        "    \"id\":1,\n" +
                        "    \"jsonrpc\":\"2.0\",\n" +
                        "    \"result\": {\n" +
                        "        \"hash\":\"0xc6ef2fc5426d6ad6fd9e2a26abeab0aa2411b7ab17f30a99d3cb96aed1d1055b\",\n" +
                        "        \"nonce\":\"0x\",\n" +
                        "        \"blockHash\": \"0xbeab0aa2411b7ab17f30a99d3cb9c6ef2fc5426d6ad6fd9e2a26a6aed1d1055b\",\n" +
                        "        \"blockNumber\": \"0x15df\",\n" +
                        "        \"transactionIndex\":  \"0x1\",\n" +
                        "        \"from\":\"0x407d73d8a49eeb85d32cf465507dd71d507100c1\",\n" +
                        "        \"to\":\"0x85h43d8a49eeb85d32cf465507dd71d507100c1\",\n" +
                        "        \"value\":\"0x7f110\",\n" +
                        "        \"gas\": \"0x7f110\",\n" +
                        "        \"gasPrice\":\"0x09184e72a000\",\n" +
                        "        \"input\":\"0x603880600c6000396000f300603880600c6000396000f3603880600c6000396000f360\"\n" +
                        "  }\n" +
                        "}"
        );
        Transaction transaction = new Transaction(
                "0xc6ef2fc5426d6ad6fd9e2a26abeab0aa2411b7ab17f30a99d3cb96aed1d1055b",
                "0x",
                "0xbeab0aa2411b7ab17f30a99d3cb9c6ef2fc5426d6ad6fd9e2a26a6aed1d1055b",
                "0x15df",
                "0x1",
                "0x407d73d8a49eeb85d32cf465507dd71d507100c1",
                "0x85h43d8a49eeb85d32cf465507dd71d507100c1",
                "0x7f110",
                "0x7f110",
                "0x09184e72a000",
                "0x603880600c6000396000f300603880600c6000396000f3603880600c6000396000f360"
        );

        EthTransaction ethTransaction = deserialiseResponse(EthTransaction.class);
        assertThat(ethTransaction.getTransaction().get(), equalTo(transaction));
    }

    @Test
    public void testEthTransactionNull() {
        buildResponse(
                "{\n" +
                        "  \"result\": null\n" +
                        "}"
        );

        EthTransaction ethTransaction = deserialiseResponse(EthTransaction.class);
        assertThat(ethTransaction.getTransaction(), is(Optional.empty()));
    }

    @Test
    public void testeEthGetTransactionReceipt() {
        buildResponse(
                "{\n" +
                        "    \"id\":1,\n" +
                        "    \"jsonrpc\":\"2.0\",\n" +
                        "    \"result\": {\n" +
                        "        \"transactionHash\": \"0xb903239f8543d04b5dc1ba6579132b143087c68db1b2168786408fcbce568238\",\n" +
                        "        \"transactionIndex\":  \"0x1\",\n" +
                        "        \"blockHash\": \"0xc6ef2fc5426d6ad6fd9e2a26abeab0aa2411b7ab17f30a99d3cb96aed1d1055b\",\n" +
                        "        \"blockNumber\": \"0xb\",\n" +
                        "        \"cumulativeGasUsed\": \"0x33bc\",\n" +
                        "        \"gasUsed\": \"0x4dc\",\n" +
                        "        \"contractAddress\": \"0xb60e8dd61c5d32be8058bb8eb970870f07233155\",\n" +
                        "        \"root\": \"9307ba10e41ecf3d40507fc908655fe72fc129d46f6d99baf7605d0e29184911\",\n" +
                        "        \"from\":\"0x407d73d8a49eeb85d32cf465507dd71d507100c1\",\n" +
                        "        \"to\":\"0x85h43d8a49eeb85d32cf465507dd71d507100c1\",\n" +
                        "        \"logs\": [{\n" +
                        "            \"removed\": false,\n" +
                        "            \"logIndex\": \"0x1\",\n" +
                        "            \"transactionIndex\": \"0x0\",\n" +
                        "            \"transactionHash\": \"0xdf829c5a142f1fccd7d8216c5785ac562ff41e2dcfdf5785ac562ff41e2dcf\",\n" +
                        "            \"blockHash\": \"0x8216c5785ac562ff41e2dcfdf5785ac562ff41e2dcfdf829c5a142f1fccd7d\",\n" +
                        "            \"blockNumber\":\"0x1b4\",\n" +
                        "            \"address\": \"0x16c5785ac562ff41e2dcfdf829c5a142f1fccd7d\",\n" +
                        "            \"data\":\"0x0000000000000000000000000000000000000000000000000000000000000000\",\n" +
                        "            \"topics\": [\"0x59ebeb90bc63057b6515673c3ecf9438e5058bca0f92585014eced636878c9a5\"]" +
                        "        }]\n" +
                        "  }\n" +
                        "}"
        );

        EthGetTransactionReceipt.TransactionReceipt transactionReceipt =
                new EthGetTransactionReceipt.TransactionReceipt(
                        "0xb903239f8543d04b5dc1ba6579132b143087c68db1b2168786408fcbce568238",
                        "0x1",
                        "0xc6ef2fc5426d6ad6fd9e2a26abeab0aa2411b7ab17f30a99d3cb96aed1d1055b",
                        "0xb",
                        "0x33bc",
                        "0x4dc",
                        "0xb60e8dd61c5d32be8058bb8eb970870f07233155",
                        "9307ba10e41ecf3d40507fc908655fe72fc129d46f6d99baf7605d0e29184911",
                        "0x407d73d8a49eeb85d32cf465507dd71d507100c1",
                        "0x85h43d8a49eeb85d32cf465507dd71d507100c1",
                        Arrays.asList(
                                new Log(
                                        false,
                                        "0x1",
                                        "0x0",
                                        "0xdf829c5a142f1fccd7d8216c5785ac562ff41e2dcfdf5785ac562ff41e2dcf",
                                        "0x8216c5785ac562ff41e2dcfdf5785ac562ff41e2dcfdf829c5a142f1fccd7d",
                                        "0x1b4",
                                        "0x16c5785ac562ff41e2dcfdf829c5a142f1fccd7d",
                                        "0x0000000000000000000000000000000000000000000000000000000000000000",
                                        Arrays.asList(
                                                "0x59ebeb90bc63057b6515673c3ecf9438e5058bca0f92585014eced636878c9a5"
                                        )
                                )
                        )

                );

        EthGetTransactionReceipt ethGetTransactionReceipt = deserialiseResponse(
                EthGetTransactionReceipt.class);
        assertThat(ethGetTransactionReceipt.getTransactionReceipt().get(),
                equalTo(transactionReceipt));
    }

    @Test
    public void testEthGetCompilers() {
        buildResponse(
                "{\n" +
                        "  \"id\":1,\n" +
                        "  \"jsonrpc\": \"2.0\",\n" +
                        "  \"result\": [\"solidity\", \"lll\", \"serpent\"]\n" +
                        "}"
        );

        EthGetCompilers ethGetCompilers = deserialiseResponse(EthGetCompilers.class);
        assertThat(ethGetCompilers.getCompilers(), equalTo(Arrays.asList(
                "solidity", "lll", "serpent"
        )));
    }

    @Test
    public void testEthCompileSolidity() {
        buildResponse(
                "{\n" +
                        "  \"id\":1,\n" +
                        "  \"jsonrpc\": \"2.0\",\n" +
                        "  \"result\": {\n" +
                        "    \"test\": {\n"   +
                        "      \"code\": \"0x605280600c6000396000f3006000357c010000000000000000000000000000000000000000000000000000000090048063c6888fa114602e57005b60376004356041565b8060005260206000f35b6000600782029050604d565b91905056\",\n"   +
                        "      \"info\": {\n"   +
                        "        \"source\": \"contract test {\\n\\tfunction multiply(uint a) returns(uint d) {\\n\\t\\treturn a * 7;\\n\\t}\\n}\\n\",\n"   +
                        "        \"language\": \"Solidity\",\n"   +
                        "        \"languageVersion\": \"0\",\n"   +
                        "        \"compilerVersion\": \"0.8.2\",\n"   +
                        "        \"compilerOptions\": \"--bin --abi --userdoc --devdoc --add-std --optimize -o /var/folders/3m/_6gnl12n1tj_5kf7sc3d72dw0000gn/T/solc498936951\",\n"   +
                        "        \"abiDefinition\": [\n"   +
                        "          {\n"   +
                        "            \"constant\": false,\n"   +
                        "            \"inputs\": [\n"   +
                        "              {\n"   +
                        "                \"name\": \"a\",\n"   +
                        "                \"type\": \"uint256\"\n"   +
                        "              }\n"   +
                        "            ],\n"   +
                        "            \"name\": \"multiply\",\n"   +
                        "            \"outputs\": [\n"   +
                        "              {\n"   +
                        "                \"name\": \"d\",\n"   +
                        "                \"type\": \"uint256\"\n"   +
                        "              }\n"   +
                        "            ],\n"   +
                        "            \"type\": \"function\",\n"   +
                        "            \"payable\": false\n"   +
                        "          }\n"   +
                        "        ],\n"   +
                        "        \"userDoc\": {\n"   +
                        "          \"methods\": {}\n"   +
                        "        },\n"   +
                        "        \"developerDoc\": {\n"   +
                        "          \"methods\": {}\n"   +
                        "        }\n"   +
                        "      }\n"   +
                        "    }\n"   +
                        "    }"   +
                        "  }\n" +
                        "}"
        );

        EthCompileSolidity.CompiledSolidity compiledSolidity =
                new EthCompileSolidity.CompiledSolidity(
                        new EthCompileSolidity.Code(
                            "0x605280600c6000396000f3006000357c010000000000000000000000000000000000000000000000000000000090048063c6888fa114602e57005b60376004356041565b8060005260206000f35b6000600782029050604d565b91905056",
                            new EthCompileSolidity.SolidityInfo(
                                    "contract test {\n\tfunction multiply(uint a) returns(uint d) {\n\t\treturn a * 7;\n\t}\n}\n",
                                    "Solidity",
                                    "0",
                                    "0.8.2",
                                    "--bin --abi --userdoc --devdoc --add-std --optimize -o /var/folders/3m/_6gnl12n1tj_5kf7sc3d72dw0000gn/T/solc498936951",
                                    Arrays.asList(new AbiDefinition(
                                            false,
                                            Arrays.asList(new AbiDefinition.NamedType("a", "uint256")),
                                            "multiply",
                                            Arrays.asList(new AbiDefinition.NamedType("d", "uint256")),
                                            "function",
                                            false
                                    )),
                                    new EthCompileSolidity.Documentation(),
                                    new EthCompileSolidity.Documentation()
                            )
                        )
                );

        EthCompileSolidity ethCompileSolidity = deserialiseResponse(EthCompileSolidity.class);
        assertThat(ethCompileSolidity.getCompiledSolidity(), equalTo(compiledSolidity));
    }

    @Test
    public void testEthCompileLLL() {
        buildResponse(
                "{\n" +
                        "  \"id\":1,\n" +
                        "  \"jsonrpc\": \"2.0\",\n" +
                        "  \"result\": \"0x603880600c6000396000f3006001600060e060020a600035048063c6888fa114601857005b6021600435602b565b8060005260206000f35b600081600702905091905056\"\n" +
                        "}"
        );

        EthCompileLLL ethCompileLLL = deserialiseResponse(EthCompileLLL.class);
        assertThat(ethCompileLLL.getCompiledSourceCode(), is("0x603880600c6000396000f3006001600060e060020a600035048063c6888fa114601857005b6021600435602b565b8060005260206000f35b600081600702905091905056"));
    }

    @Test
    public void testEthCompileSerpent() {
        buildResponse(
                "{\n" +
                        "  \"id\":1,\n" +
                        "  \"jsonrpc\": \"2.0\",\n" +
                        "  \"result\": \"0x603880600c6000396000f3006001600060e060020a600035048063c6888fa114601857005b6021600435602b565b8060005260206000f35b600081600702905091905056\"\n" +
                        "}"
        );

        EthCompileSerpent ethCompileSerpent = deserialiseResponse(EthCompileSerpent.class);
        assertThat(ethCompileSerpent.getCompiledSourceCode(), is("0x603880600c6000396000f3006001600060e060020a600035048063c6888fa114601857005b6021600435602b565b8060005260206000f35b600081600702905091905056"));
    }

    @Test
    public void testEthNewFilter() {
        buildResponse(
                "{\n" +
                        "  \"id\":1,\n" +
                        "  \"jsonrpc\": \"2.0\",\n" +
                        "  \"result\": \"0x1\"\n" +
                        "}"
        );

        EthNewFilter ethNewFilter = deserialiseResponse(EthNewFilter.class);
        assertThat(ethNewFilter.getFilterId(), is(BigInteger.valueOf(1)));
    }

    @Test
    public void testEthNewBlockFilter() {
        buildResponse(
                "{\n" +
                        "  \"id\":1,\n" +
                        "  \"jsonrpc\": \"2.0\",\n" +
                        "  \"result\": \"0x1\"\n" +
                        "}"
        );

        EthNewBlockFilter ethNewBlockFilter = deserialiseResponse(EthNewBlockFilter.class);
        assertThat(ethNewBlockFilter.getFilterId(), is(BigInteger.valueOf(1)));
    }

    @Test
    public void testEthNewPendingTransactionFilter() {
        buildResponse(
                "{\n" +
                        "  \"id\":1,\n" +
                        "  \"jsonrpc\": \"2.0\",\n" +
                        "  \"result\": \"0x1\"\n" +
                        "}"
        );

        EthNewPendingTransactionFilter ethNewPendingTransactionFilter =
                deserialiseResponse(EthNewPendingTransactionFilter.class);
        assertThat(ethNewPendingTransactionFilter.getFilterId(), is(BigInteger.valueOf(1)));
    }

    @Test
    public void testEthUninstallFilter() {
        buildResponse(
                "{\n" +
                        "  \"id\":1,\n" +
                        "  \"jsonrpc\": \"2.0\",\n" +
                        "  \"result\": true\n" +
                        "}"
        );

        EthUninstallFilter ethUninstallFilter = deserialiseResponse(EthUninstallFilter.class);
        assertThat(ethUninstallFilter.isUninstalled(), is(true));
    }

    @Test
    public void testEthLog() {
        buildResponse(
                "{\n" +
                        "    \"id\":1,\n" +
                        "    \"jsonrpc\":\"2.0\",\n" +
                        "    \"result\": [{\n" +
                        "        \"removed\": false,\n" +
                        "        \"logIndex\": \"0x1\",\n" +
                        "        \"transactionIndex\": \"0x0\",\n" +
                        "        \"transactionHash\": \"0xdf829c5a142f1fccd7d8216c5785ac562ff41e2dcfdf5785ac562ff41e2dcf\",\n" +
                        "        \"blockHash\": \"0x8216c5785ac562ff41e2dcfdf5785ac562ff41e2dcfdf829c5a142f1fccd7d\",\n" +
                        "        \"blockNumber\":\"0x1b4\",\n" +
                        "        \"address\": \"0x16c5785ac562ff41e2dcfdf829c5a142f1fccd7d\",\n" +
                        "        \"data\":\"0x0000000000000000000000000000000000000000000000000000000000000000\",\n" +
                        "        \"topics\": [\"0x59ebeb90bc63057b6515673c3ecf9438e5058bca0f92585014eced636878c9a5\"]" +
                        "    }]" +
                        "}"
        );

        List<Log> logs = Arrays.asList(
                new Log(
                        false,
                        "0x1",
                        "0x0",
                        "0xdf829c5a142f1fccd7d8216c5785ac562ff41e2dcfdf5785ac562ff41e2dcf",
                        "0x8216c5785ac562ff41e2dcfdf5785ac562ff41e2dcfdf829c5a142f1fccd7d",
                        "0x1b4",
                        "0x16c5785ac562ff41e2dcfdf829c5a142f1fccd7d",
                        "0x0000000000000000000000000000000000000000000000000000000000000000",
                        Arrays.asList(
                                "0x59ebeb90bc63057b6515673c3ecf9438e5058bca0f92585014eced636878c9a5"
                        )
                )
        );

        EthLog ethLog = deserialiseResponse(EthLog.class);
        assertThat(ethLog.getLogs(), is(logs));
    }

    @Test
    public void testEthGetWork() {
        buildResponse(
                "{\n" +
                        "  \"id\":1,\n" +
                        "  \"jsonrpc\":\"2.0\",\n" +
                        "  \"result\": [\n" +
                        "      \"0x1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef\",\n" +
                        "      \"0x5EED00000000000000000000000000005EED0000000000000000000000000000\",\n" +
                        "      \"0xd1ff1c01710000000000000000000000d1ff1c01710000000000000000000000\"\n" +
                        "    ]\n" +
                        "}"
        );

        EthGetWork ethGetWork = deserialiseResponse(EthGetWork.class);
        assertThat(ethGetWork.getCurrentBlockHeaderPowHash(), is("0x1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef"));
        assertThat(ethGetWork.getSeedHashForDag(), is("0x5EED00000000000000000000000000005EED0000000000000000000000000000"));
        assertThat(ethGetWork.getBoundaryCondition(), is("0xd1ff1c01710000000000000000000000d1ff1c01710000000000000000000000"));
    }

    @Test
    public void testEthSubmitWork() {
        buildResponse(
                "{\n" +
                        "  \"id\":1,\n" +
                        "  \"jsonrpc\":\"2.0\",\n" +
                        "  \"result\": true\n" +
                        "}"
        );

        EthSubmitWork ethSubmitWork = deserialiseResponse(EthSubmitWork.class);
        assertThat(ethSubmitWork.solutionValid(), is(true));
    }

    @Test
    public void testEthSubmitHashrate() {
        buildResponse(
                "{\n" +
                        "  \"id\":1,\n" +
                        "  \"jsonrpc\":\"2.0\",\n" +
                        "  \"result\": true\n" +
                        "}"
        );

        EthSubmitHashrate ethSubmitHashrate = deserialiseResponse(EthSubmitHashrate.class);
        assertThat(ethSubmitHashrate.submissionSuccessful(), is(true));
    }

    @Test
    public void testDbPutString() {
        buildResponse(
                "{\n" +
                        "  \"id\":1,\n" +
                        "  \"jsonrpc\":\"2.0\",\n" +
                        "  \"result\": true\n" +
                        "}"
        );

        DbPutString dbPutString = deserialiseResponse(DbPutString.class);
        assertThat(dbPutString.valueStored(), is(true));
    }

    @Test
    public void testDbGetString() {
        buildResponse(
                "{\n" +
                        "  \"id\":1,\n" +
                        "  \"jsonrpc\":\"2.0\",\n" +
                        "  \"result\": \"myString\"\n" +
                        "}"
        );

        DbGetString dbGetString = deserialiseResponse(DbGetString.class);
        assertThat(dbGetString.getStoredValue(), is("myString"));
    }

    @Test
    public void testDbPutHex() {
        buildResponse(
                "{\n" +
                        "  \"id\":1,\n" +
                        "  \"jsonrpc\":\"2.0\",\n" +
                        "  \"result\": true\n" +
                        "}"
        );

        DbPutHex dbPutHex = deserialiseResponse(DbPutHex.class);
        assertThat(dbPutHex.valueStored(), is(true));
    }

    @Test
    public void testDbGetHex() {
        buildResponse(
                "{\n" +
                        "  \"id\":1,\n" +
                        "  \"jsonrpc\":\"2.0\",\n" +
                        "  \"result\": \"0x68656c6c6f20776f726c64\"\n" +
                        "}"
        );

        DbGetHex dbGetHex = deserialiseResponse(DbGetHex.class);
        assertThat(dbGetHex.getStoredValue(), is("0x68656c6c6f20776f726c64"));
    }

    @Test
    public void testSshVersion() {
        buildResponse(
                "{\n" +
                        "  \"id\":67,\n" +
                        "  \"jsonrpc\": \"2.0\",\n" +
                        "  \"result\": \"2\"\n" +
                        "}"
        );

        ShhVersion shhVersion = deserialiseResponse(ShhVersion.class);
        assertThat(shhVersion.getVersion(), is("2"));
    }

    @Test
    public void testSshPost() {
        buildResponse(
                "{\n" +
                        "  \"id\":1,\n" +
                        "  \"jsonrpc\":\"2.0\",\n" +
                        "  \"result\": true\n" +
                        "}"
        );

        ShhPost shhPost = deserialiseResponse(ShhPost.class);
        assertThat(shhPost.messageSent(), is(true));
    }

    @Test
    public void testSshNewIdentity() {
        buildResponse(
                "{\n" +
                        "  \"id\":1,\n" +
                        "  \"jsonrpc\": \"2.0\",\n" +
                        "  \"result\": \"0xc931d93e97ab07fe42d923478ba2465f283f440fd6cabea4dd7a2c807108f651b7135d1d6ca9007d5b68aa497e4619ac10aa3b27726e1863c1fd9b570d99bbaf\"\n" +
                        "}"
        );

        ShhNewIdentity shhNewIdentity = deserialiseResponse(ShhNewIdentity.class);
        assertThat(shhNewIdentity.getAddress(), is("0xc931d93e97ab07fe42d923478ba2465f283f440fd6cabea4dd7a2c807108f651b7135d1d6ca9007d5b68aa497e4619ac10aa3b27726e1863c1fd9b570d99bbaf"));
    }

    @Test
    public void testSshHasIdentity() {
        buildResponse(
                "{\n" +
                        "  \"id\":1,\n" +
                        "  \"jsonrpc\": \"2.0\",\n" +
                        "  \"result\": true\n" +
                        "}"
        );

        ShhHasIdentity shhHasIdentity = deserialiseResponse(ShhHasIdentity.class);
        assertThat(shhHasIdentity.hasPrivateKeyForIdentity(), is(true));
    }

    @Test
    public void testSshNewGroup() {
        buildResponse(
                "{\n" +
                        "  \"id\":1,\n" +
                        "  \"jsonrpc\": \"2.0\",\n" +
                        "  \"result\": \"0xc65f283f440fd6cabea4dd7a2c807108f651b7135d1d6ca90931d93e97ab07fe42d923478ba2407d5b68aa497e4619ac10aa3b27726e1863c1fd9b570d99bbaf\"\n" +
                        "}"
        );

        ShhNewGroup shhNewGroup = deserialiseResponse(ShhNewGroup.class);
        assertThat(shhNewGroup.getAddress(), is("0xc65f283f440fd6cabea4dd7a2c807108f651b7135d1d6ca90931d93e97ab07fe42d923478ba2407d5b68aa497e4619ac10aa3b27726e1863c1fd9b570d99bbaf"));
    }

    @Test
    public void testSshAddToGroup() {
        buildResponse(
                "{\n" +
                        "  \"id\":1,\n" +
                        "  \"jsonrpc\": \"2.0\",\n" +
                        "  \"result\": true\n" +
                        "}"
        );

        ShhAddToGroup shhAddToGroup = deserialiseResponse(ShhAddToGroup.class);
        assertThat(shhAddToGroup.addedToGroup(), is(true));
    }

    @Test
    public void testSshNewFilter() {
        buildResponse(
                "{\n" +
                        "  \"id\":1,\n" +
                        "  \"jsonrpc\":\"2.0\",\n" +
                        "  \"result\": \"0x7\"\n" +
                        "}"
        );

        ShhNewFilter shhNewFilter = deserialiseResponse(ShhNewFilter.class);
        assertThat(shhNewFilter.getFilterId(), is(BigInteger.valueOf(7)));
    }

    @Test
    public void testSshUninstallFilter() {
        buildResponse(
                "{\n" +
                        "  \"id\":1,\n" +
                        "  \"jsonrpc\":\"2.0\",\n" +
                        "  \"result\": true\n" +
                        "}"
        );

        ShhUninstallFilter shhUninstallFilter = deserialiseResponse(ShhUninstallFilter.class);
        assertThat(shhUninstallFilter.isUninstalled(), is(true));
    }

    @Test
    public void testSshMessages() {
        buildResponse(
                "{\n" +
                        "  \"id\":1,\n" +
                        "  \"jsonrpc\":\"2.0\",\n" +
                        "  \"result\": [{\n" +
                        "    \"hash\": \"0x33eb2da77bf3527e28f8bf493650b1879b08c4f2a362beae4ba2f71bafcd91f9\",\n" +
                        "    \"from\": \"0x3ec052fc33...\",\n" +
                        "    \"to\": \"0x87gdf76g8d7fgdfg...\",\n" +
                        "    \"expiry\": \"0x54caa50a\",\n" +
                        "    \"ttl\": \"0x64\",\n" +
                        "    \"sent\": \"0x54ca9ea2\",\n" +
                        "    \"topics\": [\"0x6578616d\"],\n" +
                        "    \"payload\": \"0x7b2274797065223a226d657373616765222c2263686...\",\n" +
                        "    \"workProved\": \"0x0\"\n" +
                        "    }]\n" +
                        "}"
        );

        List<ShhMessages.SshMessage> messages = Arrays.asList(
                new ShhMessages.SshMessage(
                        "0x33eb2da77bf3527e28f8bf493650b1879b08c4f2a362beae4ba2f71bafcd91f9",
                        "0x3ec052fc33...",
                        "0x87gdf76g8d7fgdfg...",
                        "0x54caa50a",
                        "0x64",
                        "0x54ca9ea2",
                        Arrays.asList("0x6578616d"),
                        "0x7b2274797065223a226d657373616765222c2263686...",
                        "0x0"
                )
        );

        ShhMessages shhMessages = deserialiseResponse(ShhMessages.class);
        assertThat(shhMessages.getMessages(), equalTo(messages));
    }
}
