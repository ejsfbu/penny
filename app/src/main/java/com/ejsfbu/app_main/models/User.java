package com.ejsfbu.app_main.models;

import com.parse.ParseClassName;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Collections;
import java.util.Date;

@ParseClassName("_User")
public class User extends ParseUser {

    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_BIRTHDAY = "birthday";
    public static final String KEY_ISPARENT = "isParent";
    public static final String KEY_CHILDREN = "children";
    public static final String KEY_PARENTS = "parents";
    public static final String KEY_NEEDS_PARENT = "needsParent";

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

    public Boolean getIsParent() { return getBoolean(KEY_ISPARENT);}

    public void setIsParent(Boolean isParent) {
        put(KEY_ISPARENT, isParent);
    }

    public boolean getNeedsParent() {
        return getBoolean(KEY_NEEDS_PARENT);
    }

    public void setNeedsParent(boolean needsParent) {
        put(KEY_NEEDS_PARENT, needsParent);
    }

    public void addChild(User child) {
        addAllUnique(KEY_CHILDREN, Collections.singleton(child));
    }

    public void addParent(User parent) {
        addAllUnique(KEY_PARENTS, Collections.singleton(parent));
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
