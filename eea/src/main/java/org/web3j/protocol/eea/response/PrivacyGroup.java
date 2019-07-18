package org.web3j.protocol.eea.response;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PrivacyGroup {
    private final String privacyGroupId;
    private final String name;
    private final String description;
    private final Type type;
    private final List<String> members;

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
            @JsonProperty(value = "members") final List<String> members) {
        this.privacyGroupId = privacyGroupId;
        this.type = type;
        this.name = name;
        this.description = description;
        this.members = members;
    }

    public String getPrivacyGroupId() {
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

    public  List<String> getMembers() {
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
        return Objects.hash(getPrivacyGroupId(), getName(),
                getDescription(), getType(), getMembers());
    }
}
