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

public class ProjectImporterTest extends TempFileProvider {

    @Test
    public void goodArgsTest() {
        String[] args = {"-p=org.com", "-n=Test", "-o=" + tempDirPath, "-s=" + tempDirPath};
        ProjectImporter.PicocliRunner picocliRunner = new ProjectImporter.PicocliRunner();
        new CommandLine(picocliRunner).parseArgs(args);
        assert picocliRunner.packageName.equals("org.com");
        assert picocliRunner.projectName.equals("Test");
    }

    @Test(expected = CommandLine.MissingParameterException.class)
    public void badArgsTest() {
        ProjectImporter.PicocliRunner picocliRunner = new ProjectImporter.PicocliRunner();
        String[] args = {"-t=org.org", "-n=test", "-o=" + tempDirPath};
        CommandLine commandLine = new CommandLine(picocliRunner);
        commandLine.parseArgs(args);
    }
}
