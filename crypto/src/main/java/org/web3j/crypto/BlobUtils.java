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

import org.web3j.abi.datatypes.Bytes;

public class BlobUtils {

    private static final byte blobCommitmentVersionKZG = 0x01;
    private final Blob blobData;

    public BlobUtils(String trustedSetupFile, Blob blobData) {
        CKZG4844JNI.loadNativeLibrary();

        CKZG4844JNI.loadTrustedSetup(trustedSetupFile);
        this.blobData = blobData;
    }

    public Bytes getCommitment() {
        return new Bytes(48, CKZG4844JNI.blobToKzgCommitment(blobData.data.getValue()));
    }

    public Bytes getProof(Bytes commitment) {
        return new Bytes(
                48,
                CKZG4844JNI.computeBlobKzgProof(blobData.data.getValue(), commitment.getValue()));
    }

    public boolean checkProofValidity(byte[] commitment, byte[] proof) {
        return CKZG4844JNI.verifyBlobKzgProof(blobData.data.getValue(), commitment, proof);
    }

    public Bytes kzgToVersionedHash(Bytes commitment) {
        byte[] hash = Hash.sha256(commitment.getValue());
        hash[0] = blobCommitmentVersionKZG;
        return new Bytes(32, hash);
    }
}
