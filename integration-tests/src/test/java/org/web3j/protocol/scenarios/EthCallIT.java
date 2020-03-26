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

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.web3j.EVMTest;
import org.web3j.NodeType;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.test.contract.Revert;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@EVMTest(type = NodeType.OPEN_ETHEREUM)
public class EthCallIT extends Scenario {

    private static Revert contract;

    @BeforeAll
    public static void setUp(
            Web3j web3j,
            TransactionManager transactionManager,
            ContractGasProvider contractGasProvider)
            throws Exception {
        Scenario.web3j = web3j;
        EthCallIT.contract = Revert.deploy(web3j, transactionManager, contractGasProvider).call();
    }

    @Test
    public void testWithoutRevert() throws Exception {
        final EthCall ethCall = ethCall(BigInteger.valueOf(0L));

        assertFalse(ethCall.isReverted());
    }

    @Test
    public void testRevertWithoutMessage() throws Exception {
        final EthCall ethCall = ethCall(BigInteger.valueOf(1L));
        assertTrue(ethCall.isReverted());
    }

    @Test
    public void testRevertWithMessage() throws Exception {
        final EthCall ethCall = ethCall(BigInteger.valueOf(2L));
        assertTrue(ethCall.isReverted());
        assertTrue(ethCall.getRevertReason().endsWith("VM execution error."));
    }

    private EthCall ethCall(final BigInteger value) throws java.io.IOException {
        final Function function =
                new Function(
                        Revert.FUNC_SET,
                        Collections.singletonList(new Uint256(value)),
                        Collections.emptyList());
        final String encodedFunction = FunctionEncoder.encode(function);

        return web3j.ethCall(
                        Transaction.createEthCallTransaction(
                                Credentials.create(
                                                "0x8f2a55949038a9610f50fb23b5883af3b4ecb3c3bb792cbcefbd1542c692be63")
                                        .getAddress(),
                                contract.getContractAddress(),
                                encodedFunction),
                        DefaultBlockParameterName.LATEST)
                .send();
    }
}
