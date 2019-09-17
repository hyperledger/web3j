package org.web3j.console.project;

import org.junit.Test;
import org.web3j.TempFileProvider;
import picocli.CommandLine;

public class ProjectCreatorCLIRunnerTest extends TempFileProvider {
    @Test(expected = CommandLine.MissingParameterException.class)
    public void testWhenNonDefinedArgsArePassed() {
        final ProjectCreatorCLIRunner projectCreatorCLIRunner = new ProjectCreatorCLIRunner();
        final String[] args = {"-t=org.org", "-b=test", "-z=" + tempDirPath};
        final CommandLine commandLine = new CommandLine(projectCreatorCLIRunner);
        commandLine.parseArgs(args);
    }

    @Test(expected = CommandLine.MissingParameterException.class)
    public void testWhenNoArgsArePassed() {
        final ProjectCreatorCLIRunner projectCreatorCLIRunner = new ProjectCreatorCLIRunner();
        final String[] args = {};
        final CommandLine commandLine = new CommandLine(projectCreatorCLIRunner);
        commandLine.parseArgs(args);
    }

    @Test(expected = CommandLine.OverwrittenOptionException.class)
    public void testWhenDuplicateArgsArePassed() {
        final ProjectCreatorCLIRunner projectCreatorCLIRunner = new ProjectCreatorCLIRunner();
        final String[] args = {"-p=org.org", "-n=test", "-n=OverrideTest", "-o=" + tempDirPath};
        final CommandLine commandLine = new CommandLine(projectCreatorCLIRunner);
        commandLine.parseArgs(args);
    }

}
