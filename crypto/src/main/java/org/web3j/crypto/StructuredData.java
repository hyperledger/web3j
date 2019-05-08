package org.web3j.crypto;

import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.generated.Uint256;

public class StructuredData {
    static class Entry {
        private final String name;
        private final String type;

        @JsonCreator
        public Entry(
                @JsonProperty(value = "name") String name,
                @JsonProperty(value = "type") String type) {
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }
    }

    static class EIP712Domain {
        private final String name;
        private final String version;
        private final Uint256 chainId;
        private final Address verifyingContract;

        @JsonCreator
        public EIP712Domain(
                @JsonProperty(value = "name") String name,
                @JsonProperty(value = "version") String version,
                @JsonProperty(value = "chainId") Uint256 chainId,
                @JsonProperty(value = "verifyingContract") Address verifyingContract) {
            this.name = name;
            this.version = version;
            this.chainId = chainId;
            this.verifyingContract = verifyingContract;
        }

        public String getName() {
            return name;
        }

        public String getVersion() {
            return version;
        }

        public Uint256 getChainId() {
            return chainId;
        }

        public Address getVerifyingContract() {
            return verifyingContract;
        }
    }

    static class EIP712Message {
        private final HashMap<String, List<Entry>> types;
        private final String primaryType;
        private final Object message;
        private final EIP712Domain domain;

        @JsonCreator
        public EIP712Message(
                @JsonProperty(value = "types") HashMap<String, List<Entry>> types,
                @JsonProperty(value = "primaryType") String primaryType,
                @JsonProperty(value = "message") Object message,
                @JsonProperty(value = "domain") EIP712Domain domain) {
            this.types = types;
            this.primaryType = primaryType;
            this.message = message;
            this.domain = domain;
        }

        public HashMap<String, List<Entry>> getTypes() {
            return types;
        }

        public String getPrimaryType() {
            return primaryType;
        }

        public Object getMessage() {
            return message;
        }

        public EIP712Domain getDomain() {
            return domain;
        }

        @Override
        public String toString() {
            return "EIP712Message{"
                    + "primaryType='" + this.primaryType + '\''
                    + ", message='" + this.message + '\''
                    + '}';
        }
    }
}