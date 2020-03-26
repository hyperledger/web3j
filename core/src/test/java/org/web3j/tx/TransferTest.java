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

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.SampleKeys;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Convert;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TransferTest extends ManagedTransactionTester {

    protected TransactionReceipt transactionReceipt;

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();
        transactionReceipt = prepareTransfer();
    }

    @Test
    public void testSendFunds() throws Exception {
        assertEquals(
                sendFunds(SampleKeys.CREDENTIALS, ADDRESS, BigDecimal.TEN, Convert.Unit.ETHER),
                (transactionReceipt));
    }

    @Test
    public void testTransferInvalidValue() {

        assertThrows(
                UnsupportedOperationException.class,
                () ->
                        sendFunds(
                                SampleKeys.CREDENTIALS,
                                ADDRESS,
                                new BigDecimal(0.1),
                                Convert.Unit.WEI));
    }

    protected TransactionReceipt sendFunds(
            final Credentials credentials,
            final String toAddress,
            final BigDecimal value,
            final Convert.Unit unit)
            throws Exception {
        return new Transfer(web3j, getVerifiedTransactionManager(credentials))
                .sendFunds(toAddress, value, unit)
                .call();
    }
}
