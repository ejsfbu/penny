package com.ejsfbu.app_main.Models;

import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Collections;
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

        public Query getGroup(String groupName) {
            whereEqualTo(KEY_GROUP, groupName);
            orderByAscending(KEY_NAME);
            return this;
        }
    }

    public static Reward checkSmallGoals(User user, Goal goal) {
        ArrayList<Reward> smallGoalBadges = new Reward().getSmallGoalsBadges();
        Reward earnedBadge = null;
        if (goal.getCompleted() && (goal.getCost() <= 10.00)) {
            user.setSmallGoals(user.getSmallGoals() + 1);

            //when the user gets their first Making Money Moves badge (1st)
            if (user.getSmallGoals() == 1) {
                user.addCompletedBadge(smallGoalBadges.get(0));
                user.addInProgressBadge(smallGoalBadges.get(1));
                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            user.removeInProgressBadge(smallGoalBadges.get(0));
                        } else {
                            e.printStackTrace();
                        }
                    }
                });
                earnedBadge = smallGoalBadges.get(0);
            }

            //when the user gets their second Making Money Moves badge (5th)
            if (user.getSmallGoals() == 5) {
                user.addCompletedBadge(smallGoalBadges.get(1));
                user.addInProgressBadge(smallGoalBadges.get(2));
                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            user.removeInProgressBadge(smallGoalBadges.get(1));
                        } else {
                            e.printStackTrace();
                        }
                    }
                });
                earnedBadge = smallGoalBadges.get(1);
            }

            //when the user gets their third Making Money Moves badge (10th)
            if (user.getSmallGoals() == 10) {
                user.addCompletedBadge(smallGoalBadges.get(2));
                user.addInProgressBadge(smallGoalBadges.get(3));
                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            user.removeInProgressBadge(smallGoalBadges.get(2));
                        } else {
                            e.printStackTrace();
                        }
                    }
                });
                earnedBadge = smallGoalBadges.get(2);
            }

            //when the user gets their third Making Money Moves badge (15th)
            if (user.getSmallGoals() == 15) {
                user.addCompletedBadge(smallGoalBadges.get(3));
                user.addInProgressBadge(smallGoalBadges.get(4));
                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            user.removeInProgressBadge(smallGoalBadges.get(3));
                        } else {
                            e.printStackTrace();
                        }
                    }
                });
                earnedBadge = smallGoalBadges.get(3);
            }

            //when the user gets their third Making Money Moves badge (20th)
            if (user.getSmallGoals() == 20) {
                user.addCompletedBadge(smallGoalBadges.get(4));
                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            user.removeInProgressBadge(smallGoalBadges.get(4));
                        } else {
                            e.printStackTrace();
                        }
                    }
                });
                earnedBadge = smallGoalBadges.get(4);
            }
        }
        return earnedBadge;
    }

    public ArrayList<Reward> getSmallGoalsBadges(){
        ArrayList<Reward> badges = new ArrayList<>();
        Reward.Query query = new Reward.Query();
        query.getGroup("Small Goals");
        try {
            badges.addAll(query.find());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return badges;
    }

    public ArrayList<Reward> getMediumGoalsBadges(){
        ArrayList<Reward> badges = new ArrayList<>();
        Reward.Query query = new Reward.Query();
        query.getGroup("Medium Goals");
        try {
            badges.addAll(query.find());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return badges;
    }

    public ArrayList<Reward> getBigGoalsBadges(){
        ArrayList<Reward> badges = new ArrayList<>();
        Reward.Query query = new Reward.Query();
        query.getGroup("Big Goals");
        try {
            badges.addAll(query.find());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return badges;
    }

}
