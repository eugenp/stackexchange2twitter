package org.stackexchange.api.model;

import java.util.Date;

public class Question {

    /*
     "question_id": 14841260,
            "last_edit_date": 1360701401,
            "creation_date": 1360701076,
            "last_activity_date": 1360701681,
            "score": 0,
            "answer_count": 2,
            "title": "AsyncTask process",
            "tags": [
                "java",
                "android",
                "multithreading",
                "user-interface",
                "android-asynctask"
            ],
            "view_count": 16,
            "owner": {
                "user_id": 1204617,
                "display_name": "Mike",
                "reputation": 50,
                "user_type": "registered",
                "profile_image": "http://www.gravatar.com/avatar/419e0876a47cb109c149639f8ef3130f?d=identicon&r=PG",
                "link": "http://stackoverflow.com/users/1204617/mike",
                "accept_rate": 92
            },
            "link": "http://stackoverflow.com/questions/14841260/asynctask-process",
            "is_answered": false
     */

    private long questionId;
    private Date lastEditDate;
    private Date lastActivityDate;

}
