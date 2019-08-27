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

class ProjectProcessor {
    private final ProjectStructure projectStructure;
    private final TemplateProvider templateProvider;
    private String javaClass;
    private String gradleBuild;
    private String gradleSettings;

    String getJavaClass() {
        return javaClass;
    }

    String getGradleBuild() {
        return gradleBuild;
    }

    String getGradleSettings() {
        return gradleSettings;
    }

    ProjectProcessor(ProjectStructure projectStructure, TemplateProvider templateProvider) {
        this.projectStructure = projectStructure;
        this.templateProvider = templateProvider;
    }

    private void mainJavaClass() {

        this.javaClass =
                customizeFile(
                        templateProvider.getMainJavaClass(),
                        "<package_name>",
                        projectStructure.getPackageName());

        this.javaClass =
                customizeFile(javaClass, "<class_name>", projectStructure.getProjectName());
    }

    private void gradleBuild() {
        this.gradleBuild =
                customizeFile(
                        templateProvider.getGradleBuild(),
                        "<package_name>",
                        projectStructure.getPackageName());
    }

    private void gradleSettings() {
        this.gradleSettings =
                customizeFile(
                        templateProvider.getGradleSettings(),
                        "<project_name>",
                        projectStructure.getProjectName());
    }

    private String customizeFile(String template, String regex, String replacement) {
        return template.replaceAll(regex, replacement);
    }

    void process() {
        mainJavaClass();
        gradleBuild();
        gradleSettings();
    }
}
