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
package org.web3j.utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;

@JsonSerialize(using = Base64String.Serializer.class)
@JsonDeserialize(using = Base64String.Deserializer.class)
public class Base64String {

    public static class Serializer extends JsonSerializer {
        @Override
        public void serialize(
                final Object value, JsonGenerator gen, final SerializerProvider serializers)
                throws IOException {
            gen.writeString(value.toString());
        }
    }

    public static class Deserializer extends JsonDeserializer {
        @Override
        public Object deserialize(final JsonParser p, final DeserializationContext ctxt)
                throws IOException {
            return Base64String.wrap(p.getText());
        }
    }

    private static final Base64.Decoder DECODER = Base64.getDecoder();
    private static final Base64.Encoder ENCODER = Base64.getEncoder();

    private final byte[] enclaveB64Value = new byte[32];

    public static Base64String wrap(final String base64String) {
        return new Base64String(base64String);
    }

    public static Base64String wrap(final byte[] base64Array) {
        return new Base64String(ENCODER.encodeToString(base64Array));
    }

    public static List<Base64String> wrapList(final List<String> base64Strings) {
        return base64Strings.stream().map(Base64String::wrap).collect(Collectors.toList());
    }

    public static List<Base64String> wrapList(final String... base64Strings) {
        return Arrays.stream(base64Strings).map(Base64String::wrap).collect(Collectors.toList());
    }

    public static List<String> unwrapList(final List<Base64String> base64Strings) {
        return base64Strings.stream().map(Base64String::toString).collect(Collectors.toList());
    }

    public static RlpList unwrapListToRlp(final List<Base64String> base64Strings) {
        return new RlpList(
                base64Strings.stream().map(Base64String::asRlp).collect(Collectors.toList()));
    }

    private Base64String(final String base64String) {
        if (!isValid(base64String) || base64String.length() != 44) {
            throw new IllegalArgumentException(base64String + " is not a 32 byte base 64 value");
        }

        System.arraycopy(DECODER.decode(base64String), 0, enclaveB64Value, 0, 32);
    }

    public String toString() {
        return ENCODER.encodeToString(enclaveB64Value);
    }

    public byte[] raw() {
        return enclaveB64Value;
    }

    public RlpString asRlp() {
        return RlpString.create(raw());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Base64String that = (Base64String) o;
        return Arrays.equals(enclaveB64Value, that.enclaveB64Value);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(enclaveB64Value);
    }

    private boolean isValid(final String b64String) {
        //   ([0-9a-zA-Z+/]{4})*      # Groups of 4 valid characters decode
        //                            # to 24 bits of data for each group
        //   (                        # ending with:
        //   ([0-9a-zA-Z+/]{3}=)      # three valid characters followed by =
        return b64String.matches("(?:[A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=)");
    }
}
