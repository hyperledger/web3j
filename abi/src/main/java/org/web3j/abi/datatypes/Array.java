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
import java.util.List;
import java.util.Objects;

/** Fixed size array. */
public abstract class Array<T extends Type<?>> implements Type<List<T>> {

    private final Class<T> type;
    protected final List<T> value;

    @SafeVarargs
    Array(final Class<T> type, final T... values) {
        this(type, Arrays.asList(values));
    }

    Array(final Class<T> type, final List<T> values) {
        checkValid(type, values);

        this.type = type;
        this.value = values;
    }

    @Override
    public int bytes32PaddedLength() {
        int length = 0;
        for (T t : value) {
            int valueLength = t.bytes32PaddedLength();
            length += valueLength;
        }
        return length;
    }

    @Override
    public List<T> getValue() {
        return value;
    }

    public Class<T> getComponentType() {
        return type;
    }

    @Override
    public abstract String getTypeAsString();

    private void checkValid(final Class<T> type, final List<T> values) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(values);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Array<?> array = (Array<?>) o;

        if (!type.equals(array.type)) {
            return false;
        }
        return Objects.equals(value, array.value);
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
