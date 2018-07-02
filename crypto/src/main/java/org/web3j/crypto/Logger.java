package org.web3j.crypto;

public interface Logger {
    Logger SYSTEM = new Logger() {
        @Override
        public void info(String msg, Object... args) {
            System.out.print(String.format(msg, args));
        }

        @Override
        public void error(String msg, Object... args) {
            System.err.print(String.format(msg, args));
        }
    };

    void info(String msg, Object... args);

    void error(String msg, Object... args);
}
