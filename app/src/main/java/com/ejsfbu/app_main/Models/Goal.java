package com.ejsfbu.app_main.Models;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@ParseClassName("Goal")
public class Goal extends ParseObject implements Comparable<Goal> {
    // Parse column names
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";
    public static final String KEY_CREATED_AT = "createdAt";
    public static final String KEY_NAME = "goalName";
    public static final String KEY_SAVED = "amountSaved";
    public static final String KEY_COST = "totalCost";
    public static final String KEY_END_DATE = "endDate";
    public static final String KEY_COMPLETED = "completed";
    public static final String KEY_DATE_COMPLETED = "dateCompleted";
    public static final String KEY_TRANSACTIONS = "transactions";
    public static final String KEY_UPDATES_MADE = "updatesMade";
    public static final String KEY_COMPLETED_EARLY = "completedEarly";
    public static final String KEY_DAILY_SAVING = "dailySaving";
    public static final String KEY_PURCHASED = "purchased";
    public static final String KEY_IS_AUTO_PAYMENT = "autoWithdraw";
    public static final String KEY_AUTO_PAY_FREQUENCY = "autoWithdrawFrequency";
    public static final String KEY_AUTO_PAY_TIMES_REPEATED = "autoWithdrawFrequencyRepeat";
    public static final String KEY_AUTO_PAY_AMOUNT = "autoWithdrawAmount";

    public String getName() {
        String name = "";
        try {
            name = fetchIfNeeded().getString(KEY_NAME);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return name;
    }

    public void setName(String name) {
        put(KEY_NAME, name);
    }

    public ParseFile getImage() {
        ParseFile image;
        try {
            image = fetchIfNeeded().getParseFile(KEY_IMAGE);
        } catch (ParseException e) {
            e.printStackTrace();
            image = null;
        }
        return image;
    }

    public void setImage(ParseFile image) {
        put(KEY_IMAGE, image);
    }

    public Double getSaved() {
        Double saved;
        try {
            saved = fetchIfNeeded().getDouble(KEY_SAVED);
        } catch (ParseException e) {
            e.printStackTrace();
            saved = 0.0;
        }
        return saved;
    }

    public void setSaved(Double saved) {
        put(KEY_SAVED, saved);
    }

    public void addSaved(Double saved) {
        put(KEY_SAVED, getSaved() + saved);
    }

    public Double getCost() {
        Double cost;
        try {
            cost = fetchIfNeeded().getDouble(KEY_COST);
        } catch (ParseException e) {
            e.printStackTrace();
            cost = 0.0;
        }
        return cost;
    }

    public void setCost(Double cost) {
        put(KEY_COST, cost);
    }

    public ParseUser getUser() {
        ParseUser user;
        try {
            user = fetchIfNeeded().getParseUser(KEY_USER);
        } catch (ParseException e) {
            e.printStackTrace();
            user = null;
        }
        return user;
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public Date getEndDate() {
        Date date;
        try {
            date = fetchIfNeeded().getDate(KEY_END_DATE);
        } catch (ParseException e) {
            e.printStackTrace();
            date = null;
        }
        return date;
    }

    public void setEndDate(Date endDate) {
        put(KEY_END_DATE, endDate);
    }

    public boolean getCompleted() {
        boolean isCompleted;
        try {
            isCompleted = fetchIfNeeded().getBoolean(KEY_COMPLETED);
        } catch (ParseException e) {
            isCompleted = false;
            e.printStackTrace();
        }
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        put(KEY_COMPLETED, completed);
    }

    public Date getDateCompleted() {
        Date dateCompleted;
        try {
            dateCompleted = fetchIfNeeded().getDate(KEY_DATE_COMPLETED);
        } catch (ParseException e) {
            e.printStackTrace();
            dateCompleted = null;
        }
        return dateCompleted;
    }

    public void setDateCompleted(Date date) {
        put(KEY_DATE_COMPLETED, date);
    }

    public void addTransaction(Transaction transaction) {
        addAllUnique(KEY_TRANSACTIONS, Collections.singleton(transaction));
    }

    public void removeTransaction(Transaction transaction) {
        removeAll(KEY_TRANSACTIONS, Collections.singleton(transaction));
    }

    public List<Transaction> getTransactions() {
        List<Transaction> list;
        try {
            list = fetchIfNeeded().getList(KEY_TRANSACTIONS);
            if (list == null) {
                list = new ArrayList<>();
            }
        } catch (ParseException e) {
            e.printStackTrace();
            list = new ArrayList<>();
        }
        return list;
    }

    public boolean getUpdatesMade() {
        boolean updatesMade;
        try {
            updatesMade = fetchIfNeeded().getBoolean(KEY_UPDATES_MADE);
        } catch (ParseException e) {
            updatesMade = false;
            e.printStackTrace();
        }
        return updatesMade;
    }

    public void setUpdatesMade(boolean updatesMade) {
        put(KEY_UPDATES_MADE, updatesMade);
    }

    public boolean getCompletedEarly() {
        boolean bool;
        try {
            bool = fetchIfNeeded().getBoolean(KEY_COMPLETED_EARLY);
        } catch (ParseException e) {
            bool = false;
            e.printStackTrace();
        }
        return bool;
    }

    public void setCompletedEarly(boolean completedEarly) {
        put(KEY_COMPLETED_EARLY, completedEarly);
    }

    @Override
    public int compareTo(Goal goal) {
        if (goal.getCompleted()) {
            return goal.getEndDate().compareTo(this.getEndDate());
        } else {
            return this.getEndDate().compareTo(goal.getEndDate());
        }
    }

    public void setDailySavings(Double savings) {
        put(KEY_DAILY_SAVING, savings);
    }

    public Double getDailySavings() {
        Double saving;
        try {
            saving = fetchIfNeeded().getDouble(KEY_DAILY_SAVING);
        } catch (ParseException e) {
            saving = 0.0;
            e.printStackTrace();
        }
        return saving;
    }

    public static Double calculateDailySaving(Goal goal) {
        long startdiffInMillies = Math.abs(goal.getEndDate().getTime()
                - goal.getCreatedAt().getTime());
        long startdiffInDays = TimeUnit.DAYS.convert(startdiffInMillies, TimeUnit.MILLISECONDS);
        Double dailySaving = ((goal.getCost() - goal.getSaved()) / startdiffInDays);
        if (dailySaving > goal.getCost()) {
            dailySaving = goal.getCost();
        }
        return dailySaving;
    }

    public void setPurchased(boolean bool) {
        put(KEY_PURCHASED, bool);
    }

    public boolean getPurchased() {
        boolean bool;
        try {
            bool = fetchIfNeeded().getBoolean(KEY_PURCHASED);
        } catch (ParseException e) {
            bool = false;
            e.printStackTrace();
        }
        return bool;
    }

    public Boolean getHasAutoPayment() {
        return getBoolean(KEY_IS_AUTO_PAYMENT);
    }

    public void setHasAutoPayment(Boolean hasAutoPayment) {
        put(KEY_IS_AUTO_PAYMENT, hasAutoPayment);
    }

    public String getAutoPayFrequency() {
        return getString(KEY_AUTO_PAY_FREQUENCY);
    }

    public void setAutoPayFrequency(String frequency) {
        put(KEY_AUTO_PAY_FREQUENCY, frequency);
    }

    public String getAutoPayTimesFrequencyIsRepeated() {
        return getString(KEY_AUTO_PAY_TIMES_REPEATED);
    }

    public void setAutoPayTimesFrequencyIsRepeated(String timesFrequencyIsRepeated) {
        put(KEY_AUTO_PAY_TIMES_REPEATED, timesFrequencyIsRepeated);
    }

    public Integer getAutoPayAmount() {
        return getInt(KEY_AUTO_PAY_AMOUNT);
    }

    public void setAutoPayAmount(Double autoPayAmount) {
        put(KEY_AUTO_PAY_AMOUNT, autoPayAmount);
    }

    public static class Query extends ParseQuery<Goal> {
        public Query() {
            super(Goal.class);
        }

        public Query getTopByEndDate() {
            setLimit(20);
            orderByAscending(KEY_END_DATE);
            return this;
        }

        public Query getTopCompleted() {
            setLimit(20);
            orderByDescending(KEY_DATE_COMPLETED);
            return this;
        }

        public Query setTop(int top) {
            setLimit(top);
            orderByAscending(KEY_END_DATE);
            return this;
        }

        public Query withUser() {
            include("user");
            return this;
        }

        public Query areCompleted() {
            whereEqualTo(KEY_COMPLETED, true);
            return this;
        }

        public Query areNotCompleted() {
            whereEqualTo(KEY_COMPLETED, false);
            return this;
        }

        public Query fromCurrentUser() {
            whereEqualTo(KEY_USER, ParseUser.getCurrentUser());
            return this;
        }

        public Query fromUser(User user) {
            whereEqualTo(KEY_USER, user);
            return this;
        }
    }
}
