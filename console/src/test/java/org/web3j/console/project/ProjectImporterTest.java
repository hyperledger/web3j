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
import java.io.PrintStream;
import org.junit.Test;
import picocli.CommandLine;

import org.web3j.TempFileProvider;

import static org.mockito.Matchers.startsWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ProjectImporterTest extends TempFileProvider {

    @Test
    public void testWhenCorrectArgsArePassedProjectStructureCreated() {
        final String[] args = {"-p=org.com", "-n=Test", "-o=" + tempDirPath, "-s=" + tempDirPath};
        final ProjectImporterCLIRunner projectImporterCLIRunner = new ProjectImporterCLIRunner();
        new CommandLine(projectImporterCLIRunner).parseArgs(args);
        assert projectImporterCLIRunner.packageName.equals("org.com");
        assert projectImporterCLIRunner.projectName.equals("Test");
        assert projectImporterCLIRunner.solidityImportPath.equals(tempDirPath);
    }

    @Test
    public void runTestWhenArgumentsAreEmpty() throws IOException {
        final PrintStream output = mock(PrintStream.class);
        System.setErr(output);
        final String[] args = {"", "", ""};
        ProjectImporter.main(args);
        verify(output).println(startsWith("Missing required options [--package=<packageName>, --project name=<projectName>, --solidity path=<solidityImportPath>]"));
    }


    @Test
    public void runTestWhenArgumentsAreNotEmpty() throws IOException {
        final String formattedSolidityTestProject = File.separator
                + "web3j"
                + File.separator
                + "console"
                + File.separator
                + "src"
                + File.separator
                + "test"
                + File.separator
                + "resources"
                + File.separator
                + "Solidity";
        final PrintStream output = mock(PrintStream.class);
        System.setOut(output);
        final String[] args = {"-p=org.com", "-n=Test", "-o=" + tempDirPath, "-s=" + formattedSolidityTestProject};
        ProjectImporter.main(args);
        verify(output).println(startsWith("Project"));
    }


}
