package com.ejsfbu.app_main.Models;

import com.ejsfbu.app_main.R;
import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
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

    // check to see if they have completed enough goals for a reward.
    public static Reward checkCompletedGoals(User user) {
        int numberCompleted = user.getNumberGoalsCompleted();
        List<Reward> rewards = getGoalGroupBadges();
        Reward earnedReward;
        if (numberCompleted == 1) {
            user.addInProgressBadge(rewards.get(1));
            user.addCompletedBadge(rewards.get(0));
            user.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        user.removeInProgressBadge(rewards.get(0));
                    } else {
                        e.printStackTrace();
                    }
                }
            });
            earnedReward = rewards.get(0);
        } else if (numberCompleted == 5) {
            user.addCompletedBadge(rewards.get(1));
            user.addInProgressBadge(rewards.get(2));
            user.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        user.removeCompletedBadge(rewards.get(0));
                        user.removeInProgressBadge(rewards.get(1));
                    } else {
                        e.printStackTrace();
                    }
                }
            });
            earnedReward = rewards.get(1);
        } else if (numberCompleted == 10) {
            user.addCompletedBadge(rewards.get(2));
            user.addInProgressBadge(rewards.get(3));
            user.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        user.removeCompletedBadge(rewards.get(1));
                        user.removeInProgressBadge(rewards.get(2));
                    } else {
                        e.printStackTrace();
                    }
                }
            });
            earnedReward = rewards.get(2);
        } else if (numberCompleted == 25) {
            user.addCompletedBadge(rewards.get(3));
            user.addInProgressBadge(rewards.get(4));
            user.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        user.removeCompletedBadge(rewards.get(2));
                        user.removeInProgressBadge(rewards.get(3));
                    } else {
                        e.printStackTrace();
                    }
                }
            });
            earnedReward = rewards.get(3);
        } else if (numberCompleted == 50) {
            user.addCompletedBadge(rewards.get(4));
            user.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        user.removeCompletedBadge(rewards.get(3));
                        user.removeInProgressBadge(rewards.get(4));
                    } else {
                        e.printStackTrace();
                    }
                }
            });
            earnedReward = rewards.get(4);
        } else {
            earnedReward = null;
        }
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                }
            }
        });
        return earnedReward;
    }

    public static List<Reward> getGoalGroupBadges() {
        List<Reward> badges = new ArrayList<>();
        final Reward.Query query = new Reward.Query();
        query.getGroup("Goals Completed");
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
}
