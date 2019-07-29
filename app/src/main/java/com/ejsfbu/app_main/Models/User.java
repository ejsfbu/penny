package com.ejsfbu.app_main.Models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;


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
        return getBoolean(KEY_REQUIRES_APPROVAL);
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
        for (BankAccount bank: list){
            if (bank.getVerified()) {
                newList.add(bank);
            }
        }
        return newList;
    }

    public void addBank(BankAccount bank) {
        addAllUnique(KEY_BANK, Collections.singleton(bank));
    }

    public void removeBank(BankAccount bank) { removeAll(KEY_BANK, Collections.singleton(bank));}

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
