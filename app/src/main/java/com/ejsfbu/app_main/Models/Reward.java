package com.ejsfbu.app_main.Models;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ParseClassName("Reward")
public class Reward extends ParseObject {

    public static final String KEY_NAME = "rewardName";
    public static final String KEY_BADGE_IMAGE = "badgeImage";
    public static final String KEY_DESCRIPTION = "rewardDescription";
    public static final String KEY_IN_PROGRESS = "inProgress";
    public static final String KEY_COMPLETED = "completed";
    public static final String KEY_CREATED_AT = "createdAt";
    public static final String KEY_GROUP = "group";
    public static final String KEY_IS_LEVEL_1 = "isLevel1";

    public String getName() {
        String name;
        try {
            name = fetchIfNeeded().getString(KEY_NAME);
        } catch (ParseException e) {
            e.printStackTrace();
            name = null;
        }
        return name;
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

    public static Reward checkEarnedTotalSavedBadge(User user, Double oldTotalSaved) {
        Double totalSaved = user.getTotalSaved();
        ArrayList<Reward> totalSavedBadges = getTotalSavedBadges();
        Reward earnedBadge;
        if (totalSaved >= 1000 && oldTotalSaved < 1000) {
            user.addCompletedBadge(totalSavedBadges.get(4));
            user.removeCompletedBadge(totalSavedBadges.get(3));
            user.removeInProgressBadge(totalSavedBadges.get(4));
            earnedBadge = totalSavedBadges.get(4);
        } else if (totalSaved >= 500 && oldTotalSaved < 500) {
            user.addCompletedBadge(totalSavedBadges.get(3));
            user.addInProgressBadge(totalSavedBadges.get(4));
            user.removeCompletedBadge(totalSavedBadges.get(2));
            user.removeInProgressBadge(totalSavedBadges.get(3));
            earnedBadge = totalSavedBadges.get(3);
        } else if (totalSaved >= 250 && oldTotalSaved < 250) {
            user.addCompletedBadge(totalSavedBadges.get(2));
            user.addInProgressBadge(totalSavedBadges.get(3));
            user.removeCompletedBadge(totalSavedBadges.get(1));
            user.removeInProgressBadge(totalSavedBadges.get(2));
            earnedBadge = totalSavedBadges.get(2);
        } else if (totalSaved >= 100 && oldTotalSaved < 100) {
            user.addCompletedBadge(totalSavedBadges.get(1));
            user.addInProgressBadge(totalSavedBadges.get(2));
            user.removeCompletedBadge(totalSavedBadges.get(0));
            user.removeInProgressBadge(totalSavedBadges.get(1));
            earnedBadge = totalSavedBadges.get(1);
        } else if (totalSaved >= 50 && oldTotalSaved < 50) {
            user.addCompletedBadge(totalSavedBadges.get(0));
            user.addInProgressBadge(totalSavedBadges.get(1));
            user.removeInProgressBadge(totalSavedBadges.get(0));
            earnedBadge = totalSavedBadges.get(0);
        } else {
            earnedBadge = null;
        }
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                }
            }
        });
        return earnedBadge;
    }

    public static ArrayList<Reward> getTotalSavedBadges() {
        ArrayList<Reward> badges = new ArrayList<>();
        Query query = new Query();
        query.getGroup("Total Saved");
        try {
            badges.addAll(query.find());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return badges;
    }

    public static ArrayList<Reward> getLevel1Badges() {
        ArrayList<Reward> badges = new ArrayList<>();
        Query query = new Query();
        query.getLevel1();
        try {
            badges.addAll(query.find());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return badges;
    }

    public static class Query extends ParseQuery<Reward> {

        public Query() {
            super(Reward.class);
        }

        public Query getGroup(String groupName) {
            whereEqualTo(KEY_GROUP, groupName);
            orderByAscending(KEY_NAME);
            return this;
        }

        public Query getLevel1() {
            whereEqualTo(KEY_IS_LEVEL_1, true);
            orderByAscending(KEY_NAME);
            return this;
        }
    }
}
