package org.tweet.twitter.util;

import java.util.List;

import com.google.common.collect.Lists;

public final class RejectExpressionUtil {

    private RejectExpressionUtil() {
        throw new AssertionError();
    }

    public static List<String> rejectStrictWin(final String word) {
        final List<String> rejectExpressions = Lists.newArrayList();
    
        rejectExpressions.add(RejectExpressionUtil.rejectStrinctWinStart(word));
        rejectExpressions.add(RejectExpressionUtil.rejectStrinctWinEnd(word));
    
        return rejectExpressions;
    }

    public static List<String> rejectWin(final String word) {
        final List<String> rejectExpressions = Lists.newArrayList();
    
        rejectExpressions.add(".*\\bwin(ner|ning)?\\b.*" + word + ".*");
        rejectExpressions.add(".*" + word + ".*\\bwin(ner|ning)?\\b.*");
    
        return rejectExpressions;
    }

    public static String rejectStrinctWinStart(final String word) {
        return ".*\\bwin(ner|ning)?\\b.*\\b" + word + "\\b.*";
    }

    public static String rejectStrinctWinEnd(final String word) {
        return ".*\\b" + word + "\\b.*\\bwin(ner|ning)?\\b.*";
    }

    public static String rejectWinStart(final String word) {
        return ".*\\bwin(ner|ning)?\\b.*" + word + ".*";
    }

    public static String rejectWinEnd(final String word) {
        return ".*" + word + ".*\\bwin(ner|ning)?\\b.*";
    }

}
