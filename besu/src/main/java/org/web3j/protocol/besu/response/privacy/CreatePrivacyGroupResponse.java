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

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.web3j.utils.Base64String;

public class CreatePrivacyGroupResponse {
    private final Base64String privacyGroupId;
    private final String transactionHash;

    @JsonCreator
    public CreatePrivacyGroupResponse(
            @JsonProperty(value = "privacyGroupId") final String privacyGroupId,
            @JsonProperty(value = "transactionHash") final String transactionHash) {
        this.privacyGroupId = Base64String.wrap(privacyGroupId);
        this.transactionHash = transactionHash;
    }

    public Base64String getPrivacyGroupId() {
        return privacyGroupId;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CreatePrivacyGroupResponse that = (CreatePrivacyGroupResponse) o;
        return privacyGroupId.equals(that.privacyGroupId)
                && transactionHash.equals(that.transactionHash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(privacyGroupId, transactionHash);
    }
}
