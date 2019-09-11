package org.web3j.console.project;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Optional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.web3j.TempFileProvider;

import static org.junit.Assert.assertEquals;


public class InteractiveImporterTest extends TempFileProvider {
    String formattedSolidityPath = "/web3j/console/src/test/resources/Solidity".replaceAll("/", File.separator);
    private InputStream inputStream;

    @Before
    public void init() {
        final String input = "Test\norg.com\n" + formattedSolidityPath + "\n" + tempDirPath + "\n";
        inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

    }

    @After
    public void restore() {
        System.setIn(System.in);
    }

    @Test
    public void runInteractiveModeTest() {
        final InteractiveImporter options = new InteractiveImporter(inputStream, System.out);
        assertEquals("Test", options.getProjectName());
        assertEquals("org.com", options.getPackageName());
        assertEquals(formattedSolidityPath, options.getSolidityProjectPath());
        assertEquals(Optional.of(tempDirPath), options.getProjectDestination());
    }


}


