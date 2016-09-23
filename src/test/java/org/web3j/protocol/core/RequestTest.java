package org.web3j.protocol.core;


import org.junit.Test;

import org.web3j.protocol.core.methods.request.EthCall;
import org.web3j.protocol.RequestTester;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.http.HttpService;


public class RequestTest extends RequestTester {

    private Web3j web3j;

    @Override
    protected void initWeb3Client(HttpService httpService) {
        web3j = Web3j.build(httpService);
    }

    @Test
    public void testEthCall() throws Exception {
        web3j.ethCall(new EthCall("0x52b93c80364dc2dd4444c146d73b9836bbbb2b3f", "0x0"),
                DefaultBlockParameter.valueOf("latest")).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eth_call\",\"params\":[{\"to\":\"0x52b93c80364dc2dd4444c146d73b9836bbbb2b3f\",\"data\":\"0x0\"},\"latest\"],\"id\":1}");
    }
}
