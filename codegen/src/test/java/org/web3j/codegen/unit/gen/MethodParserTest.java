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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.squareup.javapoet.MethodSpec;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MethodParserTest extends Setup {
    private static String classAsString;
    private static Class greeterContract;
    private static List<Method> filteredMethods;

    @BeforeAll
    public static void classToString() throws IOException, ClassNotFoundException {
        classAsString =
                new BufferedReader(new FileReader(classAsFile))
                        .lines()
                        .collect(Collectors.joining("\n"));
        greeterContract =
                new ClassProvider(
                                new File(
                                        "/home/alexander/Documents/dev/web3j/web3j/web3j/codegen/src/test/resources/java/"))
                        .getClasses()
                        .get(0);
        filteredMethods = MethodFilter.extractValidMethods(greeterContract);
    }

    @Test
    public void testThatDeployMethodWasGenerated() {

        Optional<Method> deployMethod =
                filteredMethods.stream().filter(m -> m.getName().equals("deploy")).findAny();
        MethodSpec deployMethodSpec =
                new MethodParser(deployMethod.get(), greeterContract).getMethodSpec();
        assertEquals(
                deployMethodSpec.code.toString(),
                "greeter=org.com.generated.contracts.Greeter.deploy(web3j,transactionManager,contractGasProvider,\"REPLACE_ME\").send();\n");
    }

    @Test
    public void testThatLoadMethodWasGenerated() {

        Optional<Method> deployMethod =
                filteredMethods.stream().filter(m -> m.getName().equals("load")).findAny();
        MethodSpec deployMethodSpec =
                new MethodParser(deployMethod.get(), greeterContract).getMethodSpec();
        assertEquals(
                deployMethodSpec.code.toString(),
                "greeter=org.com.generated.contracts.Greeter.load(\"REPLACE_ME\",web3j,transactionManager,contractGasProvider).send();\n");
    }

    @Test
    public void testThatKillMethodWasGenerated() {

        Optional<Method> deployMethod =
                filteredMethods.stream().filter(m -> m.getName().equals("kill")).findAny();
        MethodSpec deployMethodSpec =
                new MethodParser(deployMethod.get(), greeterContract).getMethodSpec();
        assertEquals(
                deployMethodSpec.code.toString(),
                "org.web3j.protocol.core.methods.response.TransactionReceipt transactionReceiptVar=greeter.kill().send();\n");
    }

    @Test
    public void testThatNewGreetingMethodWasGenerated() {

        Optional<Method> deployMethod =
                filteredMethods.stream().filter(m -> m.getName().equals("newGreeting")).findAny();
        MethodSpec deployMethodSpec =
                new MethodParser(deployMethod.get(), greeterContract).getMethodSpec();
        assertEquals(
                deployMethodSpec.code.toString(),
                "org.web3j.protocol.core.methods.response.TransactionReceipt transactionReceiptVar=greeter.newGreeting(\"REPLACE_ME\").send();\n");
    }
}
