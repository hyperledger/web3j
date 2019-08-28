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
import java.util.concurrent.TimeUnit;

public class Project {

    private Project(Builder builder) {}

    public static class Builder {

        private ProjectStructure projectStructure;
        private TemplateProvider templateProvider;
        private File solidityImportPath;

        Builder() {}

        Builder withSolidityFile(File solidityImportPath) {
            this.solidityImportPath = solidityImportPath;
            return this;
        }

        Builder withProjectStructure(ProjectStructure projectStructure) {
            this.projectStructure = projectStructure;

            return this;
        }

        Builder withTemplateProvider(TemplateProvider templateProvider) {
            this.templateProvider = templateProvider;

            return this;
        }

        final void buildGradleProject(String os, String pathToDirectory) {
            if (os.equals("Windows")) {
                runCommand(new File(pathToDirectory), "gradlew.bat build");
            } else {
                runCommand(new File(pathToDirectory), "chmod 755 gradlew");
                runCommand(new File(pathToDirectory), "./gradlew build");
            }
        }

        final void runCommand(File workingDir, String command) {
            String[] newCommand = command.split(" ");
            try {
                new ProcessBuilder(newCommand)
                        .directory(workingDir)
                        .redirectOutput(ProcessBuilder.Redirect.INHERIT)
                        .redirectError(ProcessBuilder.Redirect.INHERIT)
                        .start()
                        .waitFor(60, TimeUnit.MINUTES);
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }

        private String getOS() {
            String[] os = System.getProperty("os.name").split(" ");
            return os[0];
        }

        Project build() {
            projectStructure.createDirectoryStructure();
            ProjectWriter projectWriter = new ProjectWriter();

            try {
                projectWriter.writeResourceFile(
                        templateProvider.getMainJavaClass(),
                        projectStructure.getProjectName() + ".java",
                        projectStructure.getMainPath());
                projectWriter.writeResourceFile(
                        templateProvider.getGradleBuild(),
                        File.separator + "build.gradle",
                        projectStructure.getProjectRoot());
                projectWriter.writeResourceFile(
                        templateProvider.getGradleSettings(),
                        File.separator + "settings.gradle",
                        projectStructure.getProjectRoot());
                projectWriter.writeSolidity(solidityImportPath, projectStructure.getSolidityPath());
                projectWriter.writeResourceFile(
                        templateProvider.getGradlewWrapperSettings(),
                        File.separator + "gradle-wrapper.properties",
                        projectStructure.getWrapperPath());
                projectWriter.writeResourceFile(
                        templateProvider.getGradlewScript(),
                        File.separator + "gradlew",
                        projectStructure.getProjectRoot());
                projectWriter.writeResourceFile(
                        templateProvider.getGradlewBatScript(),
                        File.separator + "gradlew.bat",
                        projectStructure.getProjectRoot());
                projectWriter.copyResourceFile(
                        templateProvider.getGradlewJar(),
                        projectStructure.getWrapperPath() + File.separator + "gradle-wrapper.jar");
                buildGradleProject(getOS(), projectStructure.getProjectRoot());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return new Project(this);
        }
    }
}
