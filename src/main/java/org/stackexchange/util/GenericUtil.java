package org.stackexchange.util;

import java.util.List;
import java.util.Random;

public final class GenericUtil {

    private GenericUtil() {
        throw new AssertionError();
    }

    // API

    @SuppressWarnings("unchecked")
    public static <T> T pickOneGeneric(final T... options) {
        return options[new Random().nextInt(options.length)];
    }

    public static <T> T pickOneGeneric(final List<T> options) {
        return options.get(new Random().nextInt(options.size()));
    }

}
