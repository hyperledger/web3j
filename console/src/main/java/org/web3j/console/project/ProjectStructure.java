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

public class ProjectStructure {

    private final String packageName;
    private final String projectName;
    private final String testPath;
    private final String solidityPath;
    private final String mainPath;
    private final String wrapperPath;
    private final String root;
    private final String formattedPackageName;
    private final String projectRoot;

    ProjectStructure(final String root, final String packageName, final String projectName) {
        this.root = root;
        this.packageName = packageName;
        this.formattedPackageName = formatPackageName(packageName);
        this.projectName = projectName;
        this.projectRoot = root + File.separator + projectName;
        this.mainPath =
                generatePath(this.projectRoot, "src", "main", "java", this.formattedPackageName);
        this.solidityPath = generatePath(this.projectRoot, "src", "main", "solidity");
        this.testPath =
                generatePath(this.projectRoot, "src", "test", "java", this.formattedPackageName);
        this.wrapperPath = generatePath(this.projectRoot, "gradle", "wrapper");
    }

    String getRoot() {
        return root;
    }

    String getProjectRoot() {
        return projectRoot;
    }

    String getPackageName() {
        return packageName;
    }

    String getProjectName() {
        return projectName;
    }

    String getTestPath() {
        return testPath;
    }

    String getSolidityPath() {
        return solidityPath;
    }

    String getMainPath() {
        return mainPath;
    }

    String getWrapperPath() {
        return wrapperPath;
    }

    private String generatePath(String... a) {
        StringBuilder finalPath = new StringBuilder();
        for (String b : a) {
            finalPath.append(b + File.separator);
        }
        return finalPath.toString();
    }

    private String formatPackageName(String packageName) {
        return packageName.replaceAll("[.]", File.separator);
    }

    private void createDirectory(String path) throws Exception {
        File directory = new File(path);
        directory.mkdirs();
    }

    void createDirectoryStructure() {
        try {

            createDirectory(mainPath);
            createDirectory(testPath);
            createDirectory(solidityPath);
            createDirectory(wrapperPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
