package org.web3j.protocol.pantheon;

import org.junit.Test;

import org.web3j.protocol.ResponseTester;
import org.web3j.protocol.pantheon.methods.response.EthChainIdResponse;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class ResponseTest extends ResponseTester {

    @Test
    public void testEthChainIdResponse() {
        buildResponse("{\n"
                + "    \"jsonrpc\": \"2.0\",\n"
                + "    \"id\": 1,\n"
                + "    \"result\": \"0x1\"\n"
                + "}");



        EthChainIdResponse ethChainIdResponse = deserialiseResponse(EthChainIdResponse.class);
        assertThat(ethChainIdResponse.getChainId(),equalTo(1));
    }

}
