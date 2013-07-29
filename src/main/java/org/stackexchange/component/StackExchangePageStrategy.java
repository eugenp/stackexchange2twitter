package org.stackexchange.component;

import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stackexchange.persistence.dao.IQuestionTweetJpaDAO;

@Component
public final class StackExchangePageStrategy {

    @Autowired
    private IQuestionTweetJpaDAO questionTweetApi;

    public StackExchangePageStrategy() {
        super();
    }

    // API

    // TODO: implement and publish
    final int decidePage(final String twitterAccount, final String stackTag) {
        throw new UnsupportedOperationException();
    }

    public final int decidePage(final String twitterAccount) {
        final int countAllByTwitterAccount = (int) questionTweetApi.countAllByTwitterAccount(twitterAccount);
        return decidePageInternal(countAllByTwitterAccount);
    }

    final int decidePageInternal(final int countAllByTwitterAccount) {
        final int page = countAllByTwitterAccount / 30;
        if (page < 3) {
            return page + 1;
        }

        // was + but, for a long time, a bug in the codebase meant that, from each of the resulted pages, only 1 question was actually tweeted, so now we're going back (for a while)
        // return page + RandomUtils.nextInt(4);
        return page - RandomUtils.nextInt(1);
    }

}
