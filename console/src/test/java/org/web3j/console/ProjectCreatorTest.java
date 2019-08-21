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
import java.lang.reflect.Field;

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
                generatedPath[0],
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
        projectCreator.generateGradleBuildFile(tempDirPath, "org.com.test");
        Assert.assertTrue(new File(tempDirPath + File.separator + "build.gradle").exists());
    }

    @Test
    public void testGenerateGradleSettingsFile() throws Exception {
        super.setUp();
        projectCreator.generateGradleSettingsFile(tempDirPath, "TestProject");
        Assert.assertTrue(new File(tempDirPath + File.separator + "settings.gradle").exists());
    }

    @Test
    public void testGenerateSolidityContract() throws Exception {
        super.setUp();
        projectCreator.generateSolidityContract(tempDirPath);
        Assert.assertTrue(new File(tempDirPath + File.separator + "Greater.sol").exists());

    }

    @Test
    public void testGenerateGradlewFiles() throws Exception {
        super.setUp();
        projectCreator.generateGradlewFiles(tempDirPath);
        if (new File(tempDirPath + File.separator + "gradlew.bat").exists() && new File(tempDirPath + File.separator + "gradlew").exists()) {
            Assert.assertTrue(true);
        } else {
            Assert.assertTrue(false);
        }
    }

    @Test
    public void testGenerateGradlewPropertiesFile() throws Exception {
        super.setUp();
        projectCreator.generateGradleWrapperPropertiesFile(tempDirPath);
        Assert.assertTrue(new File(tempDirPath+File.separator+"gradle-wrapper.properties").exists());
    }

    @Test
    public void testCopyWrapperJarFromResources() throws Exception {
        super.setUp();
        projectCreator.copyWrapperJarFromResources(tempDirPath);
        String pathToFile = tempDirPath + File.separator + "gradle-wrapper.jar";
        Assert.assertTrue(new File(pathToFile).exists());

    }

}