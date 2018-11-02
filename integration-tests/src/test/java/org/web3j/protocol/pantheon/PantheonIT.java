package org.web3j.protocol.pantheon;

import org.junit.Before;
import org.junit.Test;

import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.pantheon.methods.response.EthChainIdResponse;

import static org.junit.Assert.assertTrue;

/**
 * JSON-RPC 2.0 Pantheon Integration Tests
 */
public class PantheonIT {

    private Pantheon pantheon;

    @Before
    public void setUp() {
        this.pantheon = Pantheon.build(new HttpService());
    }

    @Test
    public void testEthChainId() throws Exception {
        EthChainIdResponse ethChainId = pantheon.ethChainId().send();
        assertTrue(ethChainId.getChainId() > 0);
    }
}
