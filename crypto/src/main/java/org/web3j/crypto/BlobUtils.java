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
import org.apache.tuweni.bytes.Bytes;

public class BlobUtils {

    private static final byte blobCommitmentVersionKZG = 0x01;
    private static final String trustedSetupFilePath = "/trusted_setup.txt";

    static {
        CKZG4844JNI.loadNativeLibrary();
        loadTrustedSetupParameters();
    }

    private static void loadTrustedSetupParameters() {
        CKZG4844JNI.loadTrustedSetupFromResource(
                BlobUtils.trustedSetupFilePath, BlobUtils.class, 0);
    }

    public static Bytes getCommitment(Blob blobData) {
        return Bytes.wrap(CKZG4844JNI.blobToKzgCommitment(blobData.data.toArray()));
    }

    public static Bytes getProof(Blob blobData, Bytes commitment) {
        return Bytes.wrap(
                CKZG4844JNI.computeBlobKzgProof(blobData.data.toArray(), commitment.toArray()));
    }

    public static boolean checkProofValidity(Blob blobData, Bytes commitment, Bytes proof) {
        return CKZG4844JNI.verifyBlobKzgProof(
                blobData.data.toArray(), commitment.toArray(), proof.toArray());
    }

    public static Bytes kzgToVersionedHash(Bytes commitment) {
        byte[] hash = Hash.sha256(commitment.toArray());
        hash[0] = blobCommitmentVersionKZG;
        return Bytes.wrap(hash);
    }
}
