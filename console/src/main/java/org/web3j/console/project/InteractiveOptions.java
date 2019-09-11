package org.web3j.console.project;


import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Scanner;

class InteractiveOptions {

    final static String COMMAND_INTERACTIVE = "interactive";

    private final Scanner scanner;
    private final Writer writer;

    InteractiveOptions() {
        this(System.in, System.out);
    }

    InteractiveOptions(final InputStream inputStream, final OutputStream outputStream) {
        this.scanner = new Scanner(inputStream);
        this.writer = new BufferedWriter(new OutputStreamWriter(outputStream));
    }

    final String getProjectName() throws IOException {
        writer.append("Please enter the project name: ");
        return getUserInput();
    }

    final String getPackageName() throws IOException {
        writer.append("Please enter the package name for your project: ");
        return getUserInput();
    }

    final String getProjectDestination() throws IOException {
        writer.append("Please enter the destination of your project (current by default): ");
        return getUserInput();
    }

    String getUserInput() {
        return scanner.next();
    }
}
