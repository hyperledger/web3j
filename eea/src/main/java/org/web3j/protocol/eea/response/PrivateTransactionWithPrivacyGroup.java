package org.web3j.protocol.eea.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Objects;

@JsonDeserialize(using = JsonDeserializer.None.class)
public class PrivateTransactionWithPrivacyGroup extends PrivateTransaction {

    private String privacyGroupId;

    @JsonCreator
    public PrivateTransactionWithPrivacyGroup(
            @JsonProperty(value = "hash")final String hash,
            @JsonProperty(value = "nonce")final String nonce,
            @JsonProperty(value = "from")final String from,
            @JsonProperty(value = "to")final String to,
            @JsonProperty(value = "value")final String value,
            @JsonProperty(value = "gas")final String gas,
            @JsonProperty(value = "gasPrice")final String gasPrice,
            @JsonProperty(value = "input")final String input,
            @JsonProperty(value = "r")final String r,
            @JsonProperty(value = "s")final String s,
            @JsonProperty(value = "v")final long v,
            @JsonProperty(value = "privateFrom")final String privateFrom,
            @JsonProperty(value = "privacyGroupId")final String privacyGroupId,
            @JsonProperty(value = "restriction")final String restriction) {
        super(hash, nonce, from, to, value, gas, gasPrice,
                input,r, s, v, privateFrom, restriction);
        this.privacyGroupId = privacyGroupId;
    }

    public String getPrivacyGroupId() {
        return privacyGroupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PrivateTransactionWithPrivacyGroup that = (PrivateTransactionWithPrivacyGroup) o;
        return getPrivacyGroupId().equals(that.getPrivacyGroupId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getPrivacyGroupId());
    }
}
