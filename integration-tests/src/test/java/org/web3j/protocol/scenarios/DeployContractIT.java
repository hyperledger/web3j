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
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.web3j.EVMTest;
import org.web3j.NodeType;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Numeric;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/** Integration test demonstrating the full contract deployment workflow. */
@EVMTest(type = NodeType.BESU)
public class DeployContractIT extends Scenario {

    @BeforeAll
    public static void setup(Web3j web3j) {
        Scenario.web3j = web3j;
    }

    @Test
    public void testContractCreation() throws Exception {

        final String transactionHash = sendTransaction();
        assertFalse(transactionHash.isEmpty());

        final TransactionReceipt transactionReceipt = waitForTransactionReceipt(transactionHash);

        assertEquals(transactionReceipt.getTransactionHash(), (transactionHash));

        assertFalse(transactionReceipt.getGasUsed().equals(GAS_LIMIT));

        final String contractAddress = transactionReceipt.getContractAddress();

        assertNotNull(contractAddress);

        final Function function = createFibonacciFunction();

        final String responseValue = callSmartContractFunction(function, contractAddress);
        assertFalse(responseValue.isEmpty());

        final List<Type<?>> uint =
                FunctionReturnDecoder.decode(responseValue, function.getOutputParameters());
        assertEquals(uint.size(), (1));
        assertEquals(uint.get(0).getValue(), (BigInteger.valueOf(13)));
    }

    private String sendTransaction() throws Exception {
        final BigInteger nonce = getNonce(ALICE.getAddress());

        RawTransaction transaction =
                RawTransaction.createContractTransaction(
                        nonce, GAS_PRICE, GAS_LIMIT, BigInteger.ZERO, getFibonacciSolidityBinary());

        EthSendTransaction transactionResponse =
                web3j.ethSendRawTransaction(
                                Numeric.toHexString(
                                        TransactionEncoder.signMessage(transaction, ALICE)))
                        .sendAsync()
                        .get();

        return transactionResponse.getTransactionHash();
    }

    private String callSmartContractFunction(final Function function, final String contractAddress)
            throws Exception {

        final String encodedFunction = FunctionEncoder.encode(function);

        final org.web3j.protocol.core.methods.response.EthCall response =
                web3j.ethCall(
                                Transaction.createEthCallTransaction(
                                        ALICE.getAddress(), contractAddress, encodedFunction),
                                DefaultBlockParameterName.LATEST)
                        .sendAsync()
                        .get();

        return response.getValue();
    }
}
