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
package org.web3j.protocol.besu.response.privacy;

import java.util.ArrayList;
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
    private final String commitmentHash;
    private final String transactionHash;
    private final String privateFrom;
    private final ArrayList<String> privateFor;
    private final String privacyGroupId;
    private final String status;
    private final String revertReason;

    @JsonCreator
    public PrivateTransactionReceipt(
            @JsonProperty(value = "contractAddress") final String contractAddress,
            @JsonProperty(value = "from") final String from,
            @JsonProperty(value = "to") final String to,
            @JsonProperty(value = "output") final String output,
            @JsonProperty(value = "logs") final List<Log> logs,
            @JsonProperty(value = "commitmentHash") final String commitmentHash,
            @JsonProperty(value = "transactionHash") final String transactionHash,
            @JsonProperty(value = "privateFrom") final String privateFrom,
            @JsonProperty(value = "privateFor") final ArrayList<String> privateFor,
            @JsonProperty(value = "privacyGroupId") final String privacyGroupId,
            @JsonProperty(value = "status") final String status,
            @JsonProperty(value = "revertReason") final String revertReason) {
        this.contractAddress = contractAddress;
        this.from = from;
        this.to = to;
        this.output = output;
        this.logs = logs;
        this.commitmentHash = commitmentHash;
        this.transactionHash = transactionHash;
        this.privateFrom = privateFrom;
        this.privateFor = privateFor;
        this.privacyGroupId = privacyGroupId;
        this.status = status;
        this.revertReason = revertReason;
    }

    @Override
    public String getContractAddress() {
        return contractAddress;
    }

    @Override
    public String getFrom() {
        return from;
    }

    @Override
    public String getTo() {
        return to;
    }

    public String getOutput() {
        return output;
    }

    @Override
    public List<Log> getLogs() {
        return logs;
    }

    public String getRevertReason() {
        return revertReason;
    }

    @Override
    public String getStatus() {
        return status;
    }

    public String getcommitmentHash() {
        return commitmentHash;
    }

    public String gettransactionHash() {
        return transactionHash;
    }

    public String getPrivateFrom() {
        return privateFrom;
    }

    public ArrayList<String> getPrivateFor() {
        return privateFor;
    }

    public String getPrivacyGroupId() {
        return privacyGroupId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        final PrivateTransactionReceipt that = (PrivateTransactionReceipt) o;
        return Objects.equals(contractAddress, that.contractAddress)
                && Objects.equals(from, that.from)
                && Objects.equals(to, that.to)
                && Objects.equals(output, that.output)
                && Objects.equals(logs, that.logs)
                && Objects.equals(commitmentHash, that.commitmentHash)
                && Objects.equals(transactionHash, that.transactionHash)
                && Objects.equals(privateFrom, that.privateFrom)
                && Objects.equals(privateFor, that.privateFor)
                && Objects.equals(privacyGroupId, that.privacyGroupId)
                && Objects.equals(status, that.status)
                && Objects.equals(revertReason, that.revertReason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                super.hashCode(),
                contractAddress,
                from,
                to,
                output,
                logs,
                commitmentHash,
                transactionHash,
                privateFrom,
                privateFor,
                privacyGroupId,
                status,
                revertReason);
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
                + ", commitmentHash='"
                + commitmentHash
                + '\''
                + ", transactionHash='"
                + transactionHash
                + '\''
                + ", privateFrom='"
                + privateFrom
                + '\''
                + ", privateFor="
                + privateFor
                + ", privacyGroupId='"
                + privacyGroupId
                + '\''
                + ", status='"
                + status
                + '\''
                + ", revertReason='"
                + revertReason
                + '\''
                + '}';
    }
}
