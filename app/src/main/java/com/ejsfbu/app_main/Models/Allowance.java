package com.ejsfbu.app_main.Models;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;

@ParseClassName("Allowance")
public class Allowance extends ParseObject {

    public static final String KEY_CHILD = "child";
    public static final String KEY_PARENT = "parent";
    public static final String KEY_ALLOWANCE_AMOUNT = "amount";
    public static final String KEY_ALLOWANCE_FREQUENCY = "allowanceFrequency";
    public static final String KEY_BANK_NAME = "bankName";

    public void setChild(User child) {
        put(KEY_CHILD, child);
    }

    public ParseUser getChild() {
        return getParseUser(KEY_CHILD);
    }

    public void setParent(User parent) {
        put(KEY_PARENT, parent);
    }

    public String getParent() {
        return getString(KEY_PARENT);
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

    public String getParentBankName() {
        return getString(KEY_BANK_NAME);
    }

    public void setParentBankName(String bankName) {
        put(KEY_BANK_NAME, bankName);
    }

    public static class Query extends ParseQuery<Allowance> {
        public Query() {
            super(Allowance.class);
        }

        public Allowance.Query findChild(ParseUser child) {
            whereEqualTo(KEY_CHILD, child);
            return this;
        }

        public Allowance.Query findParent(ParseUser parent) {
            whereEqualTo(KEY_PARENT, parent);
            return this;
        }
    }

    public static ArrayList<Allowance> getAllowances(User child) {
        ArrayList<Allowance> allowances = new ArrayList<>();
        Allowance.Query query = new Allowance.Query();
        query.findChild(child);
        try {
            allowances.addAll(query.find());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return allowances;
    }
}
