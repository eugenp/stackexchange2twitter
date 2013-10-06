package org.common.util;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public final class LinkUtil {
    private static final Logger logger = LoggerFactory.getLogger(LinkUtil.class);

    public final static List<String> seDomains = Lists.newArrayList("http://stackoverflow.com/", "http://askubuntu.com/", "http://superuser.com/");

    final static List<String> bannedDomainsStartsWith = Lists.newArrayList(// @formatter:off
        "http://wp-plugin-archive.de",
        "http://www.blogging-inside.de", 
        "http://www.perun.net", 
        "http://www.heyyou-app.com", 
        "http://www.freelancer.com", "https://www.freelancer.com", "http://www.peopleperhour.com", 
        "http://www.almasryalyoum.com/", // non English
        "https://www.facebook.com" // verified - no false positive after about 2 weeks - accepting
    );// @formatter:on

    public final static List<String> bannedDomainsEndsWith = Lists.newArrayList(// @formatter:off
        ".git"
    );// @formatter:on

    public final static List<String> bannedDomainsByContains = Lists.newArrayList(// @formatter:off
        "instagram.com", 
        "youtube.com", 
        "webdevers.com" // jobs
    );// @formatter:on
    public final static List<String> bannedDomainsByContainsMaybe = Lists.newArrayList(// @formatter:off
        "pic.twitter.com",
        "plus.google.com" // valid ban: | semi-valid ban: +1 +1 | invalid ban: +1
    );// @formatter:on

    final static List<String> bannedDomainsByRegex = Lists.newArrayList(// @formatter:off
        "http(s)?://(www.)?.*\\.de(\\z|/.*)" 
    );// @formatter:on
    final static List<String> bannedDomainsByRegexMaybe = Lists.newArrayList(// @formatter:off
        "https://twitter.com/.*/photo/\\d+", 
        "http(s)?://(www.)?.*job.*\\.com(\\z|/.*)", 
        "http(s)?://(www.)?.*\\.it(\\z|/.*)"
    );// @formatter:on

    private LinkUtil() {
        throw new AssertionError();
    }

    // API

    /**
     * - local <br/>
     * - note: simplistic implementation to be improved when needed
     */
    public static boolean belongsToBannedDomain(final String urlString) {
        for (final String bannedDomain : bannedDomainsStartsWith) {
            if (urlString.startsWith(bannedDomain)) {
                return true;
            }
        }
        for (final String bannedDomain : bannedDomainsEndsWith) {
            if (urlString.endsWith(bannedDomain)) {
                return true;
            }
        }

        for (final String bannedDomain : bannedDomainsByRegex) {
            if (urlString.matches(bannedDomain)) {
                return true;
            }
        }
        for (final String bannedDomainByContainsMaybe : bannedDomainsByContains) {
            if (urlString.contains(bannedDomainByContainsMaybe)) {
                logger.debug("For url: {} banned domain: {}", urlString, bannedDomainByContainsMaybe);
                return true;
            }
        }
        for (final String bannedDomainByContainsMaybe : bannedDomainsByContainsMaybe) {
            if (urlString.contains(bannedDomainByContainsMaybe)) {
                // still error - reasons to move down: +1,
                logger.error("1 - For url: {} banned domain: {}", urlString, bannedDomainByContainsMaybe);
                return true;
            }
        }
        for (final String bannedDomainByRegexMaybe : bannedDomainsByRegexMaybe) {
            if (urlString.matches(bannedDomainByRegexMaybe)) {
                // was error - interesting, but confirmed - moving down now
                logger.debug("2 - For url: {} banned domain by regex: {}", urlString, bannedDomainByRegexMaybe);
                return true;
            }
        }

        return false;
    }

    /**
     * - <b>local</b> <br/>
     * - note: will NOT return null
     */
    public static Set<String> extractUrlsOld(final String input) {
        final Set<String> result = Sets.newHashSet();

        // @formatter:off
        final Pattern pattern = Pattern.compile("\\b(((ht|f)tp(s?)\\:\\/\\/|~\\/|\\/)|www.)" + "(\\w+:\\w+@)?"
                + "((([-\\w]+\\.)+(com|org|net|gov" + "|mil|biz|info|mobi|name|aero|jobs|museum" + "|travel|[a-z]{2}))|localhost)" // host
                + "(:[\\d]{1,5})?" // port
                + "(((\\/([-\\w~!$+|.,=]|%[a-f\\d]{2})+)+|\\/)+|\\?|#)?" + "((\\?([-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?" + "([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)" + "(&(?:[-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?" + "([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)*)*"
                + "(#([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)?\\b");
        // @formatter:on

        final Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            result.add(matcher.group());
        }

        return result;
    }

    /**
     * - <b>local</b> <br/>
     * - note: will NOT return null
     */
    public static Set<String> extractUrls(final String input) {
        final Set<String> result = Sets.newHashSet();

        // @formatter:off
        final String urlPattern = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        // @formatter:on

        final Pattern pattern = Pattern.compile(urlPattern);
        final Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            result.add(matcher.group());
        }

        return result;
    }

}
