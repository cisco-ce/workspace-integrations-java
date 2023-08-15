package com.cisco.workspaceintegrations.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Set;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import com.cisco.workspaceintegrations.common.actions.Provisioning;
import com.cisco.workspaceintegrations.common.json.Json;

import static java.lang.System.lineSeparator;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableSet;
import static java.util.stream.Collectors.joining;

public final class Utils {

    private Utils() {
    }

    public static String toPiiLengthString(Object object) {
        if (object == null) {
            return "null";
        }
        return "*** " + object.toString().length() + " ***";
    }

    public static void checkRequiredArgument(String argumentName, String value, int maxLength) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException(argumentName + " is required");
        } else if (value.length() > maxLength) {
            throw new IllegalArgumentException(argumentName + " must be less than " + maxLength + " characters");
        }
    }

    public static void checkRequiredArgument(Object value, String errorMessage) {
        if (value == null) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public static void checkMinMax(String argumentName, double value, double min, double max) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(argumentName + " must be >= " + min + " or <= " + max);
        }
    }

    public static void checkNotZero(String argumentName, double value) {
        if (value == 0) {
            throw new IllegalArgumentException(argumentName + " is required");
        }
    }

    public static <T> Set<T> toUnmodifiableSet(Set<T> original) {
        return original == null ? emptySet() : unmodifiableSet(original);
    }

    public static <T> List<T> toUnmodifiableList(List<T> original) {
        return original == null ? emptyList() : unmodifiableList(original);
    }

    public static <T extends Exception, R> R sneakyThrow(Exception t) throws T {
        throw (T) t; // ( ͡° ͜ʖ ͡°)
    }

    public static Provisioning readProvisioningFromFile(String provisioningFile) throws IOException {
        return Json.fromJsonString(readContentFromFile(provisioningFile), Provisioning.class);
    }

    public static void writeProvisioningToFile(String provisioningFile, Provisioning provisioning) throws IOException {
        writeToFile(provisioningFile, Json.toJsonString(provisioning));
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
