package org.web3j.protocol.eea.response;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectReader;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.core.Response;

import java.io.IOException;
import java.util.Optional;

public class EeaPrivateTransaction extends Response<PrivateTransaction> {
    public Optional<PrivateTransaction> getPrivateTransaction() {
        return Optional.ofNullable(getResult());
    }

    public static class ResponseDeserialiser extends JsonDeserializer<PrivateTransaction> {

        private ObjectReader objectReader = ObjectMapperFactory.getObjectReader();

        @Override
        public PrivateTransaction deserialize(
                JsonParser jsonParser,
                DeserializationContext deserializationContext) throws IOException {
            if (jsonParser.getCurrentToken() != JsonToken.VALUE_NULL) {
                return objectReader.readValue(jsonParser, PrivateTransaction.class);
            } else {
                return null;  // null is wrapped by Optional in above getter
            }
        }
    }
}
