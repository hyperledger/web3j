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

import java.util.List;

/** Dynamic array type. */
public class DynamicArray<T extends Type> extends Array<T> {

    @Deprecated
    @SafeVarargs
    @SuppressWarnings({"unchecked"})
    public DynamicArray(final T... values) {
        super(
                StructType.class.isAssignableFrom(values[0].getClass())
                        ? (Class<T>) values[0].getClass()
                        : (Class<T>) AbiTypes.getType(values[0].getTypeAsString()),
                values);
    }

    @Deprecated
    @SuppressWarnings("unchecked")
    public DynamicArray(final List<T> values) {
        super(
                StructType.class.isAssignableFrom(values.get(0).getClass())
                        ? (Class<T>) values.get(0).getClass()
                        : (Class<T>) AbiTypes.getType(values.get(0).getTypeAsString()),
                values);
    }

    @Deprecated
    @SuppressWarnings("unchecked")
    private DynamicArray(final String type) {
        super((Class<T>) AbiTypes.getType(type));
    }

    @Deprecated
    public static DynamicArray empty(final String type) {
        return new DynamicArray(type);
    }

    public DynamicArray(final Class<T> type, final List<T> values) {
        super(type, values);
    }

    @Override
    public int bytes32PaddedLength() {
        return super.bytes32PaddedLength() + MAX_BYTE_LENGTH;
    }

    @SafeVarargs
    public DynamicArray(final Class<T> type, final T... values) {
        super(type, values);
    }

    @Override
    public String getTypeAsString() {
        String type;
        if (StructType.class.isAssignableFrom(value.get(0).getClass())) {
            type = value.get(0).getTypeAsString();
        } else {
            type = AbiTypes.getTypeAString(getComponentType());
        }
        return type + "[]";
    }
}
