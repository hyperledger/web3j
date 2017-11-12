package org.web3j.codegen;

import java.io.File;

import static org.web3j.codegen.Console.exitError;

/**
 * Abstract base class for the concrete function wrapper generators.
 */
abstract class FunctionWrapperGenerator {

    static final String JAVA_TYPES_ARG = "--javaTypes";
    static final String SOLIDITY_TYPES_ARG = "--solidityTypes";

    final File destinationDirLocation;
    final String basePackageName;
    final boolean useJavaNativeTypes;

    FunctionWrapperGenerator(
            String destinationDirLocation,
            String basePackageName,
            boolean useJavaNativeTypes) {

        this.destinationDirLocation = new File(destinationDirLocation);
        this.basePackageName = basePackageName;
        this.useJavaNativeTypes = useJavaNativeTypes;
    }

    static boolean useJavaNativeTypes(String argVal, String usageString) {
        boolean useJavaNativeTypes = true;
        if (SOLIDITY_TYPES_ARG.equals(argVal)) {
            useJavaNativeTypes = false;
        } else if (JAVA_TYPES_ARG.equals(argVal)) {
            useJavaNativeTypes = true;
        } else {
            exitError(usageString);
        }
        return useJavaNativeTypes;
    }

    static String parsePositionalArg(String[] args, int idx) {
        if (args != null && args.length > idx) {
            return args[idx];
        } else {
            return "";
        }
    }

    static String parseParameterArgument(String[] args, String... parameters) {
        for (String parameter : parameters) {
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals(parameter)
                        && i + 1 < args.length) {
                    String parameterValue = args[i + 1];
                    if (!parameterValue.startsWith("-")) {
                        return parameterValue;
                    }
                }
            }
        }
        return "";
    }

    static String getFileNameNoExtension(String fileName) {
        String[] splitName = fileName.split("\\.(?=[^.]*$)");
        return splitName[0];
    }

}
