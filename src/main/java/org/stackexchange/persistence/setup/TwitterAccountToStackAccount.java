package org.stackexchange.persistence.setup;

import java.util.List;

import org.stackexchange.api.constants.StackSite;
import org.stackexchange.util.SimpleTwitterAccount;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public final class TwitterAccountToStackAccount {

    private TwitterAccountToStackAccount() {
        throw new AssertionError();
    }

    // API

    public static StackSite twitterAccountToStackSite(final SimpleTwitterAccount twitterAccount) {
        final List<StackSite> allStackSites = twitterAccountToStackSites(twitterAccount);
        Preconditions.checkState(allStackSites.size() == 1);

        return allStackSites.get(0);
    }

    public static List<StackSite> twitterAccountToStackSites(final SimpleTwitterAccount twitterAccount) {
        switch (twitterAccount) {
        case AskUbuntuBest:
            return Lists.newArrayList(StackSite.AskUbuntu);

        case BestAlgorithms:
            return Lists.newArrayList(StackSite.StackOverflow);
        case BestAWS:
            return Lists.newArrayList(StackSite.StackOverflow);
        case BestBash:
            return Lists.newArrayList(StackSite.StackOverflow, StackSite.AskUbuntu, StackSite.SuperUser);
        case BestClojure:
            return Lists.newArrayList(StackSite.StackOverflow);
        case BestEclipse:
            return Lists.newArrayList(StackSite.StackOverflow);
        case BestGit:
            return Lists.newArrayList(StackSite.StackOverflow);
        case BestJavaScript:
            return Lists.newArrayList(StackSite.StackOverflow);
        case BestJPA:
            return Lists.newArrayList(StackSite.StackOverflow);
        case BestJSON:
            return Lists.newArrayList(StackSite.StackOverflow);
        case BestMaven:
            return Lists.newArrayList(StackSite.StackOverflow);
        case BestNoSQL:
            return Lists.newArrayList(StackSite.StackOverflow);
        case BestOfRuby:
            return Lists.newArrayList(StackSite.StackOverflow);
        case BestPHP:
            return Lists.newArrayList(StackSite.StackOverflow);
        case BestRubyOnRails:
            return Lists.newArrayList(StackSite.StackOverflow);
        case BestScala:
            return Lists.newArrayList(StackSite.StackOverflow);
        case BestSQL:
            return Lists.newArrayList(StackSite.StackOverflow);
        case BestOfJava:
            return Lists.newArrayList(StackSite.StackOverflow);

        case iOSdigest:
            return Lists.newArrayList(StackSite.StackOverflow);

        case JavaTopSO:
            return Lists.newArrayList(StackSite.StackOverflow);
        case jQueryDaily:
            return Lists.newArrayList(StackSite.StackOverflow);

        case LispDaily:
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
