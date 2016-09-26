package org.web3j.abi.datatypes;

/**
 * Dynamically allocated sequence of bytes.
 */
public class DynamicBytesType extends BytesType {

    public DynamicBytesType(byte[] value) {
        super(value, "bytes");
    }
}
