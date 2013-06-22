package org.gplus.service;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.services.plus.Plus;
import com.google.api.services.plus.Plus.Activities.Search;
import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.ActivityFeed;

@Service
public class GplusService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Plus plus;

    public GplusService() {
        super();
    }

    // API

    public final Activity getSingleActivity(final String activityId) throws IOException {
        return plus.activities().get(activityId).execute();
    }

    /**
     * https://developers.google.com/+/api/latest/activities/search
     * https://developers.google.com/+/api/latest/activities
     */
    public final List<Activity> search(final String query) throws IOException {
        final ActivityFeed activityFeed = searchAsFeed(query);
        final List<Activity> activities = activityFeed.getItems();
        Collections.sort(activities, new Comparator<Activity>() {
            @Override
            public final int compare(final Activity a1, final Activity a2) {
                final long reshares1 = a1.getObject().getResharers().getTotalItems();
                final long reshares2 = a2.getObject().getResharers().getTotalItems();
                final long votes1 = a1.getObject().getPlusoners().getTotalItems();
                final long votes2 = a2.getObject().getPlusoners().getTotalItems();
                final Long total1 = reshares1 + votes1;
                final Long total2 = reshares2 + votes2;
                return total2.compareTo(total1);
            }
        });
        return activities;
    }

    final ActivityFeed searchAsFeed(final String query) throws IOException {
        final Search searchQuery = plus.activities().search(query);
        searchQuery.setMaxResults(20l).setOrderBy("best").setLanguage("en-US");
        final ActivityFeed activityFeed = searchQuery.execute();
        return activityFeed;
    }

}
