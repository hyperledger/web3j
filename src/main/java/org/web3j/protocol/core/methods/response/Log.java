package org.web3j.protocol.core.methods.response;

import java.math.BigInteger;
import java.util.List;

import org.web3j.utils.Numeric;

/**
 * <p>Log object used by {@link EthLog} and {@link EthGetTransactionReceipt}.</p>
 * <p>It's not clear in the
 * <a href="https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_newfilter">docs</a>
 * If only a list of hashes are returned for filters created with eth_newBlockFilter or
 * eth_newPendingTransactionFilter.</p>
 */
public class Log {
    private boolean removed;
    private String logIndex;
    private String transactionIndex;
    private String transactionHash;
    private String blockHash;
    private String blockNumber;
    private String address;
    private String data;
    private String type;
    private List<String> topics;

    public Log() {
    }

    public Log(boolean removed, String logIndex, String transactionIndex, String transactionHash,
               String blockHash, String blockNumber, String address, String data, String type,
               List<String> topics) {
        this.removed = removed;
        this.logIndex = logIndex;
        this.transactionIndex = transactionIndex;
        this.transactionHash = transactionHash;
        this.blockHash = blockHash;
        this.blockNumber = blockNumber;
        this.address = address;
        this.data = data;
        this.type = type;
        this.topics = topics;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    public BigInteger getLogIndex() {
        return convert(logIndex);
    }

    public void setLogIndex(String logIndex) {
        this.logIndex = logIndex;
    }

    public BigInteger getTransactionIndex() {
        return convert(transactionIndex);
    }

    public void setTransactionIndex(String transactionIndex) {
        this.transactionIndex = transactionIndex;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    public BigInteger getBlockNumber() {
        return convert(blockNumber);
    }

    public void setBlockNumber(String blockNumber) {
        this.blockNumber = blockNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    private BigInteger convert(String value) {
        if (value != null) {
            return Numeric.decodeQuantity(value);
        } else {
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Log log = (Log) o;

        if (removed != log.removed) return false;
        if (logIndex != null ? !logIndex.equals(log.logIndex) : log.logIndex != null) return false;
        if (transactionIndex != null ? !transactionIndex.equals(log.transactionIndex) : log.transactionIndex != null)
            return false;
        if (transactionHash != null ? !transactionHash.equals(log.transactionHash) : log.transactionHash != null)
            return false;
        if (blockHash != null ? !blockHash.equals(log.blockHash) : log.blockHash != null)
            return false;
        if (blockNumber != null ? !blockNumber.equals(log.blockNumber) : log.blockNumber != null)
            return false;
        if (address != null ? !address.equals(log.address) : log.address != null) return false;
        if (data != null ? !data.equals(log.data) : log.data != null) return false;
        if (type != null ? !type.equals(log.type) : log.type != null) return false;
        return topics != null ? topics.equals(log.topics) : log.topics == null;

    }

    @Override
    public int hashCode() {
        int result = (removed ? 1 : 0);
        result = 31 * result + (logIndex != null ? logIndex.hashCode() : 0);
        result = 31 * result + (transactionIndex != null ? transactionIndex.hashCode() : 0);
        result = 31 * result + (transactionHash != null ? transactionHash.hashCode() : 0);
        result = 31 * result + (blockHash != null ? blockHash.hashCode() : 0);
        result = 31 * result + (blockNumber != null ? blockNumber.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (data != null ? data.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (topics != null ? topics.hashCode() : 0);
        return result;
    }
}
