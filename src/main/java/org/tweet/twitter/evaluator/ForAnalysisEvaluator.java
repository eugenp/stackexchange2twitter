package org.tweet.twitter.evaluator;

import org.tweet.twitter.util.ErrorUtil;
import org.tweet.twitter.util.TwitterUtil.ForAnalysis;

public class ForAnalysisEvaluator extends AbstractEvaluator {

    public ForAnalysisEvaluator() {
        super(ForAnalysis.acceptedRegExes, ForAnalysis.bannedRegExesMaybe, ForAnalysis.bannedRegExes, "analysis-generic", ErrorUtil.bannedRegExesMaybeErrors);
    }

}
