package org.web3j.rlp;

/**
 * RLP string type.
 */
public class RlpString implements RlpType {
    private final String value;

    public RlpString(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
