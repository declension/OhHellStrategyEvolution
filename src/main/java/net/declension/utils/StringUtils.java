package net.declension.utils;

import static java.lang.String.format;

public final class StringUtils {
    private StringUtils() {

    }

    /**
     * Tidy long, ugly floating Double numbers.
     * @param value a Number to pretty-print
     * @return a formatted string with that number.
     */
    public static String tidyNumber(Number value) {
        return format(value instanceof Double || value instanceof Float? "%.3f" : "%s", value);
    }
}
