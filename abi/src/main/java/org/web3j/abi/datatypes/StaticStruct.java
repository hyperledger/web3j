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

// TODO: provide a base interface called "StructType"
public class StaticStruct extends StaticArray<Type> implements BasicStruct {

    public List<Class<Type>> getItemTypes() {
        return itemTypes;
    }

    private final List<Class<Type>> itemTypes = new ArrayList<>();

    public StaticStruct(List<Type> values) {
        super(Type.class, values.size(), values);
        for (Type value : values) {
            itemTypes.add((Class<Type>) value.getClass());
        }
    }

    @SafeVarargs
    public StaticStruct(Type... values) {
        this(Arrays.asList(values));
    }

    @Override
    public String getTypeAsString() {
        // "(uint256,string)"
        return "("
                + itemTypes.stream().map(AbiTypes::getTypeAString).collect(Collectors.joining(","))
                + ")";
    }
}
