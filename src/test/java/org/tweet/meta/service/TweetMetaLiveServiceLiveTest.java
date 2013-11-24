package org.tweet.meta.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.classification.spring.ClassificationConfig;
import org.common.metrics.MetricsUtil;
import org.common.spring.CommonPersistenceJPAConfig;
import org.common.spring.CommonServiceConfig;
import org.common.spring.MyApplicationContextInitializerProv;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.keyval.spring.KeyValPersistenceJPAConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.SearchParameters;
import org.springframework.social.twitter.api.SearchParameters.ResultType;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stackexchange.util.TwitterAccountEnum;
import org.stackexchange.util.TwitterTag;
import org.tweet.meta.persistence.dao.IRetweetJpaDAO;
import org.tweet.meta.spring.TwitterMetaConfig;
import org.tweet.meta.spring.TwitterMetaPersistenceJPAConfig;
import org.tweet.spring.TwitterConfig;
import org.tweet.spring.TwitterLiveConfig;
import org.tweet.spring.util.SpringProfileUtil;
import org.tweet.twitter.service.live.TwitterReadLiveService;
import org.tweet.twitter.util.TweetUtil;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.core.JsonProcessingException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {// @formatter:off
    KeyValPersistenceJPAConfig.class, 
    
    CommonPersistenceJPAConfig.class, 
    CommonServiceConfig.class, 
    
    ClassificationConfig.class,
    
    TwitterConfig.class, 
    TwitterLiveConfig.class,
    
    TwitterMetaPersistenceJPAConfig.class, 
    TwitterMetaConfig.class 
}) // @formatter:on
@ActiveProfiles({ SpringProfileUtil.LIVE, SpringProfileUtil.WRITE, SpringProfileUtil.DEV, SpringProfileUtil.PERSISTENCE })
public class TweetMetaLiveServiceLiveTest {

    static {
        System.setProperty(MyApplicationContextInitializerProv.PERSISTENCE_TARGET_KEY, "prod");
    }

    @Autowired
    private TweetMetaLiveService tweetMetaLiveService;

    @Autowired
    private TwitterReadLiveService twitterReadLiveService;

    @Autowired
    private IRetweetJpaDAO retweetApi;

    @Autowired
    private MetricRegistry metrics;

    // before and after

    @Before
    public final void before() {
        System.out.println("Before - read operations usage is: " + metrics.counter(MetricsUtil.Meta.TWITTER_READ_OK).getCount());
    }

    @After
    public final void after() {
        System.out.println("After - read operations usage is: " + metrics.counter(MetricsUtil.Meta.TWITTER_READ_OK).getCount());
    }

    // tests

    @Test
    public final void whenContextIsInitialized_thenNoExceptions() {
        //
    }

    // A

    @Test
    public final void whenTweetingAboutApple_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.InTheAppleWorld.name(), TwitterTag.apple.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutAlgorithm_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.AlgorithmsFact.name(), TwitterTag.algorithm.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutAlgorithms_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.AlgorithmsFact.name(), TwitterTag.algorithms.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutAuthentication_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.SecurityFact.name(), TwitterTag.authentication.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByHashtagAboutAkka_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.ScalaFact.name(), TwitterTag.akka.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByWordAboutAkka_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByWord(TwitterAccountEnum.ScalaFact.name(), TwitterTag.akka.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutAsp_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.AspnetDaily.name(), TwitterTag.asp.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutAspNet_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.AspnetDaily.name(), TwitterTag.aspnet.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByHashtagAboutAndroid_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.GoogleDigest.name(), TwitterTag.android.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByWordAboutAndroid_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByWord(TwitterAccountEnum.GoogleDigest.name(), TwitterTag.android.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutAws_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.CloudDaily.name(), TwitterTag.aws.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutAzure_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.CloudDaily.name(), TwitterTag.azure.name());
        assertTrue(success);
    }

    // C

    @Test
    public final void whenTweetingAboutCaptcha_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.SecurityFact.name(), TwitterTag.captcha.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutCassandra_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.BestNoSQL.name(), TwitterTag.cassandra.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutClojure_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.ClojureFact.name(), TwitterTag.clojure.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutCloud_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.CloudDaily.name(), TwitterTag.cloud.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutCss_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.CssFact.name(), TwitterTag.css.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutCss3_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.CssFact.name(), TwitterTag.css3.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByHashtagAboutCrypto_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.SecurityFact.name(), TwitterTag.crypto.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByWordAboutCrypto_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByWord(TwitterAccountEnum.SecurityFact.name(), TwitterTag.crypto.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutCryptography_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.SecurityFact.name(), TwitterTag.cryptography.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutCouchbase_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.BestNoSQL.name(), TwitterTag.couchbase.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutCouchdb_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.BestNoSQL.name(), TwitterTag.couchdb.name());
        assertTrue(success);
    }

    // D

    @Test
    public final void whenTweetingAboutDatabase_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.BestSQL.name(), TwitterTag.database.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutDatomic_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.ClojureFact.name(), TwitterTag.datomic.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutDynamobd_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.BestNoSQL.name(), TwitterTag.dynamobd.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutDdos_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.SecurityFact.name(), TwitterTag.ddos.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutDtrace_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.PerformanceTip.name(), TwitterTag.dtrace.name());
        assertTrue(success);
    }

    // E

    @Test
    // this is for discovery only - Eclipse should only be tweeted from the predefined accounts
    public final void whenTweetingAboutEclipse_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.EclipseFacts.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutEclipselink_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.EclipseFacts.name(), TwitterTag.eclipselink.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutEc2_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.CloudDaily.name(), TwitterTag.ec2.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutEncryption_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.SecurityFact.name(), TwitterTag.encryption.name());
        assertTrue(success);
    }

    // F

    @Test
    public final void whenTweetingAboutFacebook_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.FacebookDigest.name(), TwitterTag.facebook.name());
        assertTrue(success);
    }

    // G

    @Test
    public final void whenTweetingAboutGae_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.CloudDaily.name(), TwitterTag.gae.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutGit_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.GitFact.name(), TwitterTag.git.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutGoogle_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.GoogleDigest.name(), TwitterTag.google.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutGmail_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.GoogleDigest.name(), TwitterTag.gmail.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutGdrive_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.GoogleDigest.name(), TwitterTag.gdrive.name());
        assertTrue(success);
    }

    // H

    @Test
    public final void whenTweetingAboutHadoop_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.HadoopDaily.name(), TwitterTag.hadoop.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutHtml_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.HTMLdaily.name(), TwitterTag.html.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutHtml5_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.BestOfHTML5.name(), TwitterTag.html5.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutHbase_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.BestNoSQL.name(), TwitterTag.hbase.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutHttpclient_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.HttpClient4.name(), TwitterTag.httpclient4.name());
        assertTrue(success);
    }

    // I

    @Test
    public final void whenTweetingAboutIbatis_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.BestJPA.name(), TwitterTag.ibatis.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutIpad_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.InTheAppleWorld.name(), TwitterTag.ipad.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutIos_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.iOSdigest.name(), TwitterTag.ios.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutIphone_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.InTheAppleWorld.name(), TwitterTag.iphone.name());
        assertTrue(success);
    }

    // J

    @Test
    public final void whenTweetingAboutJava_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.JavaFact.name(), TwitterTag.java.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutJvm_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.JavaFact.name(), TwitterTag.jvm.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutJavascript_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.ThinkJavaScript.name(), TwitterTag.javascript.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByHashtagAboutJQuery_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.jQueryDaily.name(), TwitterTag.jquery.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByWordAboutJQuery_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByWord(TwitterAccountEnum.jQueryDaily.name(), TwitterTag.jquery.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutJson_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.BestJSON.name(), TwitterTag.json.name());
        assertTrue(success);
    }

    // K

    // L

    @Test
    public final void whenTweetingAboutLisp_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.LispDaily.name(), TwitterTag.lisp.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutLinux_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.LinuxFact.name(), TwitterTag.linux.name());
        assertTrue(success);
    }

    // M

    @Test
    public final void whenTweetingAboutMath_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.MathDaily.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByHashtagAboutMacbook_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.InTheAppleWorld.name(), TwitterTag.macbook.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByWordAboutMacbook_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByWord(TwitterAccountEnum.InTheAppleWorld.name(), TwitterTag.macbook.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByWordAboutMultithreading_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByWord(TwitterAccountEnum.MultithreadFact.name(), TwitterTag.multithreading.name());
        assertTrue(success);
    }

    @Test
    /*no results*/public final void whenTweetingByHashtagAboutMultithreading_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.MultithreadFact.name(), TwitterTag.multithreading.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutMongodb_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.BestNoSQL.name(), TwitterTag.mongodb.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutMysql_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.BestSQL.name(), TwitterTag.mysql.name());
        assertTrue(success);
    }

    // N

    @Test
    public final void whenTweetingAboutNoSql_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.BestNoSQL.name(), TwitterTag.nosql.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutNeo4j_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.BestNoSQL.name(), TwitterTag.neo4j.name());
        assertTrue(success);
    }

    // O

    @Test
    public final void whenTweetingAboutOpengraph_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.FacebookDigest.name(), TwitterTag.opengraph.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutOpenJpa_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.BestJPA.name(), TwitterTag.openjpa.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutObjectiveC_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.ObjectiveCDaily.name(), TwitterTag.objectivec.name());
        assertTrue(success);
    }

    // P

    @Test
    public final void whenTweetingByHashtagAboutParsing_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.ParsingDaily.name(), TwitterTag.parsing.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByHashtagAboutPerformance_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.PerformanceTip.name(), TwitterTag.performance_java.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByWordAboutParsing_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByWord(TwitterAccountEnum.ParsingDaily.name(), TwitterTag.parsing.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByHashtagAboutPhp_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.BestPHP.name(), TwitterTag.php.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByWordAboutPhp_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByWord(TwitterAccountEnum.BestPHP.name(), TwitterTag.php.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutPasswords_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.SecurityFact.name(), TwitterTag.passwords.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByHashtagAboutPerl_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.PerlDaily.name(), TwitterTag.perl.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByWordAboutPerl_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByWord(TwitterAccountEnum.PerlDaily.name(), TwitterTag.perl.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutPython_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.PythonDaily.name(), TwitterTag.python.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutPostgresql_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.BestSQL.name(), TwitterTag.postgresql.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByHashtagAboutPuppy_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.thedogbreeds.name(), TwitterTag.puppy.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByWordAboutPuppy_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByWord(TwitterAccountEnum.thedogbreeds.name(), TwitterTag.puppy.name());
        assertTrue(success);
    }

    // R

    @Test
    public final void whenTweetingAboutRubyOnRails_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.RubyOnRailsFact.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutRails_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.RubyOnRailsFact.name(), TwitterTag.rails.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutRubyRails_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.RubyOnRailsFact.name(), "ruby_rails");
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutRiak_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.BestNoSQL.name(), TwitterTag.riak.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutRedis_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.BestNoSQL.name(), TwitterTag.redis.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutRegex_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.RegexDaily.name(), TwitterTag.regex.name());
        assertTrue(success);
    }

    // S

    @Test
    public final void whenTweetingAboutScalaByHashtag_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.ScalaFact.name(), TwitterTag.scala.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutScalaByWord_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByWord(TwitterAccountEnum.ScalaFact.name(), TwitterTag.scala.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutSecurity_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.SecurityFact.name(), TwitterTag.security.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutSqlinjection_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.SecurityFact.name(), TwitterTag.sqlinjection.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutSEO_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.LandOfSeo.name(), TwitterTag.seo.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutSQL_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.BestSQL.name(), TwitterTag.sql.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutSpringSecurity_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.SecurityFact.name(), TwitterTag.springsecurity.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutSpringframework_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.SpringTip.name(), TwitterTag.springframework.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingAboutSpringSocial_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.SpringTip.name(), TwitterTag.springsocial.name());
        assertTrue(success);
    }

    // T

    // U

    // V

    // Z

    // W

    @Test
    public final void whenTweetingAboutWordpress_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.LandOfWordpress.name(), TwitterTag.wordpress.name());
        assertTrue(success);
    }

    // X

    @Test
    public final void whenTweetingAboutXml_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.BestXML.name(), TwitterTag.xml.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByHashtagAboutXcode_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtag(TwitterAccountEnum.ObjectiveCDaily.name(), TwitterTag.xcode.name());
        assertTrue(success);
    }

    @Test
    public final void whenTweetingByWordAboutXcode_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByWord(TwitterAccountEnum.ObjectiveCDaily.name(), TwitterTag.xcode.name());
        assertTrue(success);
    }

    // predefined account

    @Test
    public final void whenTweetingFromPredefinedAccountAboutEclipse_thenNoExceptions() throws JsonProcessingException, IOException {
        final boolean success = tweetMetaLiveService.retweetAnyByHashtagOnlyFromPredefinedAccounts(TwitterAccountEnum.EclipseFacts.name());
        assertTrue(success);
    }

    // new type of search

    public final void whenNewTypeOfDataIsSearched_thenCorrect() {
        // the goal here is to pick: https://twitter.com/csdhall/status/364424663704670208
        final String hashtag = "javascript";
        final SearchParameters searchParameters = new SearchParameters("Join #" + hashtag).lang("en").count(100).includeEntities(true).resultType(ResultType.MIXED);
        final SearchResults searchResults = twitterReadLiveService.readOnlyTwitterApi().searchOperations().search(searchParameters);
        // filter out the ones that do not have `Join @`
        // evaluate the rest based on interaction value and use the best
        System.out.println(searchResults);
    }

    // pointing to something good

    @Test
    public final void givenTweetNotPointingToAnythingGood1_whenCheckingIfItIsPointingToSomethingGood_thenNo() {
        final boolean isIt = tweetMetaLiveService.isTweetPointingToSomethingGoodTechnical("Going on #facebook for a bit :) http://www.facebook.com/realshamidrees");
        assertThat(isIt, is(false));
    }

    @Test
    public final void givenTweetNotPointingToAnythingGood2_whenCheckingIfItIsPointingToSomethingGood_thenNo() {
        final Tweet tweet = twitterReadLiveService.findOne(357461150910251008l);
        final String tweetText = TweetUtil.getText(tweet);
        final boolean isIt = tweetMetaLiveService.isTweetPointingToSomethingGoodTechnical(tweetText);
        assertThat(isIt, is(false));
    }

    @Test
    public final void givenTweetNotPointingToAnythingGood3_whenCheckingIfItIsPointingToSomethingGood_thenNo() {
        final Tweet tweet = twitterReadLiveService.findOne(350599636307812355l);
        final String tweetText = TweetUtil.getText(tweet);
        final boolean isIt = tweetMetaLiveService.isTweetPointingToSomethingGoodTechnical(tweetText);
        assertThat(isIt, is(false));
    }

    // production scenarios

    @Test
    @Ignore("not yet done")
    public final void whenTweetingSimilarToProductionScenario1_thenShouldNotTweet() {
        final String twitterAccount = TwitterAccountEnum.CloudDaily.name();
        tweetMetaLiveService.tryTweetOne("Five Tips to Improve Your #AWS Security by @Cloud_Optimize ▸ http://t.co/BAMMqlxNUc #Cloud #CloudComputing", null, twitterAccount, null);
    }

}
