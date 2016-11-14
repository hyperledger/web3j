package org.web3j.protocol.core.methods.response;

import java.io.IOException;
import java.math.BigInteger;
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
import org.web3j.utils.Numeric;
import org.web3j.protocol.core.Response;

/**
 * Block object returned by:
 * <ul>
 * <li>eth_getBlockByHash</li>
 * <li>eth_getBlockByNumber</li>
 * <li>eth_getUncleByBlockHashAndIndex</li>
 * <li>eth_getUncleByBlockNumberAndIndex</li>
 * </ul>
 *
 * <p>See
 * <a href="https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_gettransactionbyhash">docs</a>
 * for further details.</p>
 *
 * <p>See the following <a href="https://github.com/ethcore/parity/issues/2401">issue</a> for
 * details on additional Parity fields present in EthBlock.</p>
 */
public class EthBlock extends Response<EthBlock.Block> {

    @Override
    @JsonDeserialize(using = EthBlock.ResponseDeserialiser.class)
    public void setResult(Block result) {
        super.setResult(result);
    }

    public Block getBlock() {
        return getResult();
    }

    public static class Block {
        private String number;
        private String hash;
        private String parentHash;
        private String nonce;
        private String sha3Uncles;
        private String logsBloom;
        private String transactionsRoot;
        private String stateRoot;
        private String receiptsRoot;  // geth has this wrong currently, see https://github.com/ethereum/go-ethereum/issues/3084
        private String author;
        private String miner;
        private String difficulty;
        private String totalDifficulty;
        private String extraData;
        private String size;
        private String gasLimit;
        private String gasUsed;
        private String timestamp;
        private List<TransactionResult> transactions;
        private List<String> uncles;
        private List<String> sealFields;

        public Block() {
        }

        public Block(String number, String hash, String parentHash, String nonce,
                     String sha3Uncles, String logsBloom, String transactionsRoot,
                     String stateRoot, String receiptsRoot, String author, String miner,
                     String difficulty, String totalDifficulty, String extraData, String size,
                     String gasLimit, String gasUsed, String timestamp,
                     List<TransactionResult> transactions, List<String> uncles,
                     List<String> sealFields) {
            this.number = number;
            this.hash = hash;
            this.parentHash = parentHash;
            this.nonce = nonce;
            this.sha3Uncles = sha3Uncles;
            this.logsBloom = logsBloom;
            this.transactionsRoot = transactionsRoot;
            this.stateRoot = stateRoot;
            this.receiptsRoot = receiptsRoot;
            this.author = author;
            this.miner = miner;
            this.difficulty = difficulty;
            this.totalDifficulty = totalDifficulty;
            this.extraData = extraData;
            this.size = size;
            this.gasLimit = gasLimit;
            this.gasUsed = gasUsed;
            this.timestamp = timestamp;
            this.transactions = transactions;
            this.uncles = uncles;
            this.sealFields = sealFields;
        }

        public BigInteger getNumber() {
            return Numeric.decodeQuantity(number);
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        public String getParentHash() {
            return parentHash;
        }

        public void setParentHash(String parentHash) {
            this.parentHash = parentHash;
        }

        public String getNonce() {
            return nonce;
        }

        public void setNonce(String nonce) {
            this.nonce = nonce;
        }

        public String getSha3Uncles() {
            return sha3Uncles;
        }

        public void setSha3Uncles(String sha3Uncles) {
            this.sha3Uncles = sha3Uncles;
        }

        public String getLogsBloom() {
            return logsBloom;
        }

        public void setLogsBloom(String logsBloom) {
            this.logsBloom = logsBloom;
        }

        public String getTransactionsRoot() {
            return transactionsRoot;
        }

        public void setTransactionsRoot(String transactionsRoot) {
            this.transactionsRoot = transactionsRoot;
        }

        public String getStateRoot() {
            return stateRoot;
        }

        public void setStateRoot(String stateRoot) {
            this.stateRoot = stateRoot;
        }

        public String getReceiptsRoot() {
            return receiptsRoot;
        }

        public void setReceiptsRoot(String receiptsRoot) {
            this.receiptsRoot = receiptsRoot;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getMiner() {
            return miner;
        }

        public void setMiner(String miner) {
            this.miner = miner;
        }

        public BigInteger getDifficulty() {
            return Numeric.decodeQuantity(difficulty);
        }

        public void setDifficulty(String difficulty) {
            this.difficulty = difficulty;
        }

        public BigInteger getTotalDifficulty() {
            return Numeric.decodeQuantity(totalDifficulty);
        }

        public void setTotalDifficulty(String totalDifficulty) {
            this.totalDifficulty = totalDifficulty;
        }

        public String getExtraData() {
            return extraData;
        }

        public void setExtraData(String extraData) {
            this.extraData = extraData;
        }

        public BigInteger getSize() {
            return Numeric.decodeQuantity(size);
        }

        public void setSize(String size) {
            this.size = size;
        }

        public BigInteger getGasLimit() {
            return Numeric.decodeQuantity(gasLimit);
        }

        public void setGasLimit(String gasLimit) {
            this.gasLimit = gasLimit;
        }

        public BigInteger getGasUsed() {
            return Numeric.decodeQuantity(gasUsed);
        }

        public void setGasUsed(String gasUsed) {
            this.gasUsed = gasUsed;
        }

        public BigInteger getTimestamp() {
            return Numeric.decodeQuantity(timestamp);
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public List<TransactionResult> getTransactions() {
            return transactions;
        }

        @JsonDeserialize(using = ResultTransactionDeserialiser.class)
        public void setTransactions(List<TransactionResult> transactions) {
            this.transactions = transactions;
        }

        public List<String> getUncles() {
            return uncles;
        }

        public void setUncles(List<String> uncles) {
            this.uncles = uncles;
        }

        public List<String> getSealFields() {
            return sealFields;
        }

        public void setSealFields(List<String> sealFields) {
            this.sealFields = sealFields;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Block that = (Block) o;

            if (number != null ? !number.equals(that.number) : that.number != null) return false;
            if (hash != null ? !hash.equals(that.hash) : that.hash != null) return false;
            if (parentHash != null ? !parentHash.equals(that.parentHash) : that.parentHash != null)
                return false;
            if (nonce != null ? !nonce.equals(that.nonce) : that.nonce != null) return false;
            if (sha3Uncles != null ? !sha3Uncles.equals(that.sha3Uncles) : that.sha3Uncles != null)
                return false;
            if (logsBloom != null ? !logsBloom.equals(that.logsBloom) : that.logsBloom != null)
                return false;
            if (transactionsRoot != null ? !transactionsRoot.equals(that.transactionsRoot) : that.transactionsRoot != null)
                return false;
            if (stateRoot != null ? !stateRoot.equals(that.stateRoot) : that.stateRoot != null)
                return false;
            if (receiptsRoot != null ? !receiptsRoot.equals(that.receiptsRoot) : that.receiptsRoot != null)
                return false;
            if (author != null ? !author.equals(that.author) : that.author != null) return false;
            if (miner != null ? !miner.equals(that.miner) : that.miner != null) return false;
            if (difficulty != null ? !difficulty.equals(that.difficulty) : that.difficulty != null)
                return false;
            if (totalDifficulty != null ? !totalDifficulty.equals(that.totalDifficulty) : that.totalDifficulty != null)
                return false;
            if (extraData != null ? !extraData.equals(that.extraData) : that.extraData != null)
                return false;
            if (size != null ? !size.equals(that.size) : that.size != null) return false;
            if (gasLimit != null ? !gasLimit.equals(that.gasLimit) : that.gasLimit != null)
                return false;
            if (gasUsed != null ? !gasUsed.equals(that.gasUsed) : that.gasUsed != null)
                return false;
            if (timestamp != null ? !timestamp.equals(that.timestamp) : that.timestamp != null)
                return false;
            if (transactions != null ? !transactions.equals(that.transactions) : that.transactions != null)
                return false;
            if (uncles != null ? !uncles.equals(that.uncles) : that.uncles != null)
                return false;
            return sealFields != null ? sealFields.equals(that.sealFields) : that.sealFields == null;

        }

        @Override
        public int hashCode() {
            int result = number != null ? number.hashCode() : 0;
            result = 31 * result + (hash != null ? hash.hashCode() : 0);
            result = 31 * result + (parentHash != null ? parentHash.hashCode() : 0);
            result = 31 * result + (nonce != null ? nonce.hashCode() : 0);
            result = 31 * result + (sha3Uncles != null ? sha3Uncles.hashCode() : 0);
            result = 31 * result + (logsBloom != null ? logsBloom.hashCode() : 0);
            result = 31 * result + (transactionsRoot != null ? transactionsRoot.hashCode() : 0);
            result = 31 * result + (stateRoot != null ? stateRoot.hashCode() : 0);
            result = 31 * result + (receiptsRoot != null ? receiptsRoot.hashCode() : 0);
            result = 31 * result + (author != null ? author.hashCode() : 0);
            result = 31 * result + (miner != null ? miner.hashCode() : 0);
            result = 31 * result + (difficulty != null ? difficulty.hashCode() : 0);
            result = 31 * result + (totalDifficulty != null ? totalDifficulty.hashCode() : 0);
            result = 31 * result + (extraData != null ? extraData.hashCode() : 0);
            result = 31 * result + (size != null ? size.hashCode() : 0);
            result = 31 * result + (gasLimit != null ? gasLimit.hashCode() : 0);
            result = 31 * result + (gasUsed != null ? gasUsed.hashCode() : 0);
            result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
            result = 31 * result + (transactions != null ? transactions.hashCode() : 0);
            result = 31 * result + (uncles != null ? uncles.hashCode() : 0);
            result = 31 * result + (sealFields != null ? sealFields.hashCode() : 0);
            return result;
        }
    }

    public interface TransactionResult<T> {
        T get();
    }

    public static class TransactionHash implements TransactionResult<String> {
        private String value;

        public TransactionHash() {
        }

        public TransactionHash(String value) {
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
            if (o == null || getClass() != o.getClass()) return false;

            TransactionHash that = (TransactionHash) o;

            return value != null ? value.equals(that.value) : that.value == null;

        }

        @Override
        public int hashCode() {
            return value != null ? value.hashCode() : 0;
        }
    }

    public static class TransactionObject extends Transaction implements TransactionResult<Transaction> {
        public TransactionObject() {
        }

        public TransactionObject(String hash, String nonce, String blockHash, String blockNumber,
                                 String transactionIndex, String from, String to, String value,
                                 String gasPrice, String gas, String input, String creates,
                                 String publicKey, String raw) {
            super(hash, nonce, blockHash, blockNumber, transactionIndex, from, to, value,
                    gasPrice, gas, input, creates, publicKey, raw);
        }

        @Override
        public Transaction get() {
            return this;
        }
    }

    public static class ResultTransactionDeserialiser extends JsonDeserializer<List<TransactionResult>> {

        private ObjectReader objectReader = ObjectMapperFactory.getObjectReader();

        @Override
        public List<TransactionResult> deserialize(
                JsonParser jsonParser,
                DeserializationContext deserializationContext) throws IOException {

            List<TransactionResult> transactionResults = new ArrayList<TransactionResult>();
            JsonToken nextToken = jsonParser.nextToken();

            if (nextToken == JsonToken.START_OBJECT) {
                Iterator<TransactionObject> transactionObjectIterator =
                        objectReader.readValues(jsonParser, TransactionObject.class);
                while (transactionObjectIterator.hasNext()) {
                    transactionResults.add(transactionObjectIterator.next());
                }
            } else if (nextToken == JsonToken.VALUE_STRING) {
                jsonParser.getValueAsString();

                Iterator<TransactionHash> transactionHashIterator =
                        objectReader.readValues(jsonParser, TransactionHash.class);
                while (transactionHashIterator.hasNext()) {
                    transactionResults.add(transactionHashIterator.next());
                }
            }

            return transactionResults;
        }
    }

    public static class ResponseDeserialiser extends JsonDeserializer<Block> {

        private ObjectReader objectReader = ObjectMapperFactory.getObjectReader();

        @Override
        public Block deserialize(
                JsonParser jsonParser,
                DeserializationContext deserializationContext) throws IOException {
            if (jsonParser.getCurrentToken() != JsonToken.VALUE_NULL) {
                return objectReader.readValue(jsonParser, Block.class);
            } else {
                return null;  // null is wrapped by Optional in above getter
            }
        }
    }
}
