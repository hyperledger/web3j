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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DynamicStruct extends DynamicArray<Type> implements BasicStruct {

    public List<Class<Type>> getItemTypes() {
        return itemTypes;
    }

    private final List<Class<Type>> itemTypes = new ArrayList<>();

    public DynamicStruct(List<Type> values) {
        this(Type.class, values);
    }

    private DynamicStruct(Class<Type> type, List<Type> values) {
        super(type, values);
        for (Type value : values) {
            itemTypes.add((Class<Type>) value.getClass());
        }
    }

    public DynamicStruct(Type... values) {
        this(Arrays.asList(values));
    }

    @SafeVarargs
    public DynamicStruct(Class<Type> type, Type... values) {
        this(type, Arrays.asList(values));
    }

    @Override
    public String getTypeAsString() {
        // "(uint256,string)"
        final StringBuilder type = new StringBuilder("(");
        type.append(
                itemTypes.stream()
                        .map(t -> AbiTypes.getTypeAString(t))
                        .collect(Collectors.joining(",")));
        type.append(")");
        return type.toString();
    }
}
