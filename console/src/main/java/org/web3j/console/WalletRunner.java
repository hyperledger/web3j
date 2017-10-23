package org.web3j.console;

import org.web3j.utils.Console;

import static org.web3j.utils.Collection.tail;

/**
 * Class for managing our wallet command line utilities.
 */
public class WalletRunner {
    private static final String USAGE = "wallet create|update|send|fromkey";

    public static void run(String[] args) {
        main(args);
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            Console.exitError(USAGE);
        } else {
            String arg = args[0];
            if (arg.equals("create")) {
                WalletCreator.main(new String[]{});

            } else if (arg.equals("update")) {
                WalletUpdater.main(tail(args));

            } else if (arg.equals("send")) {
                WalletSendFunds.main(tail(args));

            } else if (arg.equals("fromkey")) {
                KeyImporter.main(tail(args));

            } else {
                Console.exitError(USAGE);
            }
        }
    }
}
