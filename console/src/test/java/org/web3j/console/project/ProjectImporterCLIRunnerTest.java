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

import org.junit.Test;
import picocli.CommandLine;

import org.web3j.TempFileProvider;

public class ProjectImporterCLIRunnerTest extends TempFileProvider {
    @Test(expected = CommandLine.MissingParameterException.class)
    public void testWhenNonDefinedArgsArePassed() {
        final ProjectImporterCLIRunner projectImporterCLIRunner = new ProjectImporterCLIRunner();
        final String[] args = {"-t=org.org", "-b=test", "-z=" + tempDirPath};
        final CommandLine commandLine = new CommandLine(projectImporterCLIRunner);
        commandLine.parseArgs(args);
    }

    @Test(expected = CommandLine.MissingParameterException.class)
    public void testWhenNoArgsArePassed() {
        final ProjectImporterCLIRunner projectImporterCLIRunner = new ProjectImporterCLIRunner();
        final String[] args = {};
        final CommandLine commandLine = new CommandLine(projectImporterCLIRunner);
        commandLine.parseArgs(args);
    }

    @Test(expected = CommandLine.OverwrittenOptionException.class)
    public void testWhenDuplicateArgsArePassed() {
        final ProjectImporterCLIRunner projectImporterCLIRunner = new ProjectImporterCLIRunner();
        final String[] args = {
            "-p=org.org", "-n=test", "-n=OverrideTest", "-o=" + tempDirPath, "-s=test"
        };
        final CommandLine commandLine = new CommandLine(projectImporterCLIRunner);
        commandLine.parseArgs(args);
    }
}
