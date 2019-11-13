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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.web3j.protocol.Web3j;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MethodFilterTest {
    private static Class greeterContract;

    @BeforeEach
    public void classToString() throws IOException, ClassNotFoundException {
        String urlAsString =
                this.getClass()
                        .getClassLoader()
                        .getResource("java/org/com/generated/contracts/Greeter.java")
                        .getPath();
        File greeter = new File(urlAsString.substring(0, urlAsString.indexOf("org/")));
        greeterContract = new ClassProvider(greeter).getClasses().get(0);
    }

    @Test
    public void testExtractValidMethods() {
        List<Method> filteredMethods = MethodFilter.extractValidMethods(greeterContract);
        filteredMethods.forEach(m -> assertFalse(m.getName().toLowerCase().contains("event")));
    }

    @Test
    public void testThatTheCorrectDeployMethodWasExtracted() {
        List<Method> filteredMethods = MethodFilter.extractValidMethods(greeterContract);
        List<Method> deployMethod =
                filteredMethods.stream()
                        .filter(m -> m.getName().equals("deploy"))
                        .collect(Collectors.toList());
        List<Class<?>> deployMethodParameterTypes =
                Arrays.asList(deployMethod.get(0).getParameterTypes());
        assertTrue(
                deployMethodParameterTypes.containsAll(
                        Arrays.asList(
                                Web3j.class, TransactionManager.class, ContractGasProvider.class)));
    }

    @Test
    public void testThatTheCorrectLoadMethodWasExtracted() {
        List<Method> filteredMethods = MethodFilter.extractValidMethods(greeterContract);
        List<Method> deployMethod =
                filteredMethods.stream()
                        .filter(m -> m.getName().equals("load"))
                        .collect(Collectors.toList());
        List<Class<?>> deployMethodParameterTypes =
                Arrays.asList(deployMethod.get(0).getParameterTypes());
        assertTrue(
                deployMethodParameterTypes.containsAll(
                        Arrays.asList(
                                Web3j.class, TransactionManager.class, ContractGasProvider.class)));
    }
}
