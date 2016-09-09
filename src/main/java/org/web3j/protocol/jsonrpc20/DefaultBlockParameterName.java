package org.web3j.protocol.jsonrpc20;

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

    @Override
    public String getValue() {
        return name;
    }
}
