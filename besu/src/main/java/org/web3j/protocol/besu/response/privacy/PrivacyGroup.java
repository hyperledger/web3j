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
package org.web3j.protocol.besu.response.privacy;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.web3j.utils.Base64String;

public class PrivacyGroup {
    private final Base64String privacyGroupId;
    private final String name;
    private final String description;
    private final Type type;
    private final List<Base64String> members;

    public enum Type {
        LEGACY,
        PANTHEON
    }

    @JsonCreator
    public PrivacyGroup(
            @JsonProperty(value = "privacyGroupId") final String privacyGroupId,
            @JsonProperty(value = "type") final Type type,
            @JsonProperty(value = "name") final String name,
            @JsonProperty(value = "description") final String description,
            @JsonProperty(value = "members") final List<Base64String> members) {
        this.privacyGroupId = Base64String.wrap(privacyGroupId);
        this.type = type;
        this.name = name;
        this.description = description;
        this.members = members;
    }

    public Base64String getPrivacyGroupId() {
        return privacyGroupId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Type getType() {
        return type;
    }

    public List<Base64String> getMembers() {
        return members;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PrivacyGroup that = (PrivacyGroup) o;
        return getPrivacyGroupId().equals(that.getPrivacyGroupId())
                && getName().equals(that.getName())
                && getDescription().equals(that.getDescription())
                && getType() == that.getType()
                && getMembers().equals(that.getMembers());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getPrivacyGroupId(), getName(), getDescription(), getType(), getMembers());
    }
}
