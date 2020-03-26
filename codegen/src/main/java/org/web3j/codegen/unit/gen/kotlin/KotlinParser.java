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
package org.web3j.codegen.unit.gen.kotlin;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.web3j.codegen.unit.gen.Parser;
import org.web3j.codegen.unit.gen.utils.MappingHelper;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import static org.web3j.codegen.unit.gen.utils.NameUtils.toCamelCase;

public class KotlinParser extends Parser {

    public KotlinParser(
            final Class<?> theContract, final Method method, final MappingHelper mappingHelper) {
        super(theContract, method, mappingHelper);
    }

    public String generateAssertionKotlinPoetStringTypes() {
        final Type returnType = getMethodReturnType();
        final Object[] body = generatePlaceholderValues();
        final StringBuilder symbolBuilder = new StringBuilder();
        symbolBuilder.append("%T.");
        if (returnType == TransactionReceipt.class) {
            symbolBuilder.append("assertTrue(%L.isStatusOK())");
        } else {
            symbolBuilder.append("assertEquals(");
            if (returnType.getTypeName().contains("Tuple")) {
                symbolBuilder.append(" %T(");
                for (final Type t : getTypeArray(returnType)) {
                    symbolBuilder.append(mappingHelper.getPoetFormat().get(t)).append(", ");
                }
                symbolBuilder.deleteCharAt(symbolBuilder.lastIndexOf(", "));
                symbolBuilder.append(")");
            } else {
                symbolBuilder.append(mappingHelper.getPoetFormat().get(body[0]));
            }
            symbolBuilder.append(", ");
            symbolBuilder.append("%L");
            symbolBuilder.append(")");
        }
        return symbolBuilder.toString();
    }

    protected final Object[] adjustPlaceholderValues() {
        if (getMethodReturnType().equals(theContract)) {
            return mergePlaceholderValues(
                    concludeMethodReturnType(), replaceTypeWithDefaultValue());
        }
        return mergePlaceholderValues(
                Arrays.copyOfRange(
                        concludeMethodReturnType(), 1, concludeMethodReturnType().length),
                replaceTypeWithDefaultValue());
    }

    @Override
    protected String generatePoetStringTypes() {
        final StringBuilder symbolBuilder = new StringBuilder();
        if (getMethodReturnType().equals(theContract)) {
            symbolBuilder.append(" %L = %T.");
        } else {
            symbolBuilder.append("val %L = %L.");
        }
        symbolBuilder
                .append(method.getName())
                .append("(")
                .append(getPoetFormatSpecifier())
                .append(").send()");

        return symbolBuilder.toString();
    }

    @Override
    protected String getPoetFormatSpecifier() {
        final List<String> generated = new ArrayList<>();
        Arrays.asList(method.getParameterTypes())
                .forEach(
                        type ->
                                generated.add(
                                        mappingHelper.getPoetFormat().getOrDefault(type, "%L")));
        return String.join(", ", generated);
    }

    @Override
    protected Object getDefaultValueForType(final Class<?> type) {
        if (mappingHelper.getDefaultValueMap().containsKey(type)) {
            return mappingHelper.getDefaultValueMap().get(type);
        } else {
            return toCamelCase(type);
        }
    }
}
