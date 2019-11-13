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
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.shaded.com.google.common.io.Files;

import static java.io.File.separator;

public class Setup {
    static Class greeterContract;
    static List<Method> filteredMethods;
    static File temp = Files.createTempDir();
    static File classAsFile =
            new File(
                    String.join(
                            separator,
                            temp.getAbsolutePath(),
                            "test",
                            "src",
                            "integrationTest",
                            "java",
                            "org",
                            "com",
                            "generated",
                            "contracts",
                            "GreeterTest.java"));
    static String classAsString;

    @BeforeAll
    public static void setUp() throws IOException {
        String urlAsString =
                Setup.class
                        .getClassLoader()
                        .getResource("java/org/com/generated/contracts/Greeter.java")
                        .getPath();
        File greeter = new File(urlAsString.substring(0, urlAsString.indexOf("org/")));
        greeterContract = new ClassProvider(greeter).getClasses().get(0);
        filteredMethods = MethodFilter.extractValidMethods(greeterContract);
        ClassProvider classProvider = new ClassProvider(new File(urlAsString));
        Class aClass = classProvider.getClasses().get(0);
        new UnitClassGenerator(
                        aClass, "org.com.generated.contracts", temp + File.separator + "test")
                .writeClass();
        classAsString =
                new BufferedReader(new FileReader(classAsFile))
                        .lines()
                        .collect(Collectors.joining("\n"));
    }
}
