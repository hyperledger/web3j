package org.web3j.protocol.core.methods.response;

import org.web3j.protocol.core.Response;

/**
 * db_putHex
 */
public class DbPutHex extends Response<Boolean> {

    public boolean valueStored() {
        return getResult();
    }
}
