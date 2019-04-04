package org.web3j.protocol.eea.response;

import java.io.IOException;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectReader;

import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.core.Response;

public class EeaGetTransactionReceipt extends Response<PrivateTransactionReceipt> {
    public Optional<PrivateTransactionReceipt> getTransactionReceipt() {
        return Optional.ofNullable(getResult());
    }

    public static class ResponseDeserialiser extends JsonDeserializer<PrivateTransactionReceipt> {

        private ObjectReader objectReader = ObjectMapperFactory.getObjectReader();

        @Override
        public PrivateTransactionReceipt deserialize(
                JsonParser jsonParser,
                DeserializationContext deserializationContext) throws IOException {
            if (jsonParser.getCurrentToken() != JsonToken.VALUE_NULL) {
                return objectReader.readValue(jsonParser, PrivateTransactionReceipt.class);
            } else {
                return null;  // null is wrapped by Optional in above getter
            }
        }
    }
}
