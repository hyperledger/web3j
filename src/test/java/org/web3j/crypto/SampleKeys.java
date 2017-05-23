package org.web3j.crypto;

import java.math.BigInteger;

import org.web3j.utils.Numeric;

/**
 * Keys generated for unit testing purposes.
 */
public class SampleKeys {

    public static final String PRIVATE_KEY_STRING =
            "a392604efc2fad9c0b3da43b5f698a2e3f270f170d859912be0d54742275c5f6";
    static final String PUBLIC_KEY_STRING =
            "0x506bc1dc099358e5137292f4efdd57e400f29ba5132aa5d12b18dac1c1f6aab"
            + "a645c0b7b58158babbfa6c6cd5a48aa7340a8749176b120e8516216787a13dc76";
    public static final String ADDRESS = "0xef678007d18427e6022059dbc264f27507cd1ffc";
    public static final String ADDRESS_NO_PREFIX = Numeric.cleanHexPrefix(ADDRESS);

    public static final String PASSWORD = "Insecure Pa55w0rd";

    static final BigInteger PRIVATE_KEY = Numeric.toBigInt(PRIVATE_KEY_STRING);
    static final BigInteger PUBLIC_KEY = Numeric.toBigInt(PUBLIC_KEY_STRING);

    static final ECKeyPair KEY_PAIR = new ECKeyPair(PRIVATE_KEY, PUBLIC_KEY);

    public static final Credentials CREDENTIALS = Credentials.create(KEY_PAIR);

    private SampleKeys() {}
}
