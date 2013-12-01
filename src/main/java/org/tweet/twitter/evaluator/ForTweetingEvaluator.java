package org.tweet.twitter.evaluator;

import org.tweet.twitter.util.ErrorUtil;
import org.tweet.twitter.util.TwitterUtil.ForTweeting;

public class ForTweetingEvaluator extends AbstractEvaluator {

    public ForTweetingEvaluator() {
        super(ForTweeting.acceptedRegExes, ForTweeting.bannedRegExesMaybe, ForTweeting.bannedRegExes, "tweeting-generic", ErrorUtil.bannedRegExesMaybeErrors);
    }

}
