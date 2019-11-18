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
package org.web3j.codegen.unit.gen;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testcontainers.shaded.org.apache.commons.lang.ArrayUtils;

import org.web3j.tuples.Tuple;

import static org.web3j.codegen.unit.gen.utills.NameUtils.returnTypeAsLiteral;
import static org.web3j.codegen.unit.gen.utills.NameUtils.toCamelCase;

/*
       Class that provides parsing utility for JavaPoet.
*/

public class ParserUtils {

    public static String getJavaPoetStringFormatFromTypes(List<Class> types) {
        List<String> generated = new ArrayList<>();
        for (Class type : types) {
            if (type.equals(String.class)) {
                generated.add("$S");
            } else if (type.equals(BigInteger.class)) {
                generated.add("$T.ONE");
            } else if (type.equals(List.class)) {
                generated.add("new $T<>()");
            } else if (type.equals(Tuple.class)) {
                generated.add("new $T<>()");
            } else if (type.equals(byte[].class)) {
                generated.add("new $T{}");
            } else {
                generated.add("$L");
            }
        }
        return String.join(",", generated);
    }

    static Type getMethodReturnType(Method method) {
        Type genericType = method.getGenericReturnType();
        if (genericType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            return parameterizedType.getActualTypeArguments()[0];
        } else {
            return genericType;
        }
    }

    private static Object getDefaultValueForType(Class type) {
        if (type.equals(String.class)) {
            return "REPLACE_ME";
        } else if (type.equals(BigInteger.class)) {
            return BigInteger.class;
        } else if (type.equals(List.class)) {
            return ArrayList.class;
        } else if (type.equals(Tuple.class)) {
            return Tuple.class;
        } else if (type.equals(byte[].class)) {
            return byte[].class;
        } else {
            return toCamelCase(type);
        }
    }

    private static Object[] dynamicArguments(Method method) {
        return Arrays.stream(method.getParameterTypes())
                .map(ParserUtils::getDefaultValueForType)
                .toArray();
    }

    static Object[] getPlaceholderValues(Method method, Class contract) {
        Type returnType = getMethodReturnType(method);
        if (returnType.equals(contract)) {
            return ArrayUtils.addAll(
                    new Object[] {
                        toCamelCase(returnTypeAsLiteral(returnType, false)), returnType,
                    },
                    dynamicArguments(method));

        } else {
            return ArrayUtils.addAll(
                    new Object[] {
                        returnType,
                        toCamelCase(returnTypeAsLiteral(returnType, true)),
                        toCamelCase(contract)
                    },
                    dynamicArguments(method));
        }
    }

    static String generateJavaPoetStringTypes(Method method, Class theContract) {
        StringBuilder symbolBuilder = new StringBuilder();
        if (ParserUtils.getMethodReturnType(method).equals(theContract)) {
            symbolBuilder.append("$L");
        } else {
            symbolBuilder.append("$T");
            symbolBuilder.append(" ");
            symbolBuilder.append("$L");
        }
        symbolBuilder.append("=");
        if (ParserUtils.getMethodReturnType(method).equals(theContract)) {
            symbolBuilder.append("$T.");
        } else {
            symbolBuilder.append("$L.");
        }
        symbolBuilder.append(method.getName());
        symbolBuilder.append("(");
        symbolBuilder.append(
                ParserUtils.getJavaPoetStringFormatFromTypes(
                        Arrays.asList(method.getParameterTypes())));
        symbolBuilder.append(").send()");
        return symbolBuilder.toString();
    }
}
