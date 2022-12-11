package org.tweet.twitter.util;

import java.util.List;

import com.google.common.collect.Lists;

public final class RejectExpressionUtil {

    private static final String WIN_ROOT = "win(ner|ning)?";
    private static final String DEAL_ROOT = "deal(s)?";

    private RejectExpressionUtil() {
        throw new AssertionError();
    }

    // API - win

    public static List<String> rejectStrictWin(final String word) {
        final List<String> rejectExpressions = Lists.newArrayList();

        rejectExpressions.add(rejectStrictWinEnd(word));
        rejectExpressions.add(rejectStrictWinStart(word));

        return rejectExpressions;
    }

    public static List<String> rejectWin(final String word) {
        final List<String> rejectExpressions = Lists.newArrayList();

        rejectExpressions.add(rejectWinEnd(word));
        rejectExpressions.add(rejectWinStart(word));

        return rejectExpressions;
    }

    public static String rejectStrictWinEnd(final String word) {
        return rejectStrictEnd(word, WIN_ROOT);
    }

    public static String rejectStrictWinStart(final String word) {
        return rejectStrictStart(word, WIN_ROOT);
    }

    public static String rejectWinEnd(final String word) {
        return rejectEnd(word, WIN_ROOT);
    }

    public static String rejectWinStart(final String word) {
        return rejectStart(word, WIN_ROOT);
    }

    // API - deal

    public static List<String> rejectStrictDeal(final String word) {
        final List<String> rejectExpressions = Lists.newArrayList();

        rejectExpressions.add(rejectStrictDealEnd(word));
        rejectExpressions.add(rejectStrictDealStart(word));

        return rejectExpressions;
    }

    public static List<String> rejectDeal(final String word) {
        final List<String> rejectExpressions = Lists.newArrayList();

        rejectExpressions.add(rejectDealEnd(word));
        rejectExpressions.add(rejectDealStart(word));

        return rejectExpressions;
    }

    public static String rejectStrictDealEnd(final String word) {
        return rejectStrictEnd(word, DEAL_ROOT);
    }

    public static String rejectStrictDealStart(final String word) {
        return rejectStrictStart(word, DEAL_ROOT);
    }

    public static String rejectDealEnd(final String word) {
        return rejectEnd(word, DEAL_ROOT);
    }

    public static String rejectDealStart(final String word) {
        return rejectStart(word, DEAL_ROOT);
    }

    // generic

    static String rejectStart(final String word, final String root) {
        return ".*" + word + ".*\\b" + root + "\\b.*";
    }

    public static String rejectEnd(final String word, final String root) {
        return ".*\\b" + root + "\\b.*" + word + ".*";
    }

    public static String rejectStrictStart(final String word, final String root) {
        return ".*\\b" + word + "\\b.*\\b" + root + "\\b.*";
    }

    public static String rejectStrictEnd(final String word, final String root) {
        return ".*\\b" + root + "\\b.*\\b" + word + "\\b.*";
    }

}
