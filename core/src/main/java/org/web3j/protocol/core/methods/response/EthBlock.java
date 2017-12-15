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
import org.web3j.protocol.core.Response;
import org.web3j.utils.Numeric;

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

    public boolean isEmpty() {
        return getResult() == null;
    }


    public static class Proof {
        private String proposal;
        private String height;
        private String round;

        public Proof(){
        }

        public Proof(String proposal, String height, String round){
            this.proposal = proposal;
            this.height = height;
            this.round = round;
        }

        public String getProposal() {
            return proposal;
        }

        public void setProposal(String proposal) {
            this.proposal = proposal;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getRound() {
            return round;
        }

        public void setRound(String round) {
            this.round = round;
        }

        @Override
        public int hashCode() {
            int result = getProposal() != null ? getProposal().hashCode() : 0;
            result = 31 * result + (getHeight() != null ? getHeight().hashCode() : 0);
            result = 31 * result + (getRound() != null ? getRound().hashCode() : 0);
            return result;
        }
    }


    public static class Header{
        private long timestamp;
        private String prevHash;
        private String number;
        private String stateRoot;
        private String transactionsRoot;
        private String receiptsRoot;
        private String gasUsed;
        private Proof proof;


        public Header(){
        }

        public Header(long timestamp, String prevHash, String number, String stateRoot,
                      String transactionsRoot, String receiptsRoot, String gasUsed, Proof proof){
            this.timestamp = timestamp;
            this.prevHash = prevHash;
            this.number = number;
            this.stateRoot = stateRoot;
            this.transactionsRoot = transactionsRoot;
            this.receiptsRoot = receiptsRoot;
            this.gasUsed = gasUsed;
            this.proof = proof;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public String getPrevHash() {
            return prevHash;
        }

        public void setPrevHash(String prevHash) {
            this.prevHash = prevHash;
        }

        public BigInteger getNumberDec() {
            return Numeric.decodeQuantity(number);
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getStateRoot() {
            return stateRoot;
        }

        public void setStateRoot(String stateRoot) {
            this.stateRoot = stateRoot;
        }

        public String getTransactionsRoot() {
            return transactionsRoot;
        }

        public void setTransactionsRoot(String transactionsRoot) {
            this.transactionsRoot = transactionsRoot;
        }

        public String getReceiptsRoot() {
            return receiptsRoot;
        }

        public void setReceiptsRoot(String receiptsRoot) {
            this.receiptsRoot = receiptsRoot;
        }

        public BigInteger getGasUsedDec() {
            return Numeric.decodeQuantity(gasUsed);
        }

        public String getGasUsed() {
            return gasUsed;
        }

        public void setGasUsed(String gasUsed) {
            this.gasUsed = gasUsed;
        }

        public Proof getProof() {
            return proof;
        }

        public void setProof(Proof proof) {
            this.proof = proof;
        }

        @Override
        public int hashCode() {
            int result = 0;
            result = 31 * result + (getPrevHash() != null ? getPrevHash().hashCode() : 0);
            result = 31 * result + (getNumber() != null ? getNumber().hashCode() : 0);
            result = 31 * result + (getStateRoot() != null ? getStateRoot().hashCode() : 0);
            result = 31 * result + (getTransactionsRoot() != null ? getTransactionsRoot().hashCode() : 0);
            result = 31 * result + (getReceiptsRoot() != null ? getReceiptsRoot().hashCode() : 0);
            result = 31 * result + (getGasUsed() != null ? getGasUsed().hashCode() : 0);
            result = 31 * result + (getProof() != null ? getProof().hashCode() : 0);
            return result;
        }
    }

    public static class Body {
        private List<TransactionResult> transactions;

        public Body() {
        }

        public Body(List<TransactionResult> transactions) {
            this.transactions = transactions;
        }

        public List<TransactionResult> getTransactions() {
            return transactions;
        }

        @JsonDeserialize(using = EthBlock.ResultTransactionDeserialiser.class)
        public void setTransactions(List<TransactionResult> transactions) {
            this.transactions = transactions;
        }

        @Override
        public int hashCode() {
            int result = getTransactions() != null ? getTransactions().hashCode() : 0;
            return result;
        }
    }

    public static class Block {
        private String version;
        private String hash;
        private Header header;
        private Body body;

        public Block(){
        }

        public Block(String version, String hash, Header header, Body body){
            this.version = version;
            this.hash = hash;
            this.header = header;
            this.body = body;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        public Header getHeader() {
            return header;
        }

        public void setHeader(Header header) {
            this.header = header;
        }

        public Body getBody() {
            return body;
        }

        public void setBody(Body body) {
            this.body = body;
        }

        @Override
        public int hashCode() {
            int result = getVersion() != null ? getVersion().hashCode() : 0;
            result = 31 * result + (getHash() != null ? getHash().hashCode() : 0);
            result = 31 * result + (getHeader() != null ? getHeader().hashCode() : 0);
            result = 31 * result + (getBody() != null ? getBody().hashCode() : 0);
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
            if (this == o) {
                return true;
            }
            if (!(o instanceof TransactionHash)) {
                return false;
            }

            TransactionHash that = (TransactionHash) o;

            return value != null ? value.equals(that.value) : that.value == null;
        }

        @Override
        public int hashCode() {
            return value != null ? value.hashCode() : 0;
        }
    }

    public static class TransactionObject extends Transaction
            implements TransactionResult<Transaction> {
        public TransactionObject() {
        }

        public TransactionObject(String hash, String nonce, String blockHash, String blockNumber,
                                 String transactionIndex, String from, String to, String value,
                                 String gasPrice, String gas, String input, String creates,
                                 String publicKey, String raw, String r, String s, int v,
                                 String content, String index) {
            super(hash, nonce, blockHash, blockNumber, transactionIndex, from, to, value,
                    gasPrice, gas, input, creates, publicKey, raw, r, s, v, content, index);
        }

        @Override
        public Transaction get() {
            return this;
        }
    }

    public static class ResultTransactionDeserialiser
            extends JsonDeserializer<List<TransactionResult>> {

        private ObjectReader objectReader = ObjectMapperFactory.getObjectReader();

        @Override
        public List<TransactionResult> deserialize(
                JsonParser jsonParser,
                DeserializationContext deserializationContext) throws IOException {

            List<TransactionResult> transactionResults = new ArrayList<>();
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
