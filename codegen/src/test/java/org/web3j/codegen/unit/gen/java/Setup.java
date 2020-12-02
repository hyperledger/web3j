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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.io.TempDir;

import org.web3j.codegen.unit.gen.CompilerClassLoader;
import org.web3j.codegen.unit.gen.MethodFilter;

public class Setup {
    @TempDir static File temp;
    static File classAsFile;
    public static Class greeterContractClass;
    static List<Method> filteredMethods;
    static String classAsString;
    static String pathToTest;

    @BeforeAll
    public static void setUp() throws IOException, ClassNotFoundException {

        String urlAsString =
                Objects.requireNonNull(
                                Setup.class
                                        .getClassLoader()
                                        .getResource("java/org/com/test/contract/Greeter.java"))
                        .getPath();
        pathToTest =
                String.join(
                        File.separator,
                        temp.getPath(),
                        "test",
                        "org",
                        "com",
                        "generated",
                        "contracts",
                        "GreeterTest.java");
        classAsFile = new File(urlAsString);
        CompilerClassLoader compilerClassLoader =
                new CompilerClassLoader(temp, Setup.class.getClassLoader().getResource("java/"));

        greeterContractClass = compilerClassLoader.loadClass("org.com.test.contract.Greeter");

        filteredMethods = MethodFilter.extractValidMethods(greeterContractClass);
        new JavaClassGenerator(
                        greeterContractClass,
                        "org.com.generated.contracts",
                        temp + File.separator + "test")
                .writeClass();
        classAsString =
                new BufferedReader(new FileReader(new File(pathToTest)))
                        .lines()
                        .collect(Collectors.joining("\n"));
    }
}
