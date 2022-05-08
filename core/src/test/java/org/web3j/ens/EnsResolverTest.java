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
package org.web3j.ens;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.web3j.abi.TypeEncoder;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.dto.EnsGatewayResponseDTO;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthSyncing;
import org.web3j.protocol.core.methods.response.NetVersion;
import org.web3j.tx.ChainIdLong;
import org.web3j.utils.Numeric;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.web3j.ens.EnsResolver.DEFAULT_SYNC_THRESHOLD;
import static org.web3j.ens.EnsResolver.isValidEnsName;
import static org.web3j.protocol.http.HttpService.JSON_MEDIA_TYPE;

public class EnsResolverTest {

    private Web3j web3j;
    private Web3jService web3jService;
    private EnsResolver ensResolver;

    private ObjectMapper om = ObjectMapperFactory.getObjectMapper();

    private static List<String> urls = new ArrayList<>();

    private String sender = "0x226159d592E2b063810a10Ebf6dcbADA94Ed68b8";

    private String data = "0x00112233";

    @BeforeAll
    static void beforeAll() {
        urls.add("https://example-1.com/gateway/{sender}/{data}.json");
        urls.add("https://example-2.com/gateway/{sender}/{data}.json");
    }

    @BeforeEach
    public void setUp() {
        web3jService = mock(Web3jService.class);
        web3j = Web3j.build(web3jService);
        ensResolver = new EnsResolver(web3j);
    }

    @Test
    public void testResolve() throws Exception {
        configureSyncing(false);
        configureLatestBlock(System.currentTimeMillis() / 1000); // block timestamp is in seconds

        NetVersion netVersion = new NetVersion();
        netVersion.setResult(Long.toString(ChainIdLong.MAINNET));

        String resolverAddress =
                "0x0000000000000000000000004c641fb9bad9b60ef180c31f56051ce826d21a9a";
        String contractAddress =
                "0x00000000000000000000000019e03255f667bdfd50a32722df860b1eeaf4d635";

        EthCall resolverAddressResponse = new EthCall();
        resolverAddressResponse.setResult(resolverAddress);

        EthCall contractAddressResponse = new EthCall();
        contractAddressResponse.setResult(contractAddress);

        when(web3jService.send(any(Request.class), eq(NetVersion.class))).thenReturn(netVersion);
        when(web3jService.send(any(Request.class), eq(EthCall.class)))
                .thenReturn(resolverAddressResponse);
        when(web3jService.send(any(Request.class), eq(EthCall.class)))
                .thenReturn(contractAddressResponse);

        assertEquals(
                ensResolver.resolve("web3j.eth"), ("0x19e03255f667bdfd50a32722df860b1eeaf4d635"));
    }

    @Test
    public void testResolveEnsNameEmptyOrDot() throws Exception {
        assertNull(ensResolver.resolve(" "));
        assertNull(ensResolver.resolve(""));
        assertNull(ensResolver.resolve("."));
        assertNull(ensResolver.resolve(" . "));
    }

    @Test
    public void testReverseResolve() throws Exception {
        configureSyncing(false);
        configureLatestBlock(System.currentTimeMillis() / 1000); // block timestamp is in seconds

        NetVersion netVersion = new NetVersion();
        netVersion.setResult(Long.toString(ChainIdLong.MAINNET));

        String resolverAddress =
                "0x0000000000000000000000004c641fb9bad9b60ef180c31f56051ce826d21a9a";
        String contractName =
                "0x0000000000000000000000000000000000000000000000000000000000000020"
                        + TypeEncoder.encode(new Utf8String("web3j.eth"));

        EthCall resolverAddressResponse = new EthCall();
        resolverAddressResponse.setResult(resolverAddress);

        EthCall contractNameResponse = new EthCall();
        contractNameResponse.setResult(contractName);

        when(web3jService.send(any(Request.class), eq(NetVersion.class))).thenReturn(netVersion);
        when(web3jService.send(any(Request.class), eq(EthCall.class)))
                .thenReturn(resolverAddressResponse);
        when(web3jService.send(any(Request.class), eq(EthCall.class)))
                .thenReturn(contractNameResponse);

        assertEquals(
                ensResolver.reverseResolve("0x19e03255f667bdfd50a32722df860b1eeaf4d635"),
                ("web3j.eth"));
    }

    @Test
    public void testIsSyncedSyncing() throws Exception {
        configureSyncing(true);

        assertFalse(ensResolver.isSynced());
    }

    @Test
    public void testIsSyncedFullySynced() throws Exception {
        configureSyncing(false);
        configureLatestBlock(System.currentTimeMillis() / 1000); // block timestamp is in seconds

        assertTrue(ensResolver.isSynced());
    }

    @Test
    public void testIsSyncedBelowThreshold() throws Exception {
        configureSyncing(false);
        configureLatestBlock((System.currentTimeMillis() / 1000) - DEFAULT_SYNC_THRESHOLD);

        assertFalse(ensResolver.isSynced());
    }

    private void configureSyncing(boolean isSyncing) throws IOException {
        EthSyncing ethSyncing = new EthSyncing();
        EthSyncing.Result result = new EthSyncing.Result();
        result.setSyncing(isSyncing);
        ethSyncing.setResult(result);

        when(web3jService.send(any(Request.class), eq(EthSyncing.class))).thenReturn(ethSyncing);
    }

    private void configureLatestBlock(long timestamp) throws IOException {
        EthBlock.Block block = new EthBlock.Block();
        block.setTimestamp(Numeric.encodeQuantity(BigInteger.valueOf(timestamp)));
        EthBlock ethBlock = new EthBlock();
        ethBlock.setResult(block);

        when(web3jService.send(any(Request.class), eq(EthBlock.class))).thenReturn(ethBlock);
    }

    @Test
    public void testIsEnsName() {
        assertTrue(isValidEnsName("eth"));
        assertTrue(isValidEnsName("web3.eth"));
        assertTrue(isValidEnsName("0x19e03255f667bdfd50a32722df860b1eeaf4d635.eth"));

        assertFalse(isValidEnsName("0x19e03255f667bdfd50a32722df860b1eeaf4d635"));
        assertFalse(isValidEnsName("19e03255f667bdfd50a32722df860b1eeaf4d635"));

        assertTrue(isValidEnsName(""));
        assertTrue(isValidEnsName("."));
    }

    @Test
    void buildRequestWhenGetSuccessTest() throws IOException {
        String url = "https://example.com/gateway/{sender}/{data}.json";
        String sender = "0x226159d592E2b063810a10Ebf6dcbADA94Ed68b8";
        String data = "0xd5fa2b00";

        okhttp3.Request request = ensResolver.buildRequest(url, sender, data);

        assertNotNull(request);
        assertNotNull(request.url());
        assertEquals("GET", request.method());
        assertEquals(
                "https://example.com/gateway/0x226159d592E2b063810a10Ebf6dcbADA94Ed68b8/0xd5fa2b00.json",
                request.url().url().toString());
    }

    @Test
    void buildRequestWhenPostSuccessTest() throws IOException {
        String url = "https://example.com/gateway/{sender}.json";
        String sender = "0x226159d592E2b063810a10Ebf6dcbADA94Ed68b8";
        String data = "0xd5fa2b00";

        okhttp3.Request request = ensResolver.buildRequest(url, sender, data);

        assertNotNull(request);
        assertNotNull(request.url());
        assertNotNull(request.body());
        assertEquals("POST", request.method());
        assertEquals(
                "https://example.com/gateway/0x226159d592E2b063810a10Ebf6dcbADA94Ed68b8.json",
                request.url().url().toString());
    }

    @Test
    void buildRequestWhenWithoutDataTest() throws IOException {
        String url = "https://example.com/gateway/{sender}.json";
        String sender = "0x226159d592E2b063810a10Ebf6dcbADA94Ed68b8";

        assertThrows(
                EnsResolutionException.class, () -> ensResolver.buildRequest(url, sender, null));
    }

    @Test
    void buildRequestWhenWithoutSenderTest() throws IOException {
        String url = "https://example.com/gateway/{sender}.json";
        String data = "0xd5fa2b00";

        assertThrows(EnsResolutionException.class, () -> ensResolver.buildRequest(url, null, data));
    }

    @Test
    void buildRequestWhenNotValidSenderTest() throws IOException {
        String url = "https://example.com/gateway/{sender}.json";
        String data = "0xd5fa2b00";

        assertThrows(
                EnsResolutionException.class,
                () -> ensResolver.buildRequest(url, "not valid address", data));
    }

    @Test
    void buildRequestWhenNotValidUrl() throws IOException {
        String url = "https://example.com/gateway/{data}.json";
        String sender = "0x226159d592E2b063810a10Ebf6dcbADA94Ed68b8";
        String data = "0xd5fa2b00";

        assertThrows(
                EnsResolutionException.class, () -> ensResolver.buildRequest(url, sender, data));
    }

    @Test
    void ccipReadFetchWhenSuccess() throws IOException {
        OkHttpClient httpClientMock = mock(OkHttpClient.class);
        Call call = mock(Call.class);

        okhttp3.Response responseObj = buildResponse(200, urls.get(0), sender, data);

        ensResolver.setHttpClient(httpClientMock);
        when(httpClientMock.newCall(any())).thenReturn(call);
        when(call.execute()).thenReturn(responseObj);

        String responseStr = ensResolver.ccipReadFetch(urls, sender, data);

        assertNotNull(responseStr);
        EnsGatewayResponseDTO response = om.readValue(responseStr, EnsGatewayResponseDTO.class);

        assertNotNull(response);
        assertNotNull(response.getData());
        assertEquals(data, response.getData());
    }

    @Test
    void ccipReadFetchWhenFirst400_Second200() throws IOException {
        OkHttpClient httpClientMock = mock(OkHttpClient.class);
        Call call = mock(Call.class);

        okhttp3.Response responseObj_1 = buildResponse(400, urls.get(0), sender, data);
        okhttp3.Response responseObj_2 = buildResponse(200, urls.get(1), sender, data);

        ensResolver.setHttpClient(httpClientMock);
        when(httpClientMock.newCall(any())).thenReturn(call);
        when(call.execute()).thenReturn(responseObj_1, responseObj_2);

        assertThrows(
                EnsResolutionException.class, () -> ensResolver.ccipReadFetch(urls, sender, data));
    }

    @Test
    void ccipReadFetchWhenFirst200_Second400() throws IOException {
        OkHttpClient httpClientMock = mock(OkHttpClient.class);
        Call call = mock(Call.class);

        okhttp3.Response responseObj_1 = buildResponse(200, urls.get(0), sender, data);
        okhttp3.Response responseObj_2 = buildResponse(400, urls.get(1), sender, data);

        ensResolver.setHttpClient(httpClientMock);
        when(httpClientMock.newCall(any())).thenReturn(call);
        when(call.execute()).thenReturn(responseObj_1, responseObj_2);

        String responseStr = ensResolver.ccipReadFetch(urls, sender, data);

        assertNotNull(responseStr);
        EnsGatewayResponseDTO response = om.readValue(responseStr, EnsGatewayResponseDTO.class);
        assertNotNull(response);
        assertNotNull(response.getData());
        assertEquals(data, response.getData());
    }

    @Test
    void ccipReadFetchWhenFirst500_Second200() throws IOException {
        OkHttpClient httpClientMock = mock(OkHttpClient.class);
        Call call = mock(Call.class);

        okhttp3.Response responseObj_1 = buildResponse(500, urls.get(0), sender, data);
        okhttp3.Response responseObj_2 = buildResponse(200, urls.get(1), sender, data);

        ensResolver.setHttpClient(httpClientMock);
        when(httpClientMock.newCall(any())).thenReturn(call);
        when(call.execute()).thenReturn(responseObj_1, responseObj_2);

        String responseStr = ensResolver.ccipReadFetch(urls, sender, data);

        assertNotNull(responseStr);
        EnsGatewayResponseDTO response = om.readValue(responseStr, EnsGatewayResponseDTO.class);
        assertNotNull(response);
        assertNotNull(response.getData());
        assertEquals(data, response.getData());
    }

    @Test
    void ccipReadFetchWhenFirst500_Second500() throws IOException {
        OkHttpClient httpClientMock = mock(OkHttpClient.class);
        Call call = mock(Call.class);

        okhttp3.Response responseObj_1 = buildResponse(500, urls.get(0), sender, data);
        okhttp3.Response responseObj_2 = buildResponse(500, urls.get(1), sender, data);

        ensResolver.setHttpClient(httpClientMock);
        when(httpClientMock.newCall(any())).thenReturn(call);
        when(call.execute()).thenReturn(responseObj_1, responseObj_2);

        String responseStr = ensResolver.ccipReadFetch(urls, sender, data);

        assertNull(responseStr);
    }


    private okhttp3.Response buildResponse(int code, String url, String sender, String data)
            throws JsonProcessingException {
        EnsGatewayResponseDTO responseDTO = new EnsGatewayResponseDTO(data);

        return new okhttp3.Response.Builder()
                .request(ensResolver.buildRequest(url, sender, data))
                .protocol(Protocol.HTTP_2)
                .code(code)
                .body(ResponseBody.create(om.writeValueAsString(responseDTO), JSON_MEDIA_TYPE))
                .message("Some error message Code: " + code)
                .build();
    }
}
