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

import org.junit.Before;
import org.junit.Test;

import org.web3j.TempFileProvider;

import static org.junit.Assert.assertEquals;

public class ProjectStructureTest extends TempFileProvider {
    private ProjectStructure projectStructure;

    @Before
    public void init() {
        projectStructure = new ProjectStructure(tempDirPath, "test.test", "Test");
        projectStructure.createDirectoryStructure();
    }

    @Test
    public void getRootTest() {
        assertEquals(projectStructure.getRoot(), tempDirPath);
    }

    @Test
    public void getProjectRootTest() {
        assertEquals(projectStructure.getProjectRoot(), tempDirPath + File.separator + "Test");
    }

    @Test
    public void getPackageNameTest() {
        assertEquals("test.test", projectStructure.getPackageName());
    }

    @Test
    public void getProjectName() {
        assertEquals("Test", projectStructure.getProjectName());
    }

    @Test
    public void getTestPathTest() {
        final String testPath =
                tempDirPath
                        + File.separator
                        + "Test"
                        + File.separator
                        + "src"
                        + File.separator
                        + "test"
                        + File.separator
                        + "java"
                        + File.separator
                        + "test"
                        + File.separator
                        + "test"
                        + File.separator;

        assertEquals(testPath, projectStructure.getTestPath());
    }

    @Test
    public void getSolidityPathTest() {
        final String solidityPath =
                tempDirPath
                        + File.separator
                        + "Test"
                        + File.separator
                        + "src"
                        + File.separator
                        + "main"
                        + File.separator
                        + "solidity"
                        + File.separator;
        assertEquals(solidityPath, projectStructure.getSolidityPath());
    }

    @Test
    public void getMainPathTest() {
        final String mainPath =
                tempDirPath
                        + File.separator
                        + "Test"
                        + File.separator
                        + "src"
                        + File.separator
                        + "main"
                        + File.separator
                        + "java"
                        + File.separator
                        + "test"
                        + File.separator
                        + "test"
                        + File.separator;

        assertEquals(mainPath, projectStructure.getMainPath());
    }

    @Test
    public void getWrapperPathTest() {
        final String wrapperPath =
                tempDirPath
                        + File.separator
                        + "Test"
                        + File.separator
                        + "gradle"
                        + File.separator
                        + "wrapper"
                        + File.separator;

        assertEquals(wrapperPath, projectStructure.getWrapperPath());
    }
}
