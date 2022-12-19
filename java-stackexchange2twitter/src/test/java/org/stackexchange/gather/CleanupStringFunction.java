package org.stackexchange.gather;

import org.common.util.TextUtil;

import com.google.common.base.Function;

public class CleanupStringFunction implements Function<String, String> {

    @Override
    public final String apply(final String input) {
        String cleanText = TextUtil.cleanupInvalidCharacters(input);
        cleanText = cleanText.replaceAll("  ", " ");
        cleanText = cleanText.trim();
        if (cleanText.matches(".*&\\S*;.*")) {
            return "";
        }
        return cleanText;
    }

}
