package org.web3j.protocol.core.methods.response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.core.Response;

/**
 * Log object returned by:
 * <ul>
 * <li>eth_getFilterChanges</li>
 * <li>eth_getFilterLogs</li>
 * <li>eth_getLogs</li>
 * </ul>
 *
 * <p>See
 * <a href="https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_getfilterchanges">docs</a>
 * for further details.</p>
 */
public class EthLog extends Response<List<EthLog.LogResult>> {

    @Override
    @JsonDeserialize(using = LogResultDeserialiser.class)
    public void setResult(List<LogResult> result) {
        super.setResult(result);
    }

    public List<LogResult> getLogs() {
        return getResult();
    }

    public interface LogResult<T> {
        T get();
    }

    public static class LogObject extends Log implements LogResult<Log> {

        public LogObject() {
        }

        public LogObject(boolean removed, String logIndex, String transactionIndex,
                         String transactionHash, String blockHash, String blockNumber,
                         String address, String data, String type, List<String> topics) {
            super(removed, logIndex, transactionIndex, transactionHash, blockHash, blockNumber,
                    address, data, type, topics);
        }

        @Override
        public Log get() {
            return this;
        }
    }

    public static class Hash implements LogResult<String> {
        private String value;

        public Hash() {
        }

        public Hash(String value) {
            this.value = value;
        }

        @Override
        public String get() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Hash)) return false;

            Hash that = (Hash) o;

            return value != null ? value.equals(that.value) : that.value == null;

        }

        @Override
        public int hashCode() {
            return value != null ? value.hashCode() : 0;
        }
    }


    public static class LogResultDeserialiser extends JsonDeserializer<List<LogResult>> {

        private ObjectReader objectReader = ObjectMapperFactory.getObjectReader();

        @Override
        public List<LogResult> deserialize(
                JsonParser jsonParser,
                DeserializationContext deserializationContext) throws IOException {

            List<LogResult> logResults = new ArrayList<LogResult>();
            JsonToken nextToken = jsonParser.nextToken();

            if (nextToken == JsonToken.START_OBJECT) {
                Iterator<LogObject> logObjectIterator =
                        objectReader.readValues(jsonParser, LogObject.class);
                while (logObjectIterator.hasNext()) {
                    logResults.add(logObjectIterator.next());
                }
            } else if (nextToken == JsonToken.VALUE_STRING) {
                jsonParser.getValueAsString();

                Iterator<Hash> transactionHashIterator =
                        objectReader.readValues(jsonParser, Hash.class);
                while (transactionHashIterator.hasNext()) {
                    logResults.add(transactionHashIterator.next());
                }
            }
            return logResults;
        }
    }
}
