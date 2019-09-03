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

import static org.web3j.codegen.Console.exitError;

public class Project {

    private Project(Builder builder) {}

    public static class Builder {

        private ProjectStructure projectStructure;
        private TemplateProvider templateProvider;
        private File solidityImportPath;
        private String greeterTemplate;

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

        private void buildGradleProject(String pathToDirectory)
                throws IOException, InterruptedException {
            if (!isWindows()) {
                setExecutable(pathToDirectory, "gradlew");
                executeCommand(
                        new File(pathToDirectory), new String[] {"bash", "-c", "./gradlew build"});
            } else {
                setExecutable(pathToDirectory, "gradlew.bat");
                executeCommand(
                        new File(pathToDirectory),
                        new String[] {"cmd.exe", "/c", "gradlew.bat build"});
            }
        }

        private void setExecutable(final String pathToDirectory, final String gradlew) {
            File f = new File(pathToDirectory + File.separator + gradlew);
            boolean isExecutable = f.setExecutable(true);
        }

        private boolean isWindows() {
            return System.getProperty("os.name").toLowerCase().startsWith("windows");
        }

        private void executeCommand(final File workingDir, final String[] command)
                throws InterruptedException, IOException {
            new ProcessBuilder(command)
                    .directory(workingDir)
                    .redirectOutput(ProcessBuilder.Redirect.INHERIT)
                    .redirectError(ProcessBuilder.Redirect.INHERIT)
                    .start()
                    .waitFor(10, TimeUnit.SECONDS);
        }

        Project build() {
            try {
                projectStructure.createDirectoryStructure();
                ProjectWriter projectWriter = new ProjectWriter();
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
                if (solidityImportPath == null)
                    projectWriter.writeResourceFile(
                            templateProvider.getSolidityProject(),
                            "Greeter.sol",
                            projectStructure.getSolidityPath());
                projectWriter.importSolidityProject(
                        solidityImportPath, projectStructure.getSolidityPath());
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
                buildGradleProject(projectStructure.getProjectRoot());
            } catch (Exception e) {

                exitError(e);
            }

            return new Project(this);
        }
    }
}
