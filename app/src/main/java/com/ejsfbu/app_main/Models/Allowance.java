package com.ejsfbu.app_main.Models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


@ParseClassName("Allowance")
public class Allowance extends ParseObject {

    public static final String KEY_CHILD_ID = "childId";
    public static final String KEY_PARENT_ID = "parentId";
    public static final String KEY_ALLOWANCE_AMOUNT = "amount";
    public static final String KEY_ALLOWANCE_FREQUENCY = "allowanceFrequency";
    public static final String KEY_BANK_NAME = "bankName";


    public String getChildID() {
        return getString(KEY_CHILD_ID);
    }

    public String getParentID() {
        return getString(KEY_PARENT_ID);
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

    public static String getKeyBankName() {
        return KEY_BANK_NAME;
    }

    public static class Query extends ParseQuery<Allowance> {
        public Query() {
            super(Allowance.class);
        }

        public Allowance.Query findChild(String childID) {
            whereEqualTo(KEY_CHILD_ID, childID);
            return this;
        }

        public Allowance.Query findParent(String parentID) {
            whereEqualTo(KEY_CHILD_ID, parentID);
            return this;
        }
    }

}
