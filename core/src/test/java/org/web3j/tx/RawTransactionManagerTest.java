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
import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import org.web3j.crypto.SampleKeys;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.exceptions.TxHashMismatchException;
import org.web3j.utils.Convert;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class RawTransactionManagerTest extends ManagedTransactionTester {

    @Test
    public void testTxHashMismatch() throws IOException {
        final TransactionReceipt transactionReceipt = prepareTransfer();
        prepareTransaction(transactionReceipt);

        final TransactionManager transactionManager =
                new RawTransactionManager(web3j, SampleKeys.CREDENTIALS);
        final Transfer transfer = new Transfer(web3j, transactionManager);
        assertThrows(
                TxHashMismatchException.class,
                () -> transfer.sendFunds(ADDRESS, BigDecimal.ONE, Convert.Unit.ETHER).send());
    }
}
