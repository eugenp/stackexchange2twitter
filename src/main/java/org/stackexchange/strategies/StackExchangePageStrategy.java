package org.stackexchange.strategies;

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

        return page;
    }

}
