package org.web3j.console.project;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Optional;
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
        this.writer = new PrintWriter(outputStream);
    }

    final String getProjectName() {
        print("Please enter the project name: ");
        return getUserInput();
    }

    final String getPackageName() {
        print("Please enter the package name for your project: ");
        return getUserInput();
    }

    final Optional<String> getProjectDestination() {
        print("Please enter the destination of your project (current by default): ");
        final String projectDest = getUserInput();
        return projectDest.isEmpty() ? Optional.empty() : Optional.of(projectDest);
    }

    String getUserInput() {
        return scanner.nextLine();
    }

    private void print(final String text) {
        System.out.println(text);
    }
}
