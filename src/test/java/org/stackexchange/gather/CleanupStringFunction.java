package org.stackexchange.gather;

import org.common.util.TextUtil;

import com.google.common.base.Function;

public class CleanupStringFunction implements Function<String, String> {

    @Override
    public final String apply(final String input) {
        return TextUtil.cleanupInvalidCharacters(input);
    }

}
