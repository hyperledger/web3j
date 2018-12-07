package org.web3j.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * File utility functions.
 */
public class Files {

    private Files() { }

    public static byte[] readBytes(File file) throws IOException {
        final FileInputStream is = new FileInputStream(file);
        try {
            final byte[] bytes = new byte[(int) file.length()];
            is.read(bytes);
            return bytes;
        } finally {
            is.close();
        }
    }

    public static String readString(File file) throws IOException {
        return new String(readBytes(file));
    }
}
