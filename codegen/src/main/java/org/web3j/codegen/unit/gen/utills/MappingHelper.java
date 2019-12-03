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
package org.web3j.codegen.unit.gen.utills;

import java.math.BigInteger;
import java.util.*;

import org.web3j.tuples.Tuple;
import org.web3j.tuples.generated.Tuple2;

public class MappingHelper {
    private Map<Class, Object> defaultValueMap = new HashMap<>();
    private Map<Class, String> javaPoetFormat = new HashMap<>();

    public MappingHelper() {
        defaultValueMap.put(String.class, "REPLACE_ME");
        defaultValueMap.put(BigInteger.class, "BigInteger.ONE");
        defaultValueMap.put(List.class, ArrayList.class);
        defaultValueMap.put(Tuple.class, Tuple.class);
        defaultValueMap.put(byte[].class, byte[].class);
        defaultValueMap.put(Boolean.class, true);
        javaPoetFormat.put(Boolean.class, "$L");
        javaPoetFormat.put(String.class, "$S");
        javaPoetFormat.put(BigInteger.class, "$N");
        javaPoetFormat.put(List.class, "new $T<>()");
        javaPoetFormat.put(Tuple.class, "new $T<>()");
        javaPoetFormat.put(Tuple2.class, "new $T<>()");
        javaPoetFormat.put(byte[].class, "new $T{}");
    }

    public Map<Class, Object> getDefaultValueMap() {
        return defaultValueMap;
    }

    public Map<Class, String> getJavaPoetFormat() {
        return javaPoetFormat;
    }
}
