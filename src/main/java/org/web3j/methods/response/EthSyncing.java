package org.web3j.methods.response;


import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import org.web3j.protocol.Utils;
import org.web3j.protocol.jsonrpc20.Response;

/**
 * eth_syncing
 * <p>
 * Returns an object with data about the sync status or false.
 */
public class EthSyncing extends Response<EthSyncing.Result> {

    @Override
    @JsonDeserialize(using = EthSyncing.ResponseDeserialiser.class)
    public void setResult(EthSyncing.Result result) {
        super.setResult(result);
    }

    public boolean isSyncing() {
        return getResult().isSyncing();
    }

    public static class Result {
        private boolean isSyncing = true;

        public Result() {
        }

        public boolean isSyncing() {
            return isSyncing;
        }

        public void setSyncing(boolean syncing) {
            isSyncing = syncing;
        }
    }

    @JsonIgnoreProperties({"knownStates", "pulledStates"})
    // these fields although not present in the RPC specification are returned by Geth 1.4.10
    public static class Syncing extends Result {

        private String startingBlock;
        private String currentBlock;
        private String highestBlock;
        private String knownStates;
        private String pulledStates;

        public Syncing() {
        }

        public Syncing(String startingBlock, String currentBlock, String highestBlock, String knownStates, String pulledStates) {
            this.startingBlock = startingBlock;
            this.currentBlock = currentBlock;
            this.highestBlock = highestBlock;
            this.knownStates = knownStates;
            this.pulledStates = pulledStates;
        }

        public String getStartingBlock() {
            return startingBlock;
        }

        public void setStartingBlock(String startingBlock) {
            this.startingBlock = startingBlock;
        }

        public String getCurrentBlock() {
            return currentBlock;
        }

        public void setCurrentBlock(String currentBlock) {
            this.currentBlock = currentBlock;
        }

        public String getHighestBlock() {
            return highestBlock;
        }

        public void setHighestBlock(String highestBlock) {
            this.highestBlock = highestBlock;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Syncing syncing = (Syncing) o;

            if (startingBlock != null ? !startingBlock.equals(syncing.startingBlock) : syncing.startingBlock != null)
                return false;
            if (currentBlock != null ? !currentBlock.equals(syncing.currentBlock) : syncing.currentBlock != null)
                return false;
            if (highestBlock != null ? !highestBlock.equals(syncing.highestBlock) : syncing.highestBlock != null)
                return false;
            if (knownStates != null ? !knownStates.equals(syncing.knownStates) : syncing.knownStates != null)
                return false;
            return pulledStates != null ? pulledStates.equals(syncing.pulledStates) : syncing.pulledStates == null;

        }

        @Override
        public int hashCode() {
            int result = startingBlock != null ? startingBlock.hashCode() : 0;
            result = 31 * result + (currentBlock != null ? currentBlock.hashCode() : 0);
            result = 31 * result + (highestBlock != null ? highestBlock.hashCode() : 0);
            result = 31 * result + (knownStates != null ? knownStates.hashCode() : 0);
            result = 31 * result + (pulledStates != null ? pulledStates.hashCode() : 0);
            return result;
        }
    }

    public static class ResponseDeserialiser extends JsonDeserializer<EthSyncing.Result> {

        private ObjectReader objectReader = Utils.getObjectReader();

        @Override
        public Result deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
                throws IOException {
            Result result;
            if (jsonParser.getCurrentToken() == JsonToken.VALUE_FALSE) {
                result = new Result();
                result.setSyncing(jsonParser.getBooleanValue());
            } else {
                result = objectReader.readValue(jsonParser, Syncing.class);
            }
            return result;
        }
    }
}
