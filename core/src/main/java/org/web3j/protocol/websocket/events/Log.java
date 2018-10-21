package org.web3j.protocol.websocket.events;

import java.util.List;

public class Log {
    private String address;
    private String blockHash;
    private String blockNumber;
    private String data;
    private String logIndex;
    private List<String> topics;
    private String transactionHash;
    private String transactionIndex;

    public String getAddress() {
        return address;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public String getBlockNumber() {
        return blockNumber;
    }

    public String getData() {
        return data;
    }

    public String getLogIndex() {
        return logIndex;
    }

    public List<String> getTopics() {
        return topics;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public String getTransactionIndex() {
        return transactionIndex;
    }
}
