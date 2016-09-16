package org.web3j.protocol.jsonrpc20;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import org.web3j.methods.request.EthCall;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


public class RequestTest {

    private CloseableHttpClient closeableHttpClient;
    private HttpService httpService;
    private Web3j web3j;

    @Before
    public void setUp() {
        closeableHttpClient = mock(CloseableHttpClient.class);
        httpService = new HttpService("", closeableHttpClient);
        web3j = Web3j.build(httpService);
    }

    @Test
    public void testEthCall() throws Exception {
        web3j.ethCall(new EthCall("0x52b93c80364dc2dd4444c146d73b9836bbbb2b3f", "0x0"),
                DefaultBlockParameter.valueOf("latest")).send();

        verifyResult("{\"jsonRpc\":\"2.0\",\"method\":\"eth_call\",\"params\":[{\"toAddress\":\"0x52b93c80364dc2dd4444c146d73b9836bbbb2b3f\",\"data\":\"0x0\"},\"latest\"],\"id\":1}");
    }

    private void verifyResult(String expected) throws Exception {
        ArgumentCaptor<HttpPost> httpPostArgumentCaptor = ArgumentCaptor.forClass(HttpPost.class);
        verify(closeableHttpClient).execute(httpPostArgumentCaptor.capture(), any(ResponseHandler.class));

        String result = readResult(httpPostArgumentCaptor.getValue().getEntity().getContent());
        assertThat(result, is(expected));
    }

    private static String readResult(InputStream inputStream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString("UTF-8");
    }
}
