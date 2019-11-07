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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;

import org.web3j.codegen.unit.gen.templates.BalanceOf;
import org.web3j.codegen.unit.gen.templates.Deploy;
import org.web3j.codegen.unit.gen.templates.GenericTemplate;
import org.web3j.codegen.unit.gen.templates.Load;
import org.web3j.codegen.unit.gen.templates.TotalSupply;
import org.web3j.codegen.unit.gen.templates.Transfer;
import org.web3j.codegen.unit.gen.templates.TransferFrom;
import org.web3j.protocol.Web3j;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import static org.web3j.codegen.unit.gen.utills.NameUtils.toCamelCase;

public class UnitMethodGenerator {
    private final Method method;
    private final Class tClass;

    UnitMethodGenerator(final Method method, final Class tClass) {
        this.method = method;
        this.tClass = tClass;
    }

    MethodSpec getMethodSpec() {
        if (method.getName().equals("deploy")) {
            return new Deploy(
                            tClass,
                            getMethodReturnType(method),
                            Arrays.asList(method.getParameterTypes()),
                            defaultParameterSpecsForEachUnitTest())
                    .generate();
        } else if (method.getName().equals("transferFrom")) {
            return new TransferFrom(
                            tClass,
                            getMethodReturnType(method),
                            Arrays.asList(method.getParameterTypes()),
                            defaultParameterSpecsForEachUnitTest())
                    .generate();
        } else if (method.getName().equals("load")) {
            return new Load(
                            tClass,
                            getMethodReturnType(method),
                            Arrays.asList(method.getParameterTypes()),
                            defaultParameterSpecsForEachUnitTest())
                    .generate();
        } else if (method.getName().equals("balanceOf")) {

            return new BalanceOf(
                            tClass,
                            getMethodReturnType(method),
                            Arrays.asList(method.getParameterTypes()),
                            defaultParameterSpecsForEachUnitTest())
                    .generate();
        } else if (method.getName().equals("transfer")) {
            return new Transfer(
                            tClass,
                            getMethodReturnType(method),
                            Arrays.asList(method.getParameterTypes()),
                            defaultParameterSpecsForEachUnitTest())
                    .generate();
        } else if (method.getName().equals("totalSupply")) {
            return new TotalSupply(
                            tClass,
                            getMethodReturnType(method),
                            Arrays.asList(method.getParameterTypes()),
                            defaultParameterSpecsForEachUnitTest())
                    .generate();
        } else {
            return new GenericTemplate(
                            tClass,
                            getMethodReturnType(method),
                            Arrays.asList(method.getParameterTypes()),
                            defaultParameterSpecsForEachUnitTest(),
                            method.getName())
                    .generate();
        }
    }

    private List<ParameterSpec> defaultParameterSpecsForEachUnitTest() {
        List<ParameterSpec> listOfArguments = new ArrayList<>();
        listOfArguments.add(ParameterSpec.builder(Web3j.class, toCamelCase(Web3j.class)).build());
        listOfArguments.add(
                ParameterSpec.builder(
                                TransactionManager.class, toCamelCase(TransactionManager.class))
                        .build());
        listOfArguments.add(
                ParameterSpec.builder(
                                ContractGasProvider.class, toCamelCase(ContractGasProvider.class))
                        .build());
        return listOfArguments;
    }

    private Type getMethodReturnType(Method method) {
        Type genericType = method.getGenericReturnType();
        if (genericType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            return parameterizedType.getActualTypeArguments()[0];
        } else {
            return genericType;
        }
    }
}
