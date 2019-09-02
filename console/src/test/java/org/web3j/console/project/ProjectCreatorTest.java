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

import java.io.IOException;

import org.junit.Test;

import org.web3j.TempFileProvider;

public class ProjectCreatorTest extends TempFileProvider {
    static final String USAGE =
            "Missing required options [--package=<packageName>, --project name=<projectName>]\n"
                    + "Usage: new [-hV] -n=<projectName> [-o=<root>] -p=<packageName>\n"
                    + "  -h, --help               Show this help message and exit.\n"
                    + "  -V, --version            Print version information and exit.\n"
                    + "  -o, --outputDir=<root>   destination base directory.\n"
                    + "                             Default:\n"
                    + "                             /Users/alexander/Documents/DEV/web3j/web3j/console\n"
                    + "  -p, --package=<packageName>\n"
                    + "                           base package name.\n"
                    + "  -n, --project name=<projectName>\n"
                    + "                           project name.\n";

    public void setUpProjectCreator() throws IOException {
        ProjectCreator projectCreator = new ProjectCreator(tempDirPath, "test", "test");
    }

    @Test
    public void usageTest() throws IOException {
        ProjectCreator.main("new -p=test -n=test".split(" "));
    }

    @Test
    public void badArgsTest() {
        ProjectCreator.main("new -t=test -b=test".split(" "));
    }
}
