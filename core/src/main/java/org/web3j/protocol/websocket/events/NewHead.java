/*
 * Copyright 2019 Web3 Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.web3j.protocol.websocket.events;

public class NewHead {
    private String difficulty;
    private String extraData;
    private String gasLimit;
    private String gasUsed;
    private String hash;
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

    public String getHash() {
        return hash;
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
