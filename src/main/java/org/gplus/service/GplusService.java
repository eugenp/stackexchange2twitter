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

    public void getSingleActivity(final String activityId) throws IOException {
        final Activity activity = plus.activities().get(activityId).execute();
        ActivityHelper.show(activity);
    }

}
