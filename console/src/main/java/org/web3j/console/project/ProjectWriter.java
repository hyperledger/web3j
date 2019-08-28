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
import java.io.InputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;

class ProjectWriter {

    final void writeResourceFile(String file, String fileName, String writeLocation)
            throws IOException {
        Files.write(Paths.get(writeLocation + File.separator + fileName), getBytes(file));
    }

    private byte[] getBytes(final String file) {
        return file.getBytes();
    }

    final void copyResourceFile(InputStream file, String destinationPath) throws IOException {
        Files.copy(file, Paths.get(destinationPath), StandardCopyOption.REPLACE_EXISTING);
    }

    void writeSolidity(File solidityFile, String destination) throws IOException {

        Files.walkFileTree(solidityFile.toPath(), SolidityProjectVisitor(destination));
    }

    private FileVisitor<Path> SolidityProjectVisitor(String destination) {
        return new SimpleFileVisitor<Path>() {
            String temp = destination;

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                    throws IOException {
                Objects.requireNonNull(dir);
                Objects.requireNonNull(attrs);
                temp = getFile(dir);
                return FileVisitResult.CONTINUE;
            }

            private String getFile(final Path file) {
                return createDirectoryFromPath(file).getAbsolutePath();
            }

            private File createDirectoryFromPath(Path dir) {
                File directory = new File(temp + File.separator + dir.getFileName());
                directory.mkdirs();
                return directory;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException {

                Files.copy(
                        file,
                        new File(temp + File.separator + file.getFileName()).toPath(),
                        StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }
        };
    }
}
