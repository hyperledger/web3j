package org.web3j.methods.response;

import java.math.BigInteger;

import org.web3j.protocol.utils.Codec;
import org.web3j.protocol.jsonrpc20.Response;

/**
 * shh_newFilter
 */
public class ShhNewFilter extends Response<String> {

    public BigInteger getFilterId() {
        return Codec.decodeQuantity(getResult());
    }
}
