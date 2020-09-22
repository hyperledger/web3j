/*
 * Copyright 2020 Web3 Labs Ltd.
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
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import org.web3j.codegen.unit.gen.java.Setup;

public class CompilerClassLoaderTest {

    String javaSourcePath = "org/com/test/contract/";
    String greeterSourceName = "Greeter";
    URL javaSourcesUrl = Objects.requireNonNull(Setup.class.getClassLoader().getResource("java"));
    @TempDir static File temp;

    @Test
    public void compileClassTest() throws ClassNotFoundException {
        CompilerClassLoader compilerClassLoader = new CompilerClassLoader(temp, javaSourcesUrl);
        Assertions.assertNotEquals(
                null,
                compilerClassLoader.loadClass(
                        (javaSourcePath + greeterSourceName).replace(File.separator, ".")));
    }

    @Test
    public void compileClassWithPathContainingSpacesTest()
            throws ClassNotFoundException, IOException {
        File tempWithSpaces = new File(temp, "With Spaces");
        File greeterSource =
                new File(
                        Paths.get(
                                        javaSourcesUrl.getPath(),
                                        javaSourcePath,
                                        greeterSourceName + ".java")
                                .toString());
        Assertions.assertTrue(greeterSource.exists());

        String javaPathWithSpaces = tempWithSpaces.toString() + File.separator + "java";
        File sourcePathWithSpaces = new File(javaPathWithSpaces + File.separator + javaSourcePath);
        Assertions.assertTrue(sourcePathWithSpaces.mkdirs());

        Files.copy(
                greeterSource.toPath(),
                Paths.get(
                        sourcePathWithSpaces.toPath()
                                + File.separator
                                + greeterSourceName
                                + ".java"));
        File greeterSourceWithSpaces = new File(sourcePathWithSpaces, greeterSourceName + ".java");
        Assertions.assertTrue(greeterSourceWithSpaces.exists());

        CompilerClassLoader compilerClassLoader =
                new CompilerClassLoader(
                        tempWithSpaces, new File(javaPathWithSpaces).toURI().toURL());
        Assertions.assertNotEquals(
                null,
                compilerClassLoader.loadClass(
                        (javaSourcePath + greeterSourceName).replace(File.separator, ".")));
    }
}
