package org.web3j.protocol.jsonrpc20;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicStatusLine;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.web3j.Web3jService;
import org.web3j.methods.response.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Protocol tests.
 */
@RunWith(MockitoJUnitRunner.class)
public class ResponseTest {

    private Web3jService web3jService;

    private CloseableHttpClient closeableHttpClient;
    private CloseableHttpResponse httpResponse;
    private HttpEntity entity;

    @Before
    public void setUp() {
        closeableHttpClient = mock(CloseableHttpClient.class);
        web3jService = new Web3jService("", closeableHttpClient);

        httpResponse = mock(CloseableHttpResponse.class);
        entity = mock(HttpEntity.class);

        when(httpResponse.getStatusLine()).thenReturn(
                new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "Test")
        );
        when(httpResponse.getEntity()).thenReturn(entity);
    }

    private <T> T deserialiseResponse(Class<T> type) {
        T response = null;
        try {
            response = web3jService.getResponseHandler(type).handleResponse(httpResponse);
            when(closeableHttpClient.execute(isA(HttpPost.class), isA(ResponseHandler.class))).thenReturn(response);
        } catch (IOException e) {
            fail(e.getMessage());
        }
        return response;
    }

    private void buildResponse(String data) {
        try {
            when(entity.getContent()).thenReturn(buildInputStream(data));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    private InputStream buildInputStream(String input) {
        return new ByteArrayInputStream(input.getBytes());
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
}
