/*
 * Copyright 2022 Web3 Labs Ltd.
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.DERSequenceGenerator;
import org.bouncycastle.asn1.DLSequence;

public class CryptoUtils {

    public static byte[] toDerFormat(ECDSASignature signature) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            DERSequenceGenerator seq = new DERSequenceGenerator(baos);
            seq.addObject(new ASN1Integer(signature.r));
            seq.addObject(new ASN1Integer(signature.s));
            seq.close();
            return baos.toByteArray();
        } catch (IOException ex) {
            return new byte[0];
        }
    }

    public static ECDSASignature fromDerFormat(byte[] bytes) {
        try (ASN1InputStream decoder = new ASN1InputStream(bytes)) {
            DLSequence seq = (DLSequence) decoder.readObject();
            if (seq == null) {
                throw new RuntimeException("Reached past end of ASN.1 stream.");
            }
            ASN1Integer r, s;
            try {
                r = (ASN1Integer) seq.getObjectAt(0);
                s = (ASN1Integer) seq.getObjectAt(1);
            } catch (ClassCastException e) {
                throw new IllegalArgumentException(e);
            }
            // OpenSSL deviates from the DER spec by interpreting these values
            // as unsigned, though they should not be
            // Thus, we always use the positive versions. See:
            // http://r6.ca/blog/20111119T211504Z.html
            return new ECDSASignature(r.getPositiveValue(), s.getPositiveValue());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
