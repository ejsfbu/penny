package com.ejsfbu.app_main.Models;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collection;
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
    public static final String KEY_CLAIMED_REWARDS = "claimedRewards";
    public static final String KEY_EARLY_GOALS = "earlyGoals";
    public static final String KEY_SMALL_GOALS = "smallGoals";
    public static final String KEY_MEDIUM_GOALS = "mediumGoals";
    public static final String KEY_BIG_GOALS = "bigGoals";


    public String getName() {
        String name;
        try {
            name = fetchIfNeeded().getString(KEY_NAME);
        } catch (ParseException e) {
            e.printStackTrace();
            name = "Error";
        }
        return name;
    }

    public void setName(String name) {
        put(KEY_NAME, name);
    }

    public String getEmail() {
        String email = "";
        try {
            email = fetchIfNeeded().getString(KEY_EMAIL);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return email;
    }

    public void setEmail(String email) {
        put(KEY_EMAIL, email);
    }

    public String getUsername() {
        String user = "";
        try {
            user = fetchIfNeeded().getString(KEY_USERNAME);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return user;
    }

    public void setUsername(String username) {
        put(KEY_USERNAME, username);
    }

    public String getPassword() {
        String pass = "";
        try {
            pass = fetchIfNeeded().getString(KEY_PASSWORD);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return pass;
    }

    public void setPassword(String password) {
        put(KEY_PASSWORD, password);
    }

    public String getBirthday() {
        String date = "";
        try {
            date = fetchIfNeeded().getDate(KEY_BIRTHDAY).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public void setBirthday(Date date) {
        put(KEY_BIRTHDAY, date);
    }

    public Boolean getIsParent() {
        boolean bool = false;
        try {
            bool = fetchIfNeeded().getBoolean(KEY_IS_PARENT);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return bool;
    }

    public void setIsParent(Boolean isParent) {
        put(KEY_IS_PARENT, isParent);
    }

    public boolean getNeedsParent() {
        boolean bool = false;
        try {
            bool = fetchIfNeeded().getBoolean(KEY_NEEDS_PARENT);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return bool;
    }

    public void setNeedsParent(boolean needsParent) {
        put(KEY_NEEDS_PARENT, needsParent);
    }

    public boolean getRequiresApproval() {
        boolean bool = false;
        try {
            bool = fetchIfNeeded().getBoolean(KEY_REQUIRES_APPROVAL);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return bool;
    }

    public void setRequiresApproval(boolean requiresApproval) {
        put(KEY_REQUIRES_APPROVAL, requiresApproval);
    }

    public ParseFile getProfilePic() {
        ParseFile image;
        try {
            image = fetchIfNeeded().getParseFile(KEY_PROFILE_PIC);
        } catch (ParseException e) {
            e.printStackTrace();
            image = null;
        }
        return image;
    }

    public void setProfilePic(ParseFile image) {
        put(KEY_PROFILE_PIC, image);
    }

    public void addChild(User child) {
        addAllUnique(KEY_CHILDREN, Collections.singleton(child));
    }

    public List<User> getChildren() {
        return getList(KEY_CHILDREN);
    }

    public void addParent(User parent) {
        addAllUnique(KEY_PARENTS, Collections.singleton(parent));
    }

    public JSONArray getParents() {
        JSONArray parents;
        try {
            parents = fetchIfNeeded().getJSONArray(KEY_PARENTS);
        } catch (ParseException e) {
            e.printStackTrace();
            parents = null;
        }
        return parents;
    }

    public List<BankAccount> getBanks() {
        List<BankAccount> banks;
        try {
            banks = fetchIfNeeded().getList(KEY_BANK);
        } catch (ParseException e) {
            e.printStackTrace();
            banks = new ArrayList<>();
            ;
        }
        return banks;
    }

    public List<BankAccount> getVerifiedBanks() {
        List<BankAccount> newList = new ArrayList<>();
        List<BankAccount> list = getBanks();
        if (list == null) {
            return new ArrayList<>();
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
        Double amount = 0.0;
        try {
            amount = fetchIfNeeded().getDouble(KEY_TOTAL_SAVED);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return amount;
    }

    public void setTotalSaved(Double totalSaved) {
        put(KEY_TOTAL_SAVED, totalSaved);
    }

    public void addCompletedBadge(Reward reward) {
        addAllUnique(KEY_COMPLETED_BADGES, Collections.singleton(reward));
    }

    public List<Reward> getCompletedBadges() {
        List<Reward> rewards;
        try {
            rewards = fetchIfNeeded().getList(KEY_COMPLETED_BADGES);
        } catch (ParseException e) {
            e.printStackTrace();
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
        for (int i = 0; i < inProgressBadges.size(); i++) {
            if (inProgressBadges.get(i).getObjectId().equals(badgeId)) {
                return true;
            }
        }
        return false;
    }

    public List<Reward> getInProgressBadges() {
        List<Reward> rewards;
        try {
            rewards = fetchIfNeeded().getList(KEY_IN_PROGRESS_BADGES);
        } catch (ParseException e) {
            e.printStackTrace();
            rewards = new ArrayList<>();
        }
        return rewards;
    }

    public void removeInProgressBadge(Reward reward) {
        removeAll(KEY_IN_PROGRESS_BADGES, Collections.singleton(reward));
    }

    public void addCompletedGoal(Goal goal) {
        removeAll(KEY_IN_PROGRESS_GOALS, Collections.singleton(goal));
        final List<Goal> completedGoals = getCompletedGoals();
        if (completedGoals != null) {
            removeAll(KEY_COMPLETED_GOALS, completedGoals);
            try {
                save();
                completedGoals.add(0, goal);
                Collections.sort(completedGoals);
                addAllUnique(KEY_COMPLETED_GOALS, completedGoals);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            addAll(KEY_COMPLETED_GOALS, Collections.singleton(goal));
        }
    }

    public List<Goal> getCompletedGoals() {
        List<Goal> goals;
        try {
            goals = fetchIfNeeded().getList(KEY_COMPLETED_GOALS);
        } catch (ParseException e) {
            e.printStackTrace();
            goals = new ArrayList<>();
        }
        return goals;
    }

    public void addInProgressGoal(Goal goal) {
        addAllUnique(KEY_IN_PROGRESS_GOALS, Collections.singleton(goal));
    }

    public void removeInProgressGoal(Goal goal) {
        removeAll(KEY_IN_PROGRESS_GOALS, Collections.singleton(goal));
    }

    public List<Goal> getInProgressGoals() {
        List<Goal> goals;
        try {
            goals = fetchIfNeeded().getList(KEY_IN_PROGRESS_GOALS);
        } catch (ParseException e) {
            e.printStackTrace();
            goals = new ArrayList<>();
        }
        return goals;
    }

    public List<Reward> getClaimedRewards() {
        List<Reward> rewards = new ArrayList<>();
        try {
            rewards = fetchIfNeeded().getList(KEY_CLAIMED_REWARDS);
            if (rewards == null) {
                rewards = new ArrayList<>();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return rewards;
    }

    public void claimReward(Reward reward) {
        addAllUnique(KEY_CLAIMED_REWARDS, Collections.singleton(reward));
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
                        if (goal.getCost() <= 10.00) {
                            setSmallGoals(getSmallGoals() + 1);
                        }
                        if ((goal.getCost() >= 20.00) && (goal.getCost() <= 40.00) ) {
                            setMediumGoals(getMediumGoals() + 1);
                        }
                        if (goal.getCost() >= 100.00) {
                            setBigGoals(getBigGoals()+ 1);
                        }
                        addCompletedGoal(goal);
                    }
                    List<Transaction> transactions = goal.getTransactions();
                    Double addAmount = 0.0;
                    for (int j = 0; j < transactions.size(); j++) {
                        Transaction transaction = transactions.get(j);
                        if (transaction.getRecentlyApproved()) {
                            transaction.setRecentlyApproved(false);
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
        int num;
        try {
            num = fetchIfNeeded().getInt(KEY_EARLY_GOALS);
        } catch (ParseException e) {
            e.printStackTrace();
            num = 0;
        }
        return num;
    }

    public void setEarlyGoals(int earlyGoals) {
        put(KEY_EARLY_GOALS, earlyGoals);
    }

    public void setChildDefaults() {
        setIsParent(false);
        setTotalSaved(0.0);
        setEarlyGoals(0);
        // setSmallGoals(0);
        // setMediumGoals(0);
        // setLargeGoals(0);
        addInProgressBadges(Reward.getLevel1Badges());
        put(KEY_CLAIMED_REWARDS, new ArrayList<>());
        put(KEY_COMPLETED_BADGES, new ArrayList<>());
        put(KEY_COMPLETED_GOALS, new ArrayList<>());
        put(KEY_BANK, new ArrayList<>());
        put(KEY_PARENTS, new ArrayList<>());
    }

    public void setParentDefaults() {
        setIsParent(true);
        put(KEY_BANK, new ArrayList<>());
        put(KEY_CHILDREN, new ArrayList<>());
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

    public int getSmallGoals() {
        return getInt(KEY_SMALL_GOALS);
    }

    public void setSmallGoals(Integer amount) {
        put(KEY_SMALL_GOALS, amount);
    }

    public int getMediumGoals() {
        return getInt(KEY_MEDIUM_GOALS);
    }

    public void setMediumGoals(Integer amount){
        put(KEY_MEDIUM_GOALS, amount);
    }

    public int getBigGoals() {
        return getInt(KEY_BIG_GOALS);
    }

    public void setBigGoals(Integer amount){
        put(KEY_BIG_GOALS, amount);
    }

}
