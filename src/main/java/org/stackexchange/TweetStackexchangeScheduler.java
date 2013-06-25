package org.stackexchange;

import static org.stackexchange.persistence.setup.TwitterAccountToStackAccount.twitterAccountToStackSite;
import static org.stackexchange.persistence.setup.TwitterAccountToStackAccount.twitterAccountToStackSites;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.stackexchange.api.constants.StackSite;
import org.stackexchange.service.TweetStackexchangeService;
import org.stackexchange.util.SimpleTwitterAccount;
import org.stackexchange.util.StackexchangeUtil;
import org.stackexchange.util.Tag;
import org.stackexchange.util.TagAdvanced;
import org.tweet.spring.util.SpringProfileUtil;

import com.fasterxml.jackson.core.JsonProcessingException;

@Component
@Profile(SpringProfileUtil.DEPLOYED)
public class TweetStackexchangeScheduler {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TweetStackexchangeService service;

    public TweetStackexchangeScheduler() {
        super();
    }

    // API
    @Scheduled(cron = "0 0 12,18 * * *")
    public void tweetStackExchangeTopQuestion() throws JsonProcessingException, IOException {
        logger.info("Starting to execute scheduled tweet operations");

        service.tweetTopQuestionBySite(twitterAccountToStackSite(SimpleTwitterAccount.ServerFaultBest), SimpleTwitterAccount.ServerFaultBest.name(), 1);

        service.tweetTopQuestionBySite(twitterAccountToStackSite(SimpleTwitterAccount.AskUbuntuBest), SimpleTwitterAccount.AskUbuntuBest.name(), 1);

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.SpringAtSO), Tag.spring.name(), SimpleTwitterAccount.SpringAtSO.name(), 1);

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.JavaTopSO), Tag.java.name(), SimpleTwitterAccount.JavaTopSO.name(), 1);

        logger.info("Finished executing scheduled tweet operations");
    }

    /**
     * - these accounts are not StackExchange specific
     */
    @Scheduled(cron = "0 0 13,19 * * *")
    public void tweetDailyTopQuestion1() throws JsonProcessingException, IOException {
        logger.info("Starting to execute scheduled tweet operations 1");

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestClojure), Tag.clojure.name(), SimpleTwitterAccount.BestClojure.name(), 1);

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestScala), Tag.scala.name(), SimpleTwitterAccount.BestScala.name(), 1);

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.jQueryDaily), Tag.jquery.name(), SimpleTwitterAccount.jQueryDaily.name(), 1);

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.RESTDaily), Tag.rest.name(), SimpleTwitterAccount.RESTDaily.name(), 1);

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestEclipse), Tag.eclipse.name(), SimpleTwitterAccount.BestEclipse.name(), 1);

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestGit), Tag.git.name(), SimpleTwitterAccount.BestGit.name(), 1);

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestMaven), Tag.maven.name(), SimpleTwitterAccount.BestMaven.name(), 1);

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestJPA), Tag.jpa.name(), SimpleTwitterAccount.BestJPA.name(), 1);

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestAlgorithms), Tag.algorithm.name(), SimpleTwitterAccount.BestAlgorithms.name(), 1);

        final StackSite randomSite = StackexchangeUtil.pickOne(twitterAccountToStackSites(SimpleTwitterAccount.BestBash));
        service.tweetTopQuestionBySiteAndTag(randomSite, Tag.bash.name(), SimpleTwitterAccount.BestBash.name(), 1);

        logger.info("Finished executing scheduled tweet operations 1");
    }

    /**
     * - these accounts are not StackExchange specific
     */
    @Scheduled(cron = "0 0 14,20 * * *")
    public void tweetDailyTopQuestion2() throws JsonProcessingException, IOException {
        logger.info("Starting to execute scheduled tweet operations 2");

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestJSON), Tag.json.name(), SimpleTwitterAccount.BestJSON.name(), 1);

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestOfJava), Tag.java.name(), SimpleTwitterAccount.BestOfJava.name(), 1);

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestSQL), Tag.sql.name(), SimpleTwitterAccount.BestSQL.name(), 1);

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestNoSQL), Tag.nosql.name(), SimpleTwitterAccount.BestNoSQL.name(), 1);

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.RegexDaily), Tag.regex.name(), SimpleTwitterAccount.RegexDaily.name(), 1);

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestPHP), Tag.php.name(), SimpleTwitterAccount.BestPHP.name(), 1);

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.PythonDaily), Tag.python.name(), SimpleTwitterAccount.PythonDaily.name(), 1);

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestAWS), Tag.aws.name(), SimpleTwitterAccount.BestAWS.name(), 1);

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.ObjectiveCDaily), TagAdvanced.objectivec, SimpleTwitterAccount.ObjectiveCDaily.name(), 1);

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestOfSecurity), Tag.security.name(), SimpleTwitterAccount.BestOfSecurity.name(), 1);

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestOfCloud), Tag.cloud.name(), SimpleTwitterAccount.BestOfCloud.name(), 1);

        logger.info("Finished executing scheduled tweet operations 2");
    }

    /**
     * - these accounts are not StackExchange specific
     */
    @Scheduled(cron = "0 0 15,21 * * *")
    public void tweetDailyTopQuestion3() throws JsonProcessingException, IOException {
        logger.info("Starting to execute scheduled tweet operations 3");

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.iOSdigest), Tag.ios.name(), SimpleTwitterAccount.iOSdigest.name(), 1);

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestJavaScript), Tag.javascript.name(), SimpleTwitterAccount.BestJavaScript.name(), 1);

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.PerlDaily), Tag.perl.name(), SimpleTwitterAccount.PerlDaily.name(), 1);

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestOfRuby), Tag.ruby.name(), SimpleTwitterAccount.BestOfRuby.name(), 1);

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.LispDaily), Tag.lisp.name(), SimpleTwitterAccount.LispDaily.name(), 1);

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestRubyOnRails), TagAdvanced.rubyonrails, SimpleTwitterAccount.BestRubyOnRails.name(), 1);

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.InTheAppleWorld), Tag.apple.name(), SimpleTwitterAccount.InTheAppleWorld.name(), 1);

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.LandOfWordpress), Tag.wordpress.name(), SimpleTwitterAccount.LandOfWordpress.name(), 1);

        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.GoogleDigest), Tag.google.name(), SimpleTwitterAccount.GoogleDigest.name(), 1);

        logger.info("Finished executing scheduled tweet operations 4");
    }

}
