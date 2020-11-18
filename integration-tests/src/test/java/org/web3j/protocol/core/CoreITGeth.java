package org.web3j.protocol.core;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.web3j.EVMTest;
import org.web3j.NodeType;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthGetCompilers;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@EVMTest(type = NodeType.GETH)
public class CoreITGeth {

    @Test
    @Disabled
    public void testEthGetCompilers(Web3j web3j) throws Exception {
        EthGetCompilers ethGetCompilers = web3j.ethGetCompilers().send();
        assertNotNull(ethGetCompilers.getCompilers());
    }
}
