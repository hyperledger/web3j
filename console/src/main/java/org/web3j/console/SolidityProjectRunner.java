package org.web3j.console;
import org.web3j.codegen.Console;

import static org.web3j.utils.Collection.tail;

public class SolidityProjectRunner {

    private static final String USAGE = "import <path to solidity project> [--package <package-name>] <project-name>";

    public static void run(String[] args) {
        main(args);
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            Console.exitError(USAGE);
        } else {
            switch (args[0]) {
                case "import":
                    SolidityProjectCreator.main(tail(args));
                    break;
                default:
                    Console.exitError(USAGE);
            }
        }
    }
}

