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
package org.web3j.codegen.unit.gen;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;

import org.web3j.codegen.unit.gen.utils.MappingHelper;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import static org.web3j.codegen.unit.gen.utils.NameUtils.returnTypeAsLiteral;
import static org.web3j.codegen.unit.gen.utils.NameUtils.toCamelCase;

public abstract class Parser {

    protected final MappingHelper mappingHelper;
    protected final Class<?> theContract;
    protected final Method method;

    public Parser(Class<?> theContract, Method method, MappingHelper mappingHelper) {
        this.theContract = theContract;
        this.method = method;
        this.mappingHelper = mappingHelper;
    }

    protected abstract Object getDefaultValueForType(Class<?> type);

    protected abstract String generatePoetStringTypes();

    protected abstract String getPoetFormatSpecifier();

    protected final Object[] replaceTypeWithDefaultValue() {
        return Arrays.stream(method.getParameterTypes())
                .map(this::getDefaultValueForType)
                .toArray();
    }

    public final Type getMethodReturnType() {
        Type genericType = method.getGenericReturnType();
        if (genericType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            return parameterizedType.getActualTypeArguments()[0];
        } else {
            return genericType;
        }
    }

    protected final Type[] getTypeArray(Type type) {
        ParameterizedType parameterizedType = (ParameterizedType) type;
        return parameterizedType.getActualTypeArguments();
    }

    public final Object[] generateAssertionPlaceholderValues() {
        Type returnType = getMethodReturnType();
        Object[] body = generatePlaceholderValues();
        List<Object> placeHolder = new ArrayList<>();
        placeHolder.add(Assertions.class);
        if (!body[0].equals(TransactionReceipt.class)) {
            if (returnType.getTypeName().contains("Tuple")) {
                placeHolder.add(((ParameterizedType) returnType).getRawType());
                for (Type t : getTypeArray(returnType)) {
                    placeHolder.add(mappingHelper.getDefaultValueMap().get(t));
                }
            } else {
                placeHolder.add(mappingHelper.getDefaultValueMap().get(body[0]));
            }
        }
        placeHolder.add(toCamelCase(returnTypeAsLiteral(returnType, true)));
        return placeHolder.toArray();
    }

    public final Object[] generatePlaceholderValues() {
        return mergePlaceholderValues(concludeMethodReturnType(), replaceTypeWithDefaultValue());
    }

    protected final Object[] mergePlaceholderValues(Object[] source1, Object[] source2) {
        Object[] destination = new Object[source1.length + source2.length];
        System.arraycopy(source1, 0, destination, 0, source1.length);
        System.arraycopy(source2, 0, destination, source1.length, source2.length);
        return destination;
    }

    protected final Object[] concludeMethodReturnType() {
        Type returnType = getMethodReturnType();
        if (returnType.equals(theContract)) {
            return new Object[] {toCamelCase(returnTypeAsLiteral(returnType, false)), returnType};
        } else {
            return new Object[] {
                returnType,
                toCamelCase(returnTypeAsLiteral(returnType, true)),
                toCamelCase(theContract)
            };
        }
    }
}
