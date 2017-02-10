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
        byte[] bytes = new byte[(int) file.length()];
        FileInputStream fileInputStream = new FileInputStream(file);
        fileInputStream.read(bytes);
        fileInputStream.close();
        return bytes;
    }

    public static String readString(File file) throws IOException {
        return new String(readBytes(file));
    }
}
