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
package org.web3j.console;

import java.io.Console;

/** System Console device wrapper. */
public class ConsoleDevice implements IODevice {

    private Console console = System.console();

    @Override
    public void printf(String format, Object... args) {
        console.printf(format, args);
    }

    @Override
    public String readLine(String fmt, Object... args) {
        return console.readLine(fmt, args);
    }

    @Override
    public char[] readPassword(String fmt, Object... args) {
        return console.readPassword(fmt, args);
    }
}
