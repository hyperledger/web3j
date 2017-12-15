package org.web3j.benchmark;

import java.util.List;
import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Config {
    private BenchType type;
    private int txNumber;
    private int threadNumber;
    private long quota;
    private List<String> urls;
    private String code;
    private String privateKey;
    private String to;

    public BenchType getType() {
        return this.type;
    }

    public void setType(BenchType type) {
        this.type = type;
    }

    public int getTxNumber() {
        return this.txNumber;
    }

    public void setTxNumber(int txNumber) {
        this.txNumber = txNumber;
    }

    public List<String> getUrls() {
        return this.urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public int getThreadNumber() {
        return this.threadNumber;
    }

    public void setThreadNumber(int threadNumber) {
        this.threadNumber = threadNumber;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPrivateKey() {
        return this.privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public long getQuota() {
        return this.quota;
    }

    public void setQuota(int quota) {
        this.quota = quota;
    }

    public String getTo() {
        return this.to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public static Config load(String path) throws IOException {
        ObjectMapper m = new ObjectMapper();
        return m.readValue(new File(path), Config.class);
    }

    public enum BenchType {
        BlockNumber, SendTransaction, Analysis, SendTransactionAndGetReceipt
    }
}
