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

import org.junit.Assert;
import org.junit.Test;

import org.web3j.TempFileProvider;

public class ProjectStructureTest extends TempFileProvider {
    ProjectStructure projectStructure;

    private void init() throws Exception {
        setUp();
        projectStructure = new ProjectStructure(tempDirPath, "test.test", "Test");
        projectStructure.createDirectoryStructure();
    }

    @Test
    public void getRootTest() throws Exception {
        init();
        Assert.assertTrue(projectStructure.getRoot().equals(tempDirPath));
    }

    @Test
    public void getProjectRootTest() throws Exception {
        init();
        Assert.assertTrue(
                projectStructure.getProjectRoot().equals(tempDirPath + File.separator + "Test"));
    }

    @Test
    public void getPackageNameTest() throws Exception {
        init();
        Assert.assertTrue(projectStructure.getPackageName().equals("test.test"));
    }

    @Test
    public void getProjectName() throws Exception {
        init();
        Assert.assertTrue(projectStructure.getProjectName().equals("Test"));
    }

    @Test
    public void getTestPathTest() throws Exception {
        init();
        String testPath =
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

        Assert.assertEquals(testPath, projectStructure.getTestPath());
    }

    @Test
    public void getSolidityPathTest() throws Exception {
        init();
        String solidityPath =
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
        Assert.assertEquals(solidityPath, projectStructure.getSolidityPath());
    }

    @Test
    public void getMainPathTest() throws Exception {
        init();
        String mainPath =
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

        Assert.assertEquals(mainPath, projectStructure.getMainPath());
    }

    @Test
    public void getWrapperPathTest() throws Exception {
        init();
        String wrapperPath =
                tempDirPath
                        + File.separator
                        + "Test"
                        + File.separator
                        + "gradle"
                        + File.separator
                        + "wrapper"
                        + File.separator;

        Assert.assertEquals(wrapperPath, projectStructure.getWrapperPath());
    }
}
