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

import picocli.CommandLine;

import java.io.IOException;

import static org.web3j.codegen.Console.exitError;
import static org.web3j.utils.Collection.tail;
import static picocli.CommandLine.Help.Visibility.ALWAYS;

public class ProjectCreator {
    private static final String COMMAND_PREFIX = "new";
    private static final String COMMAND_NEW = "new";
    private static final String COMMAND_IMPORT = "import";


    private final String root;
    private final String packageName;
    private final String projectName;

    private ProjectCreator(String root, String packageName, String projectName) {
        this.root = root;
        this.packageName = packageName;
        this.projectName = projectName;
    }

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals(COMMAND_NEW)) {
            args = tail(args);
        }

        if (args.length > 0 && args[0].equals(COMMAND_IMPORT)) {
            args = tail(args);
        }

        CommandLine.run(new PicocliRunner(), args);

    }



    private void generate() throws IOException {
        ProjectStructure projectStructure = new ProjectStructure(root,packageName,projectName);
        TemplateProvider templateProvider = new TemplateProvider.Builder()
                .loadSolidityProject("Greeter.sol")
                .loadGradlewBatScript("gradlew.bat.template")
                .loadGradlewScript("gradlew.template")
                .loadMainJavaClass("Template.java")
                .loadGradleBuild("build.gradle.template")
                .loadGradleSettings("settings.gradle.template")
                .loadGradlewWrapperSettings("gradlew-wrapper.properties.template")
                .loadGradleJar("gradle-wrapper.jar")
                .build();
        ProjectProcessor projectProcessor = new ProjectProcessor(projectStructure,templateProvider);

        Project project = new Project.Builder()
                .withProjectStructure(projectStructure)
                .withTemplateProvider(templateProvider)
                .withProjectProcessor(projectProcessor)
                .buildNewProject();

    }

    @CommandLine.Command(
            name = COMMAND_PREFIX,
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
                names = {"-pn", "--project name"},
                description = "use native java types.",
                required = true)
        private String projectName;


        @Override
        public void run() {
            try {

                new ProjectCreator(
                        root, packageName, projectName)
                        .generate();
            } catch (Exception e) {
                exitError(e);
            }
        }
    }


}
