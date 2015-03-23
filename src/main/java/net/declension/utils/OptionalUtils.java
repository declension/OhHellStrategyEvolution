package net.declension.utils;

import java.util.Optional;

public final class OptionalUtils {

    private OptionalUtils() {
        // empty
    }

    public static <T> String optionalToString(Optional<T> opt) {
        return opt.map(Object::toString).orElse("(none)");
    }
}
