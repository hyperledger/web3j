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

import org.junit.Assert;
import org.junit.Test;

import org.web3j.TempFileProvider;

public class ProjectTest extends TempFileProvider {
    ProjectStructure projectStructure = new ProjectStructure(tempDirPath, "test", "test");
    TemplateProvider templateProviderNew =
            new TemplateProvider.Builder()
                    .loadGradlewBatScript("gradlew.bat.template")
                    .loadGradlewScript("gradlew.template")
                    .loadMainJavaClass("Template.java")
                    .loadGradleBuild("build.gradle.template")
                    .loadGradleSettings("settings.gradle.template")
                    .loadGradlewWrapperSettings("gradlew-wrapper.properties.template")
                    .loadGradleJar("gradle-wrapper.jar")
                    .loadSolidityGreeter("Greeter.sol")
                    .withPackageNameReplacement(s -> s.replaceAll("<package_name>", "test"))
                    .withProjectNameReplacement(s -> s.replaceAll("<project_name>", "test"))
                    .build();

    public ProjectTest() throws IOException {}

    public void setUpProject() throws Exception {

        Project project =
                new Project.Builder()
                        .withTemplateProvider(templateProviderNew)
                        .withProjectStructure(projectStructure)
                        .build();
    }

    @Test
    public void directoryCreationTest() throws Exception {
        setUpProject();
        boolean mainProjectDir = new File(projectStructure.getMainPath()).exists();
        boolean gradleWrapperDir = new File(projectStructure.getWrapperPath()).exists();
        boolean testProjectDir = new File(projectStructure.getTestPath()).exists();
        boolean solidityPath = new File(projectStructure.getSolidityPath()).exists();

        Assert.assertTrue(mainProjectDir && gradleWrapperDir && testProjectDir && solidityPath);
    }

    @Test
    public void fileCreationTest() throws Exception {
        setUpProject();
        boolean mainJavaClass =
                new File(projectStructure.getMainPath() + File.separator + "test.java").exists();
        boolean greeterContract =
                new File(projectStructure.getSolidityPath() + File.separator + "Greeter.sol")
                        .exists();
        boolean gradleBuild =
                new File(projectStructure.getProjectRoot() + File.separator + "build.gradle")
                        .exists();
        boolean gradleSettings =
                new File(projectStructure.getProjectRoot() + File.separator + "settings.gradle")
                        .exists();
        boolean gradleWrapperSettings =
                new File(
                                projectStructure.getWrapperPath()
                                        + File.separator
                                        + "gradle-wrapper.properties")
                        .exists();
        boolean gradleWrapperJar =
                new File(projectStructure.getWrapperPath() + File.separator + "gradle-wrapper.jar")
                        .exists();
        boolean gradlewBatScript =
                new File(projectStructure.getProjectRoot() + File.separator + "gradlew.bat")
                        .exists();
        boolean gradlewScript =
                new File(projectStructure.getProjectRoot() + File.separator + "gradlew").exists();

        Assert.assertTrue(
                mainJavaClass
                        && greeterContract
                        && gradleBuild
                        && gradleSettings
                        && gradleWrapperSettings
                        && gradleWrapperJar
                        && gradlewBatScript
                        && gradlewScript);
    }
}
