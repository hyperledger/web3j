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
package org.web3j.crypto;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.stream.Collectors;

import ethereum.ckzg4844.CKZG4844JNI;
import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.units.bigints.UInt256;
import org.junit.jupiter.api.Test;

import org.web3j.utils.Numeric;

import static java.util.stream.IntStream.range;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BlobUtilsTest {
    private static final SecureRandom RANDOM = new SecureRandom();

    @Test
    public void testBlobToCommitmentProofVersionedHashes() throws Exception {
        CKZG4844JNI.loadNativeLibrary();
        CKZG4844JNI.loadTrustedSetupFromResource("/trusted_setup.txt", BlobUtils.class, 0);

        Blob blob =
                new Blob(
                        Numeric.hexStringToByteArray(
                                loadResourceAsString("blob_data/blob_data_1.txt")));
        byte[] commitment = CKZG4844JNI.blobToKzgCommitment(blob.getData().toArray());
        byte[] proofs = CKZG4844JNI.computeBlobKzgProof(blob.getData().toArray(), commitment);

        assertTrue(CKZG4844JNI.verifyBlobKzgProof(blob.data.toArray(), commitment, proofs));
        assertEquals(
                "0xc00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
                Numeric.toHexString(commitment));
        assertEquals(
                "0xc00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
                Numeric.toHexString(proofs));

        CKZG4844JNI.freeTrustedSetup();
    }

    @Test
    public void testBlobUtils() throws Exception {
        Blob blob =
                new Blob(
                        Numeric.hexStringToByteArray(
                                loadResourceAsString("blob_data/blob_data_2.txt")));
        Bytes commitment = BlobUtils.getCommitment(blob);
        Bytes proofs = BlobUtils.getProof(blob, commitment);
        Bytes versionedHashes = BlobUtils.kzgToVersionedHash(commitment);

        assertTrue(BlobUtils.checkProofValidity(blob, commitment, proofs));
        assertEquals(
                "0xb44bafc7381d7ba2072cfbb7091c1fa1fdabcf3999270a551fe54a6741ddebc1bdfbeeabe1b74f5c3935aeedf6b2db84",
                commitment.toHexString());
        assertEquals(
                "0x963150f3ee4d5e5f065429f587b4fa199cd8a866b8f6388eb52372870052603c98194c6521077c3260c41bf3b796c833",
                proofs.toHexString());
        assertEquals(
                "0x018ef96865998238a5e1783b6cafbc1253235d636f15d318f1fb50ef6a5b8f6a",
                versionedHashes.toHexString());
    }

    public static String loadResourceAsString(String filePath) throws Exception {
        try (InputStream inputStream =
                BlobUtilsTest.class.getClassLoader().getResourceAsStream(filePath)) {
            assert inputStream != null;
            try (BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        }
    }

    private static UInt256 zeroBLSFieldElement() {
        return UInt256.ZERO;
    }

    private static UInt256 randomBLSFieldElement() {
        final BigInteger attempt = new BigInteger(CKZG4844JNI.BLS_MODULUS.bitLength(), RANDOM);
        if (attempt.compareTo(CKZG4844JNI.BLS_MODULUS) < 0) {
            return UInt256.valueOf(attempt);
        }
        return randomBLSFieldElement();
    }

    private static byte[] flatten(final byte[]... bytes) {
        final int capacity = Arrays.stream(bytes).mapToInt(b -> b.length).sum();
        final ByteBuffer buffer = ByteBuffer.allocate(capacity);
        Arrays.stream(bytes).forEach(buffer::put);
        return buffer.array();
    }

    public static byte[] createRandomBlob() {
        final byte[][] blob =
                range(0, CKZG4844JNI.FIELD_ELEMENTS_PER_BLOB)
                        .mapToObj(__ -> randomBLSFieldElement())
                        .map(fieldElement -> fieldElement.toArray(ByteOrder.BIG_ENDIAN))
                        .toArray(byte[][]::new);
        return flatten(blob);
    }

    public static byte[] createZeroBlob() {
        final byte[][] blob =
                range(0, CKZG4844JNI.FIELD_ELEMENTS_PER_BLOB)
                        .mapToObj(__ -> zeroBLSFieldElement())
                        .map(fieldElement -> fieldElement.toArray(ByteOrder.BIG_ENDIAN))
                        .toArray(byte[][]::new);
        return flatten(blob);
    }
}
