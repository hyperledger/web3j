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

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import picocli.CommandLine;

import org.web3j.console.project.utills.ClassExecutor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ProjectImporterTest extends ClassExecutor {
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
    public void testWithPicoCliWhenArgumentsAreCorrect() throws IOException, InterruptedException {

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
        int exitCode =
                executeClassAsSubProcessAndReturnProcess(
                                ProjectImporter.class, Collections.emptyList(), Arrays.asList(args))
                        .inheritIO()
                        .start()
                        .waitFor();
        assertEquals(0, exitCode);
    }

    @Test
    public void testWithPicoCliWhenArgumentsAreEmpty() {
        final String[] args = {"import", "-p= ", "-n= ", "-s= "};
        ProjectImporter.main(args);
        assertEquals(
                outContent.toString(), "Please make sure the required parameters are not empty.\n");
    }

    @Test
    public void testWhenInteractiveAndArgumentsAreCorrect()
            throws IOException, InterruptedException {
        String formattedPath =
                "/web3j/console/src/test/resources/Solidity".replaceAll("/", File.separator);
        final String[] args = {"import"};
        Process process =
                executeClassAsSubProcessAndReturnProcess(
                                ProjectImporter.class, Collections.emptyList(), Arrays.asList(args))
                        .start();
        BufferedWriter writer =
                new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
        writer.write("test", 0, "test".length());
        writer.newLine();
        writer.write("org.com", 0, "org.com".length());
        writer.newLine();
        writer.write(formattedPath, 0, formattedPath.length());
        writer.newLine();
        writer.write(tempDirPath, 0, tempDirPath.length());
        writer.newLine();
        writer.close();
        process.waitFor();
        assertEquals(0, process.exitValue());
    }

    @Test
    public void testWhenInteractiveAndArgumentsAreEmpty() {
        final String input = " \n \n \n \n";
        inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        final String[] args = {"import"};
        ProjectImporter.main(args);
        assertTrue(
                outContent
                        .toString()
                        .contains("Please make sure the required parameters are not empty."));
    }
}
