/*
 * Copyright 2019 Web3 Labs LTD.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.web3j.console.project;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.web3j.TempFileProvider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
    public void runInteractiveModeTest() {
        final InteractiveOptions options = new InteractiveOptions(inputStream, System.out);
        assertEquals("Test", options.getProjectName());
        assertEquals("org.com", options.getPackageName());
        assertEquals(tempDirPath, options.getProjectDestination().get());
    }

    @Test
    public void createNewProjectInteractive() {
        final String[] args = {"new", "interactive"};
        ProjectCreator.main(args);
        assertTrue(new File(tempDirPath + File.separator + "Test").exists());
    }
}
