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

/** Statically allocated sequence of bytes. */
public class Bytes extends BytesType {

    public static final String TYPE_NAME = "bytes";

    protected Bytes(int byteSize, byte[] value) {
        super(value, TYPE_NAME + value.length);
        if (!isValid(byteSize)) {
            throw new UnsupportedOperationException(
                    "Input byte array must be in range 0 < M <= 32 and length must match type");
        }
    }

    private boolean isValid(int byteSize) {
        int length = getValue().length;
        return length > 0 && length <= 32 && length == byteSize;
    }
}
