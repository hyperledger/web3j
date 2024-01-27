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

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import ethereum.ckzg4844.CKZG4844JNI;

import org.web3j.abi.datatypes.Bytes;

public class BlobUtils {
    public static boolean libraryLoaded = false;
    private static final byte blobCommitmentVersionKZG = 0x01;
    private final Blob blobData;

    public BlobUtils(Blob blobData) {
        if (!libraryLoaded) {
            throw new RuntimeException(
                    "Please load CKZG4844JNI Library by running BlobUtils.loadTrustedSetupFromResource()");
        }
        this.blobData = blobData;
    }

    public static void loadTrustedSetupFromResource() {
        CKZG4844JNI.loadNativeLibrary();
        try (InputStream resourceStream =
                BlobUtils.class.getClassLoader().getResourceAsStream("trusted_setup.txt")) {
            if (resourceStream == null) {
                throw new IllegalArgumentException("Resource not found");
            }
            Path tempFile = Files.createTempFile("trusted_setup", "txt");
            Files.copy(resourceStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
            CKZG4844JNI.loadTrustedSetup(tempFile.toString());
            tempFile.toFile().deleteOnExit(); // Delete temp file when JVM exits
        } catch (Exception e) {
            throw new RuntimeException("Failed to load trusted setup from resource", e);
        }
        libraryLoaded = true;
    }

    public Bytes getCommitment() {
        return new Bytes(48, CKZG4844JNI.blobToKzgCommitment(blobData.data.getValue()));
    }

    public Bytes getProof(Bytes commitment) {
        return new Bytes(
                48,
                CKZG4844JNI.computeBlobKzgProof(blobData.data.getValue(), commitment.getValue()));
    }

    public boolean checkProofValidity(Bytes commitment, Bytes proof) {
        return CKZG4844JNI.verifyBlobKzgProof(
                blobData.data.getValue(), commitment.getValue(), proof.getValue());
    }

    public Bytes kzgToVersionedHash(Bytes commitment) {
        byte[] hash = Hash.sha256(commitment.getValue());
        hash[0] = blobCommitmentVersionKZG;
        return new Bytes(32, hash);
    }

    public static void freeTrustedSetup() {
        // the current trusted setup should be freed before a new one is loaded
        CKZG4844JNI.freeTrustedSetup();
        libraryLoaded = false;
    }
}
