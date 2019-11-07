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
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static java.io.File.separator;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UnitClassGeneratorTest extends Setup {
    private static String classAsString;
    private static File classAsFile =
            new File(
                    temp
                            + separator
                            + "test"
                            + separator
                            + "src"
                            + separator
                            + "integrationTest"
                            + separator
                            + "java"
                            + separator
                            + "org"
                            + separator
                            + "com"
                            + separator
                            + "generated"
                            + separator
                            + "contracts"
                            + separator
                            + "GreeterTest.java");

    @BeforeAll
    public static void init() throws IOException {
        classAsString =
                new BufferedReader(new FileReader(classAsFile))
                        .lines()
                        .collect(Collectors.joining("\n"));
    }

    @Test
    public void testThatTheClassWasSuccessfullyWritten() {
        assertTrue(classAsFile.exists());
    }

    @Test
    public void testThatExceptionIsThrownWhenAClassIsNotWritten() {
        UnitClassGenerator unitClassGenerator =
                new UnitClassGenerator(null, "org.com", temp + separator + "test");
        assertThrows(NullPointerException.class, unitClassGenerator::writeClass);
    }

    @Test
    public void testThatClassWasGeneratedWithCorrectFields() {

        assertTrue(
                classAsString.contains(
                        "static String myAddress = \"0xfe3b557e8fb62b89f4916b721be55ceb828dbd73\";\n"));
        assertTrue(
                classAsString.contains(
                        "static String addressToTestAgainst = \"0x42699a7612a82f1d9c36148af9c77354759b210b\";\n"));
        assertTrue(classAsString.contains("private Greeter greeter;"));
    }
}
