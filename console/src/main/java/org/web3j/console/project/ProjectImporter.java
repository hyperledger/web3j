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

import picocli.CommandLine;

import java.io.File;
import java.io.IOException;

import static org.web3j.codegen.Console.exitError;
import static org.web3j.utils.Collection.tail;
import static picocli.CommandLine.Help.Visibility.ALWAYS;

public class ProjectImporter extends ProjectCreator {
    public static final String COMMAND_IMPORT = "import";

    private final String solidityImportPath;

    private ProjectImporter(
            final String root,
            final String packageName,
            final String projectName,
            final String solidityImportPath)
            throws IOException {
        super(root, packageName, projectName);
        this.solidityImportPath = solidityImportPath;
    }

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals(COMMAND_IMPORT)) {
            args = tail(args);
        }

        CommandLine.run(new PicocliRunner(), args);
    }

    public void generate() {
        File solidityFile = new File(solidityImportPath);
        try {
            Project project =
                    new Project.Builder()
                            .withProjectStructure(projectStructure)
                            .withTemplateProvider(templateProvider)
                            .withSolidityFile(solidityFile)
                            .build();
        } catch (Exception e) {

        }
    }

    @CommandLine.Command(
            name = COMMAND_IMPORT,
            mixinStandardHelpOptions = true,
            version = "4.0",
            sortOptions = false)
    static class PicocliRunner implements Runnable {


        @CommandLine.Option(
                names = {"-o", "--outputDir"},
                description = "destination base directory.",
                required = false,
                showDefaultValue = ALWAYS)
        private String root = System.getProperty("user.dir");

        @CommandLine.Option(
                names = {"-p", "--package name"},
                description = "base package name.",
                required = true)
        private String packageName;

        @CommandLine.Option(
                names = {"-n", "--project name"},
                description = "project name.",
                required = true)
        private String projectName;

        @CommandLine.Option(
                names = {"-s", "--solidity path"},
                description = "path to solidity file/folder",
                required = true)
        private String solidityImportPath;

        @Override
        public void run() {
            try {
                new ProjectImporter(root, packageName, projectName, solidityImportPath).generate();
            } catch (Exception e) {
                exitError(e);
            }
        }
    }
}
