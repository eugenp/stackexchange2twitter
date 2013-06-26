package org.gplus.stackexchange;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import org.common.spring.CommonContextConfig;
import org.common.text.TextUtils;
import org.gplus.service.ActivityHelper;
import org.gplus.service.GplusService;
import org.gplus.spring.GplusContextConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stackexchange.util.Tag;

import com.google.api.services.plus.model.Activity;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CommonContextConfig.class, GplusContextConfig.class })
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
        assertThat(gplusService.search(Tag.clojure.name()), not(empty()));
    }

    @Test
    public void whenSearchingForActivitiesScenario1_thenResultsAreCorrect() throws GeneralSecurityException, IOException {
        final List<Activity> searchResults = gplusService.search(Tag.clojure.name());
        for (final Activity activity : searchResults) {
            ActivityHelper.show(activity);
        }
    }

    @Test
    public void whenSearchingForActivitiesScenario2_thenResultsAreCorrect() throws GeneralSecurityException, IOException {
        final List<Activity> searchResults = gplusService.search(Tag.jquery.name());
        for (final Activity activity : searchResults) {
            ActivityHelper.show(activity);
        }
    }

    @Test
    public void givenActivitiesFromGplus_whenExtractingUrlsFromContent_thenResultsAreCorrect() throws GeneralSecurityException, IOException {
        final List<Activity> searchResults = gplusService.search(Tag.clojure.name());
        for (final Activity activity : searchResults) {
            System.out.println(TextUtils.extractUrls(activity.getObject().getContent()));
        }
    }

}
