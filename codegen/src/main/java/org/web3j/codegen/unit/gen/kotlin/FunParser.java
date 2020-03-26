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
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.squareup.kotlinpoet.FunSpec;
import com.squareup.kotlinpoet.ParameterSpec;
import org.junit.jupiter.api.BeforeAll;

import org.web3j.codegen.unit.gen.utils.KotlinMappingHelper;
import org.web3j.protocol.Web3j;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import static org.web3j.codegen.unit.gen.utils.NameUtils.toCamelCase;

public class FunParser {
    private final Method method;
    private final Class theContract;

    public FunParser(final Method method, final Class theContract) {
        this.method = method;
        this.theContract = theContract;
    }

    public FunSpec getFunSpec() {
        return methodNeedsInjection()
                ? new FunSpecGenerator(
                                method.getName(),
                                BeforeAll.class,
                                defaultParameterSpecsForEachUnitTest(),
                                generateStatementBody())
                        .generate()
                : new FunSpecGenerator(method.getName(), generateStatementBody()).generate();
    }

    private boolean methodNeedsInjection() {
        return Arrays.asList(method.getParameterTypes())
                .containsAll(
                        Arrays.asList(
                                Web3j.class, TransactionManager.class, ContractGasProvider.class));
    }

    private List<ParameterSpec> defaultParameterSpecsForEachUnitTest() {
        return Stream.of(
                        ParameterSpec.builder(toCamelCase(Web3j.class), Web3j.class).build(),
                        ParameterSpec.builder(
                                        toCamelCase(TransactionManager.class),
                                        TransactionManager.class)
                                .build(),
                        ParameterSpec.builder(
                                        toCamelCase(ContractGasProvider.class),
                                        ContractGasProvider.class)
                                .build())
                .collect(Collectors.toList());
    }

    private Map<String, Object[]> generateStatementBody() {
        final Map<String, Object[]> methodBodySpecification = new LinkedHashMap<>();
        final KotlinParser parser =
                new KotlinParser(theContract, method, new KotlinMappingHelper());
        final String kotlinPoetStringTypes = parser.generatePoetStringTypes();
        final Object[] genericParameters = parser.adjustPlaceholderValues();
        methodBodySpecification.put(kotlinPoetStringTypes, genericParameters);
        if (methodNeedsAssertion()) {
            final String assertionKotlinPoet = parser.generateAssertionKotlinPoetStringTypes();
            final Object[] assertionParams = parser.generateAssertionPlaceholderValues();
            methodBodySpecification.put(assertionKotlinPoet, assertionParams);
        }
        return methodBodySpecification;
    }

    private boolean methodNeedsAssertion() {
        return !methodNeedsInjection();
    }
}
