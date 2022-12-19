package org.stackexchange.util;

import java.util.List;
import java.util.Random;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

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

    /**
     * return - not null
     */
    public static List<String> breakApart(final String commaSeparated) {
        final Iterable<String> split = Splitter.on(',').split(commaSeparated);
        return Lists.newArrayList(split);
    }

}
