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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;

import org.web3j.protocol.Web3j;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import static org.web3j.codegen.unit.gen.utills.NameUtils.toCamelCase;

/*
    Class that when given a method provides a JavaPoet method spec.
*/

public class MethodParser {
    private final Method method;
    private final Class theContract;

    MethodParser(final Method method, final Class theContract) {
        this.method = method;
        this.theContract = theContract;
    }

    MethodSpec getMethodSpec() {
        if (Arrays.asList(method.getParameterTypes())
                .containsAll(
                        Arrays.asList(
                                Web3j.class, TransactionManager.class, ContractGasProvider.class)))
            return new MethodSpecGenerator(
                            method.getName(),
                            generateStatementBody(),
                            defaultParameterSpecsForEachUnitTest())
                    .generate();
        else {
            return new MethodSpecGenerator(method.getName(), generateStatementBody()).generate();
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

    private Map<String, Object[]> generateStatementBody() {
        String genericStatement = ParserUtils.generateJavaPoetStringTypes(method, theContract);
        Object[] genericParameters = ParserUtils.getPlaceholderValues(method, theContract);
        Map<String, Object[]> methodBodySpecification = new LinkedHashMap<>();
        methodBodySpecification.put(genericStatement, genericParameters);
        return methodBodySpecification;
    }
}
