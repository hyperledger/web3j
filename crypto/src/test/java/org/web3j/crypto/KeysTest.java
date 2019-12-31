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
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import org.web3j.utils.Numeric;
import org.web3j.utils.Strings;

import static org.junit.jupiter.api.Assertions.*;

public class KeysTest {

    private static final byte[] ENCODED;

    static {
        byte[] privateKey = Numeric.hexStringToByteArray(SampleKeys.PRIVATE_KEY_STRING);
        byte[] publicKey = Numeric.hexStringToByteArray(SampleKeys.PUBLIC_KEY_STRING);
        ENCODED = Arrays.copyOf(privateKey, privateKey.length + publicKey.length);
        System.arraycopy(publicKey, 0, ENCODED, privateKey.length, publicKey.length);
    }

    @Test
    public void testCreateSecp256k1KeyPair() throws Exception {
        KeyPair keyPair = Keys.createSecp256k1KeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        assertNotNull(privateKey);
        assertNotNull(publicKey);

        assertEquals(privateKey.getEncoded().length, (144));
        assertEquals(publicKey.getEncoded().length, (88));
    }

    @Test
    public void testCreateEcKeyPair() throws Exception {
        ECKeyPair ecKeyPair = Keys.createEcKeyPair();
        assertEquals(ecKeyPair.getPublicKey().signum(), (1));
        assertEquals(ecKeyPair.getPrivateKey().signum(), (1));
    }

    @Test
    public void testGetAddressString() {
        assertEquals(Keys.getAddress(SampleKeys.PUBLIC_KEY_STRING), (SampleKeys.ADDRESS_NO_PREFIX));
    }

    @Test
    public void testGetAddressZeroPaddedAddress() {
        String publicKey =
                "0xa1b31be4d58a7ddd24b135db0da56a90fb5382077ae26b250e1dc9cd6232ce22"
                        + "70f4c995428bc76aa78e522316e95d7834d725efc9ca754d043233af6ca90113";
        assertEquals(Keys.getAddress(publicKey), ("01c52b08330e05d731e38c856c1043288f7d9744"));
    }

    @Test
    public void testGetAddressBigInteger() {
        assertEquals(Keys.getAddress(SampleKeys.PUBLIC_KEY), (SampleKeys.ADDRESS_NO_PREFIX));
    }

    @Test
    public void testGetAddressSmallPublicKey() {
        byte[] address =
                Keys.getAddress(
                        Numeric.toBytesPadded(BigInteger.valueOf(0x1234), Keys.PUBLIC_KEY_SIZE));
        String expected = Numeric.toHexStringNoPrefix(address);

        assertEquals(Keys.getAddress("0x1234"), (expected));
    }

    @Test
    public void testGetAddressZeroPadded() {
        byte[] address =
                Keys.getAddress(
                        Numeric.toBytesPadded(BigInteger.valueOf(0x1234), Keys.PUBLIC_KEY_SIZE));
        String expected = Numeric.toHexStringNoPrefix(address);

        String value = "1234";
        assertEquals(
                Keys.getAddress(
                        "0x"
                                + Strings.zeros(Keys.PUBLIC_KEY_LENGTH_IN_HEX - value.length())
                                + value),
                (expected));
    }

    @Test
    public void testToChecksumAddress() {
        // Test cases as per https://github.com/ethereum/EIPs/blob/master/EIPS/eip-55.md#test-cases

        assertEquals(
                Keys.toChecksumAddress("0xfb6916095ca1df60bb79ce92ce3ea74c37c5d359"),
                ("0xfB6916095ca1df60bB79Ce92cE3Ea74c37c5d359"));

        // All uppercase
        assertEquals(
                Keys.toChecksumAddress("0x52908400098527886E0F7030069857D2E4169EE7"),
                ("0x52908400098527886E0F7030069857D2E4169EE7"));
        assertEquals(
                Keys.toChecksumAddress("0x8617E340B3D01FA5F11F306F4090FD50E238070D"),
                ("0x8617E340B3D01FA5F11F306F4090FD50E238070D"));

        // All lowercase
        assertEquals(
                Keys.toChecksumAddress("0xde709f2102306220921060314715629080e2fb77"),
                ("0xde709f2102306220921060314715629080e2fb77"));
        assertEquals(
                Keys.toChecksumAddress("0x27b1fdb04752bbc536007a920d24acb045561c26"),
                ("0x27b1fdb04752bbc536007a920d24acb045561c26"));

        // Normal
        assertEquals(
                Keys.toChecksumAddress("0x5aAeb6053F3E94C9b9A09f33669435E7Ef1BeAed"),
                ("0x5aAeb6053F3E94C9b9A09f33669435E7Ef1BeAed"));
        assertEquals(
                Keys.toChecksumAddress("0xfB6916095ca1df60bB79Ce92cE3Ea74c37c5d359"),
                ("0xfB6916095ca1df60bB79Ce92cE3Ea74c37c5d359"));
        assertEquals(
                Keys.toChecksumAddress("0xdbF03B407c01E7cD3CBea99509d93f8DDDC8C6FB"),
                ("0xdbF03B407c01E7cD3CBea99509d93f8DDDC8C6FB"));
        assertEquals(
                Keys.toChecksumAddress("0xD1220A0cf47c7B9Be7A2E6BA89F429762e7b9aDb"),
                ("0xD1220A0cf47c7B9Be7A2E6BA89F429762e7b9aDb"));
    }

    @Test
    public void testSerializeECKey() {
        assertArrayEquals(Keys.serialize(SampleKeys.KEY_PAIR), (ENCODED));
    }

    @Test
    public void testDeserializeECKey() {
        assertEquals(Keys.deserialize(ENCODED), (SampleKeys.KEY_PAIR));
    }

    @Test
    public void testDeserializeInvalidKey() {
        assertThrows(RuntimeException.class, () -> Keys.deserialize(new byte[0]));
    }
}
