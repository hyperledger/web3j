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

import org.junit.Test;

import org.web3j.TempFileProvider;

import static org.junit.Assert.assertTrue;

public class ProjectWriterTest extends TempFileProvider {

    private static final ProjectWriter projectWriter = new ProjectWriter();
    private static final TemplateProvider templateProvider =
            new TemplateProvider.Builder().loadGradleJar("gradle-wrapper.jar").build();

    @Test
    public void writeResourceFileTest() throws Exception {
        projectWriter.writeResourceFile("Greeter.sol", "Greeter.sol", tempDirPath);
        assertTrue(new File(tempDirPath + File.separator + "Greeter.sol").exists());
    }

    @Test
    public void copyResourceFileTest() throws IOException {
        projectWriter.copyResourceFile(
                templateProvider.getGradlewJar(),
                tempDirPath + File.separator + "gradle-wrapper.jar");
        assertTrue(new File(tempDirPath + File.separator + "gradle-wrapper.jar").exists());
    }

    @Test
    public void importSolidityProjectTest() throws IOException {
        final File file = new File(tempDirPath + File.separator + "tempSolidityDir");
        file.mkdirs();
        projectWriter.writeResourceFile(
                "Greeter.sol", "Greeter.sol", tempDirPath + File.separator + "tempSolidityDir");
        projectWriter.importSolidityProject(
                new File(tempDirPath + File.separator + "tempSolidityDir"),
                tempDirPath + File.separator + "tempSolidityDestination");
        assertTrue(
                new File(
                                tempDirPath
                                        + File.separator
                                        + "tempSolidityDestination"
                                        + File.separator
                                        + "tempSolidityDir"
                                        + File.separator
                                        + "Greeter.sol")
                        .exists());
    }
}
