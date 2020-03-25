/*
 * Copyright 2020 Web3 Labs Ltd.
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
package org.web3j.codegen.unit.gen.utils;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.web3j.tuples.Tuple;
import org.web3j.tuples.generated.Tuple2;

public class KotlinMappingHelper implements MappingHelper {
    private Map<Class<?>, Object> defaultValueMapKotlin = new HashMap<>();
    private Map<Class<?>, String> kotlinPoetFormat = new HashMap<>();

    public KotlinMappingHelper() {

        defaultValueMapKotlin.put(String.class, "REPLACE_ME");
        defaultValueMapKotlin.put(BigInteger.class, BigInteger.class);
        defaultValueMapKotlin.put(List.class, "arrayListOf()");
        defaultValueMapKotlin.put(Tuple.class, Tuple.class);
        defaultValueMapKotlin.put(byte[].class, "byteArrayOf()");
        defaultValueMapKotlin.put(Boolean.class, true);
        kotlinPoetFormat.put(Boolean.class, "%L");
        kotlinPoetFormat.put(String.class, "%S");
        kotlinPoetFormat.put(BigInteger.class, "%T.ONE");
        kotlinPoetFormat.put(List.class, " %L");
        kotlinPoetFormat.put(Tuple.class, "%T()");
        kotlinPoetFormat.put(Tuple2.class, " %T()");
        kotlinPoetFormat.put(byte[].class, " %L");
    }

    public Map<Class<?>, Object> getDefaultValueMap() {
        return defaultValueMapKotlin;
    }

    public Map<Class<?>, String> getPoetFormat() {
        return kotlinPoetFormat;
    }
}
