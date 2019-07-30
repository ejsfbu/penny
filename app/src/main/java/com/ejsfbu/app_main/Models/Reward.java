package com.ejsfbu.app_main.Models;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

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

    public static void checkMakingMoves(User user, Goal goal) {
        if (goal.getCompleted() && (goal.getCost() <= 10.00)) {
            user.setMakingMoves(user.getMakingMoves() + 1);
            user.saveInBackground();
            //when the user gets their first Making Money Moves badge (1st)
            if (user.getMakingMoves() == 1) {
                Reward.Query makingMoves = new Reward.Query();
                makingMoves.whereEqualTo("rewardName", "Making Money Moves: Level 1");
                makingMoves.findInBackground(new FindCallback<Reward>() {
                    @Override
                    public void done(List<Reward> objects, ParseException e) {
                        user.addCompletedBadge(objects.get(0));
                    }
                });

                Reward.Query makingMovesIP = new Reward.Query();
                makingMovesIP.whereEqualTo("rewardName", "Making Money Moves: Level 2");
                makingMovesIP.findInBackground(new FindCallback<Reward>() {
                    @Override
                    public void done(List<Reward> objects, ParseException e) {
                        user.addInProgressBadge(objects.get(0));
                    }
                });
            }

            //when the user gets their second Making Money Moves badge (5th)
            if (user.getMakingMoves() == 5) {
                Reward.Query makingMoves = new Reward.Query();
                makingMoves.whereEqualTo("rewardName", "Making Money Moves: Level 2");
                makingMoves.findInBackground(new FindCallback<Reward>() {
                    @Override
                    public void done(List<Reward> objects, ParseException e) {
                        user.addCompletedBadge(objects.get(0));
                    }
                });

                Reward.Query makingMovesIP = new Reward.Query();
                makingMovesIP.whereEqualTo("rewardName", "Making Money Moves: Level 3");
                makingMovesIP.findInBackground(new FindCallback<Reward>() {
                    @Override
                    public void done(List<Reward> objects, ParseException e) {
                        user.addInProgressBadge(objects.get(0));
                    }
                });
            }

            //when the user gets their third Making Money Moves badge (10th)
            if (user.getMakingMoves() == 10) {
                Reward.Query makingMoves = new Reward.Query();
                makingMoves.whereEqualTo("rewardName", "Making Money Moves: Level 3");
                makingMoves.findInBackground(new FindCallback<Reward>() {
                    @Override
                    public void done(List<Reward> objects, ParseException e) {
                        user.addCompletedBadge(objects.get(0));
                    }
                });

                Reward.Query makingMovesIP = new Reward.Query();
                makingMovesIP.whereEqualTo("rewardName", "Making Money Moves: Level 4");
                makingMovesIP.findInBackground(new FindCallback<Reward>() {
                    @Override
                    public void done(List<Reward> objects, ParseException e) {
                        user.addInProgressBadge(objects.get(0));
                    }
                });
            }

            //when the user gets their third Making Money Moves badge (15th)
            if (user.getMakingMoves() == 15) {
                Reward.Query makingMoves = new Reward.Query();
                makingMoves.whereEqualTo("rewardName", "Making Money Moves: Level 4");
                makingMoves.findInBackground(new FindCallback<Reward>() {
                    @Override
                    public void done(List<Reward> objects, ParseException e) {
                        user.addCompletedBadge(objects.get(0));
                    }
                });

                Reward.Query makingMovesIP = new Reward.Query();
                makingMovesIP.whereEqualTo("rewardName", "Making Money Moves: Level 5");
                makingMovesIP.findInBackground(new FindCallback<Reward>() {
                    @Override
                    public void done(List<Reward> objects, ParseException e) {
                        user.addInProgressBadge(objects.get(0));
                    }
                });
            }

            //when the user gets their third Making Money Moves badge (20th)
            if (user.getMakingMoves() == 20) {
                Reward.Query makingMoves = new Reward.Query();
                makingMoves.whereEqualTo("rewardName", "Making Money Moves: Level 5");
                makingMoves.findInBackground(new FindCallback<Reward>() {
                    @Override
                    public void done(List<Reward> objects, ParseException e) {
                        user.addCompletedBadge(objects.get(0));
                    }
                });
            }
            user.saveInBackground();
        }
    }
}
