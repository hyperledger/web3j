package org.web3j.protocol;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Before;
import org.mockito.ArgumentCaptor;

import org.web3j.protocol.http.HttpService;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


public abstract class RequestTester {

    private CloseableHttpClient closeableHttpClient;
    private HttpService httpService;

    @Before
    public void setUp() {
        closeableHttpClient = mock(CloseableHttpClient.class);
        httpService = new HttpService("", closeableHttpClient);
        initWeb3Client(httpService);
    }

    protected abstract void initWeb3Client(HttpService httpService);

    protected void verifyResult(String expected) throws Exception {
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
