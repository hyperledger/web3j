package org.web3j.console.project;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.web3j.TempFileProvider;

import static org.junit.Assert.assertEquals;


public class InteractiveImporterTest extends TempFileProvider {
    private InputStream inputStream;
    String formattedSolidityPath = "/web3j/console/src/test/resources/Solidity".replaceAll("/", File.separator);

    @Before
    public void init() {
        final String input = "Test\norg.com\n"+formattedSolidityPath+"\n"+ tempDirPath + "\n";
        inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

    }

    @After
    public void restore() {
        System.setIn(System.in);
    }

    @Test
    public void runInteractiveModeTest() throws IOException {
        final InteractiveImporter options = new InteractiveImporter(inputStream, System.out);
        assertEquals("Test", options.getProjectName());
        assertEquals("org.com", options.getPackageName());
        assertEquals(formattedSolidityPath,options.getSolidityProjectPath());
        assertEquals(tempDirPath, options.getProjectDestination());
    }


}


