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
import java.util.function.Function;

public class TemplateProvider {
    private final String mainJavaClass;
    private final String solidityProject;
    private final String gradleBuild;
    private final String gradleSettings;
    private final String gradlewWrapperSettings;
    private final String gradlewBatScript;
    private final String gradlewScript;
    private final InputStream gradlewJar;

    private TemplateProvider(
            final String mainJavaClass,
            final String solidityProject,
            final String gradleBuild,
            final String gradleSettings,
            final String gradlewWrapperSettings,
            final String gradlewBatScript,
            final String gradlewScript,
            final InputStream gradlewJar) {
        this.mainJavaClass = mainJavaClass;
        this.solidityProject = solidityProject;
        this.gradleBuild = gradleBuild;
        this.gradleSettings = gradleSettings;
        this.gradlewWrapperSettings = gradlewWrapperSettings;
        this.gradlewBatScript = gradlewBatScript;
        this.gradlewScript = gradlewScript;
        this.gradlewJar = gradlewJar;
    }

    String getSolidityProject() {
        return solidityProject;
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
        private String gradlewWrapperSettings;
        private String gradlewBatScript;
        private String gradlewScript;
        private String solidityProject;
        private InputStream gradlewJar;
        private Function<String, String> packageNameReplacement = s -> s;
        private Function<String, String> projectNameReplacement = s -> s;

        public Builder loadMainJavaClass(final String name) throws IOException {
            this.mainJavaClass = readFile(name);
            return this;
        }

        public Builder loadGradleBuild(final String name) throws IOException {
            this.gradleBuild = readFile(name);
            return this;
        }

        public Builder loadSolidityGreeter(final String name) throws IOException {

            this.solidityProject = readFile(name);

            return this;
        }

        public Builder loadGradleSettings(final String name) throws IOException {

            this.gradleSettings = readFile(name);
            return this;
        }

        public Builder loadGradlewWrapperSettings(final String name) throws IOException {

            this.gradlewWrapperSettings = readFile(name);
            return this;
        }

        public Builder loadGradlewBatScript(final String name) throws IOException {

            this.gradlewBatScript = readFile(name);
            return this;
        }

        public Builder loadGradlewScript(final String name) throws IOException {

            this.gradlewScript = readFile(name);
            return this;
        }

        public Builder loadGradleJar(final String name) {
            this.gradlewJar = getClass().getClassLoader().getResourceAsStream(name);
            return this;
        }

        public Builder withPackageNameReplacement(
                final Function<String, String> packageNameReplacement) {
            this.packageNameReplacement = packageNameReplacement;
            return this;
        }

        public Builder withProjectNameReplacement(
                final Function<String, String> classNameReplacement) {
            this.projectNameReplacement = classNameReplacement;
            return this;
        }

        TemplateProvider build() {
            return new TemplateProvider(
                    projectNameReplacement.apply(packageNameReplacement.apply(mainJavaClass)),
                    solidityProject,
                    packageNameReplacement.apply(gradleBuild),
                    projectNameReplacement.apply(gradleSettings),
                    gradlewWrapperSettings,
                    gradlewBatScript,
                    gradlewScript,
                    gradlewJar);
        }

        private String readFile(final String name) throws IOException {
            try (final InputStream stream = getClass().getClassLoader().getResourceAsStream(name)) {
                return readStream(stream);
            }
        }

        private String readStream(final InputStream stream) throws IOException {

            try (final BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
                String temp;
                final StringBuilder stringBuilder = new StringBuilder();
                while ((temp = reader.readLine()) != null) {
                    stringBuilder.append(temp).append("\n");
                }
                return stringBuilder.toString();
            }
        }
    }
}
