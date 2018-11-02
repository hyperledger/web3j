package org.web3j.protocol.pantheon;

import org.junit.Test;

import org.web3j.protocol.RequestTester;
import org.web3j.protocol.http.HttpService;

public class RequestTest extends RequestTester {

    private Pantheon web3j;

    @Override
    protected void initWeb3Client(HttpService httpService) {
        web3j = Pantheon.build(httpService);
    }

    @Test
    public void testEthGetChainIdReqeust() throws Exception {
        web3j.ethChainId().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eth_chainId\",\"params\":[],\"id\":1}");
    }

}
