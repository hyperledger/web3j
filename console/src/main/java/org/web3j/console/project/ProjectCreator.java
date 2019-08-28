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
import java.net.URISyntaxException;
import java.net.URL;

import picocli.CommandLine;

import static org.web3j.utils.Collection.tail;
import static picocli.CommandLine.Help.Visibility.ALWAYS;

public class ProjectCreator {

    public static final String COMMAND_NEW = "new";

    final ProjectStructure projectStructure;
    final TemplateProvider templateProvider;

    ProjectCreator(String root, String packageName, String projectName) throws IOException {
        this.projectStructure = new ProjectStructure(root, packageName, projectName);

        this.templateProvider =
                new TemplateProvider.Builder()
                        .loadGradlewBatScript("gradlew.bat.template")
                        .loadGradlewScript("gradlew.template")
                        .loadMainJavaClass("Template.java")
                        .loadGradleBuild("build.gradle.template")
                        .loadGradleSettings("settings.gradle.template")
                        .loadGradlewWrapperSettings("gradlew-wrapper.properties.template")
                        .loadGradleJar("gradle-wrapper.jar")
                        .withPackageNameReplacement(
                                s -> s.replaceAll("<package_name>", packageName))
                        .withProjectNameReplacement(
                                s -> s.replaceAll("<project_name>", projectName))
                        .build();
    }

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals(COMMAND_NEW)) {
            args = tail(args);
        }
        CommandLine.run(new PicocliRunner(), args);
    }

    private void generate() {
        URL solidityUrl = ClassLoader.getSystemResource("Greeter.sol");
        File templateSolidityFile = null;
        try {
            templateSolidityFile = new File(solidityUrl.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        Project project =
                new Project.Builder()
                        .withProjectStructure(projectStructure)
                        .withTemplateProvider(templateProvider)
                        .withSolidityFile(templateSolidityFile)
                        .build();
    }

    @CommandLine.Command(
            name = COMMAND_NEW,
            mixinStandardHelpOptions = true,
            version = "4.0",
            sortOptions = false)
    static class PicocliRunner implements Runnable {
        @CommandLine.Option(
                names = {"-o", "--outputDir"},
                description = "destination base directory.",
                required = false,
                showDefaultValue = ALWAYS)
        private String root = System.getProperty("user.dir");

        @CommandLine.Option(
                names = {"-p", "--package"},
                description = "base package name.",
                required = true)
        private String packageName;

        @CommandLine.Option(
                names = {"-n", "--project name"},
                description = "project name.",
                required = true)
        private String projectName;

        @Override
        public void run() {
            try {
                new ProjectCreator(root, packageName, projectName).generate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
