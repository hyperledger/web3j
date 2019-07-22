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
package org.web3j.protocol.eea.response;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

public class PrivateTransactionReceipt extends TransactionReceipt {

    private final String contractAddress;
    private final String from;
    private final String to;
    private final String output;
    private final List<Log> logs;

    @JsonCreator
    public PrivateTransactionReceipt(
            @JsonProperty(value = "contractAddress") final String contractAddress,
            @JsonProperty(value = "from") final String from,
            @JsonProperty(value = "to") final String to,
            @JsonProperty(value = "output") final String output,
            @JsonProperty(value = "logs") final List<Log> logs) {
        this.contractAddress = contractAddress;
        this.from = from;
        this.to = to;
        this.output = output;
        this.logs = logs;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public List<Log> getLogs() {
        return logs;
    }

    public String getOutput() {
        return output;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PrivateTransactionReceipt that = (PrivateTransactionReceipt) o;
        return Objects.equals(contractAddress, that.contractAddress)
                && from.equals(that.from)
                && Objects.equals(to, that.to)
                && Objects.equals(output, that.output)
                && Objects.equals(logs, that.logs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contractAddress, from, to, output, logs);
    }

    @Override
    public String toString() {
        return "PrivateTransactionReceipt{"
                + "contractAddress='"
                + contractAddress
                + '\''
                + ", from='"
                + from
                + '\''
                + ", to='"
                + to
                + '\''
                + ", output='"
                + output
                + '\''
                + ", logs="
                + logs
                + '}';
    }
}
