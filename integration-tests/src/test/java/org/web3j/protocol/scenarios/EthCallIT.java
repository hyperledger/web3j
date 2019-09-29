package org.web3j.protocol.scenarios;

import org.junit.Before;
import org.junit.Test;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.generated.Revert;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;
import java.util.Collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EthCallIT extends Scenario {

    private Revert contract;

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        this.contract = Revert.deploy(web3j, ALICE, new DefaultGasProvider()).send();
    }

    @Test
    public void testWithoutRevert() throws Exception {
        EthCall ethCall = ethCall(BigInteger.valueOf(0L));

        assertFalse(ethCall.isReverted());
    }

    @Test
    public void testRevertWithoutMessage() throws Exception {
        EthCall ethCall = ethCall(BigInteger.valueOf(1L));

        assertTrue(ethCall.isReverted());
        assertTrue(ethCall.getRevertReason().endsWith("revert"));
    }

    @Test
    public void testRevertWithMessage() throws Exception {
        EthCall ethCall = ethCall(BigInteger.valueOf(2L));

        assertTrue(ethCall.isReverted());
        assertTrue(ethCall.reverts());
        assertTrue(ethCall.getRevertReason().endsWith("revert The reason for revert"));
    }

    private EthCall ethCall(BigInteger value) throws java.io.IOException {
        final Function function = new Function(
                Revert.FUNC_SET,
                Collections.singletonList(new Uint256(value)),
                Collections.emptyList());
        String encodedFunction = FunctionEncoder.encode(function);

        return web3j.ethCall(
                Transaction.createEthCallTransaction(ALICE.getAddress(), contract.getContractAddress(), encodedFunction),
                DefaultBlockParameterName.LATEST)
                .send();
    }

}
