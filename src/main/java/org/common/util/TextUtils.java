package org.common.util;

import org.apache.commons.lang3.StringEscapeUtils;
import org.h2.util.StringUtils;

public final class TextUtils {

    private TextUtils() {
        throw new AssertionError();
    }

    // API

    /**
     * - cleans the invalid characters from text
     */
    public static String cleanupInvalidCharacters(final String text) {
        String cleanedText = text;
        cleanedText = StringEscapeUtils.unescapeHtml4(cleanedText);
        cleanedText = StringEscapeUtils.escapeHtml4(cleanedText);
        cleanedText = StringUtils.replaceAll(cleanedText, "&ldquo;", "\"");
        cleanedText = StringUtils.replaceAll(cleanedText, "&rdquo;", "\"");
        cleanedText = StringUtils.replaceAll(cleanedText, "&quot;", "\"");

        cleanedText = StringUtils.replaceAll(cleanedText, "&lt;", "<");
        cleanedText = StringUtils.replaceAll(cleanedText, "&gt;", ">");

        cleanedText = StringUtils.replaceAll(cleanedText, "&amp;", "&");

        cleanedText = StringUtils.replaceAll(cleanedText, "&mdash;", "-");
        cleanedText = StringUtils.replaceAll(cleanedText, "&ndash;", "-");

        cleanedText = StringUtils.replaceAll(cleanedText, "&nbsp;", " ");
        cleanedText = StringUtils.replaceAll(cleanedText, "&hellip;", "...");

        cleanedText = StringUtils.replaceAll(cleanedText, "&#39;", "'");
        cleanedText = StringUtils.replaceAll(cleanedText, "&rsquo;", "'");
        cleanedText = StringUtils.replaceAll(cleanedText, "&lsquo;", "'");

        return cleanedText;
    }

    public static String trimTweet(final String text) {
        String result = text.trim();
        if (text.endsWith("...")) {
            result = result.substring(0, (result.length() - 2));
        }

        return result;
    }

}
