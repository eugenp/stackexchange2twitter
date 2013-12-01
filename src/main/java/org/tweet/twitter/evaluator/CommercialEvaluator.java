package org.tweet.twitter.evaluator;

import org.tweet.twitter.util.ErrorUtil;
import org.tweet.twitter.util.TwitterUtil.ForAnalysis;

public class CommercialEvaluator extends AbstractEvaluator {

    public CommercialEvaluator() {
        super(ForAnalysis.acceptedRegExes, ForAnalysis.bannedRegExesMaybe, ForAnalysis.bannedRegExes, "generic", ErrorUtil.bannedRegExesMaybeErrors);
    }

}
