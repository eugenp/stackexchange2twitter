package org.common.util;

import org.apache.commons.lang3.StringEscapeUtils;
import org.h2.util.StringUtils;

public final class TextUtils {

    private TextUtils() {
        throw new AssertionError();
    }

    // API

    public static String preProcessTweetText(final String tweetText) {
        String cleanedText = tweetText;
        cleanedText = StringEscapeUtils.unescapeHtml4(cleanedText);
        cleanedText = StringEscapeUtils.escapeHtml4(cleanedText);
        cleanedText = StringUtils.replaceAll(cleanedText, "&ldquo;", "\"");
        cleanedText = StringUtils.replaceAll(cleanedText, "&rdquo;", "\"");
        cleanedText = StringUtils.replaceAll(cleanedText, "&lt;", "<");
        cleanedText = StringUtils.replaceAll(cleanedText, "&gt;", ">");
        cleanedText = StringUtils.replaceAll(cleanedText, "&amp;", "&");
        cleanedText = StringUtils.replaceAll(cleanedText, "&mdash;", "-");
        cleanedText = StringUtils.replaceAll(cleanedText, "&ndash;", "-");
        cleanedText = StringUtils.replaceAll(cleanedText, "&quot;", "\"");
        cleanedText = StringUtils.replaceAll(cleanedText, "&nbsp;", " ");

        // may not be necessary
        cleanedText = StringUtils.replaceAll(cleanedText, "&#39;", "'");
        cleanedText = StringUtils.replaceAll(cleanedText, "&rsquo;", "'");

        return cleanedText;
    }

}
