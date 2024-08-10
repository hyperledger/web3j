/*
 * Copyright 2024 Web3 Labs Ltd.
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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.web3j.crypto.SampleKeys;
import org.web3j.protocol.Web3j;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class FastRawTransactionManagerTest {
    private Web3j web3j;
    private FastRawTransactionManager fastRawTransactionManager;

    @BeforeEach
    public void setUp() throws Exception {
        web3j = mock(Web3j.class);
        fastRawTransactionManager = new FastRawTransactionManager(web3j, SampleKeys.CREDENTIALS);
    }

    @Test
    void clearNonce() throws IOException {
        fastRawTransactionManager.setNonce(BigInteger.valueOf(42));

        fastRawTransactionManager.clearNonce();

        BigInteger currentNonce = fastRawTransactionManager.getCurrentNonce();
        assertEquals(currentNonce, BigInteger.valueOf(-1));
    }
}
