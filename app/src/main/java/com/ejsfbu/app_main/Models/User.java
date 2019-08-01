package com.ejsfbu.app_main.Models;

import com.ejsfbu.app_main.Activities.ParentActivity;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@ParseClassName("_User")
public class User extends ParseUser {

    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_BIRTHDAY = "birthday";
    public static final String KEY_BANK = "bankAccounts";
    public static final String KEY_IS_PARENT = "isParent";
    public static final String KEY_CHILDREN = "children";
    public static final String KEY_PARENTS = "parents";
    public static final String KEY_NEEDS_PARENT = "needsParent";
    public static final String KEY_REQUIRES_APPROVAL = "requireApproval";
    public static final String KEY_PROFILE_PIC = "profileImage";
    public static final String KEY_COMPLETED_BADGES = "completedBadges";
    public static final String KEY_IN_PROGRESS_BADGES = "inProgressBadges";
    public static final String KEY_TOTAL_SAVED = "totalSaved";
    public static final String KEY_COMPLETED_GOALS = "completedGoals";
    public static final String KEY_IN_PROGRESS_GOALS = "inProgressGoals";
    public static final String KEY_EARLY_GOALS = "earlyGoals";

    public String getName() {
        return getString(KEY_NAME);
    }

    public void setName(String name) {
        put(KEY_NAME, name);
    }

    public String getEmail() {
        return getString(KEY_EMAIL);
    }

    public void setEmail(String email) {
        put(KEY_EMAIL, email);
    }

    public String getUsername() {
        return getString(KEY_USERNAME);
    }

    public void setUsername(String username) {
        put(KEY_USERNAME, username);
    }

    public String getPassword() {
        return getString(KEY_PASSWORD);
    }

    public void setPassword(String password) {
        put(KEY_PASSWORD, password);
    }

    public String getBirthday() {
        return getDate(KEY_BIRTHDAY).toString();
    }

    public void setBirthday(Date date) {
        put(KEY_BIRTHDAY, date);
    }

    public Boolean getIsParent() {
        return getBoolean(KEY_IS_PARENT);
    }

    public void setIsParent(Boolean isParent) {
        put(KEY_IS_PARENT, isParent);
    }

    public boolean getNeedsParent() {
        return getBoolean(KEY_NEEDS_PARENT);
    }

    public void setNeedsParent(boolean needsParent) {
        put(KEY_NEEDS_PARENT, needsParent);
    }

    public boolean getRequiresApproval() {
        boolean requiresApproval = getBoolean(KEY_REQUIRES_APPROVAL);
        return requiresApproval;
    }

    public void setRequiresApproval(boolean requiresApproval) {
        put(KEY_REQUIRES_APPROVAL, requiresApproval);
    }

    public ParseFile getProfilePic() {
        return getParseFile(KEY_PROFILE_PIC);
    }

    public void setProfilePic(ParseFile image) {
        put(KEY_PROFILE_PIC, image);
    }

    public void addChild(User child) {
        addAllUnique(KEY_CHILDREN, Collections.singleton(child));
    }

    public JSONArray getChildren() {
        return getJSONArray(KEY_CHILDREN);
    }

    public void addParent(User parent) {
        addAllUnique(KEY_PARENTS, Collections.singleton(parent));
    }

    public JSONArray getParents() {
        return getJSONArray(KEY_PARENTS);
    }

    public List<BankAccount> getBanks() {
        return getList(KEY_BANK);
    }

    public List<BankAccount> getVerifiedBanks() {
        List<BankAccount> newList = new ArrayList<>();
        List<BankAccount> list = getList(KEY_BANK);
        if (list == null) {
            return null;
        }
        for (BankAccount bank : list) {
            if (bank.getVerified()) {
                newList.add(bank);
            }
        }
        return newList;
    }

    public void addBank(BankAccount bank) {
        addAllUnique(KEY_BANK, Collections.singleton(bank));
    }

    public void removeBank(BankAccount bank) {
        removeAll(KEY_BANK, Collections.singleton(bank));
    }

    public Double getTotalSaved() {
        return getDouble(KEY_TOTAL_SAVED);
    }

    public void setTotalSaved(Double totalSaved) {
        put(KEY_TOTAL_SAVED, totalSaved);
    }

    public void addCompletedBadge(Reward reward) {
        addAllUnique(KEY_COMPLETED_BADGES, Collections.singleton(reward));
    }

    public List<Reward> getCompletedBadges() {
        List<Reward> rewards =  getList(KEY_COMPLETED_BADGES);
        if (rewards == null) {
            rewards = new ArrayList<>();
        }
        return rewards;
    }

    public void removeCompletedBadge(Reward reward) {
        removeAll(KEY_COMPLETED_BADGES, Collections.singleton(reward));
    }

    public void addInProgressBadge(Reward reward) {
        addAllUnique(KEY_IN_PROGRESS_BADGES, Collections.singleton(reward));
    }

    public void addInProgressBadges(List<Reward> rewards) {
        addAllUnique(KEY_IN_PROGRESS_BADGES, rewards);
    }

    public boolean hasInProgressBadge(String badgeId) {
        List<Reward> inProgressBadges = getInProgressBadges();
        for (int i = 0; i < inProgressBadges.size(); i ++) {
            if (inProgressBadges.get(i).getObjectId().equals(badgeId)) {
                return true;
            }
        }
        return false;
    }

    public List<Reward> getInProgressBadges() {
        return getList(KEY_IN_PROGRESS_BADGES);
    }

    public void removeInProgressBadge(Reward reward) {
        removeAll(KEY_IN_PROGRESS_BADGES, Collections.singleton(reward));
    }

    public void addCompletedGoal(Goal goal) {
        removeAll(KEY_IN_PROGRESS_GOALS, Collections.singleton(goal));
        List<Goal> completedGoals = getCompletedGoals();
        removeAll(KEY_COMPLETED_GOALS, completedGoals);
        saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                completedGoals.add(0, goal);
                Collections.sort(completedGoals);
                addAllUnique(KEY_COMPLETED_GOALS, completedGoals);
            }
        });
    }

    public List<Goal> getCompletedGoals() {
        return getList(KEY_COMPLETED_GOALS);
    }

    public void addInProgressGoal(Goal goal) {
        addAllUnique(KEY_IN_PROGRESS_GOALS, Collections.singleton(goal));
    }

    public void removeInProgressGoal(Goal goal) {
        removeAll(KEY_IN_PROGRESS_GOALS, Collections.singleton(goal));
    }

    public List<Goal> getInProgressGoals() {
        return getList(KEY_IN_PROGRESS_GOALS);
    }

    public int getNumberGoalsCompleted() {
        List<Goal> goals = getCompletedGoals();
        if (goals == null) {
            return 0;
        } else {
            return goals.size();
        }
    }

    public boolean hasUpdatedGoals() {
        List<Goal> goals = getInProgressGoals();
        boolean hasUpdatedGoals = false;
        if (goals != null) {
            for (int i = 0; i < goals.size(); i++) {
                Goal goal = goals.get(i);
                if (goal.getUpdatesMade()) {
                    goal.setUpdatesMade(false);
                    goal.saveInBackground();
                    if (goal.getCompleted()) {
                        if (goal.getCompletedEarly()) {
                            setEarlyGoals(getEarlyGoals() + 1);
                        }
                        addCompletedGoal(goal);
                    }
                    List<Transaction> transactions = goal.getTransactions();
                    Double addAmount = 0.0;
                    for (int j = 0; j < transactions.size(); j ++) {
                        if (transactions.get(j).getRecentlyApproved()) {
                            transactions.get(i).setRecentlyApproved(false);
                            addAmount += transactions.get(j).getAmount();
                        }
                    }
                    setTotalSaved(getTotalSaved() + addAmount);
                    hasUpdatedGoals = true;
                }
            }
        }
        return hasUpdatedGoals;
    }

    public int getEarlyGoals() {
        return getInt(KEY_EARLY_GOALS);
    }

    public void setEarlyGoals(int earlyGoals) {
        put(KEY_EARLY_GOALS, earlyGoals);
    }

    public void setChildDefaults() {
        setIsParent(false);
        setTotalSaved(0.0);
        setEarlyGoals(0);
        addInProgressBadges(Reward.getLevel1Badges());
    }

    public static class Query extends ParseQuery<User> {
        public Query() {
            super(User.class);
        }

        public Query testUsername(String username) {
            whereEqualTo(KEY_USERNAME, username);
            return this;
        }

        public Query testEmail(String email) {
            whereEqualTo(KEY_EMAIL, email);
            return this;
        }
    }
}
