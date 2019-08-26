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

    private final String projectRoot;

    public ProjectStructure(final String root, final String packageName, final String projectName) {
        this.root = root;
        this.packageName = packageName;
        this.projectName = projectName;
        this.projectRoot = root + File.separator + projectName;
        this.mainPath = createPath(root, projectName, "main", "java", packageName);
        this.testPath = createPath(root, projectName, "test", "java", packageName);
        this.solidityPath = createPath(root, projectName, "main", "solidity");
        this.wrapperPath = getWrapperPath(root, projectName);
    }

    public String getRoot() {
        return root;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getProjectRoot() {
        return projectRoot;
    }

    public String getTestPath() {
        return testPath;
    }

    public String getSolidityPath() {
        return solidityPath;
    }

    public String getMainPath() {
        return mainPath;
    }

    public String getWrapperPath() {
        return wrapperPath;
    }

    private String createPath(
            final String root,
            final String projectName,
            final String test,
            final String java,
            final String packageName) {
        return createPath(root, projectName, test, java)
                + File.separator
                + packageName.replaceAll("[.]", File.separator);
    }

    private String createPath(
            final String root, final String projectName, final String main, final String java) {
        return root
                + File.separator
                + projectName
                + File.separator
                + "src"
                + File.separator
                + main
                + File.separator
                + java;
    }

    private String getWrapperPath(final String root, final String projectName) {
        return root
                + File.separator
                + projectName
                + File.separator
                + "gradle"
                + File.separator
                + "wrapper";
    }

    private void createDirectory(String path) throws Exception {
        File directory = new File(path);
        directory.mkdirs();
    }

    public void createDirectoryStructure() {
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
