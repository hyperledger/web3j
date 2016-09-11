package org.web3j.methods.response;

import org.web3j.protocol.jsonrpc20.Response;

/**
 * db_putHex
 */
public class DbPutHex extends Response<Boolean> {

    public boolean valueStored() {
        return getResult();
    }
}
