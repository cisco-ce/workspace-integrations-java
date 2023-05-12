package com.cisco.workspaceintegrations.examples.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import static java.lang.System.lineSeparator;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.joining;

public final class ExampleUtils {

    private ExampleUtils() {
    }

    @SuppressFBWarnings("PATH_TRAVERSAL_IN")
    public static String readContentFromFile(String fileName) throws IOException {
        return Files.readAllLines(Paths.get(fileName), UTF_8)
                    .stream()
                    .collect(joining(lineSeparator()));
    }

    @SuppressFBWarnings("PATH_TRAVERSAL_IN")
    public static void writeToFile(String fileName, String content) throws IOException {
        Files.writeString(Paths.get(fileName),
                          content,
                          UTF_8,
                          StandardOpenOption.CREATE,
                          StandardOpenOption.TRUNCATE_EXISTING);
    }
}
