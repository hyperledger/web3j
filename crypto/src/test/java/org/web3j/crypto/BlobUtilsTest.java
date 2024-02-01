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

import ethereum.ckzg4844.CKZG4844JNI;
import org.apache.tuweni.units.bigints.UInt256;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.SecureRandom;
import java.util.Arrays;

import static java.util.stream.IntStream.range;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BlobUtilsTest {
    private static final SecureRandom RANDOM = new SecureRandom();

        @Test
        public void testBlobToCommitmentProofVersionedHashes() {
            BlobUtils.loadTrustedSetupFromResource();
            Blob blob = new Blob(createRandomBlob());
            byte[] commitment = CKZG4844JNI.blobToKzgCommitment(blob.getData().toArray());
            byte[] proofs = CKZG4844JNI.computeBlobKzgProof(blob.getData().toArray(), commitment);
            System.out.println("commitments = " + Arrays.toString(commitment));
            System.out.println("proofs = " + Arrays.toString(proofs));
            BlobUtils.freeTrustedSetup();
        }

        @Test
        public void testBlobUtils() {
            BlobUtils.loadTrustedSetupFromResource();
            Blob blob = new Blob(createRandomBlob());
            System.out.println("blob = " + blob.getData());
            BlobUtils blobUtils = new BlobUtils(blob);
            System.out.println("commitments = " + blobUtils.getCommitment());
            System.out.println("proofs = " + blobUtils.getProof(blobUtils.getCommitment()));
            System.out.println(
                    "versioned Hashes = " +
     blobUtils.kzgToVersionedHash(blobUtils.getCommitment()));
            BlobUtils.freeTrustedSetup();
        }

    private static UInt256 randomBLSFieldElement() {
        final BigInteger attempt = new BigInteger(CKZG4844JNI.BLS_MODULUS.bitLength(), RANDOM);
        if (attempt.compareTo(CKZG4844JNI.BLS_MODULUS) < 0) {
            return UInt256.valueOf(attempt);
        }
        return randomBLSFieldElement();
    }

    public static byte[] flatten(final byte[]... bytes) {
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
}
