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

    private final String output;
    private final String commitmentHash;
    private final String privateFrom;
    private final ArrayList<String> privateFor;
    private final String privacyGroupId;

    @JsonCreator
    public PrivateTransactionReceipt(
            @JsonProperty(value = "transactionHash") final String transactionHash,
            @JsonProperty(value = "transactionIndex") final String transactionIndex,
            @JsonProperty(value = "blockHash") final String blockHash,
            @JsonProperty(value = "blockNumber") final String blockNumber,
            @JsonProperty(value = "cumulativeGasUsed") final String cumulativeGasUsed,
            @JsonProperty(value = "gasUsed") final String gasUsed,
            @JsonProperty(value = "contractAddress") final String contractAddress,
            @JsonProperty(value = "root") final String root,
            @JsonProperty(value = "status") final String status,
            @JsonProperty(value = "from") final String from,
            @JsonProperty(value = "to") final String to,
            @JsonProperty(value = "logs") final List<Log> logs,
            @JsonProperty(value = "logsBloom") final String logsBloom,
            @JsonProperty(value = "revertReason") final String revertReason,
            @JsonProperty(value = "output") final String output,
            @JsonProperty(value = "commitmentHash") final String commitmentHash,
            @JsonProperty(value = "privateFrom") final String privateFrom,
            @JsonProperty(value = "privateFor") final ArrayList<String> privateFor,
            @JsonProperty(value = "privacyGroupId") final String privacyGroupId) {
        super(
                transactionHash,
                transactionIndex,
                blockHash,
                blockNumber,
                cumulativeGasUsed,
                gasUsed,
                contractAddress,
                root,
                status,
                from,
                to,
                logs,
                logsBloom,
                revertReason);
        this.output = output;
        this.commitmentHash = commitmentHash;
        this.privateFrom = privateFrom;
        this.privateFor = privateFor;
        this.privacyGroupId = privacyGroupId;
    }

    public String getOutput() {
        return output;
    }

    public String getcommitmentHash() {
        return commitmentHash;
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
        return Objects.equals(output, that.output)
                && Objects.equals(commitmentHash, that.commitmentHash)
                && Objects.equals(privateFrom, that.privateFrom)
                && Objects.equals(privateFor, that.privateFor)
                && Objects.equals(privacyGroupId, that.privacyGroupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                super.hashCode(), output, commitmentHash, privateFrom, privateFor, privacyGroupId);
    }
}
