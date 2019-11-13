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
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClassProvider {
    private final File pathToJavaFiles;

    public ClassProvider(final File pathToJavaFiles) {
        this.pathToJavaFiles = pathToJavaFiles;
    }

    public final List<Class> getClasses() throws IOException {
        return loadClassesToList(compileClasses());
    }

    private CompilerClassLoader compileClasses() throws IOException {
        URL[] classPathURL = new URL[] {pathToJavaFiles.toURI().toURL()};
        Path outputDirectory = Files.createTempDirectory("tmp");
        return new CompilerClassLoader(
                Objects.requireNonNull(outputDirectory).toFile(), classPathURL);
    }

    private List<Class> loadClassesToList(final CompilerClassLoader compilerClassLoader)
            throws IOException {
        return getFormattedClassPath().stream()
                .map(
                        cp -> {
                            try {
                                return compilerClassLoader.loadClass(
                                        cp.replace(File.separator, "."));
                            } catch (ClassNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                        })
                .collect(Collectors.toList());
    }

    private List<String> getFormattedClassPath() throws IOException {
        Stream<Path> walk = Files.walk(Paths.get(pathToJavaFiles.toURI()));
        return getClassPathFromURL(
                walk.filter(Files::isRegularFile).map(Path::toString).collect(Collectors.toList()));
    }

    private List<String> getClassPathFromURL(final List<String> listOfUrl) throws IOException {
        int length = pathToJavaFiles.getCanonicalPath().length();
        List<String> formattedClassPath = new ArrayList<>();
        for (String s : listOfUrl) {
            formattedClassPath.add(s.substring(length + 1, s.lastIndexOf(".java")));
        }
        return formattedClassPath;
    }
}
