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
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.lang.model.element.Modifier;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import org.junit.jupiter.api.BeforeAll;

import org.web3j.codegen.unit.gen.utils.JavaMappingHelper;
import org.web3j.protocol.Web3j;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import static org.web3j.codegen.unit.gen.utils.NameUtils.toCamelCase;

/*Class that when given a method provides a JavaPoet method spec. */
public class MethodParser {
    private final Method method;
    private final Class theContract;

    public MethodParser(final Method method, final Class theContract) {
        this.method = method;
        this.theContract = theContract;
    }

    public MethodSpec getMethodSpec() {
        return methodNeedsInjection()
                ? new MethodSpecGenerator(
                                method.getName(),
                                BeforeAll.class,
                                Modifier.STATIC,
                                defaultParameterSpecsForEachUnitTest(),
                                generateStatementBody())
                        .generate()
                : new MethodSpecGenerator(method.getName(), generateStatementBody()).generate();
    }

    private boolean methodNeedsInjection() {
        return Arrays.asList(method.getParameterTypes())
                .containsAll(
                        Arrays.asList(
                                Web3j.class, TransactionManager.class, ContractGasProvider.class));
    }

    private List<ParameterSpec> defaultParameterSpecsForEachUnitTest() {
        return Stream.of(
                        ParameterSpec.builder(Web3j.class, toCamelCase(Web3j.class)).build(),
                        ParameterSpec.builder(
                                        TransactionManager.class,
                                        toCamelCase(TransactionManager.class))
                                .build(),
                        ParameterSpec.builder(
                                        ContractGasProvider.class,
                                        toCamelCase(ContractGasProvider.class))
                                .build())
                .collect(Collectors.toList());
    }

    private Map<String, Object[]> generateStatementBody() {
        Map<String, Object[]> methodBodySpecification = new LinkedHashMap<>();
        JavaParser parser = new JavaParser(theContract, method, new JavaMappingHelper());
        String javaPoetStringTypes = parser.generatePoetStringTypes();
        Object[] genericParameters = parser.generatePlaceholderValues();
        methodBodySpecification.put(javaPoetStringTypes, genericParameters);
        if (methodNeedsAssertion()) {
            String assertionJavaPoet = parser.generateAssertionJavaPoetStringTypes();
            Object[] assertionParams = parser.generateAssertionPlaceholderValues();
            methodBodySpecification.put(assertionJavaPoet, assertionParams);
        }
        return methodBodySpecification;
    }

    private boolean methodNeedsAssertion() {
        return !methodNeedsInjection();
    }
}
