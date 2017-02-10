package org.web3j.console;

import java.io.Console;

/**
 * System Console device wrapper.
 */
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
        return console.readPassword();
    }
}
