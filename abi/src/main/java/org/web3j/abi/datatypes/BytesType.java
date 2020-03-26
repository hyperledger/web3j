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

import java.util.Arrays;

/** Binary sequence of bytes. */
public abstract class BytesType implements Type<byte[]> {

    private final byte[] value;
    private final String type;

    public BytesType(final byte[] src, final String type) {
        this.value = src;
        this.type = type;
    }

    @Override
    public int bytes32PaddedLength() {
        return value.length <= 32
                ? MAX_BYTE_LENGTH
                : (value.length / MAX_BYTE_LENGTH + 1) * MAX_BYTE_LENGTH;
    }

    @Override
    public byte[] getValue() {
        return value;
    }

    @Override
    public String getTypeAsString() {
        return type;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final BytesType bytesType = (BytesType) o;

        if (!Arrays.equals(value, bytesType.value)) {
            return false;
        }
        return type.equals(bytesType.type);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(value);
        result = 31 * result + type.hashCode();
        return result;
    }
}
