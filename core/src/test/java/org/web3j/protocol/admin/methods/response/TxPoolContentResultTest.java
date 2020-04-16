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
package org.web3j.protocol.admin.methods.response;

import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import org.web3j.protocol.admin.methods.response.TxPoolContent.TxPoolContentResult;
import org.web3j.protocol.core.methods.response.Transaction;

import static org.junit.jupiter.api.Assertions.assertFalse;

class TxPoolContentResultTest {
    @Test
    void testImmutability() {
        BigInteger id = BigInteger.ONE;

        Map<BigInteger, Transaction> idToTransaction = new HashMap<>();
        idToTransaction.put(id, new Transaction());
        Map<String, Map<BigInteger, Transaction>> pending =
                Collections.singletonMap("foo", idToTransaction);

        TxPoolContentResult result = new TxPoolContentResult(pending, Collections.emptyMap());
        List<Transaction> before = result.getPendingTransactions();
        assertFalse(before.isEmpty(), "no pending transactions");

        idToTransaction.remove(id);
        List<Transaction> after = result.getPendingTransactions();
        assertFalse(after.isEmpty(), "no pending transactions");
    }
}
