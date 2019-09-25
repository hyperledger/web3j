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
import org.web3j.utils.Base64String;
import org.web3j.utils.Numeric;

import static org.junit.Assert.assertEquals;
import static org.web3j.utils.Restriction.RESTRICTED;

public class PrivateTransactionEncoderTest {

    private static final Base64String MOCK_ENCLAVE_KEY =
            Base64String.wrap("A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=");
    private static final Base64String MOCK_ENCLAVE_KEY_2 =
            Base64String.wrap("Ko2bVqD+nNlNYL5EE7y3IdOnviftjiizpjRt+HTuFBs=");
    private static final Base64String MOCK_PRIVACY_GROUP_ID =
            Base64String.wrap("DyAOiF/ynpc+JXa2YAGB0bCitSlOMNm+ShmB/7M6C4w=");
    private static final List<Base64String> MOCK_PRIVATE_FOR =
            Arrays.asList(MOCK_ENCLAVE_KEY, MOCK_ENCLAVE_KEY_2);

    @Test
    public void testSignLegacyTransaction() {
        final String expected =
                "0xf8d4808203e8832dc6c094627306090abab3a6e1400e9345bc60c78a8bef578080820fe8a0e0b547d71d7a23d52382288b3a2a5a1610e0b504c404cc5009d7ada97d9015b2a076e997a83856d876fa2397b74510890eea3b73ffeda33daa4188120dac42d62fa0035695b4cc4b0941e60551d7a19cf30603db5bfc23e5ac43a56f57f25f75486af842a0035695b4cc4b0941e60551d7a19cf30603db5bfc23e5ac43a56f57f25f75486aa02a8d9b56a0fe9cd94d60be4413bcb721d3a7be27ed8e28b3a6346df874ee141b8a72657374726963746564";
        final RawPrivateTransaction privateTransactionCreation =
                new RawPrivateTransaction(
                        BigInteger.ZERO,
                        BigInteger.valueOf(1000),
                        BigInteger.valueOf(3000000),
                        "0x627306090abab3a6e1400e9345bc60c78a8bef57",
                        "0x",
                        MOCK_ENCLAVE_KEY,
                        MOCK_PRIVATE_FOR,
                        null,
                        RESTRICTED);
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

    @Test
    public void testSignBesuTransaction() {
        final String expected =
                "0xf8b1808203e8832dc6c094627306090abab3a6e1400e9345bc60c78a8bef578080820fe7a060c70c3f989ef5459021142959f8fc1ad6e5fe8542cf238484c6d6b8c8a6dbcca075727642ce691c4bf5ae945523cdd172d44b451ddfe11ae67c376f1e5c7069eea0035695b4cc4b0941e60551d7a19cf30603db5bfc23e5ac43a56f57f25f75486aa00f200e885ff29e973e2576b6600181d1b0a2b5294e30d9be4a1981ffb33a0b8c8a72657374726963746564";
        final RawPrivateTransaction privateTransactionCreation =
                new RawPrivateTransaction(
                        BigInteger.ZERO,
                        BigInteger.valueOf(1000),
                        BigInteger.valueOf(3000000),
                        "0x627306090abab3a6e1400e9345bc60c78a8bef57",
                        "0x",
                        MOCK_ENCLAVE_KEY,
                        null,
                        MOCK_PRIVACY_GROUP_ID,
                        RESTRICTED);
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
