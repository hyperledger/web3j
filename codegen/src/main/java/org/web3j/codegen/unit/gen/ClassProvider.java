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
import java.util.List;
import java.util.stream.Collectors;

/** Loads all Java classes from a given directory. */
public class ClassProvider {
    private final File pathToJavaFiles;
    private final ClassLoader classLoader;

    public ClassProvider(final File pathToJavaFiles) throws IOException {
        this.pathToJavaFiles = pathToJavaFiles;

        final URL[] classPathURL = new URL[] {pathToJavaFiles.toURI().toURL()};
        final Path outputDirectory = Files.createTempDirectory("tmp");

        classLoader = new CompilerClassLoader(outputDirectory.toFile(), classPathURL);
    }

    public final List<Class<?>> getClasses() throws IOException {
        return Files.walk(Paths.get(pathToJavaFiles.toURI()))
                .filter(Files::isRegularFile)
                .map(Path::toString)
                .map(this::toClassName)
                .map(this::loadClass)
                .collect(Collectors.toList());
    }

    private String toClassName(final String sourceFile) {
        try {
            return sourceFile
                    .substring(
                            pathToJavaFiles.getCanonicalPath().length() + 1,
                            sourceFile.lastIndexOf(".java"))
                    .replace(File.separator, ".");
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private Class<?> loadClass(final String className) {
        try {
            return classLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }
}
