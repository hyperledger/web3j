package org.web3j.console.project;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.web3j.TempFileProvider;

import static org.junit.Assert.assertEquals;


public class InteractiveOptionsTest extends TempFileProvider {
    private InputStream inputStream;


    @Before
    public void init() {

        final String input = "Test\norg.com\n" + tempDirPath + "\n";
        inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

    }

    @After
    public void restore() {
        System.setIn(System.in);
    }

    @Test
    public void runInteractiveModeTest() throws IOException {
        final InteractiveOptions options = new InteractiveOptions(inputStream, System.out);
        assertEquals("Test", options.getProjectName());
        assertEquals("org.com", options.getPackageName());

        assertEquals(tempDirPath, options.getProjectDestination());
    }


}


