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
package org.web3j.codegen.unit.gen.java;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.web3j.codegen.unit.gen.Parser;
import org.web3j.codegen.unit.gen.utils.MappingHelper;

import static org.web3j.codegen.unit.gen.utils.NameUtils.toCamelCase;

/*
 * Class that provides parsing utility between Unit Generation and JavaPoet.
 */
public class JavaParser extends Parser {

    public JavaParser(
            final Class<?> theContract, final Method method, final MappingHelper mappingHelper) {
        super(theContract, method, mappingHelper);
    }

    public String generateAssertionJavaPoetStringTypes() {
        final Type returnType = getMethodReturnType();
        final Object[] body = generatePlaceholderValues();
        final StringBuilder symbolBuilder = new StringBuilder();
        symbolBuilder.append("$T.");
        if (isTransactionalMethod()) {
            symbolBuilder.append("assertTrue(transactionReceipt.isStatusOK())");
        } else {
            symbolBuilder.append("assertEquals(");
            if (returnType.getTypeName().contains("Tuple")) {
                symbolBuilder.append("new $T(");
                for (final Type t : getTypeArray(returnType)) {
                    symbolBuilder.append(mappingHelper.getPoetFormat().get(t)).append(", ");
                }
                symbolBuilder.deleteCharAt(symbolBuilder.lastIndexOf(", "));
                symbolBuilder.append(")");
            } else {
                symbolBuilder.append(mappingHelper.getPoetFormat().get(body[0]));
            }
            symbolBuilder.append(", ");
            symbolBuilder.append("$L");
            symbolBuilder.append(")");
        }

        return symbolBuilder.toString();
    }

    @Override
    public String generatePoetStringTypes() {
        final StringBuilder symbolBuilder = new StringBuilder();
        if (isDeployOrLoadMethod()) {
            symbolBuilder.append("$L = $T.");
        } else {
            if (isTransactionalMethod()) {
                symbolBuilder.append("$T transactionReceipt = $L.");

            } else {
                symbolBuilder.append("$T $L = $L.");
            }
        }
        symbolBuilder.append(method.getName()).append("(").append(getPoetFormatSpecifier());

        if (isTransactionalMethod() || isDeployOrLoadMethod()) {
            symbolBuilder.append(").send()");
        } else {
            symbolBuilder.append(").call()");
        }

        return symbolBuilder.toString();
    }

    @Override
    protected Object getDefaultValueForType(final Class<?> type) {
        if (mappingHelper.getDefaultValueMap().containsKey(type)) {
            return mappingHelper.getDefaultValueMap().get(type);
        } else {
            return toCamelCase(type);
        }
    }

    @Override
    protected String getPoetFormatSpecifier() {
        final List<String> generated = new ArrayList<>();
        Arrays.asList(method.getParameterTypes())
                .forEach(
                        type ->
                                generated.add(
                                        mappingHelper.getPoetFormat().getOrDefault(type, "$L")));
        return String.join(", ", generated);
    }
}
