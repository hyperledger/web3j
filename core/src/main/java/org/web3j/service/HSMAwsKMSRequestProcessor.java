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
package org.web3j.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.MessageType;
import software.amazon.awssdk.services.kms.model.SignRequest;
import software.amazon.awssdk.services.kms.model.SigningAlgorithmSpec;
import software.amazon.awssdk.services.kms.model.VerifyRequest;

import org.web3j.crypto.CryptoUtils;
import org.web3j.crypto.HSMPass;
import org.web3j.crypto.Sign;

/**
 * HSM request processor for AWS KMS. Notice the KMS key must be ECC_SECG_P256K1, this key is
 * supported in crypto space.
 */
public class HSMAwsKMSRequestProcessor implements HSMRequestProcessor {

    private KmsClient kmsClient;
    private String keyID;

    public HSMAwsKMSRequestProcessor(KmsClient kmsClient, String keyID) {
        this.kmsClient = kmsClient;
        this.keyID = keyID;
    }

    /**
     * Entry point method which creates the KMS sign request
     *
     * @param dataToSign - data to be signed
     * @param pass - public key of the asymmetric KMS key pair used for signing The @{@link
     *     org.web3j.crypto.HSMPass} should be instantiated before this method call. Use the
     *     following code for getting and setting the public key:
     *     <p>byte[] rawPublicKey = KmsClient.create() .getPublicKey((var builder) -> {
     *     builder.keyId(kmsKeyId); }) .publicKey() .asByteArray();
     *     <p>byte[] publicKey = SubjectPublicKeyInfo .getInstance(rawPublicKey) .getPublicKeyData()
     *     .getBytes();
     *     <p>BigInteger publicKey = new BigInteger(1, Arrays.copyOfRange(publicKey, 1,
     *     publicKey.length));
     *     <p>HSMPass pass = new HSMPass(null, publicKey);
     * @return SignatureData v | r | s
     */
    @Override
    public Sign.SignatureData callHSM(byte[] dataToSign, HSMPass pass) {
        byte[] dataHash = new byte[0];
        try {
            dataHash = MessageDigest.getInstance("SHA-256").digest(dataToSign);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(
                    "Algorithm SHA-256 is not available for the given data!");
        }

        // Create the SignRequest for AWS KMS
        var signRequest =
                SignRequest.builder()
                        .keyId(keyID)
                        .message(SdkBytes.fromByteArray(dataHash))
                        .messageType(MessageType.DIGEST)
                        .signingAlgorithm(SigningAlgorithmSpec.ECDSA_SHA_256)
                        .build();

        // Sign the data using AWS KMS
        var signResult = kmsClient.sign(signRequest);
        var signatureBuffer = signResult.signature().asByteBuffer();

        // Convert the signature to byte array
        var signBytes = new byte[signatureBuffer.remaining()];
        signatureBuffer.get(signBytes);

        // Verify signature on KMS
        var verifyRequest =
                VerifyRequest.builder()
                        .keyId(keyID)
                        .message(SdkBytes.fromByteArray(dataHash))
                        .messageType(MessageType.DIGEST)
                        .signingAlgorithm(SigningAlgorithmSpec.ECDSA_SHA_256)
                        .signature(SdkBytes.fromByteArray(signBytes))
                        .build();

        var verifyRequestResult = kmsClient.verify(verifyRequest);
        if (!verifyRequestResult.signatureValid()) {
            throw new RuntimeException("KMS signature is not valid!");
        }

        var signature = CryptoUtils.fromDerFormat(signBytes);
        return Sign.createSignatureData(signature, pass.getPublicKey(), dataHash);
    }
}
