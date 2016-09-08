package org.conor10.web3j.protocol.jsonrpc20;

/**
 * https://github.com/ethereum/wiki/wiki/JSON-RPC#the-default-block-parameter
 */
public enum DefaultBlockParamTag {
    EARLIEST("earliest"),
    LATEST("latest"),
    PENDING("pending");

    private String value;

    DefaultBlockParamTag(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
