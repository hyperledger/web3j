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

import java.math.BigInteger;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bouncycastle.math.ec.ECPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import org.web3j.utils.Numeric;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.web3j.crypto.Sign.CHAIN_ID_INC;
import static org.web3j.crypto.Sign.LOWER_REAL_V;
import static org.web3j.crypto.Sign.REPLAY_PROTECTED_V_MIN;

public class SignR1Test {

    private static final byte[] TEST_MESSAGE = "A test message".getBytes();

    @Test
    public void testSignMessage() {
        Sign.SignatureData signatureData =
                Sign.signPrefixedMessage(TEST_MESSAGE, SampleR1Keys.KEY_PAIR);

        Sign.SignatureData expected =
                new Sign.SignatureData(
                        (byte) 27,
                        Numeric.hexStringToByteArray(
                                "0x5384eea41f6a9fe7a45740b276fc827dc5c3824c9e86c46b539a6e96eb7ea9ba"),
                        Numeric.hexStringToByteArray(
                                "0x7ffc6e1691190524865d97ca45cf528ee8fe6840ba282e9941cc8c39b759704e"));

        assertEquals(signatureData, (expected));
    }

    @Test
    public void testSignedMessageToKey() throws SignatureException {
        Sign.SignatureData signatureData =
                Sign.signPrefixedMessage(TEST_MESSAGE, SampleR1Keys.KEY_PAIR);
        BigInteger key = Sign.signedPrefixedMessageToR1Key(TEST_MESSAGE, signatureData);
        assertEquals(key, (SampleR1Keys.PUBLIC_KEY));
    }

    @Test
    public void testPublicKeyFromPrivateKey() {
        assertEquals(
                Sign.publicKeyFromPrivate(SampleR1Keys.PRIVATE_KEY, Sign.NIST_CURVE),
                (SampleR1Keys.PUBLIC_KEY));
    }

    @Test
    public void testInvalidSignature() {

        assertThrows(
                RuntimeException.class,
                () ->
                        Sign.signedMessageToKey(
                                TEST_MESSAGE,
                                new Sign.SignatureData((byte) 27, new byte[] {1}, new byte[] {0})));
    }

    @Test
    public void testPublicKeyFromPrivatePoint() {
        ECPoint point = Sign.publicPointFromPrivate(SampleR1Keys.PRIVATE_KEY, Sign.NIST_CURVE);
        assertEquals(Sign.publicFromPoint(point.getEncoded(false)), (SampleR1Keys.PUBLIC_KEY));
    }

    @ParameterizedTest(name = "testGetRecId(chainId={0}, recId={1}, isEip155={2})")
    @MethodSource("recIdArguments")
    public void testGetRecId(final long chainId, final long recId, final boolean isEip155) {
        final long testV = isEip155 ? CHAIN_ID_INC + chainId * 2 + recId : LOWER_REAL_V + recId;
        final Sign.SignatureData signedMsg =
                new Sign.SignatureData((byte) testV, new byte[] {}, new byte[] {});

        int recoveredRecId = Sign.getRecId(signedMsg, chainId);
        assertEquals(recId, recoveredRecId);
    }

    @ParameterizedTest(name = "testGetRecIdWithInvalidVParam(v = {0})")
    @MethodSource("invalidVSigningParams")
    public void testGetRecIdWithInvalidVParam(final long invalidV) {
        final Sign.SignatureData signedMsg =
                new Sign.SignatureData((byte) invalidV, new byte[] {}, new byte[] {});

        assertThrows(
                IllegalArgumentException.class,
                () -> Sign.getRecId(signedMsg, 1),
                "Unsupported v parameter: " + invalidV);
    }

    public static List<Arguments> recIdArguments() {
        final List<Long> chainIds = Arrays.asList(1L, 2L, 100L);
        final List<Long> recIds = Arrays.asList(0L, 1L);
        final List<Boolean> isEip155Options = Arrays.asList(true, false);

        final List<Arguments> args = new ArrayList<>();
        for (Long chainId : chainIds) {
            for (Long recId : recIds) {
                for (Boolean isEip155Option : isEip155Options) {
                    args.add(Arguments.of(chainId, recId, isEip155Option));
                }
            }
        }

        return args;
    }

    public static List<Arguments> invalidVSigningParams() {
        final List<Arguments> args = new ArrayList<>();
        for (int i = 0; i < LOWER_REAL_V; i++) {
            args.add(Arguments.of(i));
        }
        for (int i = LOWER_REAL_V + 2; i < REPLAY_PROTECTED_V_MIN; i++) {
            args.add(Arguments.of(i));
        }

        return args;
    }
}
