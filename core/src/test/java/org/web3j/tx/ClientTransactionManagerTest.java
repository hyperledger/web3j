/*
 * Copyright 2022 Web3 Labs Ltd.
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
package org.web3j.tx;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.tx.exceptions.ContractCallException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ClientTransactionManagerTest {

    private Web3j web3j;
    private ClientTransactionManager clientTransactionManager;

    private String address = "0x19e03255f667bdfd50a32722df860b1eeaf4d635";

    private String responseData =
            "0x556f1830000000000000000000000000c1735677a60884abbcf72295e88d47764be";

    @BeforeEach
    void setUp() {
        web3j = mock(Web3j.class);
        clientTransactionManager = new ClientTransactionManager(web3j, address);
    }

    /** According to EIP 3668, error is a valid behavior in a new functionality. */
    @Test
    void sendCallErrorResponseNotRevert() throws IOException {
        EthCall lookupDataHex = new EthCall();
        Response.Error error = new Response.Error();
        error.setCode(3);
        error.setMessage("execution reverted");
        error.setData(responseData);
        lookupDataHex.setError(error);

        Request request = mock(Request.class);

        when(request.send()).thenReturn(lookupDataHex);

        when(web3j.ethCall(any(Transaction.class), any(DefaultBlockParameter.class)))
                .thenReturn(request);

        String result =
                clientTransactionManager.sendCall(
                        "0xAdress", "data", DefaultBlockParameter.valueOf("latest"));

        assertEquals(responseData, result);
    }

    @Test
    void sendCallErrorRevertByCode() throws IOException {
        EthCall lookupDataHex = new EthCall();
        Response.Error error = new Response.Error();
        error.setCode(10);
        error.setData(responseData);
        lookupDataHex.setError(error);

        Request request = mock(Request.class);
        when(request.send()).thenReturn(lookupDataHex);
        when(web3j.ethCall(any(Transaction.class), any(DefaultBlockParameter.class)))
                .thenReturn(request);

        assertThrows(
                ContractCallException.class,
                () ->
                        clientTransactionManager.sendCall(
                                "0xAdress", "data", DefaultBlockParameter.valueOf("latest")));
    }

    @Test
    void sendCallErrorRevertByDataNull() throws IOException {
        EthCall lookupDataHex = new EthCall();
        Response.Error error = new Response.Error();
        error.setCode(3);
        error.setData(null);
        lookupDataHex.setError(error);

        Request request = mock(Request.class);
        when(request.send()).thenReturn(lookupDataHex);
        when(web3j.ethCall(any(Transaction.class), any(DefaultBlockParameter.class)))
                .thenReturn(request);

        assertThrows(
                ContractCallException.class,
                () ->
                        clientTransactionManager.sendCall(
                                "0xAdress", "data", DefaultBlockParameter.valueOf("latest")));
    }

    @Test
    void sendCallErrorSuccess() throws IOException {
        EthCall lookupDataHex = new EthCall();
        lookupDataHex.setResult(responseData);

        Request request = mock(Request.class);
        when(request.send()).thenReturn(lookupDataHex);
        when(web3j.ethCall(any(Transaction.class), any(DefaultBlockParameter.class)))
                .thenReturn(request);

        String result =
                clientTransactionManager.sendCall(
                        "0xAdress", "data", DefaultBlockParameter.valueOf("latest"));

        assertEquals(responseData, result);
    }
}
