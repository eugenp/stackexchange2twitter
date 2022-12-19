package org.stackexchange.api.client;

import org.stackexchange.api.constants.ApiConstants.Questions;
import org.stackexchange.api.constants.StackSite;

public class ApiUris {
    private static final String API_2_1 = "https://api.stackexchange.com/2.1";

    private ApiUris() {
        throw new AssertionError();
    }

    // API

    public static String getQuestionsUri(final int min, final StackSite site) {
        return getQuestionsUri(min, site, 1);
    }

    public static String getQuestionsUri(final int min, final StackSite site, final int page) {
        return getMultipleUri(min, site, "/questions", page);
    }

    public static String getSingleQuestionUri(final StackSite site, final long id) {
        final String operation = "/questions/" + id;
        return getSingleUri(site, operation);
    }

    public static String getTagUri(final int min, final StackSite site, final String tag) {
        return getTagUri(min, site, tag, 1);
    }

    public static String getTagUri(final int min, final StackSite site, final String tag, final int page) {
        return getMultipleUri(min, site, "/tags/" + tag + "/faq", page);
    }

    // util

    static String getMultipleUri(final int min, final StackSite site, final String operation, final int page) {
        final String params = new RequestBuilder().add(Questions.order, "desc").add(Questions.sort, "votes").add(Questions.min, min).add(Questions.site, site).add(Questions.page, page).build();
        return API_2_1 + operation + params;
    }

    static String getSingleUri(final StackSite site, final String operation) {
        final String params = new RequestBuilder().add(Questions.site, site).build();
        return API_2_1 + operation + params;
    }

}
