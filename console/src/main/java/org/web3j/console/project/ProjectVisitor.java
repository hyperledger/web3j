/*
 * Copyright 2019 Web3 Labs LTD.
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
package org.web3j.console.project;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;

public class ProjectVisitor extends SimpleFileVisitor<Path> {
    private final String destination;
    private String temp;

    public ProjectVisitor(final String destination) {
        this.destination = destination;
        this.temp = destination;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
        Objects.requireNonNull(dir);
        Objects.requireNonNull(attrs);
        temp = getFile(dir);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {

        Files.copy(
                path,
                new File(temp + File.separator + path.getFileName()).toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        return FileVisitResult.CONTINUE;
    }

    private String getFile(final Path path) {
        return createDirectoryFromPath(path).getAbsolutePath();
    }

    private File createDirectoryFromPath(Path path) {
        File directory = new File(temp + File.separator + path.getFileName());
        directory.mkdirs();
        return directory;
    }
}
