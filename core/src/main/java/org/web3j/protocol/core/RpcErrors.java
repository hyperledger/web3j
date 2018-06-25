package org.web3j.protocol.core;

public final class RpcErrors {
    private RpcErrors() {}

    public static final int FILTER_NOT_FOUND = -32000;

    public static final int PARSE_ERROR = -32700;

    public static final int INVALID_REQUEST = -32601;

    public static final int INVALID_PARAMS = -32602;

    public static final int INTERNAL_ERROR = -32603;
}
