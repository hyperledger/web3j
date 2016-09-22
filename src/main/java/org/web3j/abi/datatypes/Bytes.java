package org.web3j.abi.datatypes;

/**
 * Binary sequence of bytes.
 */
public class Bytes implements Type<byte[]> {

    private byte[] value;
    private String type;

    public Bytes(byte[] src, String type) {
        this.value = src;
        this.type = type;
    }

    @Override
    public byte[] getValue() {
        return value;
    }

    @Override
    public String getTypeAsString() {
        return type;
    }
}
