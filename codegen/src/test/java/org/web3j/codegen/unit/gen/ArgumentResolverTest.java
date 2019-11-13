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
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.Tuple;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ArgumentResolverTest {

    Class greeterContract;

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
    public void testGetJavaPoetStringFormatFromTypes() {
        List<Class> listOfClasses =
                Arrays.asList(
                        Web3j.class,
                        String.class,
                        BigInteger.class,
                        List.class,
                        Tuple.class,
                        byte[].class);
        ArgumentResolver.getJavaPoetStringFormatFromTypes(listOfClasses);
        assertEquals(
                ArgumentResolver.getJavaPoetStringFormatFromTypes(listOfClasses),
                "$L,$S,$T.ONE,new $T<>(),new $T<>(),new $T{}");
    }

    @Test
    public void testGenerateJavaPoetStringTypesWhenReturnTypeIsContract() {
        List<Method> listOfFilteredMethods = MethodFilter.extractValidMethods(greeterContract);
        Method deploy =
                listOfFilteredMethods.stream()
                        .filter(m -> m.getName().equals("deploy"))
                        .collect(Collectors.toList())
                        .get(0);
        assertEquals(
                ArgumentResolver.generateJavaPoetStringTypes(deploy, greeterContract),
                "$L=$T.deploy($L,$L,$L,$S).send()");
    }

    @Test
    public void testGenerateJavaPoetStringTypesWhenReturnTypeIsNotContract() {
        List<Method> listOfFilteredMethods = MethodFilter.extractValidMethods(greeterContract);
        Method newGreeting =
                listOfFilteredMethods.stream()
                        .filter(m -> m.getName().equals("newGreeting"))
                        .collect(Collectors.toList())
                        .get(0);
        assertEquals(
                ArgumentResolver.generateJavaPoetStringTypes(newGreeting, greeterContract),
                "$T $L=$L.newGreeting($S).send()");
    }

    @Test
    public void testGetMethodReturnType() {
        List<Method> listOfFilteredMethods = MethodFilter.extractValidMethods(greeterContract);
        Method newGreeting =
                listOfFilteredMethods.stream()
                        .filter(m -> m.getName().equals("newGreeting"))
                        .collect(Collectors.toList())
                        .get(0);
        assertEquals(ArgumentResolver.getMethodReturnType(newGreeting), TransactionReceipt.class);
    }
}
