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
package org.web3j.protocol.deserializer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import org.web3j.protocol.core.Response;

/** A jackson deserializer that sets the rawResponse variable of Response objects. */
public class RawResponseDeserializer extends StdDeserializer<Response>
        implements ResolvableDeserializer {

    private final JsonDeserializer<?> defaultDeserializer;

    public RawResponseDeserializer(JsonDeserializer<?> defaultDeserializer) {
        super(Response.class);
        this.defaultDeserializer = defaultDeserializer;
    }

    @Override
    public Response deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        Response deserializedResponse = (Response) defaultDeserializer.deserialize(jp, ctxt);

        deserializedResponse.setRawResponse(getRawResponse(jp));
        return deserializedResponse;
    }

    // Must implement ResolvableDeserializer when modifying BeanDeserializer
    // otherwise deserializing throws JsonMappingException
    @Override
    public void resolve(DeserializationContext ctxt) throws JsonMappingException {
        ((ResolvableDeserializer) defaultDeserializer).resolve(ctxt);
    }

    private String getRawResponse(JsonParser jp) throws IOException {
        final InputStream inputSource = (InputStream) jp.getInputSource();

        if (inputSource == null) {
            return "";
        }

        inputSource.reset();

        return streamToString(inputSource);
    }

    private String streamToString(InputStream input) throws IOException {
        return new Scanner(input, StandardCharsets.UTF_8.name()).useDelimiter("\\Z").next();
    }
}
