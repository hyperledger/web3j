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
package org.web3j.crypto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.web3j.crypto.TransactionUtils.generateTransactionHashHexEncoded;

public class TransactionUtilsTest {

    @Test
    public void testGenerateTransactionHash() {
        assertEquals(
                generateTransactionHashHexEncoded(
                        TransactionEncoderTest.createContractTransaction(), SampleKeys.CREDENTIALS),
                ("0xc3a0f520404c8cd0cb1c98be6b8e17ee32bf134ac1697d078e90422525c2d902"));
    }

    @Test
    public void testGenerateEip155TransactionHash() {
        assertEquals(
                generateTransactionHashHexEncoded(
                        TransactionEncoderTest.createContractTransaction(),
                        (byte) 1,
                        SampleKeys.CREDENTIALS),
                ("0x568c7f6920c1cee8332e245c473657b9c53044eb96ed7532f5550f1139861e9e"));
    }

    @Test
    void deriveChainIdWhenMainNet() {
        long v = 37;

        long chainId = TransactionUtils.deriveChainId(v);

        assertEquals(1, chainId);
    }

    @Test
    void deriveChainIdWhenRopstenNet() {
        long v = 42;

        long chainId = TransactionUtils.deriveChainId(v);

        assertEquals(3, chainId);
    }

    @Test
    void deriveChainIdWhenLegacySignature() {
        long v1 = 27;
        long v2 = 28;

        long chainId_1 = TransactionUtils.deriveChainId(v1);
        long chainId_2 = TransactionUtils.deriveChainId(v2);

        assertEquals(0, chainId_1);
        assertEquals(0, chainId_2);
    }
}
