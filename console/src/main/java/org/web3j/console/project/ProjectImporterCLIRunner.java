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

import org.web3j.console.project.utills.InputVerifier;

import static org.web3j.codegen.Console.exitError;
import static org.web3j.console.project.ProjectImporter.COMMAND_IMPORT;

@CommandLine.Command(name = COMMAND_IMPORT)
public class ProjectImporterCLIRunner extends ProjectCreatorCLIRunner {

    @CommandLine.Option(
            names = {"-s", "--solidity path"},
            description = "path to solidity file/folder",
            required = true)
    String solidityImportPath;

    @Override
    public void run() {
        if (InputVerifier.requiredArgsAreNotEmpty(projectName, packageName, solidityImportPath)
                && InputVerifier.classNameIsValid(projectName)
                && InputVerifier.packageNameIsValid(packageName)) {
            try {
                new ProjectImporter(root, packageName, projectName, solidityImportPath).generate();
            } catch (final Exception e) {
                exitError(e);
            }
        }
    }
}
