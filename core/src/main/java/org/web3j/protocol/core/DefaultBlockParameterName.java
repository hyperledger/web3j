package org.web3j.protocol.core;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * https://github.com/ethereum/wiki/wiki/JSON-RPC#the-default-block-parameter
 */
public enum DefaultBlockParameterName implements DefaultBlockParameter {
    EARLIEST("earliest"),
    LATEST("latest"),
    PENDING("pending");

    private String name;

    DefaultBlockParameterName(String name) {
        this.name = name;
    }

    @JsonValue
    @Override
    public String getValue() {
        return name;
    }

    public static DefaultBlockParameterName fromString(String name) {
        if (name != null) {
            for (DefaultBlockParameterName defaultBlockParameterName:
                    DefaultBlockParameterName.values()) {
                if (name.equalsIgnoreCase(defaultBlockParameterName.name)) {
                    return defaultBlockParameterName;
                }
            }
        }
        return valueOf(name);
    }
}
