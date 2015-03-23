package net.declension.utils;

import java.util.Collection;

import static java.lang.String.format;
import static java.lang.Thread.currentThread;

public class Validation {
    private Validation() {
        // empty and private
    }

    public static void requireNonNullParam(Object parameter, String msg) {
        if (parameter == null) {
            throw createException(msg, Problem.NULL);
        }
    }

    public static void requireNonEmptyParam(Collection<?> parameter, String msg) {
        if (parameter == null) {
            throw createException(msg, Problem.NULL);
        } else if (parameter.isEmpty()) {
            throw createException(msg, Problem.EMPTY);
        }
    }

    private static IllegalArgumentException createException(String msg, Problem problem) {
        StackTraceElement stackTraceElement = currentThread().getStackTrace()[4];
        return new IllegalArgumentException(
                format("\"%s\" parameter for %s() cannot be %s.", msg, stackTraceElement.getMethodName(), problem));
    }

    private static enum Problem {
        NULL("null"), EMPTY("empty");

        private final String desc;

        Problem(String desc) {
            this.desc = desc;
        }

        @Override
        public String toString() {
            return desc;
        }
    }
}
