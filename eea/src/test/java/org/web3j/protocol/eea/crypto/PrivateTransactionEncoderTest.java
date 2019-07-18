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
package org.web3j.protocol.eea.crypto;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import org.web3j.crypto.Credentials;
import org.web3j.utils.Numeric;

import static org.junit.Assert.assertEquals;

public class PrivateTransactionEncoderTest {

    @Test
    public void testSignLegacyTransaction() {
        //CHECKSTYLE:OFF
        final String expected =
            "0xf8f8808203e8832dc6c094627306090abab3a6e1400e9345bc60c78a8bef578080820fe7a0bc57fcf71721cf4c61ea8299af7bbfe77208791272f282ef9248037595d4456ea0294abde80c037090b9b7b7821170b4ae3b95da3b91465ba1c4f6418df2b7da9dac41316156744d784c4355486d425648586f5a7a7a42675062572f776a3561784470573958386c393153476f3df85aac41316156744d784c4355486d425648586f5a7a7a42675062572f776a3561784470573958386c393153476f3dac4b6f32625671442b6e4e6c4e594c35454537793349644f6e766966746a69697a706a52742b4854754642733d8a72657374726963746564";
        //CHECKSTYLE:ON
        final String privateFrom = "A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=";
        final List<String> privateFor = Arrays.asList(
                "A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=",
                "Ko2bVqD+nNlNYL5EE7y3IdOnviftjiizpjRt+HTuFBs=");
        final RawPrivateTransaction privateTransactionCreation = new RawPrivateTransaction(
                BigInteger.ZERO,
                BigInteger.valueOf(1000),
                BigInteger.valueOf(3000000),
                "0x627306090abab3a6e1400e9345bc60c78a8bef57",
                "0x",
                privateFrom,
                privateFor,
                null,
                "restricted"
        );
        final long chainId = 2018;
        final String privateKey =
                "8f2a55949038a9610f50fb23b5883af3b4ecb3c3bb792cbcefbd1542c692be63";
        final Credentials credentials = Credentials.create(privateKey);
        final String privateRawTransaction = Numeric.toHexString(
                PrivateTransactionEncoder.signMessage(
                        privateTransactionCreation, chainId, credentials));

        assertEquals(expected, privateRawTransaction);
    }

    @Test
    public void testSignPantheonTransaction() {
        //CHECKSTYLE:OFF
        final String expected =
                "0xf8c9808203e8832dc6c094627306090abab3a6e1400e9345bc60c78a8bef578080820fe7a08ddaacba3a9b0b52aedadde7da2cbba32f89e8dc11e1f1996cf412a77c93b38ea02dd157058a80f443c810e77f5824e1dd5acdeceddababd1deba902deab71ae26ac41316156744d784c4355486d425648586f5a7a7a42675062572f776a3561784470573958386c393153476f3dac4479414f69462f796e70632b4a586132594147423062436974536c4f4d4e6d2b53686d422f374d364334773d8a72657374726963746564";
        //CHECKSTYLE:ON
        final String privateFrom = "A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=";
        final String privacyGroupId = "DyAOiF/ynpc+JXa2YAGB0bCitSlOMNm+ShmB/7M6C4w=";
        final RawPrivateTransaction privateTransactionCreation = new RawPrivateTransaction(
                BigInteger.ZERO,
                BigInteger.valueOf(1000),
                BigInteger.valueOf(3000000),
                "0x627306090abab3a6e1400e9345bc60c78a8bef57",
                "0x",
                privateFrom,
                        null,
                        privacyGroupId,
                        "restricted"
        );
        final long chainId = 2018;
        final String privateKey =
                "8f2a55949038a9610f50fb23b5883af3b4ecb3c3bb792cbcefbd1542c692be63";
        final Credentials credentials = Credentials.create(privateKey);
        final String privateRawTransaction = Numeric.toHexString(
                PrivateTransactionEncoder.signMessage(
                        privateTransactionCreation, chainId, credentials));

        assertEquals(expected, privateRawTransaction);
    }

    @Test
    public void testSignPantheonTransactionWithoutPrivateFrom() {
        //CHECKSTYLE:OFF
        final String expected =
                "0xf89c808203e8832dc6c094627306090abab3a6e1400e9345bc60c78a8bef578080820fe7a05fc917fe3335e0ff8bf39d243ccb6c97d0277c47524947f746d7d5cb5de97eafa069bb947308726359d8211eb1990990f2c13d1659222621a493f84fdec6ec04faac4479414f69462f796e70632b4a586132594147423062436974536c4f4d4e6d2b53686d422f374d364334773d8a72657374726963746564";
        //CHECKSTYLE:ON
        final String privacyGroupId = "DyAOiF/ynpc+JXa2YAGB0bCitSlOMNm+ShmB/7M6C4w=";
        final RawPrivateTransaction privateTransactionCreation = new RawPrivateTransaction(
                BigInteger.ZERO,
                        BigInteger.valueOf(1000),
                BigInteger.valueOf(3000000),
                "0x627306090abab3a6e1400e9345bc60c78a8bef57",
                "0x",
                null,
                null,
                privacyGroupId,
                        "restricted");
        final long chainId = 2018;
        final String privateKey =
                "8f2a55949038a9610f50fb23b5883af3b4ecb3c3bb792cbcefbd1542c692be63";
        final Credentials credentials = Credentials.create(privateKey);
        final String privateRawTransaction =
                Numeric.toHexString(
                        PrivateTransactionEncoder.signMessage(
                                privateTransactionCreation, chainId, credentials));

        assertEquals(expected, privateRawTransaction);
    }
}
