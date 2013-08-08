package org.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Lists;

public final class LinkUtil {

    final static List<String> bannedDomains = Lists.newArrayList(// @formatter:off
        "http://wp-plugin-archive.de",
        "http://www.blogging-inside.de", 
        "http://www.perun.net"
    );// @formatter:on

    private LinkUtil() {
        throw new AssertionError();
    }

    // API

    /**
     * - note: simplistic implementation to be improved when needed
     */
    public static boolean belongsToBannedDomain(final String urlString) {
        for (final String bannedDomain : bannedDomains) {
            if (urlString.startsWith(bannedDomain)) {
                return true;
            }
        }

        return false;
    }

    /**
     * - <b>local</b> <br/>
     * - note: will NOT return null
     */
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

}
