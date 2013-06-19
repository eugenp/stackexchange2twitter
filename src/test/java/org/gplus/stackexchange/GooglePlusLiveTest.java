package org.gplus.stackexchange;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.gplus.service.GplusService;
import org.gplus.spring.GplusContextConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { GplusContextConfig.class })
public class GooglePlusLiveTest {

    @Autowired
    private GplusService gplusService;

    @Test
    public void whenConsumingTheGoogleApi_thenNoExceptions() throws GeneralSecurityException, IOException {
        // A known public activity ID
        final String activityId = "z12gtjhq3qn2xxl2o224exwiqruvtda0i";

        gplusService.getSingleActivity(activityId);
    }

}
