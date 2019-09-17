package org.web3j.console.project;

import org.junit.Test;
import org.web3j.TempFileProvider;
import picocli.CommandLine;

public class ProjectImporterCLIRunnerTest extends TempFileProvider {
    @Test(expected = CommandLine.MissingParameterException.class)
    public void testWhenNonDefinedArgsArePassed() {
        final ProjectImporterCLIRunner projectImporterCLIRunner = new ProjectImporterCLIRunner();
        final String[] args = {"-t=org.org", "-b=test", "-z=" + tempDirPath};
        final CommandLine commandLine = new CommandLine(projectImporterCLIRunner);
        commandLine.parseArgs(args);
    }

    @Test(expected = CommandLine.MissingParameterException.class)
    public void testWhenNoArgsArePassed() {
        final ProjectImporterCLIRunner projectImporterCLIRunner = new ProjectImporterCLIRunner();
        final String[] args = {};
        final CommandLine commandLine = new CommandLine(projectImporterCLIRunner);
        commandLine.parseArgs(args);
    }

    @Test(expected = CommandLine.OverwrittenOptionException.class)
    public void testWhenDuplicateArgsArePassed() {
        final ProjectImporterCLIRunner projectImporterCLIRunner = new ProjectImporterCLIRunner();
        final String[] args = {"-p=org.org", "-n=test", "-n=OverrideTest", "-o=" + tempDirPath ,"-s=test"};
        final CommandLine commandLine = new CommandLine(projectImporterCLIRunner);
        commandLine.parseArgs(args);
    }

}
