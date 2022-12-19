/*
 * Copyright (c) 2011 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.gplus.service;

import com.google.api.services.plus.model.Activity;

public class ActivityHelper {

    public static final void show(final Activity activity) {
        System.out.println("id: " + activity.getId());
        System.out.println("url: " + activity.getUrl());
        System.out.println("content: " + activity.getObject().getContent());

        System.out.println("Reshares: " + activity.getObject().getResharers().getTotalItems());
        System.out.println("Plusoners: " + activity.getObject().getPlusoners().getTotalItems());
        System.out.println("Replies: " + activity.getObject().getReplies().getTotalItems());

        System.out.println("--------------------------------------------------------------");
    }

    public static final String toString(final Activity activity) {
        final StringBuilder toString = new StringBuilder("\n");
        toString.append("--------------------------------------------------------------").append("\n");

        toString.append("id: " + activity.getId()).append("\n");
        toString.append("url: " + activity.getUrl()).append("\n");
        toString.append("content: " + activity.getObject().getContent()).append("\n");

        toString.append("Reshares: " + activity.getObject().getResharers().getTotalItems()).append("\n");
        toString.append("Plusoners: " + activity.getObject().getPlusoners().getTotalItems()).append("\n");
        toString.append("Replies: " + activity.getObject().getReplies().getTotalItems()).append("\n");

        toString.append("--------------------------------------------------------------").append("\n");

        return toString.toString();
    }

}
