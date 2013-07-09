package org.common.text;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        return cleanedText;
    }

    public static List<String> extractUrls(final String input) {
        final List<String> result = new ArrayList<String>();

        final Pattern pattern = Pattern.compile("\\b(((ht|f)tp(s?)\\:\\/\\/|~\\/|\\/)|www.)" + "(\\w+:\\w+@)?(([-\\w]+\\.)+(com|org|net|gov" + "|mil|biz|info|mobi|name|aero|jobs|museum" + "|travel|[a-z]{2}))(:[\\d]{1,5})?"
                + "(((\\/([-\\w~!$+|.,=]|%[a-f\\d]{2})+)+|\\/)+|\\?|#)?" + "((\\?([-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?" + "([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)" + "(&(?:[-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?" + "([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)*)*"
                + "(#([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)?\\b");

        final Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            result.add(matcher.group());
        }

        return result;
    }

    /**
     * - note: may return null
     */
    public static String determineMainUrl(final List<String> extractedUrls) {
        for (final String urlCandidate : extractedUrls) {
            if (urlCandidate.contains("plus.google.com") || urlCandidate.endsWith(".git") || urlCandidate.contains("youtube.com")) {
                continue;
            }

            return urlCandidate;
        }

        return null;
    }

}
