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
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.web3j.EVMTest;
import org.web3j.NodeType;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Numeric;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Integration test demonstrating integration with Greeter contract taken from the <a
 * href="https://github.com/ethereum/go-ethereum/wiki/Contract-Tutorial">Contract Tutorial</a> on
 * the Go Ethereum Wiki.
 */
@EVMTest(type = NodeType.GETH)
public class GreeterContractIT extends Scenario {

    private static final String VALUE = "Greetings!";

    @BeforeAll
    public static void setUp(Web3j web3j) {
        Scenario.web3j = web3j;
    }

    private static String getGreeterSolidityBinary() throws Exception {
        return load("/Greeter.bin");
    }

    @Test
    public void testGreeterContract() throws Exception {

        // create our smart contract
        String createTransactionHash = sendCreateContractTransaction();
        assertFalse(createTransactionHash.isEmpty());

        TransactionReceipt createTransactionReceipt =
                waitForTransactionReceipt(createTransactionHash);

        assertEquals(createTransactionReceipt.getTransactionHash(), (createTransactionHash));

        assertFalse(createTransactionReceipt.getGasUsed().equals(GAS_LIMIT));

        String contractAddress = createTransactionReceipt.getContractAddress();

        assertNotNull(contractAddress);

        // call our getter
        Function getFunction = createGreetFunction();
        String responseValue = callSmartContractFunction(getFunction, contractAddress);
        assertFalse(responseValue.isEmpty());

        List<Type> response =
                FunctionReturnDecoder.decode(responseValue, getFunction.getOutputParameters());
        assertEquals(response.size(), (1));
        assertEquals(response.get(0).getValue(), (VALUE));
    }

    private String sendCreateContractTransaction() throws Exception {
        BigInteger nonce = getNonce(ALICE.getAddress());
        String encodedConstructor =
                FunctionEncoder.encodeConstructor(Collections.singletonList(new Utf8String(VALUE)));
        RawTransaction transaction =
                RawTransaction.createContractTransaction(
                        nonce,
                        GAS_PRICE,
                        GAS_LIMIT,
                        BigInteger.ZERO,
                        getGreeterSolidityBinary() + encodedConstructor);

        org.web3j.protocol.core.methods.response.EthSendTransaction transactionResponse =
                web3j.ethSendRawTransaction(
                                Numeric.toHexString(
                                        TransactionEncoder.signMessage(transaction, ALICE)))
                        .sendAsync()
                        .get();

        return transactionResponse.getTransactionHash();
    }

    private String callSmartContractFunction(Function function, String contractAddress)
            throws Exception {

        String encodedFunction = FunctionEncoder.encode(function);

        org.web3j.protocol.core.methods.response.EthCall response =
                web3j.ethCall(
                                Transaction.createEthCallTransaction(
                                        ALICE.getAddress(), contractAddress, encodedFunction),
                                DefaultBlockParameterName.LATEST)
                        .sendAsync()
                        .get();

        return response.getValue();
    }

    Function createGreetFunction() {
        return new Function(
                "greet",
                Collections.emptyList(),
                Collections.singletonList(new TypeReference<Utf8String>() {}));
    }
}
