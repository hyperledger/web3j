/*
 * Copyright 2019 Web3 Labs LTD.
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

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.web3j.crypto.TransactionUtils.generateTransactionHashHexEncoded;

public class TransactionUtilsTest {

    @Test
    public void testGenerateTransactionHash() {
        assertThat(
                generateTransactionHashHexEncoded(
                        TransactionEncoderTest.createContractTransaction(), SampleKeys.CREDENTIALS),
                is("0xc3a0f520404c8cd0cb1c98be6b8e17ee32bf134ac1697d078e90422525c2d902"));
    }

    @Test
    public void testGenerateEip155TransactionHash() {
        assertThat(
                generateTransactionHashHexEncoded(
                        TransactionEncoderTest.createContractTransaction(),
                        (byte) 1,
                        SampleKeys.CREDENTIALS),
                is("0x568c7f6920c1cee8332e245c473657b9c53044eb96ed7532f5550f1139861e9e"));
    }
}
