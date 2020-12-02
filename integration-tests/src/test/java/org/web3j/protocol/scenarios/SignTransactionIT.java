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

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.web3j.EVMTest;
import org.web3j.NodeType;
import org.web3j.crypto.Hash;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthSign;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/** Sign transaction using Ethereum node. */
@EVMTest(type = NodeType.GETH)
public class SignTransactionIT extends Scenario {
    @BeforeAll
    public static void setUp(Web3j web3j) {
        Scenario.web3j = web3j;
    }

    private static RawTransaction createTransaction() {
        BigInteger value = Convert.toWei("1", Convert.Unit.ETHER).toBigInteger();

        return RawTransaction.createEtherTransaction(
                BigInteger.valueOf(1048587),
                BigInteger.valueOf(500000),
                BigInteger.valueOf(500000),
                "0x9C98E381Edc5Fe1Ac514935F3Cc3eDAA764cf004",
                value);
    }

    @Test
    @Disabled // Passes on its own but when running the IT as a whole it fails. The account is
    // unlocked in geth.
    public void testSignTransaction() throws Exception {
        RawTransaction rawTransaction = createTransaction();
        byte[] encoded = TransactionEncoder.encode(rawTransaction);
        byte[] hashed = Hash.sha3(encoded);

        EthSign ethSign =
                web3j.ethSign(Scenario.UNLOCKED_ACCOUNT, Numeric.toHexString(hashed))
                        .sendAsync()
                        .get();

        String signature = ethSign.getSignature();
        assertNotNull(signature);
        assertFalse(signature.isEmpty());
    }
}
