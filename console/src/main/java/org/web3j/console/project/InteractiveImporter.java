package org.web3j.console.project;

import java.io.InputStream;
import java.io.OutputStream;

class InteractiveImporter extends InteractiveOptions {

    InteractiveImporter() {
    }

    InteractiveImporter(final InputStream inputStream, final OutputStream outputStream) {
        super(inputStream, outputStream);
    }

    String getSolidityProjectPath() {
        System.out.println("Please enter the path to your solidity file/folder");
        return getUserInput();
    }

}


