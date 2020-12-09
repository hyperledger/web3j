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
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.io.TempDir;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class TestSetup {

    @TempDir public static File temp;

    protected Class<?> greeterContractClass;
    protected List<Method> filteredMethods;
    protected String classAsString;

    @BeforeAll
    public void setUp() throws Exception {
        greeterContractClass =
                new CompilerClassLoader(temp, TestSetup.class.getClassLoader().getResource("java"))
                        .loadClass("org.web3j.test.contract.Greeter");

        filteredMethods = MethodFilter.extractValidMethods(greeterContractClass);
        buildGenerator().writeClass();

        final String pathToTest =
                String.join(
                        File.separator,
                        temp.getPath(),
                        "test",
                        "org",
                        "com",
                        "generated",
                        "contracts",
                        "GreeterTest." + testFileExtension());

        classAsString =
                new BufferedReader(new FileReader(pathToTest))
                        .lines()
                        .collect(Collectors.joining("\n"));
    }

    protected abstract UnitClassGenerator buildGenerator();

    protected abstract String testFileExtension();
}
