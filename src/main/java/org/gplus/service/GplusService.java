package org.gplus.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Activity;

@Service
public class GplusService {

    @Autowired
    private Plus plus;

    public GplusService() {
        super();
    }

    // API

    /** Get an activity for which we already know the ID. */
    public void getActivity() throws IOException {
        // A known public activity ID
        final String activityId = "z12gtjhq3qn2xxl2o224exwiqruvtda0i";
        // We do not need to be authenticated to fetch this activity

        View.header1("Get an explicit public activity by ID");
        final Activity activity = plus.activities().get(activityId).execute();
        View.show(activity);
    }

}
