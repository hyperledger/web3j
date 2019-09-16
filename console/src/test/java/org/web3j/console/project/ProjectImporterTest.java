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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import org.junit.Before;
import org.junit.Test;
import org.web3j.TempFileProvider;
import picocli.CommandLine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ProjectImporterTest extends TempFileProvider {
    private ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private InputStream inputStream;

    @Before
    public void setUpStreams() {

        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));

    }

    @Test
    public void testWhenCorrectArgsArePassedProjectStructureCreated() {
        final String[] args = {"-p=org.com", "-n=Test", "-o=" + tempDirPath, "-s=" + tempDirPath};
        final ProjectImporterCLIRunner projectImporterCLIRunner = new ProjectImporterCLIRunner();
        new CommandLine(projectImporterCLIRunner).parseArgs(args);
        assertEquals(projectImporterCLIRunner.packageName, "org.com");
        assertEquals(projectImporterCLIRunner.projectName, "Test");
        assertEquals(projectImporterCLIRunner.solidityImportPath, tempDirPath);
    }

    @Test
    public void runTestWhenArgumentsAreEmpty() {
        final String[] args = {"", "", ""};
        ProjectImporter.main(args);
        assertTrue(
                errContent
                        .toString()
                        .contains("Missing required options [--solidity path=<solidityImportPath>, --package=<packageName>, --project name=<projectName>]"));

    }

    @Test
    public void runTestWhenArgumentsAreNotEmpty() {
        final String formattedSolidityTestProject =
                File.separator
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


        final String[] args = {
                "-p=org.com", "-n=Test", "-o=" + tempDirPath, "-s=" + formattedSolidityTestProject
        };
        ProjectImporter.main(args);
        assertTrue(outContent.toString().contains("Project created with name:"));
    }

    @Test
    public void createImportProjectInteractive() {
        String formattedPath = "/web3j/console/src/test/resources/Solidity".replaceAll("/", File.separator);
        final String input = "Test\norg.com\n" + formattedPath + "\n" + tempDirPath + "\n";
        inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        final String[] args = {"import", "interactive"};
        ProjectImporter.main(args);
        assertTrue(outContent.toString().contains("Project created with name:"));

    }

    @Test
    public void runTestWhenArgumentsAreNewInteractive() {
        final String input = " \n \n \n \n";
        inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        final String[] args = {"import", "interactive"};
        ProjectImporter.main(args);
        assertTrue(outContent.toString().contains("Please make sure the required parameters are not empty."));
    }
}
