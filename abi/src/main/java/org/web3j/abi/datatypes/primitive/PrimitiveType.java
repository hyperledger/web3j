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
package org.web3j.abi.datatypes.primitive;

import java.io.Serializable;
import java8.util.Objects;

import org.web3j.abi.datatypes.Type;

public abstract class PrimitiveType<T extends Serializable & Comparable<T>> implements Type<T> {

    private final String type;
    private final T value;

    PrimitiveType(final T value) {
        this.type = getClass().getSimpleName().toLowerCase();
        this.value = value;
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public String getTypeAsString() {
        return type;
    }

    public abstract Type toSolidityType();

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PrimitiveType<?> that = (PrimitiveType<?>) o;
        return type.equals(that.type) && value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value);
    }
}
