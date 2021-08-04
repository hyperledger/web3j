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
import java.util.List;

import org.junit.jupiter.api.Test;

import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Numeric;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("deprecation")
public class TransactionEncoderTest {

    @Test
    public void testSignMessage() {
        byte[] signedMessage =
                TransactionEncoder.signMessage(createEtherTransaction(), SampleKeys.CREDENTIALS);
        String hexMessage = Numeric.toHexString(signedMessage);
        assertEquals(
                hexMessage,
                ("0xf85580010a840add5355887fffffffffffffff80"
                        + "1c"
                        + "a046360b50498ddf5566551ce1ce69c46c565f1f478bb0ee680caf31fbc08ab727"
                        + "a01b2f1432de16d110407d544f519fc91b84c8e16d3b6ec899592d486a94974cd0"));
    }

    @Test
    public void testEtherTransactionAsRlpValues() {
        List<RlpType> rlpStrings =
                TransactionEncoder.asRlpValues(
                        createEtherTransaction(),
                        new Sign.SignatureData((byte) 0, new byte[32], new byte[32]));
        assertEquals(rlpStrings.size(), (9));
        assertEquals(rlpStrings.get(3), (RlpString.create(new BigInteger("add5355", 16))));
    }

    @Test
    public void testContractAsRlpValues() {
        List<RlpType> rlpStrings =
                TransactionEncoder.asRlpValues(createContractTransaction(), null);
        assertEquals(rlpStrings.size(), (6));
        assertEquals(rlpStrings.get(3), (RlpString.create("")));
    }

    @Test
    public void testEip155Encode() {
        assertArrayEquals(
                TransactionEncoder.encode(createEip155RawTransaction(), (byte) 1),
                (Numeric.hexStringToByteArray(
                        "0xec098504a817c800825208943535353535353535353535353535353535353535880de0"
                                + "b6b3a764000080018080")));
    }

    @Test
    public void testEip155Transaction() {
        // https://github.com/ethereum/EIPs/issues/155
        Credentials credentials =
                Credentials.create(
                        "0x4646464646464646464646464646464646464646464646464646464646464646");

        assertArrayEquals(
                TransactionEncoder.signMessage(createEip155RawTransaction(), (byte) 1, credentials),
                (Numeric.hexStringToByteArray(
                        "0xf86c098504a817c800825208943535353535353535353535353535353535353535880"
                                + "de0b6b3a76400008025a028ef61340bd939bc2195fe537567866003e1a15d"
                                + "3c71ff63e1590620aa636276a067cbe9d8997f761aecb703304b3800ccf55"
                                + "5c9f3dc64214b297fb1966a3b6d83")));
    }

    @Test
    public void testEip155TransactionWithLargeChainId() {
        // https://github.com/ethereum/EIPs/issues/155
        Credentials credentials =
                Credentials.create(
                        "0x4646464646464646464646464646464646464646464646464646464646464646");

        assertArrayEquals(
                TransactionEncoder.signMessage(
                        createEip155RawTransaction(), Long.MAX_VALUE, credentials),
                (Numeric.hexStringToByteArray(
                        "0xf875098504a817c800825208943535353535353535353535353535353535353535880de0b6b3a76400008089010000000000000021a0ed14bd16ddd7788623f4439db3ddbc8bf548c241c3af87819c187a638ef40e17a03b4972ee3adb77b6b06784d12fe098c2cb84c03afd79d17b1caf8f63483101f0")));
    }

    @Test
    public void testEip1559Transaction() {
        // https://github.com/ethereum/EIPs/blob/master/EIPS/eip-1559.md
        Credentials credentials =
                Credentials.create(
                        "0x4646464646464646464646464646464646464646464646464646464646464646");
        assertArrayEquals(
                TransactionEncoder.signMessage(createEip1559RawTransaction(), credentials),
                (Numeric.hexStringToByteArray(
                        "02f8698206178082162e8310c8e082753094627306090abab3a6e1400e9345bc60c78a8bef577b80c001a0d1f9ee3bdde4d4e0792c7089b84059fb28e17f494556d8a775450b1dd6c318a1a038bd3e2fb9e018528e0a41f57c7a32a8d23b2693e0451aa6ef4519b234466e7f")));

        assertArrayEquals(
                TransactionEncoder.signMessage(createEip1559RawTransaction(), 1559L, credentials),
                (Numeric.hexStringToByteArray(
                        "02f8698206178082162e8310c8e082753094627306090abab3a6e1400e9345bc60c78a8bef577b80c001a0d1f9ee3bdde4d4e0792c7089b84059fb28e17f494556d8a775450b1dd6c318a1a038bd3e2fb9e018528e0a41f57c7a32a8d23b2693e0451aa6ef4519b234466e7f")));
    }

    private static RawTransaction createEtherTransaction() {
        return RawTransaction.createEtherTransaction(
                BigInteger.ZERO,
                BigInteger.ONE,
                BigInteger.TEN,
                "0xadd5355",
                BigInteger.valueOf(Long.MAX_VALUE));
    }

    static RawTransaction createContractTransaction() {
        return RawTransaction.createContractTransaction(
                BigInteger.ZERO,
                BigInteger.ONE,
                BigInteger.TEN,
                BigInteger.valueOf(Long.MAX_VALUE),
                "01234566789");
    }

    private static RawTransaction createEip155RawTransaction() {
        return RawTransaction.createEtherTransaction(
                BigInteger.valueOf(9),
                BigInteger.valueOf(20000000000L),
                BigInteger.valueOf(21000),
                "0x3535353535353535353535353535353535353535",
                BigInteger.valueOf(1000000000000000000L));
    }

    private static RawTransaction createEip1559RawTransaction() {
        return RawTransaction.createEtherTransaction(
                1559L,
                BigInteger.valueOf(0),
                BigInteger.valueOf(30000),
                "0x627306090abaB3A6e1400e9345bC60c78a8BEf57",
                BigInteger.valueOf(123),
                BigInteger.valueOf(5678),
                BigInteger.valueOf(1100000));
    }
}
