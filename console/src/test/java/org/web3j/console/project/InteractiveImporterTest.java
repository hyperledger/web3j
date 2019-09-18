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
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import org.web3j.TempFileProvider;

import static org.junit.Assert.assertEquals;

public class InteractiveImporterTest extends TempFileProvider {
    private static final String FORMATTED_SOLIDITY_PATH =
            "/web3j/console/src/test/resources/Solidity".replaceAll("/", File.separator);
    private InputStream inputStream;

    @Before
    public void init() {
        final String input =
                "Test\norg.com\n" + FORMATTED_SOLIDITY_PATH + "\n" + tempDirPath + "\n";
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
