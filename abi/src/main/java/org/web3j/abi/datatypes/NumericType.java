/*
 * Copyright 2019 Web3 Labs Ltd.
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

/** Common numeric type. */
public abstract class NumericType implements Type<BigInteger> {

    private final String type;
    BigInteger value;

    public NumericType(final String type, final BigInteger value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public String getTypeAsString() {
        return type;
    }

    @Override
    public BigInteger getValue() {
        return value;
    }

    public abstract int getBitSize();

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final NumericType that = (NumericType) o;

        if (!type.equals(that.type)) {
            return false;
        }

        return value != null ? value.equals(that.value) : that.value == null;
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
