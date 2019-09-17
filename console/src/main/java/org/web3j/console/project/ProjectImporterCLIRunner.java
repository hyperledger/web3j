package org.web3j.console.project;

import org.web3j.console.project.utills.InputVerifier;
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
        if (InputVerifier.requiredArgsAreNotEmpty(projectName, packageName, solidityImportPath) && InputVerifier.classNameIsValid(projectName) && InputVerifier.packageNameIsValid(packageName)) {
            try {
                new ProjectImporter(root, packageName, projectName, solidityImportPath).generate();
            } catch (final Exception e) {
                exitError(e);
            }
        }
    }
}

