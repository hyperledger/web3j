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

    private TemplateProvider(final String mainJavaClass, final String gradleBuild, final String gradleSettings, final String solidityProject, final String gradlewWrapperSettings, final String gradlewBatScript, final String gradlewScript, final InputStream gradlewJar) {
        this.mainJavaClass = mainJavaClass;
        this.gradleBuild = gradleBuild;
        this.gradleSettings = gradleSettings;
        this.solidityProject = solidityProject;
        this.gradlewWrapperSettings = gradlewWrapperSettings;
        this.gradlewBatScript = gradlewBatScript;
        this.gradlewScript = gradlewScript;
        this.gradlewJar = gradlewJar;
    }


    public String getMainJavaClass() {
        return mainJavaClass;
    }

    public String getGradleBuild() {
        return gradleBuild;
    }

    public String getGradleSettings() {
        return gradleSettings;
    }

    public String getSolidityProject() {
        return solidityProject;
    }

    public String getGradlewWrapperSettings() {
        return gradlewWrapperSettings;
    }

    public String getGradlewBatScript() {
        return gradlewBatScript;
    }

    public String getGradlewScript() {
        return gradlewScript;
    }
    public InputStream getGradlewJar() {
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


        public Builder loadMainJavaClass(String name) throws IOException {

            InputStream resourcePath = getClass().getClassLoader().getResourceAsStream(name);
            this.mainJavaClass = readFile(resourcePath);


            return this;
        }

        public Builder loadGradleBuild(String name) throws IOException {
            InputStream resourcePath = getClass().getClassLoader().getResourceAsStream(name);
            this.gradleBuild = readFile(resourcePath);
            return this;
        }

        public Builder loadGradleSettings(String name) throws IOException {
            InputStream resourcePath = getClass().getClassLoader().getResourceAsStream(name);
            this.gradleSettings = readFile(resourcePath);
            return this;
        }

        public Builder loadSolidityProject(String name) throws IOException {
            InputStream resourcePath = getClass().getClassLoader().getResourceAsStream(name);
            this.solidityProject = readFile(resourcePath);
            return this;
        }

        public Builder loadGradlewWrapperSettings(String name) throws IOException {
            InputStream resourcePath = getClass().getClassLoader().getResourceAsStream(name);
            this.gradlewWrapperSettings = readFile(resourcePath);
            return this;
        }

        public Builder loadGradlewBatScript(String name) throws IOException {
            InputStream resourcePath = getClass().getClassLoader().getResourceAsStream(name);
            this.gradlewBatScript = readFile(resourcePath);
            return this;
        }

        public Builder loadGradlewScript(String name) throws IOException {
            InputStream resourcePath = getClass().getClassLoader().getResourceAsStream(name);
            this.gradlewScript = readFile(resourcePath);
            return this;
        }

        public Builder loadGradleJar(String name) throws IOException {
            InputStream resourcePath = getClass().getClassLoader().getResourceAsStream(name);
            this.gradlewJar = resourcePath;
            return this;
        }


        public TemplateProvider build() {
            return new TemplateProvider(mainJavaClass, gradleBuild, gradleSettings, solidityProject, gradlewWrapperSettings, gradlewBatScript, gradlewScript, gradlewJar);
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
