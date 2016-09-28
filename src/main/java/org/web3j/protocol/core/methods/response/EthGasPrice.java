package org.web3j.protocol.core.methods.response;

import java.math.BigInteger;

import org.web3j.utils.Numeric;
import org.web3j.protocol.core.Response;

/**
 * eth_gasPrice
 */
public class EthGasPrice extends Response<String> {
    public BigInteger getGasPrice() {
        return Numeric.decodeQuantity(getResult());
    }
}
