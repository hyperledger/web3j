package org.web3j.console.project;

import picocli.CommandLine;

import static org.web3j.codegen.Console.exitError;
import static org.web3j.console.project.ProjectImporter.COMMAND_IMPORT;

@CommandLine.Command(
        name = COMMAND_IMPORT)
public class ProjectImporterCLIRunner extends ProjectCreatorCLIRunner {


    @CommandLine.Option(
            names = {"-s", "--solidity path"},
            description = "path to solidity file/folder",
            required = true)
    String solidityImportPath;

    @Override
    public void run() {
        if (requiredArgsAreEmpty(projectName, packageName, solidityImportPath)) {
            System.out.println(projectName + packageName + solidityImportPath);
            exitError("Project name, package name and solidity project cannot be empty");
        }
        try {
            new ProjectImporter(root, packageName, projectName, solidityImportPath).generate();
        } catch (final Exception e) {
            exitError(e);
        }
    }


}

