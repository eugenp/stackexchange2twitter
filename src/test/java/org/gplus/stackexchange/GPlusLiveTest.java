package org.gplus.stackexchange;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import org.common.spring.CommonContextConfig;
import org.common.util.LinkUtil;
import org.gplus.service.ActivityHelper;
import org.gplus.service.GplusService;
import org.gplus.spring.GplusContextConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stackexchange.util.TwitterTag;
import org.tweet.spring.util.SpringProfileUtil;

import com.google.api.services.plus.model.Activity;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CommonContextConfig.class, GplusContextConfig.class })
@ActiveProfiles(SpringProfileUtil.LIVE)
public class GPlusLiveTest {

    @Autowired
    private GplusService gplusService;

    // tests

    @Test
    public void whenConsumingTheGoogleApi_thenNoExceptions() throws GeneralSecurityException, IOException {
        final String activityId = "z12gtjhq3qn2xxl2o224exwiqruvtda0i";
        ActivityHelper.show(gplusService.getSingleActivity(activityId));
    }

    @Test
    public void whenSearchingForActivities_thenResultsAreFound() throws GeneralSecurityException, IOException {
        assertThat(gplusService.search(TwitterTag.clojure.name()), not(empty()));
    }

    @Test
    public void whenSearchingForActivitiesScenario1_thenResultsAreCorrect() throws GeneralSecurityException, IOException {
        final List<Activity> searchResults = gplusService.search(TwitterTag.clojure.name());
        for (final Activity activity : searchResults) {
            ActivityHelper.show(activity);
        }
    }

    @Test
    public void whenSearchingForActivitiesScenario2_thenResultsAreCorrect() throws GeneralSecurityException, IOException {
        final List<Activity> searchResults = gplusService.search(TwitterTag.jquery.name());
        for (final Activity activity : searchResults) {
            ActivityHelper.show(activity);
        }
    }

    @Test
    public void givenActivitiesFromGplusByKeyword1_whenExtractingUrlsFromContent_thenNoException() throws GeneralSecurityException, IOException {
        final List<Activity> searchResults = gplusService.search(TwitterTag.clojure.name());
        for (final Activity activity : searchResults) {
            System.out.println(LinkUtil.extractUrls(activity.getObject().getContent()));
        }
    }

    @Test
    public void givenActivitiesFromGplusByKeyword2_whenExtractingUrlsFromContent_thenResultsAreCorrect() throws GeneralSecurityException, IOException {
        final List<Activity> searchResults = gplusService.search(TwitterTag.facebook.name());
        for (final Activity activity : searchResults) {
            System.out.println(LinkUtil.extractUrls(activity.getObject().getContent()));
        }
    }

}
