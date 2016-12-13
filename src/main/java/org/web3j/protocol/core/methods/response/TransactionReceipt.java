package org.web3j.protocol.core.methods.response;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import org.web3j.utils.Numeric;

/**
 * TransactionReceipt object used by {@link EthGetTransactionReceipt}.
 */
public class TransactionReceipt {
    private String transactionHash;
    private String transactionIndex;
    private String blockHash;
    private String blockNumber;
    private String cumulativeGasUsed;
    private String gasUsed;
    private String contractAddress;  // this is present in the spec
    private String root;
    private String from;
    private String to;
    private List<Log> logs;
    private String logsBloom;

    public TransactionReceipt() {
    }

    public TransactionReceipt(String transactionHash, String transactionIndex,
                              String blockHash, String blockNumber, String cumulativeGasUsed,
                              String gasUsed, String contractAddress, String root, String from,
                              String to, List<Log> logs, String logsBloom) {
        this.transactionHash = transactionHash;
        this.transactionIndex = transactionIndex;
        this.blockHash = blockHash;
        this.blockNumber = blockNumber;
        this.cumulativeGasUsed = cumulativeGasUsed;
        this.gasUsed = gasUsed;
        this.contractAddress = contractAddress;
        this.root = root;
        this.from = from;
        this.to = to;
        this.logs = logs;
        this.logsBloom = logsBloom;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    public BigInteger getTransactionIndex() {
        return Numeric.decodeQuantity(transactionIndex);
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
        return Numeric.decodeQuantity(blockNumber);
    }

    public void setBlockNumber(String blockNumber) {
        this.blockNumber = blockNumber;
    }

    public BigInteger getCumulativeGasUsed() {
        return Numeric.decodeQuantity(cumulativeGasUsed);
    }

    public void setCumulativeGasUsed(String cumulativeGasUsed) {
        this.cumulativeGasUsed = cumulativeGasUsed;
    }

    public BigInteger getGasUsed() {
        return Numeric.decodeQuantity(gasUsed);
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

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public List<Log> getLogs() {
        return logs;
    }

    public void setLogs(List<Log> logs) {
        this.logs = logs;
    }

    public String getLogsBloom() {
        return logsBloom;
    }

    public void setLogsBloom(String logsBloom) {
        this.logsBloom = logsBloom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransactionReceipt)) return false;

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
        if (gasUsed != null ? !gasUsed.equals(that.gasUsed) : that.gasUsed != null) return false;
        if (contractAddress != null ? !contractAddress.equals(that.contractAddress) : that.contractAddress != null)
            return false;
        if (root != null ? !root.equals(that.root) : that.root != null) return false;
        if (from != null ? !from.equals(that.from) : that.from != null) return false;
        if (to != null ? !to.equals(that.to) : that.to != null) return false;
        if (logs != null ? !logs.equals(that.logs) : that.logs != null) return false;
        return logsBloom != null ? logsBloom.equals(that.logsBloom) : that.logsBloom == null;
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
        result = 31 * result + (root != null ? root.hashCode() : 0);
        result = 31 * result + (from != null ? from.hashCode() : 0);
        result = 31 * result + (to != null ? to.hashCode() : 0);
        result = 31 * result + (logs != null ? logs.hashCode() : 0);
        result = 31 * result + (logsBloom != null ? logsBloom.hashCode() : 0);
        return result;
    }
}
