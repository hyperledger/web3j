package org.web3j.methods.response;

import java.io.IOException;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectReader;

import org.web3j.protocol.Utils;
import org.web3j.protocol.jsonrpc20.Response;

/**
 * <p>Transaction object returned by:
 * <ul>
 * <li>eth_getTransactionByHash</li>
 * <li>eth_getTransactionByBlockHashAndIndex</li>
 * <li>eth_getTransactionByBlockNumberAndIndex</li>
 * </ul>
 * </p>
 * <p>This differs slightly from the request {@link EthSendTransaction} Transaction object.</p>
 * <p>
 * <p>See
 * <a href="https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_gettransactionbyhash">docs</a>
 * for further details.</p>
 */
public class EthTransaction extends Response<Transaction> {

    public Optional<Transaction> getTransaction() {
        return Optional.ofNullable(getResult());
    }

    public static class ResponseDeserialiser extends JsonDeserializer<Transaction> {

        private ObjectReader objectReader = Utils.getObjectReader();

        @Override
        public Transaction deserialize(
                JsonParser jsonParser,
                DeserializationContext deserializationContext) throws IOException {
            if (jsonParser.getCurrentToken() != JsonToken.VALUE_NULL) {
                return objectReader.readValue(jsonParser, Transaction.class);
            } else {
                return null;  // null is wrapped by Optional in above getter
            }
        }
    }
}
