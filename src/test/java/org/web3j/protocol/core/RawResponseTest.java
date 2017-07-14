package org.web3j.protocol.core;

import org.junit.Test;

import org.web3j.protocol.ResponseTester;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;

import static org.junit.Assert.assertThat;

/**
 * Raw Response tests.
 */
public class RawResponseTest extends ResponseTester {

    private static final String RAW_RESPONSE = "{\n"
            + "  \"id\":67,\n"
            + "  \"jsonrpc\":\"2.0\",\n"
            + "  \"result\": \"Mist/v0.9.3/darwin/go1.4.1\"\n"
            + "}";

    @Test
    public void testRawResponseEnabled() {
        configureWeb3Service(true);
        final Web3ClientVersion web3ClientVersion = deserialiseWeb3ClientVersionResponse();
        assertThat(web3ClientVersion.getRawResponse(), is(RAW_RESPONSE));
    }

    @Test
    public void testRawResponseDisabled() {
        configureWeb3Service(false);
        final Web3ClientVersion web3ClientVersion = deserialiseWeb3ClientVersionResponse();
        assertThat(web3ClientVersion.getRawResponse(), nullValue());
    }

    private Web3ClientVersion deserialiseWeb3ClientVersionResponse() {
        buildResponse(RAW_RESPONSE);

        return deserialiseResponse(Web3ClientVersion.class);
    }
}
