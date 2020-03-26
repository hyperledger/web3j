/*
 * Copyright 2019 Web3 Labs Ltd.
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
package org.web3j.codegen;

import java.io.File;
import java.io.IOException;

import org.web3j.tx.Contract;

import static org.web3j.codegen.Console.exitError;

/** Abstract base class for the concrete function wrapper generators. */
abstract class FunctionWrapperGenerator {

    static final String JAVA_TYPES_ARG = "--javaTypes";
    static final String SOLIDITY_TYPES_ARG = "--solidityTypes";
    static final String PRIMITIVE_TYPES_ARG = "--primitiveTypes";

    final File destinationDirLocation;
    final String basePackageName;
    final boolean useJavaNativeTypes;
    final boolean useJavaPrimitiveTypes;

    final Class<? extends Contract> contractClass;

    FunctionWrapperGenerator(
            final File destinationDirLocation,
            final String basePackageName,
            final boolean useJavaNativeTypes) {

        this(Contract.class, destinationDirLocation, basePackageName, useJavaNativeTypes, false);
    }

    FunctionWrapperGenerator(
            final Class<? extends Contract> contractClass,
            final File destinationDirLocation,
            final String basePackageName,
            final boolean useJavaNativeTypes,
            final boolean useJavaPrimitiveTypes) {

        this.destinationDirLocation = destinationDirLocation;
        this.basePackageName = basePackageName;
        this.useJavaNativeTypes = useJavaNativeTypes;
        this.useJavaPrimitiveTypes = useJavaPrimitiveTypes;
        this.contractClass = contractClass;
    }

    public abstract void generate() throws IOException, ClassNotFoundException;

    static boolean useJavaNativeTypes(final String argVal, final String usageString) {
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

    static String parsePositionalArg(final String[] args, final int idx) {
        if (args != null && args.length > idx) {
            return args[idx];
        } else {
            return "";
        }
    }

    static String parseParameterArgument(final String[] args, final String... parameters) {
        for (final String parameter : parameters) {
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals(parameter) && i + 1 < args.length) {
                    final String parameterValue = args[i + 1];
                    if (!parameterValue.startsWith("-")) {
                        return parameterValue;
                    }
                }
            }
        }
        return "";
    }

    static String getFileNameNoExtension(final String fileName) {
        final String[] splitName = fileName.split("\\.(?=[^.]*$)");
        return splitName[0];
    }
}
