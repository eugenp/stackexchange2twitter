package org.gplus.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.ActivityFeed;

@Service
public class GplusService {

    @Autowired
    private Plus plus;

    public GplusService() {
        super();
    }

    // API

    public final Activity getSingleActivity(final String activityId) throws IOException {
        return plus.activities().get(activityId).execute();
    }

    public final List<Activity> search(final String query) throws IOException {
        final ActivityFeed activityFeed = plus.activities().search(query).execute();
        return activityFeed.getItems();
    }

}
