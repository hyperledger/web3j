/*
 * Copyright 2019 Web3 Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.web3j.protocol.scenarios;

import java.math.BigInteger;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.generated.Revert;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.tx.gas.DefaultGasProvider;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EthCallIT extends Scenario {

    private Revert contract;

    @BeforeEach
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
        assertTrue(ethCall.getRevertReason().endsWith("revert The reason for revert"));
    }

    private EthCall ethCall(BigInteger value) throws java.io.IOException {
        final Function function =
                new Function(
                        Revert.FUNC_SET,
                        Collections.singletonList(new Uint256(value)),
                        Collections.emptyList());
        String encodedFunction = FunctionEncoder.encode(function);

        return web3j.ethCall(
                        Transaction.createEthCallTransaction(
                                ALICE.getAddress(), contract.getContractAddress(), encodedFunction),
                        DefaultBlockParameterName.LATEST)
                .send();
    }
}
