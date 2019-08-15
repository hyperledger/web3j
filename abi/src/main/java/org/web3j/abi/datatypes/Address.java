/*
 * Copyright 2019 Web3 Labs LTD.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.web3j.abi.datatypes;

import java.math.BigInteger;

import org.web3j.utils.Numeric;

/** Address type, which by default is equivalent to uint160 which follows the Ethereum specification. */
public class Address implements Type<String> {

    public static final String TYPE_NAME = "address";
    public static final int DEFAULT_LENGTH = 160;
    public static final Address DEFAULT = new Address(BigInteger.ZERO);

    private final Uint value;

    public Address(Uint value) {
        this.value = value;
    }

    public Address(BigInteger value) {
        this(DEFAULT_LENGTH, value);
    }

    public Address(int bitSize, BigInteger value) {
        this(new Uint(bitSize, value));
    }

    public Address(String hexValue) {
        this(DEFAULT_LENGTH, hexValue);
    }

    public Address(int bitSize, String hexValue) {
        this(bitSize, Numeric.toBigInt(hexValue));
    }

    public Uint toUint() {
        return value;
    }

    @Override
    public String getTypeAsString() {
        return TYPE_NAME;
    }

    @Override
    public String toString() {
        return Numeric.toHexStringWithPrefixZeroPadded(value.getValue(), value.getBitSize() >> 2);
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

        return value != null ? value.value.equals(address.value.value) : address.value == null;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
