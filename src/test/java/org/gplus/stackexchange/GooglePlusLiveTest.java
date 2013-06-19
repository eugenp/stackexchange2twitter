package org.gplus.stackexchange;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import org.gplus.service.ActivityHelper;
import org.gplus.service.GplusService;
import org.gplus.spring.GplusContextConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.api.services.plus.model.Activity;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { GplusContextConfig.class })
public class GooglePlusLiveTest {

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
        assertThat(gplusService.search("clojure"), not(empty()));
    }

    @Test
    public void whenSearchingForActivities_thenResultsAreCorrect() throws GeneralSecurityException, IOException {
        final List<Activity> searchResults = gplusService.search("clojure");
        for (final Activity activity : searchResults) {
            ActivityHelper.show(activity);
        }
    }

}
