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
package org.web3j.tx;

import java.io.IOException;
import java.math.BigInteger;

import org.junit.jupiter.api.Test;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.tx.exceptions.ContractCallException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.web3j.tx.TransactionManager.REVERT_ERR_STR;

public class ReadonlyTransactionManagerTest {

    private static final String OWNER_REVERT_MSG_STR =
            "Only the contract owner can perform this action";

    Web3jService service = mock(Web3jService.class);
    Web3j web3j = Web3j.build(service);
    DefaultBlockParameter defaultBlockParameter = mock(DefaultBlockParameter.class);
    EthCall response = mock(EthCall.class);

    @Test
    public void sendCallTest() throws IOException {
        when(response.getValue()).thenReturn("test");
        when(service.send(any(), any())).thenReturn(response);
        final ReadonlyTransactionManager readonlyTransactionManager =
                new ReadonlyTransactionManager(web3j, "");
        final String value = readonlyTransactionManager.sendCall("", "", defaultBlockParameter);
        assertEquals("test", value);
    }

    @Test
    public void sendCallRevertedTest() throws IOException {
        when(response.isReverted()).thenReturn(true);
        when(response.getRevertReason()).thenReturn(OWNER_REVERT_MSG_STR);
        when(service.send(any(), any())).thenReturn(response);

        final ReadonlyTransactionManager readonlyTransactionManager =
                new ReadonlyTransactionManager(web3j, "");

        final ContractCallException thrown =
                assertThrows(
                        ContractCallException.class,
                        () -> readonlyTransactionManager.sendCall("", "", defaultBlockParameter));

        assertEquals(String.format(REVERT_ERR_STR, OWNER_REVERT_MSG_STR), thrown.getMessage());
    }

    @Test
    public void testSendTransaction() {
        final ReadonlyTransactionManager readonlyTransactionManager =
                new ReadonlyTransactionManager(web3j, "");
        assertThrows(
                UnsupportedOperationException.class,
                () ->
                        readonlyTransactionManager.sendTransaction(
                                BigInteger.ZERO, BigInteger.ZERO, "", "", BigInteger.ZERO));
    }
}
