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
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import org.web3j.codegen.unit.gen.MethodFilter;
import org.web3j.codegen.unit.gen.Parser;
import org.web3j.codegen.unit.gen.utils.JavaMappingHelper;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JavaParserTest extends JavaTestSetup {

    @Test
    public void testGenerateJavaPoetStringTypesWhenReturnTypeIsContract() {
        final List<Method> listOfFilteredMethods =
                MethodFilter.extractValidMethods(greeterContractClass);
        final Method deploy =
                listOfFilteredMethods.stream()
                        .filter(m -> m.getName().equals("deploy"))
                        .collect(Collectors.toList())
                        .get(0);
        final JavaParser parser =
                new JavaParser(greeterContractClass, deploy, new JavaMappingHelper());

        assertEquals("$L = $T.deploy($L, $L, $L, $S).send()", parser.generatePoetStringTypes());
    }

    @Test
    public void testGenerateJavaPoetStringTypesWhenReturnTypeIsNotContract() {

        final List<Method> listOfFilteredMethods =
                MethodFilter.extractValidMethods(greeterContractClass);
        final Method newGreeting =
                listOfFilteredMethods.stream()
                        .filter(m -> m.getName().equals("newGreeting"))
                        .collect(Collectors.toList())
                        .get(0);
        final JavaParser parser =
                new JavaParser(greeterContractClass, newGreeting, new JavaMappingHelper());
        assertEquals("$T $L = $L.newGreeting($S).send()", parser.generatePoetStringTypes());
    }

    @Test
    public void testGetMethodReturnType() {
        final List<Method> listOfFilteredMethods =
                MethodFilter.extractValidMethods(greeterContractClass);
        final Method newGreeting =
                listOfFilteredMethods.stream()
                        .filter(m -> m.getName().equals("newGreeting"))
                        .collect(Collectors.toList())
                        .get(0);
        final Parser parser =
                new JavaParser(greeterContractClass, newGreeting, new JavaMappingHelper());

        assertEquals(TransactionReceipt.class, parser.getMethodReturnType());
    }
}
