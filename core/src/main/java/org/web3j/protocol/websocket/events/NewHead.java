package org.web3j.protocol.websocket.events;

public class NewHead {
    private String difficulty;
    private String extraData;
    private String gasLimit;
    private String gasUsed;
    private String logsBloom;
    private String miner;
    private String nonce;
    private String number;
    private String parentHash;
    private String receiptRoot;
    private String sha3Uncles;
    private String stateRoot;
    private String timestamp;
    private String transactionRoot;

    public String getDifficulty() {
        return difficulty;
    }

    public String getExtraData() {
        return extraData;
    }

    public String getGasLimit() {
        return gasLimit;
    }

    public String getGasUsed() {
        return gasUsed;
    }

    public String getLogsBloom() {
        return logsBloom;
    }

    public String getMiner() {
        return miner;
    }

    public String getNonce() {
        return nonce;
    }

    public String getNumber() {
        return number;
    }

    public String getParentHash() {
        return parentHash;
    }

    public String getReceiptRoot() {
        return receiptRoot;
    }

    public String getSha3Uncles() {
        return sha3Uncles;
    }

    public String getStateRoot() {
        return stateRoot;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getTransactionRoot() {
        return transactionRoot;
    }
}
