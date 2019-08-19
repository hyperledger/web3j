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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class ProjectCreator extends ProjectManager {

    private String packageName;
    private String projectName;
    private String pathToDirectory;

    public ProjectCreator(String pathToDirectory, String packageName, String projectName) {
        this.pathToDirectory = pathToDirectory;
        this.packageName = packageName;
        this.projectName = projectName;
    }

    public ProjectCreator() {
    }

    public static void main(String[] args) {
        new ProjectCreator(".", args[0], args[1]).run();
    }

    private void run() {

        try {

            String[] projectMainFolder = createFolderStructure(pathToDirectory, projectName);
            String packagePath = createPackageStructure(projectMainFolder[0], packageName);
            generateJavaClass(packagePath, projectName, packageName);
            generateGradleBuildFile(pathToDirectory, projectName, packageName);
            generateGradleSettingsFile(pathToDirectory, projectName);
            generateSolidityContract(projectMainFolder[2]);
            generateGradlewFiles(pathToDirectory + File.separator + projectName);
            generateGradleWrapperPropertiesFile(projectMainFolder[3]);
            copyWrapperJarFromResources(projectMainFolder[3]);
            console.printf("Project created at path : " + "/" + projectName + "\n");
            getOS(projectName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void generateJavaClass(String path, String className, String packageName) {
        String userClassName = ProjectTemplates.rawJavaClass.replaceAll("<class_name>", className);
        String customizedString = userClassName.replaceAll("<package_name>", packageName);
        byte[] javaClass = customizedString.getBytes();
        try {
            Files.write(Paths.get(path + File.separator + className + ".java"), javaClass);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void generateGradleBuildFile(String path, String projectName, String packageName) {
        String temp = ProjectTemplates.rawGradleBuildFile.replaceAll("<package_name>", packageName);
        byte[] buildFile = temp.getBytes();
        try {
            Files.write(Paths.get(path + File.separator + projectName + File.separator + "build.gradle"), buildFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void generateGradleSettingsFile(String path, String projectName) {
        String temp = ProjectTemplates.rawGradleSettingsFile.replaceAll("<project_name>", projectName);
        byte[] settingsFile = temp.getBytes();
        try {
            Files.write(Paths.get(path + File.separator + projectName + File.separator + "settings.gradle"), settingsFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String createPackageStructure(String path, String packageName) {
        File packageStructure;
        packageStructure =
                new File(path + File.separator + packageName.replaceAll("[.]", File.separator));
        packageStructure.mkdirs();
        return packageStructure.getPath();
    }

    void generateSolidityContract(String path) {
        byte[] temp = ProjectTemplates.solidityContract.getBytes();
        try {
            Files.write(Paths.get(path + File.separator + "Greater.sol"), temp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void generateGradlewFiles(String path) {
        byte[] gradlew = ProjectTemplates.gradlew.getBytes();
        try {
            Files.write(Paths.get(path + File.separator + "gradlew"), gradlew);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] gradlewBat = ProjectTemplates.gradlewBat.getBytes();
        try {
            Files.write(Paths.get(path + File.separator + "gradlew.bat"), gradlewBat);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void generateGradleWrapperPropertiesFile(String path) {
        byte[] gradleWrapperProperties = ProjectTemplates.gradleWrapperProperties.getBytes();
        try {
            Files.write(Paths.get(path + File.separator + "gradle-wrapper.properties"), gradleWrapperProperties);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void copyWrapperJarFromResources(String destination) {
        File gradleWrapperJar = new File("/Users/alexander/Documents/DEV/web3j/web3j/console/src/main/resources/gradle-wrapper.jar");
        File destinationDirectory = new File(destination + File.separator + "gradle-wrapper.jar");
        try {
            Files.copy(gradleWrapperJar.toPath(), destinationDirectory.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void getOS(String projectName) {
        String[] os = System.getProperty("os.name").split("");
        if (os[0].equals("Windows")) {

            runCommand(new File(pathToDirectory + File.separator + projectName), "gradlew.bat build");
        } else {

            runCommand(new File(pathToDirectory + File.separator + projectName), "chmod 755 gradlew");
            runCommand(new File(pathToDirectory + File.separator + projectName), "./gradlew build");


        }
    }

    void runCommand(File workingDir, String command) {
        String[] newCommand = command.split(" ");
        for (String s : newCommand) {
            System.out.println(s);

        }
        try {
            new ProcessBuilder(newCommand)
                    .directory(workingDir)
                    .redirectOutput(ProcessBuilder.Redirect.INHERIT)
                    .redirectError(ProcessBuilder.Redirect.INHERIT)
                    .start()
                    .waitFor(60, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
