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

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ProjectWriter {

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

    private void copySolidityFile(String path, String destination) throws IOException {

        Files.copy(
                new File(path).toPath(),
                new File(destination + File.separator + new File(path).getName()).toPath(),
                StandardCopyOption.REPLACE_EXISTING);
    }

    private void copySolidityFiles(String path, String destination) throws IOException {

        for (String fileName : new File(path).list()) {
            if (fileName.endsWith(".sol"))
                Files.copy(
                        new File(path + File.separator + fileName).toPath(),
                        new File(destination + File.separator + fileName).toPath(),
                        StandardCopyOption.REPLACE_EXISTING);
        }
    }

    void writeSolidityProject(String path, String destination) throws IOException {

        File solidityPath = new File(path);
        if (solidityPath.exists()) {
            if (solidityPath.isDirectory()) {
                copySolidityFiles(path, destination);
            } else {
                copySolidityFile(path, destination);
            }
        }
    }
}
