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
package org.web3j;

import java.io.File;
import java.nio.file.Files;

import org.junit.After;
import org.junit.Before;

/** Base class for tests wishing to use temporary file locations. */
public class TempFileProvider {
    private File tempDir;
    protected String tempDirPath;

    @Before
    public void setUp() throws Exception {
        tempDir = Files.createTempDirectory(TempFileProvider.class.getSimpleName()).toFile();
        tempDirPath = tempDir.getPath();
    }

    @After
    public void tearDown() throws Exception {
        for (File file : tempDir.listFiles()) {
            file.delete();
        }
        tempDir.delete();
    }
}
