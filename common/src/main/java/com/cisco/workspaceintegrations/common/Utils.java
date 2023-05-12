package com.cisco.workspaceintegrations.common;

import java.util.List;
import java.util.Set;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableSet;

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
}
