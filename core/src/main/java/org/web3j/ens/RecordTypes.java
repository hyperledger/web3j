package org.web3j.ens;

import org.web3j.utils.Numeric;

/**
 * Record type interfaces supported by resolvers as per
 * <a href="https://github.com/ethereum/EIPs/blob/master/EIPS/eip-137.md#resolver-specification">EIP-137</a>
 */
public class RecordTypes {

    public static final byte[] ADDR = Numeric.hexStringToByteArray("0x3b3b57de");
    public static final byte[] NAME = Numeric.hexStringToByteArray("0x691f3431");
    public static final byte[] ABI = Numeric.hexStringToByteArray("0x2203ab56");
    public static final byte[] PUB_KEY = Numeric.hexStringToByteArray("0xc8690233");
}
