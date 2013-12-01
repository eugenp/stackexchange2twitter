package org.tweet.twitter.evaluator;

import org.tweet.twitter.util.ErrorUtil;
import org.tweet.twitter.util.TwitterUtil.ForAnalysis;

public class CommercialForAnalysisEvaluator extends AbstractEvaluator {

    public CommercialForAnalysisEvaluator() {
        super(ForAnalysis.Commercial.acceptedRegExes, ForAnalysis.Commercial.bannedRegExesMaybe, ForAnalysis.Commercial.bannedRegExes, "commercial", ErrorUtil.bannedCommercialRegExesMaybeErrors);
    }

}
