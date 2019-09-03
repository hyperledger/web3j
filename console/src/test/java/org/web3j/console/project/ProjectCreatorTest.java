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
import org.web3j.TempFileProvider;
import picocli.CommandLine;

public class ProjectCreatorTest extends TempFileProvider {

    @Test
    public void goodArgsTest() throws Exception {
        String[] args = {"-p=org.com", "-n=Test", "-o=" + tempDirPath};
        ProjectCreator.PicocliRunner picocliRunner = new ProjectCreator.PicocliRunner();
        new CommandLine(picocliRunner).parseArgs(args);
        assert picocliRunner.packageName.equals("org.com");
        assert picocliRunner.projectName.equals("Test");


    }

    @Test
    public void badArgsTest() {
        String[] args = {"-p=", "-n=", "-o=" + tempDirPath};
        ProjectCreator.PicocliRunner picocliRunner = new ProjectCreator.PicocliRunner();
        new CommandLine(picocliRunner).parseArgs(args);
        picocliRunner.run();


    }

}
