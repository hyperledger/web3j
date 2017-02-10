package org.web3j.protocol;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpStatus;
import cz.msebera.android.httpclient.HttpVersion;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.methods.CloseableHttpResponse;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.CloseableHttpClient;
import cz.msebera.android.httpclient.message.BasicStatusLine;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import org.web3j.protocol.core.Response;
import org.web3j.protocol.http.HttpService;

import static org.junit.Assert.fail;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Protocol Response tests.
 */
@RunWith(MockitoJUnitRunner.class)
public abstract class ResponseTester {

    private HttpService web3jService;

    private CloseableHttpClient closeableHttpClient;
    private CloseableHttpResponse httpResponse;
    private HttpEntity entity;

    @Before
    public void setUp() {
        closeableHttpClient = mock(CloseableHttpClient.class);
        web3jService = new HttpService("", closeableHttpClient);

        httpResponse = mock(CloseableHttpResponse.class);
        entity = mock(HttpEntity.class);

        when(httpResponse.getStatusLine()).thenReturn(
                new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "Test")
        );
        when(httpResponse.getEntity()).thenReturn(entity);
    }

    protected <T extends Response> T deserialiseResponse(Class<T> type) {
        T response = null;
        try {
            response = web3jService.getResponseHandler(type).handleResponse(httpResponse);
            when(closeableHttpClient.execute(isA(HttpPost.class), isA(ResponseHandler.class))).thenReturn(response);
        } catch (IOException e) {
            fail(e.getMessage());
        }
        return response;
    }

    protected void buildResponse(String data) {
        try {
            when(entity.getContent()).thenReturn(buildInputStream(data));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    private InputStream buildInputStream(String input) {
        return new ByteArrayInputStream(input.getBytes());
    }
}
