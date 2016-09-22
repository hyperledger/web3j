package org.web3j.abi.datatypes;

import java.math.BigInteger;

import org.web3j.protocol.utils.Codec;

/**
 * Address type, which is equivalent to uint160.
 */
public class Address extends Uint {
    public Address(BigInteger value) {
        super("address", 160, value);
    }

    public Address(String hexValue) {
        this(Codec.toBigInt(hexValue));
    }
}
