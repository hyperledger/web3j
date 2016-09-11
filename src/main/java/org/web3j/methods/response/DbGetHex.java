package org.web3j.methods.response;

import org.web3j.protocol.jsonrpc20.Response;

/**
 * db_getHex
 */
public class DbGetHex extends Response<String> {

    public String getStoredValue() {
        return getResult();
    }
}
