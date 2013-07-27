package org.common.text;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.common.util.LinkUtil;
import org.junit.Test;

public class LinkUtilUnitTest {

    // tests

    // extract urls

    @Test
    public final void whenExtractingUrlFromContentScenario1_thenNoExceptions() {
        final String content = "For functional programming fans: <span class=\"proflinkWrapper\"><span class=\"proflinkPrefix\">+</span><a href=\"https://plus.google.com/112870314339261400768\" class=\"proflink\" oid=\"112870314339261400768\">Adam Bard</a></span>�takes a look at Clojure/core.reducers, and how the parallelism expressed in the framework impacts performance. In particular, the new &#39;fold&#39; method (a parallel &#39;reduce&#39; and &#39;combine&#39;) can have major benefits.<br /><br />More on reducers at <a href=\"http://clojure.com/blog/2012/05/15/anatomy-of-reducer.html\" class=\"ot-anchor\" rel=\"nofollow\">clojure.com/blog/2012/05/15/anatomy-of-reducer.html</a>.";
        final List<String> extractedUrls = LinkUtil.extractUrls(content);
        assertThat(extractedUrls, hasSize(2));
    }

    @Test
    public final void whenExtractingUrlFromContentScenario2_thenNoExceptions() {
        final String content = "<b>Clojure Koans</b><br /><br />$ git clone git://<a href=\"http://github.com/functional-koans/clojure-koans.git\" class=\"ot-anchor\">github.com/functional-koans/clojure-koans.git</a> <br />$ cd clojure-koans<br />$ curl <a href=\"https://raw.github.com/technomancy/leiningen/stable/bin/lein\" class=\"ot-anchor\">https://raw.github.com/technomancy/leiningen/stable/bin/lein</a> &gt; script/lein<br />$ chmod +x script/lein<br />$ script/lein deps<br />$ script/run<br /><br />Enlightenment awaits.";
        final List<String> extractedUrls = LinkUtil.extractUrls(content);
        assertThat(extractedUrls, hasSize(3));
    }

    // determine main url

    @Test
    public final void givenUrls_whenDeterminingMainUrlScenario1_thenCorrectlyDetermined() {
        final String content = "For functional programming fans: <span class=\"proflinkWrapper\"><span class=\"proflinkPrefix\">+</span><a href=\"https://plus.google.com/112870314339261400768\" class=\"proflink\" oid=\"112870314339261400768\">Adam Bard</a></span>�takes a look at Clojure/core.reducers, and how the parallelism expressed in the framework impacts performance. In particular, the new &#39;fold&#39; method (a parallel &#39;reduce&#39; and &#39;combine&#39;) can have major benefits.<br /><br />More on reducers at <a href=\"http://clojure.com/blog/2012/05/15/anatomy-of-reducer.html\" class=\"ot-anchor\" rel=\"nofollow\">clojure.com/blog/2012/05/15/anatomy-of-reducer.html</a>.";
        final List<String> extractedUrls = LinkUtil.extractUrls(content);
        final String mainUrl = LinkUtil.determineMainUrl(extractedUrls);
        assertThat(mainUrl, equalTo("http://clojure.com/blog/2012/05/15/anatomy-of-reducer.html"));
    }

    @Test
    public final void givenUrls_whenDeterminingMainUrlScenario2_thenCorrectlyDetermined() {
        final String content = "<b>Clojure Koans</b><br /><br />$ git clone git://<a href=\"http://github.com/functional-koans/clojure-koans.git\" class=\"ot-anchor\">github.com/functional-koans/clojure-koans.git</a> <br />$ cd clojure-koans<br />$ curl <a href=\"https://raw.github.com/technomancy/leiningen/stable/bin/lein\" class=\"ot-anchor\">https://raw.github.com/technomancy/leiningen/stable/bin/lein</a> &gt; script/lein<br />$ chmod +x script/lein<br />$ script/lein deps<br />$ script/run<br /><br />Enlightenment awaits.";
        final List<String> extractedUrls = LinkUtil.extractUrls(content);
        final String mainUrl = LinkUtil.determineMainUrl(extractedUrls);
        assertThat(mainUrl, equalTo("https://raw.github.com/technomancy/leiningen/stable/bin/lein"));
    }

}
