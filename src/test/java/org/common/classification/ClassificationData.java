package org.common.classification;

import static org.common.classification.ClassificationUtil.COMMERCIAL;
import static org.common.classification.ClassificationUtil.NONCOMMERCIAL;
import static org.common.classification.ClassificationUtil.encode;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.mahout.math.NamedVector;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

public final class ClassificationData {

    private ClassificationData() {
        throw new AssertionError();
    }

    public static List<NamedVector> commercialVsNonCommercialTestData() throws IOException {
        final List<String> noncommercialTweets = Lists.newArrayList(// @formatter:off
                "A set of great #scala and #akka examples http://bit.ly/KlJkro  #java #mapred #programming #dev", 
                "New report shows that 11% of #Java devs also use #Scala on some projects http://0t.ee/devprodrep2012c", 
                "What features of #java have been dropped in #scala? http://www.javacodegeeks.com/2011/08/what-features-of-java-have-been-dropped.html …", 
                "Why should a #Java #developer learn #Scala? To write better Java, says @jessitron: http://bit.ly/13ReUfU  via @JAXenter", 
                "#Mixin in #Java with Aspects – for a #Scala traits sample http://buff.ly/10nAOks", 
                "The Play Framework at LinkedIn #java #linkedin #scala http://es.slideshare.net/brikis98/the-play-framework-at-linkedin …", 
                "Why Scala is terser than Java? http://sortega.github.io/development/2013/06/22/terseness/ … #Scala #Java", 
                "So @springrod 'says' there is more junk in #scala libs than in #java ones, I want to see evidence for that(45:10): http://www.parleys.com", 
                "What are the popular code conventions based on some @github hosted code ? http://sideeffect.kr/popularconvention/ … (via @heyitsnoah)", 
                "Good Presentation. The #PlayFramework at #LinkedIn: Productivity and Performance at Scale: http://youtu.be/8z3h4Uv9YbE  #Scala #Java" 
        ); // @formatter:on
        final List<String> commercialTweets = Lists.newArrayList(// @formatter:off
                "We're looking to #hire a Front End Developer/Creative Designer to join our team in Leeds. Get in touch for more information.", 
                "New job Nurse Practitioners & Physician Assistants - New Jersey  #hire #jobs", 
                "Looking to hire excellent senior Java developers preferrably with MongoDb skills to our Espoo, Finland office #agile #hire #Java #MongoDb",
                "I'm looking for #scala dev #contract in #Melbourne or #Sydney. #job", 
                "Scala Developer with World-Class Tech Co in Cambridge #job #scala #cambridge", 
                "Hiring a C# or Java Software Developer / Programmer - London in London, United Kingdom http://bull.hn/l/119NU/3  #job #.net #Java", 
                "We have lots of #java engineer #job opportunities available! http://bit.ly/10b6Oap", 
                "Know anyone for this job? JAVA/OLYMPIC consultant, german fluent in Luxembourg City, Luxembourg http://bull.hn/l/YWLX/5  #job #java", 
                "Looking for a Senior Java Developer in Hoboken, NJ http://bull.hn/l/12MHI/  #job #java", 
                "Know anyone for this job? Sr. Java Software Engineer in Atlanta, GA http://bull.hn/l/12D6I/5  #job #java", 
                "Are you a good fit for this job? Java Backend Developer in Amsterdam, Netherlands http://bull.hn/l/XVTE/6  #job #java #amsterdam"
        ); // @formatter:on
    
        final List<NamedVector> noncommercialNamedVectors = Lists.<NamedVector> newArrayList();
        final List<NamedVector> commercialNamedVectors = Lists.<NamedVector> newArrayList();
        for (final String noncommercialTweet : noncommercialTweets) {
            noncommercialNamedVectors.add(encode(NONCOMMERCIAL, Splitter.on(CharMatcher.anyOf(" ")).split(noncommercialTweet)));
        }
        for (final String commercialTweet : commercialTweets) {
            noncommercialNamedVectors.add(encode(COMMERCIAL, Splitter.on(CharMatcher.anyOf(" ")).split(commercialTweet)));
        }
    
        final List<NamedVector> allNamedVectors = Lists.<NamedVector> newArrayList();
        allNamedVectors.addAll(commercialNamedVectors);
        allNamedVectors.addAll(noncommercialNamedVectors);
        Collections.shuffle(allNamedVectors);
        return allNamedVectors;
    }

    // API

}
