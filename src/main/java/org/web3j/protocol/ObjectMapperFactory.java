package org.web3j.protocol;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.module.SimpleModule;

import org.web3j.protocol.core.Response;
import org.web3j.protocol.deserializer.ResponseRawMessageDeserializer;

/**
 * Factory for managing our ObjectMapper instances.
 */
public class ObjectMapperFactory {

    private static final ObjectMapper DEFAULT_OBJECT_MAPPER = new ObjectMapper();

    static {
        configureObjectMapper(DEFAULT_OBJECT_MAPPER, false);
    }

    public static ObjectMapper getObjectMapper() {
        return getObjectMapper(false);
    }

    public static ObjectMapper getObjectMapper(boolean shouldIncludeRawResponses) {
        if (!shouldIncludeRawResponses) {
            return DEFAULT_OBJECT_MAPPER;
        }

        return configureObjectMapper(new ObjectMapper(), true);
    }

    public static ObjectReader getObjectReader() {
        return DEFAULT_OBJECT_MAPPER.reader();
    }

    private static ObjectMapper configureObjectMapper(
            ObjectMapper objectMapper, boolean shouldIncludeRawResponses) {
        if (shouldIncludeRawResponses) {
            SimpleModule module = new SimpleModule();
            module.setDeserializerModifier(new BeanDeserializerModifier() {
                @Override
                public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config,
                                                              BeanDescription beanDesc,
                                                              JsonDeserializer<?> deserializer) {
                    if (Response.class.isAssignableFrom(beanDesc.getBeanClass())) {
                        return new ResponseRawMessageDeserializer(deserializer);
                    }

                    return deserializer;
                }
            });

            objectMapper.registerModule(module);
        }

        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return objectMapper;
    }
}
