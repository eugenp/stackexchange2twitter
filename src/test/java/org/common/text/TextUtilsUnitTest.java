package org.common.text;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.common.util.TextUtil;
import org.junit.Test;
import org.tweet.twitter.util.TwitterUtil;

public class TextUtilsUnitTest {

    // tests

    // pre-processing text

    @Test
    public final void givenTextContainsInvalidCharactersScenario1_whenProcessingTweetText_thenCorrectlyProcessed() {
        final String expectedText = "System(\"pause\"); - Why is it wrong? - stackoverflow.com/questions/1107";
        final String preProcessedTweet = TextUtil.cleanupInvalidCharacters("System(“pause”); - Why is it wrong? - stackoverflow.com/questions/1107");
        assertThat(preProcessedTweet, equalTo(expectedText));
    }

    @Test
    public final void givenTextContainsInvalidCharactersScenario2_whenProcessingTweetText_thenCorrectlyProcessed() {
        final String expectedText = "<context:property-placeholder> properties not accessible to the child (web) context";
        final String preProcessedTweet = TextUtil.cleanupInvalidCharacters("<context:property-placeholder> properties not accessible to the child (web) context");
        assertThat(preProcessedTweet, equalTo(expectedText));
    }

    @Test
    public final void givenTextContainsInvalidCharactersScenario3_whenProcessingTweetText_thenCorrectlyProcessed() {
        final String expectedText = "#Akka 2.2.0 FINAL Released - http://tmblr.co/ZlHOLwpD-j-a";
        final String preProcessedTweet = TextUtil.cleanupInvalidCharacters("#Akka 2.2.0 FINAL Released — http://tmblr.co/ZlHOLwpD-j-a");
        assertThat(preProcessedTweet, equalTo(expectedText));
    }

    @Test
    public final void givenTextContainsInvalidCharactersScenario4_whenProcessingTweetText_thenCorrectlyProcessed() {
        final String expectedText = "What is the difference between an EAR file and a \"WebSphere Enhanced EAR\"?";
        final String preProcessedTweet = TextUtil.cleanupInvalidCharacters("What is the difference between an EAR file and a “WebSphere Enhanced EAR”?");
        assertThat(preProcessedTweet, equalTo(expectedText));
    }

    @Test
    public final void givenTextContainsInvalidCharactersScenario5_whenProcessingTweetText_thenCorrectlyProcessed() {
        final String expectedText = "Spring Batch Example - XML File To Database";
        final String preProcessedTweet = TextUtil.cleanupInvalidCharacters("Spring Batch Example – XML File To Database");
        assertThat(preProcessedTweet, equalTo(expectedText));
    }

    @Test
    public final void givenTextContainsInvalidCharactersScenario6_whenProcessingTweetText_thenCorrectlyProcessed() {
        final String expectedText = "What's the best way to validate an XML file against an XSD file?";
        final String preProcessedTweet = TextUtil.cleanupInvalidCharacters("What's the best way to validate an XML file against an XSD file?");
        assertThat(preProcessedTweet, equalTo(expectedText));
    }

    @Test
    public final void givenTextContainsInvalidCharactersScenario7_whenProcessingTweetText_thenCorrectlyProcessed() {
        final String expectedText = "Understanding \"randomness\"";
        final String preProcessedTweet = TextUtil.cleanupInvalidCharacters("Understanding “randomness”");
        assertThat(preProcessedTweet, equalTo(expectedText));
    }

    @Test
    public final void givenTextContainsInvalidCharactersScenario8_whenProcessingTweetText_thenCorrectlyProcessed() {
        final String expectedText = "What's the best way to communicate between view controllers?";
        final String preProcessedTweet = TextUtil.cleanupInvalidCharacters("What's the best way to communicate between view controllers?");
        assertThat(preProcessedTweet, equalTo(expectedText));
    }

    @Test
    public final void givenTextContainsInvalidCharactersScenario9_whenProcessingTweetText_thenCorrectlyProcessed() {
        final String expectedText = "I'm broadcasting #LIVE on #HangWith for #iPhone! Come Hang w/souljaboy! http://bit.ly/hangwsocial";
        final String preProcessedTweet = TextUtil.cleanupInvalidCharacters("I&rsquo;m broadcasting #LIVE on #HangWith for #iPhone! Come Hang w/souljaboy! http://bit.ly/hangwsocial");
        assertThat(preProcessedTweet, equalTo(expectedText));
    }

    @Test
    public final void givenTextContainsInvalidCharactersScenario10_whenProcessingTweetText_thenCorrectlyProcessed() {
        final String expectedText = "#Google Search API for C# - http://stackoverflow.com/questions/2580396/google-searchapi-for-c";
        final String preProcessedTweet = TextUtil.cleanupInvalidCharacters("#Google Search&nbsp;API for C# - http://stackoverflow.com/questions/2580396/google-searchapi-for-c");
        assertThat(preProcessedTweet, equalTo(expectedText));
    }

    @Test
    public final void givenTextContainsInvalidCharactersScenario11_whenProcessingTweetText_thenCorrectlyProcessed() {
        final String expectedText = "#Akka 2.2 Spotlight: Publish/Subscribe in Cluster... http://tmblr.co/ZlHOLwq2afmO";
        final String preProcessedTweet = TextUtil.cleanupInvalidCharacters("#Akka 2.2 Spotlight: Publish/Subscribe in Cluster… http://tmblr.co/ZlHOLwq2afmO");
        assertThat(preProcessedTweet, equalTo(expectedText));
    }

    @Test
    public final void givenTextContainsInvalidCharactersScenario12_whenProcessingTweetText_thenCorrectlyProcessed() {
        final String expectedText = "What are Unity's keyboard and mouse shortcuts?";
        final String preProcessedTweet = TextUtil.cleanupInvalidCharacters("What are Unity&#39;s keyboard and mouse shortcuts?");
        assertThat(preProcessedTweet, equalTo(expectedText));
    }

    @Test
    public final void givenTextContainsInvalidCharactersScenario13_whenProcessingTweetText_thenCorrectlyProcessed() {
        final String expectedText = "SearchHub: Join @ErikHatcher at the #Lucene/Solr Meetup in Sao Paulo, June 11th. Don't miss out! http://wp.me/p3cNDk-2vx";
        final String preProcessedTweet = TextUtil.cleanupInvalidCharacters("SearchHub: Join @ErikHatcher at the #Lucene/Solr Meetup in SÃ£o Paulo, June 11th. Don't miss out! http://wp.me/p3cNDk-2vx");
        assertThat(preProcessedTweet, equalTo(expectedText));
    }

    @Test
    public final void givenTextContainsInvalidCharactersScenario15_whenProcessingTweetText_thenCorrectlyProcessed() {
        final String expectedText = "Custom USB sticks bypassing Windows 7/8's AutoRun protection measure going mainstream - http://is.gd/5TlNuz  #security #malware";
        final String preProcessedTweet = TextUtil.cleanupInvalidCharacters("Custom USB sticks bypassing Windows 7/8′s AutoRun protection measure going mainstream - http://is.gd/5TlNuz  #security #malware");
        assertThat(preProcessedTweet, equalTo(expectedText));
    }

    @Test
    public final void givenTextContainsInvalidCharactersScenario16_whenProcessingTweetText_thenCorrectlyProcessed() {
        final String expectedText = "Know anyone for this job? Ruby on Rails Developer in Chicago, IL http://bull.hn/l/14445f889f1d5ebf504e098c9429f3d1/5 | #job #ruby #chicago";
        final String preProcessedTweet = TextUtil.cleanupInvalidCharacters("Know anyone for this job? Ruby on Rails Developer in Chicago, IL http://bull.hn/l/14445f889f1d5ebf504e098c9429f3d1/5 ¦ #job #ruby #chicago");
        assertThat(preProcessedTweet, equalTo(expectedText));
    }

    @Test
    public final void givenTextContainsInvalidCharactersScenario17_whenProcessingTweetText_thenCorrectlyProcessed() {
        final String expectedText = "Verivo is looking for a Sr. Build Engineer with #Maven & #Jenkins experience; interested? Apply here: http://ow.ly/h8naT #jobs";
        final String preProcessedTweet = TextUtil.cleanupInvalidCharacters("Verivo is looking for a Sr. Build Engineer with #Maven & #Jenkins experience; interested? Apply here: http://ow.ly/h8naT #jobs");
        assertThat(preProcessedTweet, equalTo(expectedText));
    }

    @Test
    public final void givenTextContainsInvalidCharactersScenario18_whenProcessingTweetText_thenCorrectlyProcessed() {
        final String expectedText = "What is the \"Grave\" button?";
        final String preProcessedTweet = TextUtil.cleanupInvalidCharacters("What is the “Grave” button?");
        assertThat(preProcessedTweet, equalTo(expectedText));
    }

    @Test
    public final void givenTextContainsInvalidCharactersScenario19_whenProcessingTweetText_thenCorrectlyProcessed() {
        final String expectedText = "Hibernate Spatial: \"No Dialect mapping for JDBC type: 3000\"";
        final String preProcessedTweet = TextUtil.cleanupInvalidCharacters("Hibernate Spatial: “No Dialect mapping for JDBC type: 3000”");
        assertThat(preProcessedTweet, equalTo(expectedText));
    }

    @Test
    public final void givenTextContainsInvalidCharactersScenario20_whenProcessingTweetText_thenCorrectlyProcessed() {
        final String expectedText = "Find largest rectangle containing only zeros in an NxN binary matrix";
        final String preProcessedTweet = TextUtil.cleanupInvalidCharacters("Find largest rectangle containing only zeros in an N×N binary matrix");
        assertThat(preProcessedTweet, equalTo(expectedText));
    }

    @Test
    public final void givenTextContainsInvalidCharactersScenario21_whenProcessingTweetText_thenCorrectlyProcessed() {
        final String expectedText = "#FACEBOOK Facebook Likes - 5k by majsi: I would like 5,000 likes for my my Facebook Page for 10E Condition... http://bit.ly/15tGJrT";
        final String preProcessedTweet = TextUtil.cleanupInvalidCharacters("#FACEBOOK Facebook Likes - 5k by majsi: I would like 5,000 likes for my my Facebook Page for 10€ Condition... http://bit.ly/15tGJrT");
        assertThat(preProcessedTweet, equalTo(expectedText));
    }

    @Test
    public final void givenTextContainsInvalidCharactersScenario22_whenProcessingTweetText_thenCorrectlyProcessed() {
        final String expectedText = "Write a series of 6 articles by TrakRecruit: Background: I'm an Australian executive recruiter ... http://bit.ly/14fPkBB  #Ghostwriting";
        final String preProcessedTweet = TextUtil.cleanupInvalidCharacters("Write a series of 6 articles by TrakRecruit: Background: I’m an Australian executive recruiter ... http://bit.ly/14fPkBB  #Ghostwriting");
        assertThat(preProcessedTweet, equalTo(expectedText));
    }

    @Test
    // new
    public final void givenTextContainsInvalidCharactersScenario23_whenProcessingTweetText_thenCorrectlyProcessed() {
        final String expectedText = "RT @Finding4Job: Facebook for Business and Marketing - The 5 Advantages #fb #business #marketing http://t.co/r8928HLoqH #business #facebook";
        final String preProcessedTweet = TextUtil.cleanupInvalidCharacters("RT @Finding4Job: Facebook for Business and Marketing � The 5 Advantages #fb #business #marketing http://t.co/r8928HLoqH #business #facebook");
        assertThat(preProcessedTweet, equalTo(expectedText));
    }

    @Test
    public final void givenTextContainsInvalidCharactersScenario24_whenProcessingTweetText_thenCorrectlyProcessed() {
        final String expectedText = "RT #iPhone #App #Best #Phone #Tools # #Utilities $1.99-> #FREE http://t.co/zWgezCu9EN #DEAL #iOS #heyyou #heyyouapp 4 http://t.co/e8RB2K2VxG";
        final String preProcessedTweet = TextUtil.cleanupInvalidCharacters("RT #iPhone #App #Best #Phone #Tools # #Utilities $1.99-&gt; #FREE http://t.co/zWgezCu9EN #DEAL #iOS #heyyou #heyyouapp 4 http://t.co/e8RB2K2VxG");
        assertThat(preProcessedTweet, equalTo(expectedText));
    }

    @Test
    // new
    public final void givenTextContainsInvalidCharactersScenario25_whenProcessingTweetText_thenCorrectlyProcessed() {
        final String expectedText = "1st β of #KDE Software Compilation 4.11 released. Help us find bugs, translate it - http://www.kde.org/announcements/announce-4.11-beta1.php … — #Linux #OpenSource";
        final String preProcessedTweet = TextUtil.cleanupInvalidCharacters("1st β of #KDE Software Compilation 4.11 released. Help us find bugs, translate it → http://www.kde.org/announcements/announce-4.11-beta1.php … — #Linux #OpenSource");
        assertThat(preProcessedTweet, equalTo(expectedText));
    }

    // ✔ - I cannot add this into some Strings - it simply get replaced by a square with something inside
    // others: ü

    // trim

    @Test
    public final void whenTrimmingLargeTweetScenario1_thenCorrectResult() {
        final String largeTweet = "RT @__GiMa__: #NEW [#iOS] #Math #Bracket|# #Education|$0.99-> #FREE #NOW| http://t.co/dVzJKsKFUY #LIMITED #TIME|#iPhone http://t.co/5ZPOw9a...";
        final String trimmedTweet = TextUtil.trimTweet(largeTweet);
        assertTrue(TwitterUtil.isTweetTextWithLinkValid(trimmedTweet));
    }

    @Test
    public final void whenTrimmingLargeTweetScenario2_thenCorrectResult() {
        final String largeTweet = "RT @__GiMa__: #NEW [#iOS] #iGet #- #Download ...|# #Utilities|$0.99-> #FREE #NOW| http://t.co/AYA08aMFKa #LIMITED #TIME|#iPhone http://t.co...";
        final String trimmedTweet = TextUtil.trimTweet(largeTweet);
        assertTrue(TwitterUtil.isTweetTextWithLinkValid(trimmedTweet));
    }

    @Test
    public final void whenTrimmingLargeTweetScenario3_thenCorrectResult() {
        final String largeTweet = "RT @__GiMa__: #NEW [#iOS] #Cool #Barcelona #!|# #Travel|$3.99-> #FREE #NOW| http://t.co/gEBl8pFb11 #LIMITED #TIME|#iPhone http://t.co/EtDBk...";
        final String trimmedTweet = TextUtil.trimTweet(largeTweet);
        assertTrue(TwitterUtil.isTweetTextWithLinkValid(trimmedTweet));
    }

    @Test
    public final void whenTrimmingLargeTweetScenario4_thenCorrectResult() {
        final String largeTweet = "RT @linuxfoundation: A fellow #Linux SysAdmin shares tips and tricks in his blog: http://t.co/mlKBdh413q And a thank u from LF: http://t.co...";
        final String trimmedTweet = TextUtil.trimTweet(largeTweet);
        assertTrue(TwitterUtil.isTweetTextWithLinkValid(trimmedTweet));
    }

    @Test
    public final void whenTrimmingLargeTweetScenario5_thenCorrectResult() {
        final String largeTweet = "RT @FightingCEO: #SQL Formatting standards - Capitalization, Indentation, Comments, Parenthesis http://t.co/gEo3kl91XF via @sharethis #SQL #...";
        final String trimmedTweet = TextUtil.trimTweet(largeTweet);
        assertTrue(TwitterUtil.isTweetTextWithLinkValid(trimmedTweet));
    }

    @Test
    public final void whenTrimmingLargeTweetScenario6_thenCorrectResult() {
        final String largeTweet = "The latest release of #Zend Studio - 10.1.0 - makes it easier than ever to work with #PHP libraries and #Zend Server http://t.co/3k...";
        final String trimmedTweet = TextUtil.trimTweet(largeTweet);
        assertTrue(TwitterUtil.isTweetTextWithLinkValid(trimmedTweet));
    }

    @Test
    public final void whenTrimmingLargeTweetScenario7_thenCorrectResult() {
        final String largeTweet = "#NEW [#iOS] #Ace #Block ...|# #Games|$0.99-> #FREE #NOW| http://bit.ly/17KgsqD  #LIMITED #TIME|#iPhone|#DEAL|#RT|#App|657";
        final String trimmedTweet = TextUtil.trimTweet(largeTweet);
        assertTrue(TwitterUtil.isTweetTextWithLinkValid(trimmedTweet));
    }

}
