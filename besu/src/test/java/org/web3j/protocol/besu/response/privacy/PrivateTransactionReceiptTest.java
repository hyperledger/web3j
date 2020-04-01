/*
 * Copyright 2020 Web3 Labs Ltd.
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
package org.web3j.protocol.besu.response.privacy;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import org.web3j.protocol.core.methods.response.TransactionReceipt;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class PrivateTransactionReceiptTest {

    @ParameterizedTest
    @ValueSource(classes = {TransactionReceipt.class, PrivateTransactionReceipt.class})
    public void testMethodCorrectlyDelegated(Class<TransactionReceipt> receiptClass) {
        TransactionReceipt receipt = mock(receiptClass, CALLS_REAL_METHODS);
        when(receipt.getStatus()).thenReturn(null);
        assertTrue(receipt.isStatusOK());
        verify(receipt, Mockito.times(1)).getStatus();
    }
}
