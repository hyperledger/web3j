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
package org.web3j.console.docker;

import java.io.*;

public class Docker {

    public final boolean isInstalled() throws IOException {
        String result = getExecutionResultAsString("docker -v");
        if (result.contains("Docker version")) {
            return true;
        }
        return false;
    }

    String getExecutionResultAsString(String commandToExecute) throws IOException {
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(runCommand(commandToExecute.split(" "))));
        StringBuilder out = new StringBuilder();
        String result;
        while ((result = reader.readLine()) != null) {
            out.append(result);
        }
        return out.toString();
    }

    InputStream runCommand(final String[] commandToRun) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(commandToRun);
        Process process = processBuilder.start();

        return process.getInputStream();
    }
}
