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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TemplateProvider {
    private final String mainJavaClass;
    private final String gradleBuild;
    private final String gradleSettings;
    private final String solidityProject;
    private final String gradlewWrapperSettings;
    private final String gradlewBatScript;
    private final String gradlewScript;
    private final InputStream gradlewJar;

    private TemplateProvider(
            final String mainJavaClass,
            final String gradleBuild,
            final String gradleSettings,
            final String solidityProject,
            final String gradlewWrapperSettings,
            final String gradlewBatScript,
            final String gradlewScript,
            final InputStream gradlewJar) {
        this.mainJavaClass = mainJavaClass;
        this.gradleBuild = gradleBuild;
        this.gradleSettings = gradleSettings;
        this.solidityProject = solidityProject;
        this.gradlewWrapperSettings = gradlewWrapperSettings;
        this.gradlewBatScript = gradlewBatScript;
        this.gradlewScript = gradlewScript;
        this.gradlewJar = gradlewJar;
    }

    String getMainJavaClass() {
        return mainJavaClass;
    }

    String getGradleBuild() {
        return gradleBuild;
    }

    String getGradleSettings() {
        return gradleSettings;
    }

    String getSolidityProject() {
        return solidityProject;
    }

    String getGradlewWrapperSettings() {
        return gradlewWrapperSettings;
    }

    String getGradlewBatScript() {
        return gradlewBatScript;
    }

    String getGradlewScript() {
        return gradlewScript;
    }

    InputStream getGradlewJar() {
        return gradlewJar;
    }

    public static class Builder {
        private String mainJavaClass;
        private String gradleBuild;
        private String gradleSettings;
        private String solidityProject;
        private String gradlewWrapperSettings;
        private String gradlewBatScript;
        private String gradlewScript;
        private InputStream gradlewJar;

        Builder loadMainJavaClass(String name) throws IOException {

            InputStream resourcePath = getClass().getClassLoader().getResourceAsStream(name);
            this.mainJavaClass = readFile(resourcePath);

            return this;
        }

        Builder loadGradleBuild(String name) throws IOException {
            InputStream resourcePath = getClass().getClassLoader().getResourceAsStream(name);
            this.gradleBuild = readFile(resourcePath);
            return this;
        }

        Builder loadGradleSettings(String name) throws IOException {
            InputStream resourcePath = getClass().getClassLoader().getResourceAsStream(name);
            this.gradleSettings = readFile(resourcePath);
            return this;
        }

        Builder loadSolidityProject(String name) throws IOException {
            InputStream resourcePath = getClass().getClassLoader().getResourceAsStream(name);
            this.solidityProject = readFile(resourcePath);
            return this;
        }

        Builder loadGradlewWrapperSettings(String name) throws IOException {
            InputStream resourcePath = getClass().getClassLoader().getResourceAsStream(name);
            this.gradlewWrapperSettings = readFile(resourcePath);
            return this;
        }

        Builder loadGradlewBatScript(String name) throws IOException {
            InputStream resourcePath = getClass().getClassLoader().getResourceAsStream(name);
            this.gradlewBatScript = readFile(resourcePath);
            return this;
        }

        Builder loadGradlewScript(String name) throws IOException {
            InputStream resourcePath = getClass().getClassLoader().getResourceAsStream(name);
            this.gradlewScript = readFile(resourcePath);
            return this;
        }

        Builder loadGradleJar(String name) {
            this.gradlewJar = getClass().getClassLoader().getResourceAsStream(name);
            return this;
        }

        TemplateProvider build() {
            return new TemplateProvider(
                    mainJavaClass,
                    gradleBuild,
                    gradleSettings,
                    solidityProject,
                    gradlewWrapperSettings,
                    gradlewBatScript,
                    gradlewScript,
                    gradlewJar);
        }

        private String readFile(InputStream file) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(file));
            String temp;
            StringBuilder stringBuilder = new StringBuilder();
            while ((temp = reader.readLine()) != null) {
                stringBuilder.append(temp).append("\n");
            }
            return stringBuilder.toString();
        }
    }
}
