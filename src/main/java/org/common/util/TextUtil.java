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
        cleanedText = StringUtils.replaceAll(cleanedText, "&bdquo;", "\"");
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
        cleanedText = StringUtils.replaceAll(cleanedText, "&pound;", "£"); // new - ?

        cleanedText = StringUtils.replaceAll(cleanedText, "&prime;", "'");

        cleanedText = StringUtils.replaceAll(cleanedText, "&brvbar;", "|");

        cleanedText = StringUtils.replaceAll(cleanedText, "&Omega;", "O");

        cleanedText = StringUtils.replaceAll(cleanedText, "&times;", "x");

        // backtick - not on the keyboard
        cleanedText = StringUtils.replaceAll(cleanedText, "&acute;", "´"); // ? does it work on twitter (though the API)? - not sure

        cleanedText = StringUtils.replaceAll(cleanedText, "&euro;", "E");

        cleanedText = StringUtils.replaceAll(cleanedText, "&bull;", " ");

        cleanedText = StringUtils.replaceAll(cleanedText, "�", "-");

        cleanedText = StringUtils.replaceAll(cleanedText, "&rarr;", "-"); // →

        cleanedText = StringUtils.replaceAll(cleanedText, "&rsaquo;", ">"); // ›

        cleanedText = StringUtils.replaceAll(cleanedText, "&middot;", "-"); // ·

        cleanedText = StringUtils.replaceAll(cleanedText, "&deg;", "-"); // °

        // &equiv; => ≡
        cleanedText = StringUtils.replaceAll(cleanedText, "&equiv;", "="); // not exact replacement

        // others - unclear if these should be turned back or not...
        // &hearts; => ♥

        cleanedText = StringUtils.replaceAll(cleanedText, "&minus;", "-");

        // simply remove
        cleanedText = StringUtils.replaceAll(cleanedText, "&darr;", ""); // ↓
        cleanedText = StringUtils.replaceAll(cleanedText, "&copy;", ""); // ©
        cleanedText = StringUtils.replaceAll(cleanedText, "&reg;", ""); // ®
        cleanedText = StringUtils.replaceAll(cleanedText, "❤", "");
        cleanedText = StringUtils.replaceAll(cleanedText, "ߒ", "");
        cleanedText = StringUtils.replaceAll(cleanedText, "ߘ", "");
        cleanedText = StringUtils.replaceAll(cleanedText, "✔", "");
        cleanedText = StringUtils.replaceAll(cleanedText, "&forall;", ""); // ∀

        // potentially (to consider) - back the way they were
        cleanedText = StringUtils.replaceAll(cleanedText, "&ouml;", "o"); // ö
        cleanedText = StringUtils.replaceAll(cleanedText, "&iacute;", "i"); // í -> i
        cleanedText = StringUtils.replaceAll(cleanedText, "&aacute;", "a"); // a with an accent
        cleanedText = StringUtils.replaceAll(cleanedText, "&eacute;", "e");

        // back the way they were
        cleanedText = StringUtils.replaceAll(cleanedText, "&ntilde;", "ñ");
        cleanedText = StringUtils.replaceAll(cleanedText, "&egrave;", "è");
        cleanedText = StringUtils.replaceAll(cleanedText, "&uuml;", "ü");

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
