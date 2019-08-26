package org.web3j.console.project;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ProjectWriter {


    final void writeFile(String file, String fileName, String writeLocation) throws IOException {
        Files.write(Paths.get(writeLocation + File.separator + fileName), getBytes(file));
    }

    private byte[] getBytes(final String file) {
        return file.getBytes();
    }

    final void copyFile(InputStream file, String destinationPath) throws IOException {
        Files.copy(file, Paths.get(destinationPath), StandardCopyOption.REPLACE_EXISTING);
    }
}
