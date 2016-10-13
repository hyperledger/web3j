package org.web3j.crypto;

import java.math.BigInteger;

import org.web3j.utils.Numeric;

/**
 * Keys generated for unit testing purposes.
 */
public class SampleKeys {

    static final BigInteger PRIVATE_KEY = Numeric.toBigInt(
            "a2d27ba84871112bb2ab87d849b8bce790667762fd7f30981ea775880c691e45");
    static final BigInteger PUBLIC_KEY = Numeric.toBigInt(
            "0x54c8cda130d3bfda86bd698cee738e5e502abc1fcb9e45709ee1fe38e855cda" +
                    "334ca6f9288ab6d867f6baa2b2afeced0478e6a7225a5b1bb263ab21611817507");

    static final ECKeyPair KEY_PAIR = new ECKeyPair(PRIVATE_KEY, PUBLIC_KEY);

    private SampleKeys() {}
}
