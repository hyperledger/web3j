package org.web3j.protocol.pantheon;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import org.web3j.protocol.core.Request;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.pantheon.methods.response.EthChainIdResponse;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

import static org.junit.Assert.assertThat;

import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class JsonRpc2_0PantheonTest {

    private HttpService httpService = mock(HttpService.class);
    private Pantheon pantheon = Pantheon.build(httpService);

    @Test
    public void testEthChainId() throws Exception {
        pantheon.ethChainId().send();

        ArgumentCaptor<Request> captor = ArgumentCaptor.forClass(Request.class);
        verify(httpService).send(captor.capture(), eq(EthChainIdResponse.class));

        Request request = captor.getValue();
        assertThat(request.getMethod(), is(equalTo("eth_chainId")));
        assertThat(request.getParams().size(), is(0));
    }
}