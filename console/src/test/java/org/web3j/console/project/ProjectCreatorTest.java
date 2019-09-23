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

public class ProjectCreatorTest extends ClassExecutor {
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
        final String[] args = {"-p=org.com", "-n=Test", "-o=" + tempDirPath};
        final ProjectCreatorCLIRunner projectCreatorCLIRunner = new ProjectCreatorCLIRunner();
        new CommandLine(projectCreatorCLIRunner).parseArgs(args);
        assert projectCreatorCLIRunner.packageName.equals("org.com");
        assert projectCreatorCLIRunner.projectName.equals("Test");
        assert projectCreatorCLIRunner.root.equals(tempDirPath);
    }

    @Test
    public void testWithPicoCliWhenArgumentsAreCorrect() throws IOException, InterruptedException {
        final String[] args = {"new", "-p", "org.com", "-n", "Test", "-o" + tempDirPath};
        int extiCode =
                executeClassAsSubProcessAndReturnProcess(
                                ProjectCreator.class, Collections.emptyList(), Arrays.asList(args))
                        .inheritIO()
                        .start()
                        .waitFor();
        assertEquals(0, extiCode);
    }

    @Test
    public void testWithPicoCliWhenArgumentsAreEmpty() {
        final String[] args = {"new", "-n= ", "-p= "};
        ProjectCreator.main(args);
        assertEquals(
                outContent.toString(), "Please make sure the required parameters are not empty.\n");
    }

    @Test
    public void testWhenInteractiveAndArgumentsAreCorrect()
            throws IOException, InterruptedException {
        final String[] args = {"new"};
        Process process =
                executeClassAsSubProcessAndReturnProcess(
                                ProjectCreator.class, Collections.emptyList(), Arrays.asList(args))
                        .start();
        BufferedWriter writer =
                new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
        writer.write("test", 0, "test".length());
        writer.newLine();
        writer.write("org.com", 0, "org.com".length());
        writer.newLine();
        writer.write(tempDirPath, 0, tempDirPath.length());
        writer.newLine();
        writer.close();
        process.waitFor();
        assertEquals(0, process.exitValue());
    }

    @Test
    public void testWhenInteractiveAndArgumentsAreEmpty() {
        final String input = " \n \n \n";
        inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        final String[] args = {"new"};
        ProjectCreator.main(args);
        assertTrue(
                outContent
                        .toString()
                        .contains("Please make sure the required parameters are not empty."));
    }
}
