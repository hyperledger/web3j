package org.web3j.console.project;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Project {


    private Project(Builder builder) {
    }


    public static class Builder {

        private ProjectStructure projectStructure;
        private TemplateProvider templateProvider;
        private ProjectProcessor projectProcessor;


        public Builder() {

        }

        public Builder withProjectStructure(ProjectStructure projectStructure) {
            this.projectStructure = projectStructure;

            return this;
        }

        public Builder withTemplateProvider(TemplateProvider templateProvider) {
            this.templateProvider = templateProvider;

            return this;
        }

        public Builder withProjectProcessor(ProjectProcessor projectProcessor) {
            this.projectProcessor = projectProcessor;
            return this;
        }

        final void buildGradleProject(String os, String pathToDirectory) {
            if (os.equals("Windows")) {
                runCommand(new File(pathToDirectory), "gradlew.bat build");
            } else {
                runCommand(new File(pathToDirectory), "chmod 755 gradlew");
                runCommand(new File(pathToDirectory), "./gradlew build");
            }
        }

        final void runCommand(File workingDir, String command) {
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

        final String getOS() {
            String[] os = System.getProperty("os.name").split(" ");
            return os[0];
        }


        public Project buildNewProject() {
            ProjectWriter projectWriter = new ProjectWriter();
            projectStructure.createDirectoryStructure();
            projectProcessor.process();
            try {
                projectWriter.writeFile(projectProcessor.getJavaClass(), projectStructure.getProjectName() + ".java", projectStructure.getMainPath());
                projectWriter.writeFile(projectProcessor.getGradleBuild(), File.separator + "build.gradle", projectStructure.getProjectRoot());
                projectWriter.writeFile(projectProcessor.getGradleSettings(), File.separator + "settings.gradle", projectStructure.getProjectRoot());
                projectWriter.writeFile(templateProvider.getSolidityProject(), File.separator + "Greeter.sol", projectStructure.getSolidityPath());
                projectWriter.writeFile(templateProvider.getGradlewWrapperSettings(), File.separator + "gradle-wrapper.properties", projectStructure.getWrapperPath());
                projectWriter.writeFile(templateProvider.getGradlewScript(), File.separator + "gradlew", projectStructure.getProjectRoot());
                projectWriter.writeFile(templateProvider.getGradlewBatScript(), File.separator + "gradlew.bat", projectStructure.getProjectRoot());
                projectWriter.copyFile(templateProvider.getGradlewJar(), projectStructure.getWrapperPath() + File.separator + "gradle-wrapper.jar");
                buildGradleProject(getOS(), projectStructure.getProjectRoot());
            } catch (IOException e) {
                e.getMessage();
            }

            return new Project(this);
        }


    }
}
