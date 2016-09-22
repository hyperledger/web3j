package org.web3j.abi.datatypes;

/**
 * Dynamically allocated sequence of bytes.
 */
public class DynamicBytes extends Bytes {

    public DynamicBytes(byte[] src) {
        super(src, "bytes");
    }
}
