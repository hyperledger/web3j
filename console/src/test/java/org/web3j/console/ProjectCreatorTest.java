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
package org.web3j.console;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import org.web3j.TempFileProvider;

public class ProjectCreatorTest extends TempFileProvider {
    ProjectCreator projectCreator = new ProjectCreator();

    @Test
    public void testCreateFolderStructure() throws Exception {
        super.setUp();
        String[] generatedPath = projectCreator.createFolderStructure(tempDirPath, "Test");
        Assert.assertEquals(
                generatedPath,
                tempDirPath
                        + File.separator
                        + "Test"
                        + File.separator
                        + "src"
                        + File.separator
                        + "main"
                        + File.separator
                        + "java");
    }

    @Test
    public void testCreatePackageStructure() throws Exception {
        super.setUp();
        String packageStructure =
                projectCreator.createPackageStructure(tempDirPath, "org.com.test");
        Assert.assertEquals(
                packageStructure,
                tempDirPath
                        + File.separator
                        + "org"
                        + File.separator
                        + "com"
                        + File.separator
                        + "test");
    }

    @Test
    public void testCreateJavaFile() throws Exception {
        super.setUp();
        projectCreator.generateJavaClass(tempDirPath, "TestClass", "org.com.test");
        String filePath = tempDirPath + File.separator + "TestClass.java";
        Assert.assertTrue(new File(filePath).exists());
    }

    @Test
    public void testGenerateGradleBuildFile() throws Exception {
        super.setUp();
        projectCreator.generateGradleBuildFile(tempDirPath,"Test","org.com.test");
        String filePath = tempDirPath + File.separator + "build.gradle";
        Assert.assertTrue(new File(filePath).exists());
    }

    @Test
    public void testGenerateGradleSettingsFile() throws Exception {
        super.setUp();
        projectCreator.generateGradleSettingsFile(tempDirPath, "TestProject");
        String filePath = tempDirPath + File.separator + "settings.gradle";
        System.out.println(filePath);
        Assert.assertTrue(new File(filePath).exists());
    }
    @Test
    public void testCopyWrapperJarFromResources()
    {

    }
}