package org.web3j.console;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class SolidityProjectCreator extends ProjectCreator {
    private String packageName;
    private String projectName;
    private String pathToDirectory;
    private String pathToSolidityProject;

    private SolidityProjectCreator(String pathToDirectory, String pathToSolidityProject, String packageName, String projectName) {
        this.pathToDirectory = pathToDirectory;
        this.pathToSolidityProject = pathToSolidityProject;
        this.packageName = packageName;
        this.projectName = projectName;
    }

    public SolidityProjectCreator() {
    }

    public static void main(String[] args) {
        new SolidityProjectCreator(".", args[0], args[1], args[2]).run();
    }

    private void run() {

        try {

            String[] projectMainFolder = createFolderStructure(pathToDirectory, projectName);
            String packagePath = createPackageStructure(projectMainFolder[0], packageName);
            generateJavaClass(packagePath, projectName, packageName);
            generateGradleBuildFile(pathToDirectory + File.separator + projectName, packageName);
            generateGradleSettingsFile(pathToDirectory + File.separator + projectName, projectName);
            getSolidityProject(pathToSolidityProject, projectMainFolder[2] + File.separator);
            generateGradlewFiles(pathToDirectory + File.separator + projectName);
            generateGradleWrapperPropertiesFile(projectMainFolder[3]);
            copyWrapperJarFromResources(projectMainFolder[3]);
            buildGradleProject(getOS(), pathToDirectory, projectName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void getSolidityFile(String path, String destinationDirectory) {
        if (new File(path).exists()) {
            try {
                Files.copy(new File(path).toPath(), new File(destinationDirectory + File.separator + new File(path).getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void getSolidityFiles(String path, String destination) {

        for (String fileName : new File(path).list()) {
            if (fileName.endsWith(".sol"))
                try {
                    Files.copy(new File(path + File.separator + fileName).toPath(), new File(destination + File.separator + fileName).toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }

        }
    }

    void getSolidityProject(String path, String destination) {
        File solidityPath = new File(path);
        if (solidityPath.exists() && solidityPath.isDirectory()) {
            if (solidityPath.isDirectory()) {
                getSolidityFiles(path, destination);
            } else {
                getSolidityFile(path, destination);
            }


        }


    }

}
