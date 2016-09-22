package org.web3j.abi.datatypes;

import java.nio.charset.StandardCharsets;

/**
 * UTF-8 encoded string type.
 */
public class Utf8String implements Type<String> {

    private String value;

    public Utf8String(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getTypeAsString() {
        return "string";
    }
}
