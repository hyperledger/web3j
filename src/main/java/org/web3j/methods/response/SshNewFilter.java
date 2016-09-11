package org.web3j.methods.response;

import org.web3j.protocol.Utils;
import org.web3j.protocol.jsonrpc20.Response;

import java.math.BigInteger;

/**
 * shh_newFilter
 */
public class SshNewFilter extends Response<String> {

    public BigInteger getFilterId() {
        return Utils.decodeQuantity(getResult());
    }
}
