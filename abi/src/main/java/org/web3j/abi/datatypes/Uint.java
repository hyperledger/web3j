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

/** Unsigned integer type. */
public class Uint extends IntType {

    public static final String TYPE_NAME = "uint";
    public static final Uint DEFAULT = new Uint(BigInteger.ZERO);

    /** This constructor is required by the {@link Address} type. */
    Uint(String typePrefix, int bitSize, BigInteger value) {
        super(typePrefix, bitSize, value);
    }

    protected Uint(int bitSize, BigInteger value) {
        this(TYPE_NAME, bitSize, value);
    }

    public Uint(BigInteger value) {
        // "int" values should be declared as int256 in computing function selectors
        this(MAX_BIT_LENGTH, value);
    }

    @Override
    boolean valid(int bitSize, BigInteger value) {
        return super.valid(bitSize, value) && value.signum() != -1;
    }
}
