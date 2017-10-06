package org.web3j.abi.datatypes;

import java.math.BigInteger;

import org.web3j.abi.datatypes.generated.Uint160;
import org.web3j.crypto.Keys;
import org.web3j.utils.Numeric;

/**
 * Address type, which is equivalent to uint160.
 */
public class Address implements Type<String> {

    public static final String TYPE_NAME = "address";
    public static final int LENGTH = 160;
    public static final Address DEFAULT = new Address(BigInteger.ZERO);

    private final Uint160 value;

    public Address(Uint160 value) {
        this.value = value;
    }

    public Address(BigInteger value) {
        this(new Uint160(value));
    }

    public Address(String hexValue) {
        this(Numeric.toBigInt(hexValue));
    }

    public Uint160 toUint160() {
        return value;
    }

    @Override
    public String getTypeAsString() {
        return TYPE_NAME;
    }

    @Override
    public String toString() {
        return Numeric.toHexStringWithPrefixZeroPadded(
                value.getValue(), Keys.ADDRESS_LENGTH_IN_HEX);
    }

    @Override
    public String getValue() {
        return toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Address address = (Address) o;

        return value != null ? value.equals(address.value) : address.value == null;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
