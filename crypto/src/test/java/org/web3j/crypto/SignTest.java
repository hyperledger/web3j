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

import java.math.BigInteger;
import java.security.SignatureException;

import org.bouncycastle.math.ec.ECPoint;
import org.junit.Test;

import org.web3j.utils.Numeric;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class SignTest {

    private static final byte[] TEST_MESSAGE = "A test message".getBytes();

    @Test
    public void testSignMessage() {
        Sign.SignatureData signatureData =
                Sign.signPrefixedMessage(TEST_MESSAGE, SampleKeys.KEY_PAIR);

        Sign.SignatureData expected =
                new Sign.SignatureData(
                        (byte) 28,
                        Numeric.hexStringToByteArray(
                                "0x0464eee9e2fe1a10ffe48c78b80de1ed8dcf996f3f60955cb2e03cb21903d930"),
                        Numeric.hexStringToByteArray(
                                "0x06624da478b3f862582e85b31c6a21c6cae2eee2bd50f55c93c4faad9d9c8d7f"));

        assertThat(signatureData, is(expected));
    }

    @Test
    public void testSignedMessageToKey() throws SignatureException {
        Sign.SignatureData signatureData =
                Sign.signPrefixedMessage(TEST_MESSAGE, SampleKeys.KEY_PAIR);
        BigInteger key = Sign.signedPrefixedMessageToKey(TEST_MESSAGE, signatureData);
        assertThat(key, equalTo(SampleKeys.PUBLIC_KEY));
    }

    @Test
    public void testPublicKeyFromPrivateKey() {
        assertThat(
                Sign.publicKeyFromPrivate(SampleKeys.PRIVATE_KEY), equalTo(SampleKeys.PUBLIC_KEY));
    }

    @Test(expected = RuntimeException.class)
    public void testInvalidSignature() throws SignatureException {
        Sign.signedMessageToKey(
                TEST_MESSAGE, new Sign.SignatureData((byte) 27, new byte[] {1}, new byte[] {0}));
    }

    @Test
    public void testPublicKeyFromPrivatePoint() {
        ECPoint point = Sign.publicPointFromPrivate(SampleKeys.PRIVATE_KEY);
        assertThat(Sign.publicFromPoint(point.getEncoded(false)), equalTo(SampleKeys.PUBLIC_KEY));
    }
}
