package org.stackexchange.strategies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stackexchange.persistence.dao.IQuestionTweetJpaDAO;
import org.stackexchange.util.SimpleTwitterAccount;

@Component
public final class StackExchangePageStrategy {

    @Autowired
    private IQuestionTweetJpaDAO questionTweetApi;

    public StackExchangePageStrategy() {
        super();
    }

    // API

    public final int decidePage(final SimpleTwitterAccount twitterAccount, final String tag) {
        final long countAllByTwitterAccount = questionTweetApi.countAllByTwitterAccount(twitterAccount.name());

        return 1;
    }

}
