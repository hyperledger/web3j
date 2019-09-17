package org.web3j.console.project;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.web3j.TempFileProvider;

import static org.junit.Assert.assertEquals;


public class InteractiveImporterTest extends TempFileProvider {
    private static final String FORMATTED_SOLIDITY_PATH = "/web3j/console/src/test/resources/Solidity".replaceAll("/", File.separator);
    private InputStream inputStream;

    @Before
    public void init() {
        final String input = "Test\norg.com\n" + FORMATTED_SOLIDITY_PATH + "\n" + tempDirPath + "\n";
        inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

    }

    @Test
    public void runInteractiveModeTest() {
        final InteractiveImporter options = new InteractiveImporter(inputStream, System.out);
        assertEquals("Test", options.getProjectName());
        assertEquals("org.com", options.getPackageName());
        assertEquals(FORMATTED_SOLIDITY_PATH, options.getSolidityProjectPath());
        assertEquals(Optional.of(tempDirPath), options.getProjectDestination());
    }

}


