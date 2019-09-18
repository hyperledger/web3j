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

import java.io.IOException;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import org.web3j.console.project.utills.InputVerifier;

import static org.web3j.codegen.Console.exitError;
import static org.web3j.console.project.ProjectCreator.COMMAND_NEW;
import static picocli.CommandLine.Help.Visibility.ALWAYS;

@Command(name = COMMAND_NEW, mixinStandardHelpOptions = true, version = "4.0", sortOptions = false)
public class ProjectCreatorCLIRunner implements Runnable {
    @Option(
            names = {"-o", "--outputDir"},
            description = "destination base directory.",
            required = false,
            showDefaultValue = ALWAYS)
    String root = System.getProperty("user.dir");

    @Option(
            names = {"-p", "--package"},
            description = "base package name.",
            required = true)
    String packageName;

    @Option(
            names = {"-n", "--project name"},
            description = "project name.",
            required = true)
    String projectName;

    @Override
    public void run() {
        if (InputVerifier.requiredArgsAreNotEmpty(projectName, packageName)
                && InputVerifier.classNameIsValid(projectName)
                && InputVerifier.packageNameIsValid(packageName)) {
            try {
                new ProjectCreator(root, packageName, projectName).generate();
            } catch (final IOException e) {
                exitError(e);
            }
        }
    }
}
