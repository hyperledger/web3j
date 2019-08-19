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
import java.nio.file.Paths;

public class ProjectCreator extends ProjectManager {
    final String rawJavaClass =
            "package <package_name>;\n" + "\n" + "public class <class_name>{\n" + "\n" + "}";
    final String rawGradleBuildFile =
           "plugins {\n" +
                   "    id 'java'\n" +
                   "    id 'org.web3j' version '4.4.0'\n" +
                   "}\n" +
                   "\n" +
                   "group 'org.web3j'\n" +
                   "version '0.2.0'\n" +
                   "\n" +
                   "sourceCompatibility = 1.8\n" +
                   "\n" +
                   "repositories {\n" +
                   "    mavenCentral()\n" +
                   "}\n" +
                   "\n" +
                   "web3j {\n" +
                   "    generatedPackageName = 'org.web3j.sample.contracts.generated'\n" +
                   "    excludedContracts = ['Mortal']\n" +
                   "}\n" +
                   "\n" +
                   "ext {\n" +
                   "    web3jVersion = '4.4.0'\n" +
                   "    logbackVersion = '1.2.3'\n" +
                   "    junitVersion = '4.12'\n" +
                   "}\n" +
                   "\n" +
                   "dependencies {\n" +
                   "    implementation \"org.web3j:core:$web3jVersion\",\n" +
                   "            \"ch.qos.logback:logback-core:$logbackVersion\",\n" +
                   "            \"ch.qos.logback:logback-classic:$logbackVersion\"\n" +
                   "    testImplementation \"junit:junit:$junitVersion\"\n" +
                   "}\n";
    final String rawGradleSettingsFile = "rootProject.name = '<project_name>'\n";
    final String solidityContract = "pragma solidity ^0.4.25;\n" +
            "\n" +
            "// Modified Greeter contract. Based on example at https://www.ethereum.org/greeter.\n" +
            "\n" +
            "contract Mortal {\n" +
            "    /* Define variable owner of the type address*/\n" +
            "    address owner;\n" +
            "\n" +
            "    /* this function is executed at initialization and sets the owner of the contract */\n" +
            "    constructor () public { owner = msg.sender; }\n" +
            "\n" +
            "    /* Function to recover the funds on the contract */\n" +
            "    function kill() public { if (msg.sender == owner) selfdestruct(owner); }\n" +
            "}\n" +
            "\n" +
            "contract Greeter is Mortal {\n" +
            "    /* define variable greeting of the type string */\n" +
            "    string greeting;\n" +
            "\n" +
            "    /* this runs when the contract is executed */\n" +
            "    constructor (string _greeting) public {\n" +
            "        greeting = _greeting;\n" +
            "    }\n" +
            "\n" +
            "    function newGreeting(string _greeting) public {\n" +
            "        emit Modified(greeting, _greeting, greeting, _greeting);\n" +
            "        greeting = _greeting;\n" +
            "    }\n" +
            "\n" +
            "    /* main function */\n" +
            "    function greet() public constant returns (string)  {\n" +
            "        return greeting;\n" +
            "    }\n" +
            "\n" +
            "    /* we include indexed events to demonstrate the difference that can be\n" +
            "    captured versus non-indexed */\n" +
            "    event Modified(\n" +
            "            string indexed oldGreetingIdx, string indexed newGreetingIdx,\n" +
            "            string oldGreeting, string newGreeting);\n" +
            "}";
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
            console.printf("Project created at path : " + "./" + projectName + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void generateJavaClass(String path, String className, String packageName) {
        String userClassName = rawJavaClass.replaceAll("<class_name>", className);
        String customizedString = userClassName.replaceAll("<package_name>", packageName);
        byte[] javaClass = customizedString.getBytes();
        try {
            Files.write(Paths.get(path + File.separator + className + ".java"), javaClass);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void generateGradleBuildFile(String path, String projectName, String packageName) {
        String temp = rawGradleBuildFile.replaceAll("<package_name>", packageName);
        byte[] buildFile = temp.getBytes();
        try {
            Files.write(Paths.get(path + File.separator + projectName + File.separator + "build.gradle"), buildFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void generateGradleSettingsFile(String path, String projectName) {
        String temp = rawGradleSettingsFile.replaceAll("<project_name>", projectName);
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
        byte[] temp = solidityContract.getBytes();
        try {
            Files.write(Paths.get(path + File.separator + "Greater.sol"), temp);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
