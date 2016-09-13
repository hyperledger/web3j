package org.web3j.methods.response;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectReader;

import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.utils.Codec;
import org.web3j.protocol.jsonrpc20.Response;

/**
 * eth_getTransactionReceipt
 */
public class EthGetTransactionReceipt extends Response<EthGetTransactionReceipt.TransactionReceipt> {

    public Optional<TransactionReceipt> getTransactionReceipt() {
        return Optional.ofNullable(getResult());
    }

    public static class TransactionReceipt {
        private String transactionHash;
        private String transactionIndex;
        private String blockHash;
        private String blockNumber;
        private String cumulativeGasUsed;
        private String gasUsed;
        private String contractAddress;
        private List<Log> logs;

        public TransactionReceipt() {
        }

        public TransactionReceipt(String transactionHash, String transactionIndex,
                                  String blockHash, String blockNumber, String cumulativeGasUsed,
                                  String gasUsed, String contractAddress, List<Log> logs) {
            this.transactionHash = transactionHash;
            this.transactionIndex = transactionIndex;
            this.blockHash = blockHash;
            this.blockNumber = blockNumber;
            this.cumulativeGasUsed = cumulativeGasUsed;
            this.gasUsed = gasUsed;
            this.contractAddress = contractAddress;
            this.logs = logs;
        }

        public String getTransactionHash() {
            return transactionHash;
        }

        public void setTransactionHash(String transactionHash) {
            this.transactionHash = transactionHash;
        }

        public BigInteger getTransactionIndex() {
            return Codec.decodeQuantity(transactionIndex);
        }

        public void setTransactionIndex(String transactionIndex) {
            this.transactionIndex = transactionIndex;
        }

        public String getBlockHash() {
            return blockHash;
        }

        public void setBlockHash(String blockHash) {
            this.blockHash = blockHash;
        }

        public BigInteger getBlockNumber() {
            return Codec.decodeQuantity(blockNumber);
        }

        public void setBlockNumber(String blockNumber) {
            this.blockNumber = blockNumber;
        }

        public BigInteger getCumulativeGasUsed() {
            return Codec.decodeQuantity(cumulativeGasUsed);
        }

        public void setCumulativeGasUsed(String cumulativeGasUsed) {
            this.cumulativeGasUsed = cumulativeGasUsed;
        }

        public BigInteger getGasUsed() {
            return Codec.decodeQuantity(gasUsed);
        }

        public void setGasUsed(String gasUsed) {
            this.gasUsed = gasUsed;
        }

        public Optional<String> getContractAddress() {
            return Optional.ofNullable(contractAddress);
        }

        public void setContractAddress(String contractAddress) {
            this.contractAddress = contractAddress;
        }

        public List<Log> getLogs() {
            return logs;
        }

        public void setLogs(List<Log> logs) {
            this.logs = logs;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TransactionReceipt that = (TransactionReceipt) o;

            if (transactionHash != null ? !transactionHash.equals(that.transactionHash) : that.transactionHash != null)
                return false;
            if (transactionIndex != null ? !transactionIndex.equals(that.transactionIndex) : that.transactionIndex != null)
                return false;
            if (blockHash != null ? !blockHash.equals(that.blockHash) : that.blockHash != null)
                return false;
            if (blockNumber != null ? !blockNumber.equals(that.blockNumber) : that.blockNumber != null)
                return false;
            if (cumulativeGasUsed != null ? !cumulativeGasUsed.equals(that.cumulativeGasUsed) : that.cumulativeGasUsed != null)
                return false;
            if (gasUsed != null ? !gasUsed.equals(that.gasUsed) : that.gasUsed != null)
                return false;
            if (contractAddress != null ? !contractAddress.equals(that.contractAddress) : that.contractAddress != null)
                return false;
            return logs != null ? logs.equals(that.logs) : that.logs == null;

        }

        @Override
        public int hashCode() {
            int result = transactionHash != null ? transactionHash.hashCode() : 0;
            result = 31 * result + (transactionIndex != null ? transactionIndex.hashCode() : 0);
            result = 31 * result + (blockHash != null ? blockHash.hashCode() : 0);
            result = 31 * result + (blockNumber != null ? blockNumber.hashCode() : 0);
            result = 31 * result + (cumulativeGasUsed != null ? cumulativeGasUsed.hashCode() : 0);
            result = 31 * result + (gasUsed != null ? gasUsed.hashCode() : 0);
            result = 31 * result + (contractAddress != null ? contractAddress.hashCode() : 0);
            result = 31 * result + (logs != null ? logs.hashCode() : 0);
            return result;
        }
    }

    public static class ResponseDeserialiser extends JsonDeserializer<TransactionReceipt> {

        private ObjectReader objectReader = ObjectMapperFactory.getObjectReader();

        @Override
        public TransactionReceipt deserialize(
                JsonParser jsonParser,
                DeserializationContext deserializationContext) throws IOException {
            if (jsonParser.getCurrentToken() != JsonToken.VALUE_NULL) {
                return objectReader.readValue(jsonParser, TransactionReceipt.class);
            } else {
                return null;  // null is wrapped by Optional in above getter
            }
        }
    }
}
