package org.web3j.console;

/**
 * IO device abstraction.
 */
public interface IODevice {
    void printf(String format, Object ... args);
    String readLine(String fmt, Object ... args);
    char[] readPassword(String fmt, Object ... args);
}
