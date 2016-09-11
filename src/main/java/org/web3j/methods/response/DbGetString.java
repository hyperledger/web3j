package org.web3j.methods.response;

import org.web3j.protocol.jsonrpc20.Response;

/**
 * db_getString
 */
public class DbGetString extends Response<String> {

    public String getStoredValue() {
        return getResult();
    }
}
