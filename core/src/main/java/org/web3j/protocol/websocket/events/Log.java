/*
 * Copyright 2019 Web3 Labs LTD.
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
