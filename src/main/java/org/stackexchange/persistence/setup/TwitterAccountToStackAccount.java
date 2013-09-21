package org.stackexchange.persistence.setup;

import java.util.List;

import org.stackexchange.api.constants.StackSite;
import org.stackexchange.util.GenericUtil;
import org.stackexchange.util.TwitterAccountEnum;

import com.google.common.collect.Lists;

public final class TwitterAccountToStackAccount {

    private TwitterAccountToStackAccount() {
        throw new AssertionError();
    }

    // API

    public static StackSite twitterAccountToStackSite(final TwitterAccountEnum twitterAccount) {
        final List<StackSite> allStackSites = twitterAccountToStackSites(twitterAccount);
        return GenericUtil.pickOneGeneric(allStackSites);
    }

    public static List<StackSite> twitterAccountToStackSites(final TwitterAccountEnum twitterAccount) {
        switch (twitterAccount) {
        case AskUbuntuBest:
            return Lists.newArrayList(StackSite.AskUbuntu);
        case AspnetDaily:
            return Lists.newArrayList(StackSite.StackOverflow);
        case AndroidFact:
            return Lists.newArrayList(StackSite.StackOverflow);

        case BestAlgorithms:
            return Lists.newArrayList(StackSite.StackOverflow);
        case BestAWS:
            return Lists.newArrayList(StackSite.StackOverflow);
        case BestBash:
            return Lists.newArrayList(StackSite.StackOverflow, StackSite.SuperUser); // StackSite.AskUbuntu - no more results
        case CryptoFact:
            return Lists.newArrayList(StackSite.StackOverflow);
        case BestClojure:
            return Lists.newArrayList(StackSite.StackOverflow);
        case BestEclipse:
            return Lists.newArrayList(StackSite.StackOverflow);
        case BestGit:
            return Lists.newArrayList(StackSite.StackOverflow);
        case BestJavaScript:
            return Lists.newArrayList(StackSite.StackOverflow);
        case BestJSP:
            return Lists.newArrayList(StackSite.StackOverflow);
        case BestJPA:
            return Lists.newArrayList(StackSite.StackOverflow);
        case BestJSON:
            return Lists.newArrayList(StackSite.StackOverflow);
        case BestMaven:
            return Lists.newArrayList(StackSite.StackOverflow);
        case BestMultithread:
            return Lists.newArrayList(StackSite.StackOverflow);

        case BestNoSQL:
            return Lists.newArrayList(StackSite.StackOverflow);

        case BestOfCocoa:
            return Lists.newArrayList(StackSite.StackOverflow);
        case BestOfCss:
            return Lists.newArrayList(StackSite.StackOverflow);
        case BestOfRuby:
            return Lists.newArrayList(StackSite.StackOverflow);
        case BestOfSecurity:
            return Lists.newArrayList(StackSite.StackOverflow);
        case BestOfCloud:
            return Lists.newArrayList(StackSite.StackOverflow);
        case BestOfLinux:
            return Lists.newArrayList(StackSite.StackOverflow);
        case BestOfJava:
            return Lists.newArrayList(StackSite.StackOverflow);
        case BestOfHTML5:
            return Lists.newArrayList(StackSite.StackOverflow);
        case BestPHP:
            return Lists.newArrayList(StackSite.StackOverflow);
        case BestRubyOnRails:
            return Lists.newArrayList(StackSite.StackOverflow);
        case BestScala:
            return Lists.newArrayList(StackSite.StackOverflow);
        case BestSQL:
            return Lists.newArrayList(StackSite.StackOverflow);
        case BestXML:
            return Lists.newArrayList(StackSite.StackOverflow);
        case BestWPF:
            return Lists.newArrayList(StackSite.StackOverflow);

        case DjangoDaily:
            return Lists.newArrayList(StackSite.StackOverflow);
        case DotNetFact:
            return Lists.newArrayList(StackSite.StackOverflow);

        case FacebookDigest:
            return Lists.newArrayList(StackSite.StackOverflow);

        case GoogleDigest:
            return Lists.newArrayList(StackSite.StackOverflow);

        case HadoopDaily:
            return Lists.newArrayList(StackSite.StackOverflow);
        case HibernateDaily:
            return Lists.newArrayList(StackSite.StackOverflow);
        case HTMLdaily:
            return Lists.newArrayList(StackSite.StackOverflow);

        case LandOfWordpress:
            return Lists.newArrayList(StackSite.StackOverflow);
        case LandOfSeo:
            return Lists.newArrayList(StackSite.StackOverflow);

        case iOSdigest:
            return Lists.newArrayList(StackSite.StackOverflow);
        case InTheAppleWorld:
            return Lists.newArrayList(StackSite.StackOverflow);

        case JavaTopSO:
            return Lists.newArrayList(StackSite.StackOverflow);
        case jQueryDaily:
            return Lists.newArrayList(StackSite.StackOverflow);

        case LispDaily:
            return Lists.newArrayList(StackSite.StackOverflow);

        case MathDaily:
            return Lists.newArrayList(StackSite.StackOverflow);
        case MysqlDaily:
            return Lists.newArrayList(StackSite.StackOverflow);

        case ParsingDaily:
            return Lists.newArrayList(StackSite.StackOverflow);
        case PerlDaily:
            return Lists.newArrayList(StackSite.StackOverflow);
        case PythonDaily:
            return Lists.newArrayList(StackSite.StackOverflow);

        case RESTDaily:
            return Lists.newArrayList(StackSite.StackOverflow);
        case RegexDaily:
            return Lists.newArrayList(StackSite.StackOverflow);

        case ServerFaultBest:
            return Lists.newArrayList(StackSite.ServerFault);
        case SpringAtSO:
            return Lists.newArrayList(StackSite.StackOverflow);

        case ObjectiveCDaily:
            return Lists.newArrayList(StackSite.StackOverflow);

        default:
            break;
        }

        return null;
    }

}
