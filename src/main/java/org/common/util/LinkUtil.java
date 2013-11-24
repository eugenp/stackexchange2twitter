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

    public static class Common {

        final static List<String> bannedDomainsStartsWith = Lists.newArrayList(// @formatter:off
            "http://wp-plugin-archive.de",
            "http://www.blogging-inside.de", 
            "http://www.perun.net", 
            "http://www.heyyou-app.com", 
            "http://www.freelancer.com", "https://www.freelancer.com", "http://www.peopleperhour.com", 
            "http://www.almasryalyoum.com/", // non English
            "https://www.facebook.com" // verified - no false positive after about 2 weeks - accepting
        );// @formatter:on
        final static List<String> bannedDomainsEndsWith = Lists.newArrayList(// @formatter:off
            ".git"
        );// @formatter:on
        final static List<String> bannedDomainsByContains = Lists.newArrayList(// @formatter:off
             "youtube.com" 
            ,"webdevers.com" // jobs
            ,"plus.google.com" // making this decision - it's OK
        );// @formatter:on
        public final static List<String> bannedDomainsByContainsMaybe = Lists.newArrayList(// @formatter:off
            // 
        );// @formatter:on
        final static List<String> bannedDomainsByRegex = Lists.newArrayList(// @formatter:off
            "http(s)?://(www.)?.*\\.de(\\z|/.*)" 
        );// @formatter:on
        final static List<String> bannedDomainsByRegexMaybe = Lists.newArrayList(// @formatter:off
            "http(s)?://(www.)?.*job.*\\.com(\\z|/.*)", 
            "http(s)?://(www.)?.*\\.it(\\z|/.*)"
        );// @formatter:on
    }

    public static class Technical {
        public final static List<String> seDomains = Lists.newArrayList("http://stackoverflow.com/", "http://askubuntu.com/", "http://superuser.com/");

        final static List<String> bannedDomainsStartsWith = Lists.newArrayList(// @formatter:off
            //
        );// @formatter:on
        final static List<String> bannedDomainsEndsWith = Lists.newArrayList(// @formatter:off
            // 
        );// @formatter:on
        final static List<String> bannedDomainsByContains = Lists.newArrayList(// @formatter:off
             "instagram.com" 
        );// @formatter:on
        public final static List<String> bannedDomainsByContainsMaybe = Lists.newArrayList(// @formatter:off
            "pic.twitter.com"
        );// @formatter:on
        final static List<String> bannedDomainsByRegex = Lists.newArrayList(// @formatter:off
            //  
        );// @formatter:on
        final static List<String> bannedDomainsByRegexMaybe = Lists.newArrayList(// @formatter:off
            "https://twitter.com/.*/photo/\\d+" 
        );// @formatter:on
    }

    private LinkUtil() {
        throw new AssertionError();
    }

    // API

    /**
     * - local <br/>
     * - note: simplistic implementation to be improved when needed
     */
    public static boolean belongsToBannedDomainTechnical(final String urlString) {
        if (belongsToBannedDomainCommon(urlString)) {
            return true;
        }
        if (belongsToBannedDomainTechnicalOnly(urlString)) {
            return true;
        }

        return false;
    }

    public static boolean belongsToBannedDomainCommon(final String urlString) {
        for (final String bannedDomain : Common.bannedDomainsStartsWith) {
            if (urlString.startsWith(bannedDomain)) {
                return true;
            }
        }
        for (final String bannedDomain : Common.bannedDomainsEndsWith) {
            if (urlString.endsWith(bannedDomain)) {
                return true;
            }
        }

        for (final String bannedDomain : Common.bannedDomainsByRegex) {
            if (urlString.matches(bannedDomain)) {
                return true;
            }
        }
        for (final String bannedDomainByContainsMaybe : Common.bannedDomainsByContains) {
            if (urlString.contains(bannedDomainByContainsMaybe)) {
                logger.debug("For url: {} banned domain: {}", urlString, bannedDomainByContainsMaybe);
                return true;
            }
        }
        for (final String bannedDomainByContainsMaybe : Common.bannedDomainsByContainsMaybe) {
            if (urlString.contains(bannedDomainByContainsMaybe)) {
                logger.error("1 - For url: {} banned domain: {}", urlString, bannedDomainByContainsMaybe);
                return true;
            }
        }
        for (final String bannedDomainByRegexMaybe : Common.bannedDomainsByRegexMaybe) {
            if (urlString.matches(bannedDomainByRegexMaybe)) {
                // was error - interesting, but confirmed - moving down now
                logger.debug("2 - For url: {} banned domain by regex: {}", urlString, bannedDomainByRegexMaybe);
                return true;
            }
        }

        return false;
    }

    private static boolean belongsToBannedDomainTechnicalOnly(final String urlString) {
        for (final String bannedDomain : Technical.bannedDomainsStartsWith) {
            if (urlString.startsWith(bannedDomain)) {
                return true;
            }
        }
        for (final String bannedDomain : Technical.bannedDomainsEndsWith) {
            if (urlString.endsWith(bannedDomain)) {
                return true;
            }
        }

        for (final String bannedDomain : Technical.bannedDomainsByRegex) {
            if (urlString.matches(bannedDomain)) {
                return true;
            }
        }
        for (final String bannedDomainByContainsMaybe : Technical.bannedDomainsByContains) {
            if (urlString.contains(bannedDomainByContainsMaybe)) {
                logger.debug("For url: {} banned domain: {}", urlString, bannedDomainByContainsMaybe);
                return true;
            }
        }
        for (final String bannedDomainByContainsMaybe : Technical.bannedDomainsByContainsMaybe) {
            if (urlString.contains(bannedDomainByContainsMaybe)) {
                logger.error("1 - For url: {} banned domain: {}", urlString, bannedDomainByContainsMaybe);
                return true;
            }
        }
        for (final String bannedDomainByRegexMaybe : Technical.bannedDomainsByRegexMaybe) {
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
        // final String urlPattern = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
           final String urlPattern = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$~_?\\+-=\\\\\\.&]*)";
        // @formatter:on

        final Pattern pattern = Pattern.compile(urlPattern);
        final Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            result.add(matcher.group());
        }

        return result;
    }

}
