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

import picocli.CommandLine;

import static org.web3j.codegen.Console.exitError;
import static org.web3j.codegen.Console.exitSuccess;
import static org.web3j.console.project.InteractiveOptions.COMMAND_INTERACTIVE;
import static org.web3j.utils.Collection.tail;

public class ProjectImporter extends ProjectCreator {
    public static final String COMMAND_IMPORT = "import";

    private final String solidityImportPath;

    public ProjectImporter(
            final String root,
            final String packageName,
            final String projectName,
            final String solidityImportPath)
            throws IOException {
        super(root, packageName, projectName);
        this.solidityImportPath = solidityImportPath;
    }

    public static void main(String[] args) throws IOException {
        if (args.length > 0 && args[0].equals(COMMAND_IMPORT)) {
            args = tail(args);
            if (args.length > 0 && args[0].equals(COMMAND_INTERACTIVE)) {
                final InteractiveImporter options = new InteractiveImporter();
                args =
                        new String[] {
                            COMMAND_NEW,
                            "-n",
                            options.getProjectName(),
                            "-p",
                            options.getPackageName(),
                            "-s",
                            options.getSolidityProjectPath(),
                            "-o",
                            options.getProjectDestination()
                        };
            }
        }

        CommandLine.run(new ProjectImporterCLIRunner(), args);
    }

    public void generate() {
        final File solidityFile = new File(solidityImportPath);
        try {
            Project.builder()
                    .withProjectStructure(projectStructure)
                    .withTemplateProvider(templateProvider)
                    .withSolidityFile(solidityFile)
                    .build();
            exitSuccess(
                    "Project created with name: "
                            + projectStructure.getProjectName()
                            + " at location: "
                            + projectStructure.getProjectRoot());
        } catch (final Exception e) {
            exitError(e);
        }
    }
}
