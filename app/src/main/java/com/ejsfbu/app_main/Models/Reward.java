package com.ejsfbu.app_main.Models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Date;

@ParseClassName("Reward")
public class Reward extends ParseObject {

    public static final String KEY_NAME = "rewardName";
    public static final String KEY_BADGE_IMAGE = "badgeImage";
    public static final String KEY_DESCRIPTION = "rewardDescription";
    public static final String KEY_IN_PROGRESS = "inProgress";
    public static final String KEY_COMPLETED = "completed";
    public static final String KEY_CREATED_AT = "createdAt";

    public String getName() {
        return getString(KEY_NAME);
    }

    public ParseFile getBadgeImage() {
        return getParseFile(KEY_BADGE_IMAGE);
    }

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public boolean getInProgress() {
        return getBoolean(KEY_IN_PROGRESS);
    }

    public void setInProgress(boolean inProgress) {
        put(KEY_IN_PROGRESS, inProgress);
    }

    public boolean getCompleted() {
        return getBoolean(KEY_COMPLETED);
    }

    public void setCompleted(boolean completed) {
        put(KEY_COMPLETED, completed);
    }

    public static class Query extends ParseQuery<Reward> {

        public Query() {
            super(Reward.class);
        }

        public Query getTopCompleted() {
            setLimit(20);
            orderByAscending(KEY_NAME);
            return this;
        }

        public Query getTopInProgress() {
            setLimit(20);
            orderByAscending(KEY_NAME);
            return this;
        }

        public Query areCompleted() {
            whereEqualTo(KEY_COMPLETED, true);
            return this;
        }

        public Query areInProgress() {
            whereEqualTo(KEY_IN_PROGRESS, true);
            return this;
        }
    }
}
