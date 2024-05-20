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
package org.web3j.codegen.unit.gen.kotlin;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.squareup.kotlinpoet.FunSpec;
import org.junit.jupiter.api.Test;

import org.web3j.codegen.unit.gen.MethodFilter;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FunParserTest extends KotlinTestSetup {
    @Test
    public void testThatDeployMethodWasGenerated() {
        Optional<Method> deployFun =
                filteredMethods.stream().filter(m -> m.getName().equals("deploy")).findAny();
        FunSpec deployFunSpec =
                new FunParser(deployFun.get(), greeterContractClass, "deploy").getFunSpec();
        assertEquals(
                deployFunSpec.toString(),
                "@org.junit.jupiter.api.BeforeAll\n"
                        + "public fun deploy(\n"
                        + "  web3j: org.web3j.protocol.Web3j,\n"
                        + "  transactionManager: org.web3j.tx.TransactionManager,\n"
                        + "  contractGasProvider: org.web3j.tx.gas.ContractGasProvider,\n"
                        + ") {\n"
                        + "   greeter = org.web3j.test.contract.Greeter.deploy(web3j, transactionManager, contractGasProvider, \"REPLACE_ME\").send()\n"
                        + "}\n");
    }

    @Test
    public void testThatNewGreetingMethodWasGenerated() {
        Optional<Method> deployFun =
                filteredMethods.stream().filter(m -> m.getName().equals("newGreeting")).findAny();
        FunSpec deployFunSpec =
                new FunParser(deployFun.get(), greeterContractClass, "newGreeting").getFunSpec();
        assertEquals(
                deployFunSpec.toString(),
                "@org.junit.jupiter.api.Test\n"
                        + "public fun newGreeting() {\n"
                        + "  val transactionReceiptVar = greeter.newGreeting(\"REPLACE_ME\").send()\n"
                        + "  org.junit.jupiter.api.Assertions.assertTrue(transactionReceiptVar.isStatusOK())\n"
                        + "}\n");
    }

    @Test
    public void testGeneratedDuplicateGreetingMethods() {
        List<FunSpec> allMethodSpecs =
                MethodFilter.generateFunctionSpecsForEachTest(greeterContractClass);

        // Filter all FunSpecs for those related to "greet" methods
        List<FunSpec> greetFunSpecs =
                allMethodSpecs.stream()
                        .filter(funSpec -> funSpec.getName().startsWith("greet"))
                        .collect(Collectors.toList());

        assertTrue(
                greetFunSpecs.stream()
                        .anyMatch(methodSpec -> methodSpec.getName().equals("greet")));
        assertTrue(
                greetFunSpecs.stream()
                        .anyMatch(methodSpec -> methodSpec.getName().equals("greet1")));
        assertEquals(2, greetFunSpecs.size());
    }

    @Test
    public void testGetDeploymentBinaryMethodNotGenerated() {
        List<FunSpec> allMethodSpecs =
                MethodFilter.generateFunctionSpecsForEachTest(greeterContractClass);

        // Filter all FunSpecs for those related to "getDeploymentBinary" method
        List<FunSpec> getDeploymentBinaryFunSpecs =
                allMethodSpecs.stream()
                        .filter(funSpec -> funSpec.getName().startsWith("getDeploymentBinary"))
                        .collect(Collectors.toList());

        // Ensure no MethodSpecs were generated for getDeploymentBinary method
        assertEquals(
                0,
                getDeploymentBinaryFunSpecs.size(),
                "MethodSpec list should not contain getDeploymentBinary function");
    }
}
