package org.web3j.console;

import java.util.Arrays;

import org.web3j.codegen.SolidityFunctionWrapperGenerator;

/**
 * Main entry point for running command line utilities.
 */
public class Runner {

    private static String USAGE = "Usage: web3j createwallet|updatewallet|generate";

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            exitError(USAGE);
        } else {
            switch (args[0]) {
                case "createwallet":
                    WalletCreator.main(remainingArgs(args));
                    break;
                case "updatewallet":
                    WalletUpdater.main(remainingArgs(args));
                    break;
                case "generate":
                    SolidityFunctionWrapperGenerator.main(args);
                    break;
                default:
                    exitError(USAGE);
            }
        }
    }

    private static String[] remainingArgs(String[] args) {
        return Arrays.copyOfRange(args, 1, args.length);
    }

    private static void exitError(String message) {
        System.err.println(message);
        System.exit(1);
    }
}
