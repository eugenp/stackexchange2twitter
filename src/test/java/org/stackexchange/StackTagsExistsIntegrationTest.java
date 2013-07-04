package org.stackexchange;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stackexchange.spring.StackexchangeConfig;
import org.stackexchange.util.SimpleTwitterAccount;
import org.tweet.spring.TwitterConfig;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TwitterConfig.class, StackexchangeConfig.class })
public class StackTagsExistsIntegrationTest {

    @Autowired
    private Environment env;

    // API

    @Test
    public final void whenRetrievingMinScoresOfAllStackTags_thenFound() {
        for (final SimpleTwitterAccount account : SimpleTwitterAccount.values()) {
            final String stackTagsOfAccountRaw = env.getProperty(account.name() + ".stack.tags");
            assertNotNull("No stack tags found for account: " + account, stackTagsOfAccountRaw);

            final List<String> stackTagsOfAccount = Lists.newArrayList(Splitter.on(',').split(stackTagsOfAccountRaw));
            for (final String stackTag : stackTagsOfAccount) {
                if (stackTag.length() == 0) { // skip over empty tags from accounts that do not have any
                    continue;
                }
                assertNotNull("No min score found for stack tag: " + stackTag + "; on account: " + account, env.getProperty(stackTag + ".minscore"));
            }
        }
    }

}
