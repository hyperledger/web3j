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
import java.util.concurrent.TimeUnit;

import static org.web3j.codegen.Console.exitError;

public class ProjectManager {

    final IODevice console;

    ProjectManager(IODevice console) {
        this.console = console;
    }

    ProjectManager() {
        console = new ConsoleDevice();

        if (console == null) {
            exitError(
                    "Unable to access console - please ensure you are running "
                            + "from the command line");
        }
    }

    String[] createFolderStructure(String path, String projectName) throws Exception {

        String testStructure =
                path
                        + File.separator
                        + projectName
                        + File.separator
                        + "src"
                        + File.separator
                        + "test"
                        + File.separator
                        + "java";

        File testDirectory = new File(testStructure);
        testDirectory.mkdirs();

        String mainStructure =
                path
                        + File.separator
                        + projectName
                        + File.separator
                        + "src"
                        + File.separator
                        + "main"
                        + File.separator
                        + "java";
        File mainDirectory = new File(mainStructure);
        mainDirectory.mkdirs();
        String solidityStructure =
                path
                        + File.separator
                        + projectName
                        + File.separator
                        + "src"
                        + File.separator
                        + "main"
                        + File.separator
                        + "solidity";
        File solidityDirectory = new File(solidityStructure);
        solidityDirectory.mkdirs();

        String gradleWrapperStructure = path
                + File.separator
                + projectName
                + File.separator
                + "gradle"
                + File.separator
                + "wrapper";
        File gradleWrapperDirectory = new File(gradleWrapperStructure);
        gradleWrapperDirectory.mkdirs();

        return new String[]{mainDirectory.getPath(), testDirectory.getPath(), solidityDirectory.getPath(), gradleWrapperDirectory.getPath()};
    }
    void runCommand(File workingDir, String command) {
        String[] newCommand = command.split(" ");
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
    String getOS() {
        String[] os = System.getProperty("os.name").split(" ");
        return  os[0];
    }

}