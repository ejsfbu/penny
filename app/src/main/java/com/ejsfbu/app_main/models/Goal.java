package com.ejsfbu.app_main.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@ParseClassName("Goal")
public class Goal extends ParseObject {
    // Parse column names
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";
    public static final String KEY_CREATED_AT = "createdAt";
    public static final String KEY_NAME = "goalName";
    public static final String KEY_SAVED = "amountSaved";
    public static final String KEY_COST = "totalCost";

    public String getName() {
        return getString(KEY_NAME);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public Double getSaved() { return  getDouble(KEY_SAVED); }

    public Double getCost() { return  getDouble(KEY_COST); }

    public static class Query extends ParseQuery<Goal> {
        public Query() {
            super(Goal.class);
        }

        public Query getTop() {
            setLimit(20);
            return this;
        }

        public Query setTop(int top) {
            setLimit(top);
            return this;
        }


        public Query withUser() {
            include("user");
            return this;
        }
    }
}
