package org.web3j.console;

import java.io.Console;
import java.io.File;
import java.util.Arrays;

import org.web3j.crypto.WalletUtils;

/**
 * Common functions used by the wallet console tools.
 */
abstract class WalletManager {

    final Console console;

    WalletManager() {
        console = System.console();

        if (console == null) {
            exitError("Unable to access console - please ensure you are running " +
                    "from the command line");
        }
    }

    static void exitError(Throwable throwable) {
        exitError(throwable.getMessage());
    }

    static void exitError(String message) {
        System.err.println("The command failed with the following error: " + message);
        System.exit(1);
    }

    String getPassword(String initialPrompt) {
        while (true) {
            char[] input1 = console.readPassword(initialPrompt);
            char[] input2 = console.readPassword("Please re-enter the password: ");

            if (Arrays.equals(input1, input2)) {
                return new String(input1);
            } else {
                console.printf("Sorry, passwords did not match\n");
            }
        }
    }

    String getDestinationDir() {
        String defaultDir = WalletUtils.getTestnetKeyDirectory();
        String destinationDir = console.readLine(
                "Please enter a destination directory location [" + defaultDir + "]: ");
        if (destinationDir.equals("")) {
            return defaultDir;
        } else if (destinationDir.startsWith("~")) {
            return System.getProperty("user.home") + destinationDir.substring(1);
        } else {
            return destinationDir;
        }
    }

    File createDir(String destinationDir) {
        File destination = new File(destinationDir);

        if (!destination.exists()) {
            console.printf("Creating directory: " + destinationDir + " ...");
            if (!destination.mkdirs()) {
                exitError("Unable to create destination directory [" +
                        destinationDir + "], exiting...");
            } else {
                console.printf("complete\n");
            }
        }

        return destination;
    }
}
