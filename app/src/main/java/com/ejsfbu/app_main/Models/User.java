package com.ejsfbu.app_main.Models;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.ejsfbu.app_main.Activities.MainActivity.subscribeTopic;

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
    public static final String KEY_CHILD_RECENTLY_UPDATED = "childRecentlyUpdated";
    public static final String KEY_RECENTLY_ADDED_PARENT = "recentlyAddedParent";
    public static final String KEY_HAS_ALLOWANCE = "hasAllowance";
    public static final String KEY_ALLOWANCE_AMOUNT = "allowance";
    public static final String KEY_ALLOWANCE_FREQUENCY = "allowanceFrequency";
    public static final String KEY_INVITER = "inviter";

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

    public Date getBirthday() {
        Date date;
        try {
            date = fetchIfNeeded().getDate(KEY_BIRTHDAY);
        } catch (ParseException e) {
            e.printStackTrace();
            date = null;
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
        subscribeTopic(child.getObjectId());
        addAllUnique(KEY_CHILDREN, Collections.singleton(child));
    }

    public List<User> getChildren() {
        List<User> children;
        try {
            children = fetchIfNeeded().getList(KEY_CHILDREN);
        } catch (ParseException e) {
            e.printStackTrace();
            children = new ArrayList<>();
        }
        return children;
    }

    public void removeChild(User child) {
        removeAll(KEY_CHILDREN, Collections.singleton(child));
    }

    public boolean getChildRecentlyUpdated() {
        boolean recentlyUpdated;
        try {
            recentlyUpdated = fetchIfNeeded().getBoolean(KEY_CHILD_RECENTLY_UPDATED);
        } catch (ParseException e) {
            e.printStackTrace();
            recentlyUpdated = false;
        }
        return recentlyUpdated;
    }

    public void setChildRecentlyUpdated(boolean recentlyUpdated) {
        put(KEY_CHILD_RECENTLY_UPDATED, recentlyUpdated);
    }

    public void addParent(User parent) {
        subscribeTopic(parent.getObjectId() + ParseUser.getCurrentUser().getObjectId());
        addAllUnique(KEY_PARENTS, Collections.singleton(parent));
    }

    public List<User> getParents() {
        List<User> parents;
        try {
            parents = fetchIfNeeded().getList(KEY_PARENTS);
        } catch (ParseException e) {
            e.printStackTrace();
            parents = new ArrayList<>();
        }
        return parents;
    }

    public void removeParent(User parent) {
        removeAll(KEY_PARENTS, Collections.singleton(parent));
    }

    public List<BankAccount> getBanks() {
        List<BankAccount> banks;
        try {
            banks = fetchIfNeeded().getList(KEY_BANK);
        } catch (ParseException e) {
            e.printStackTrace();
            banks = new ArrayList<>();
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
            saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    completedGoals.add(0, goal);
                    Collections.sort(completedGoals);
                    addAllUnique(KEY_COMPLETED_GOALS, completedGoals);
                }
            });
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
                        if ((goal.getCost() >= 25.00) && (goal.getCost() <= 50.00)) {
                            setMediumGoals(getMediumGoals() + 1);
                        }
                        if (goal.getCost() >= 100.00) {
                            setBigGoals(getBigGoals() + 1);
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

    public void checkChildrenCorrect() {
        Query query = new Query();
        query.whereContainedIn(KEY_PARENTS, Collections.singleton(this));
        query.findInBackground(new FindCallback<User>() {
            @Override
            public void done(List<User> queriedChildren, ParseException e) {
                if (e == null) {
                    List<User> listChildren = getChildren();
                    if (queriedChildren.size() > listChildren.size()) {
                        addNewChild(queriedChildren, listChildren);
                    }
                }
            }
        });
        setChildRecentlyUpdated(false);
    }

    public void addNewChild(List<User> queriedChildren, List<User> listChildren) {
        for (int i = 0; i < queriedChildren.size(); i++) {
            String queriedChildId = queriedChildren.get(i).getObjectId();
            boolean found = false;
            for (int j = 0; j < listChildren.size(); j++) {
                String listChildId = listChildren.get(j).getObjectId();
                if (queriedChildId.equals(listChildId)) {
                    found = true;
                }
            }
            if (!found) {
                User child = queriedChildren.get(i);
                addChild(child);

                /*ParseACL parseACL = new ParseACL();
                parseACL.setReadAccess(child.getObjectId(), true);
                setACL(parseACL);*/
            }
        }
    }

    public void addNewParent(List<User> queriedParents, List<User> listParents) {
        for (int i = 0; i < queriedParents.size(); i++) {
            String queriedParentId = queriedParents.get(i).getObjectId();
            boolean found = false;
            for (int j = 0; j < listParents.size(); j++) {
                String listParentId = listParents.get(j).getObjectId();
                if (queriedParentId.equals(listParentId)) {
                    found = true;
                }
            }
            if (!found) {
                User parent = queriedParents.get(i);
                addParent(parent);

                /*ParseACL parseACL = new ParseACL();
                parseACL.setReadAccess(parent.getObjectId(), true);
                parseACL.setWriteAccess(parent.getObjectId(), true);
                setACL(parseACL);*/
            }
        }
    }

    public void unlinkParent(List<User> queriedParents, List<User> listParents) {
        for (int i = 0; i < listParents.size(); i++) {
            String listParentId = listParents.get(i).getObjectId();
            boolean found = false;
            for (int j = 0; j < queriedParents.size(); i++) {
                String queriedParentId = queriedParents.get(j).getObjectId();
                if (listParentId.equals(queriedParentId)) {
                    found = true;
                }
            }
            if (!found) {
                User parent = listParents.get(i);
                removeParent(parent);

                /*ParseACL parseACL = new ParseACL();
                parseACL.setReadAccess(parent.getObjectId(), false);
                parseACL.setWriteAccess(parent.getObjectId(), false);
                setACL(parseACL);*/
            }
        }
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

    public void setRecentlyAddedParent(boolean recentlyAddedParent) {
        put(KEY_RECENTLY_ADDED_PARENT, recentlyAddedParent);
    }

    public boolean getRecentlyAddedParent() {
        boolean recentlyAddedParent;
        try {
            recentlyAddedParent = fetchIfNeeded().getBoolean(KEY_RECENTLY_ADDED_PARENT);
        } catch (ParseException e) {
            e.printStackTrace();
            recentlyAddedParent = false;
        }
        return recentlyAddedParent;
    }

    public void setChildDefaults() {
        setIsParent(false);
        setTotalSaved(0.0);
        setEarlyGoals(0);
        setSmallGoals(0);
        setMediumGoals(0);
        setBigGoals(0);
        addInProgressBadges(Reward.getLevel1Badges());
        put(KEY_CLAIMED_REWARDS, new ArrayList<>());
        put(KEY_COMPLETED_BADGES, new ArrayList<>());
        put(KEY_COMPLETED_GOALS, new ArrayList<>());
        put(KEY_BANK, new ArrayList<>());
        put(KEY_PARENTS, new ArrayList<>());
        put(KEY_RECENTLY_ADDED_PARENT, false);
    }

    public void setParentDefaults() {
        setIsParent(true);
        put(KEY_BANK, new ArrayList<>());
        put(KEY_CHILDREN, new ArrayList<>());
        put(KEY_CHILD_RECENTLY_UPDATED, false);
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

    public void setMediumGoals(Integer amount) {
        put(KEY_MEDIUM_GOALS, amount);
    }

    public int getBigGoals() {
        return getInt(KEY_BIG_GOALS);
    }

    public void setBigGoals(Integer amount) {
        put(KEY_BIG_GOALS, amount);
    }

    public Boolean getHasAllowance() {
        boolean hasAllowance;
        try {
            hasAllowance = fetchIfNeeded().getBoolean(KEY_HAS_ALLOWANCE);
        } catch (ParseException e) {
            e.printStackTrace();
            hasAllowance = false;
        }
        return hasAllowance;
    }

    public void setHasAllowance(Boolean hasAllowance) {
        put(KEY_HAS_ALLOWANCE, hasAllowance);
    }

    public Double getAllowanceAmount() {
        return getDouble(KEY_ALLOWANCE_AMOUNT);
    }

    public void setAllowanceAmount(Double allowanceAmount) {
        put(KEY_ALLOWANCE_AMOUNT, allowanceAmount);
    }

    public String getAllowanceFrequency() {
        return getString(KEY_ALLOWANCE_FREQUENCY);
    }

    public void setAllowanceFrequency(String frequency) {
        put(KEY_ALLOWANCE_FREQUENCY, frequency);

    }

    public User getInviter() {
        User inviter;
        try {
            inviter = (User) fetchIfNeeded().getParseUser(KEY_INVITER);
        } catch (ParseException e) {
            e.printStackTrace();
            inviter = null;
        }
        return inviter;
    }

    public void setInviter(User inviter) {
        put(KEY_INVITER, inviter);
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
