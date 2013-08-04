package org.common.util;

import org.apache.commons.lang3.StringEscapeUtils;
import org.h2.util.StringUtils;

public final class TextUtil {

    private TextUtil() {
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
        cleanedText = StringUtils.replaceAll(cleanedText, "\u2013", "-");

        cleanedText = StringUtils.replaceAll(cleanedText, "&nbsp;", " ");
        cleanedText = StringUtils.replaceAll(cleanedText, "&hellip;", "...");
        cleanedText = StringUtils.replaceAll(cleanedText, "\u2026", "...");

        cleanedText = StringUtils.replaceAll(cleanedText, "&#39;", "'");
        cleanedText = StringUtils.replaceAll(cleanedText, "&rsquo;", "'");
        cleanedText = StringUtils.replaceAll(cleanedText, "&lsquo;", "'");
        cleanedText = StringUtils.replaceAll(cleanedText, "&acirc;&euro;&trade;", "'");
        cleanedText = StringUtils.replaceAll(cleanedText, "\u2019", "'");

        cleanedText = StringUtils.replaceAll(cleanedText, "&laquo;", "<<");
        cleanedText = StringUtils.replaceAll(cleanedText, "&raquo;", ">>");

        cleanedText = StringUtils.replaceAll(cleanedText, "Ã£", "a");
        cleanedText = StringUtils.replaceAll(cleanedText, "ã", "a");
        cleanedText = StringUtils.replaceAll(cleanedText, "&Atilde;&pound;", "a");

        cleanedText = StringUtils.replaceAll(cleanedText, "✔", "");

        cleanedText = StringUtils.replaceAll(cleanedText, "&prime;", "'");

        cleanedText = StringUtils.replaceAll(cleanedText, "&brvbar;", "|");

        cleanedText = StringUtils.replaceAll(cleanedText, "&Omega;", "O");

        cleanedText = StringUtils.replaceAll(cleanedText, "&times;", "x");

        // backtick - not on the keyboard
        cleanedText = StringUtils.replaceAll(cleanedText, "&acute;", "´"); // ? does it work on twitter (though the API)? - not sure

        cleanedText = StringUtils.replaceAll(cleanedText, "&aacute;", "a"); // a with an accent

        cleanedText = StringUtils.replaceAll(cleanedText, "&euro;", "E");

        cleanedText = StringUtils.replaceAll(cleanedText, "&bull;", " ");

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
