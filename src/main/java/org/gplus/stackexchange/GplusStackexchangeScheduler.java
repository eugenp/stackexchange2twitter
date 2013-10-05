package org.gplus.stackexchange;

// @Service
// @Profile(SpringProfileUtil.WRITE_PRODUCTION)
//public class GplusStackexchangeScheduler {
//    private final Logger logger = LoggerFactory.getLogger(getClass());
//
//    @Autowired
//    private TweetStackexchangeService service;
//
//    public GplusStackexchangeScheduler() {
//        super();
//    }
//
//    // API
//    // minute accuracy
//    // @Scheduled(cron = "0 23 9,12 * * *")
//    @Scheduled(cron = "0 0 1,5 * * *")
//    public void tweetStackExchangeTopQuestion() throws JsonProcessingException, IOException {
//        logger.info("Starting to execute scheduled tweet operations");
//
//        service.tweetTopQuestionBySite(twitterAccountToStackSite(SimpleTwitterAccount.ServerFaultBest), SimpleTwitterAccount.ServerFaultBest.name(), 1);
//
//        service.tweetTopQuestionBySite(twitterAccountToStackSite(SimpleTwitterAccount.AskUbuntuBest), SimpleTwitterAccount.AskUbuntuBest.name(), 1);
//
//        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.SpringTip), Tag.spring.name(), SimpleTwitterAccount.SpringTip.name(), 1);
//
//        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.JavaTopSO), Tag.java.name(), SimpleTwitterAccount.JavaTopSO.name(), 1);
//
//        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestClojure), Tag.clojure.name(), SimpleTwitterAccount.BestClojure.name(), 1);
//
//        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestScala), Tag.scala.name(), SimpleTwitterAccount.BestScala.name(), 1);
//
//        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.jQueryDaily), Tag.jquery.name(), SimpleTwitterAccount.jQueryDaily.name(), 1);
//
//        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.RESTDaily), Tag.rest.name(), SimpleTwitterAccount.RESTDaily.name(), 1);
//
//        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.EclipseFacts), Tag.eclipse.name(), SimpleTwitterAccount.EclipseFacts.name(), 1);
//
//        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestGit), Tag.git.name(), SimpleTwitterAccount.BestGit.name(), 1);
//
//        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestMaven), Tag.maven.name(), SimpleTwitterAccount.BestMaven.name(), 1);
//
//        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestJPA), Tag.jpa.name(), SimpleTwitterAccount.BestJPA.name(), 1);
//
//        service.tweetTopQuestionBySiteAndTag(twitterAccountToStackSite(SimpleTwitterAccount.BestAlgorithms), Tag.algorithm.name(), SimpleTwitterAccount.BestAlgorithms.name(), 1);
//
//        final StackSite randomSite = StackexchangeUtil.pickOne(twitterAccountToStackSites(SimpleTwitterAccount.BestBash));
//        service.tweetTopQuestionBySiteAndTag(randomSite, Tag.bash.name(), SimpleTwitterAccount.BestBash.name(), 1);
//
//        logger.info("Finished executing scheduled tweet operations");
//    }
//
// }
