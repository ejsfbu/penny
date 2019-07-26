package com.ejsfbu.app_main.models;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;

@ParseClassName("BankAccount")
public class BankAccount extends ParseObject {
    public static final String KEY_BANK_NAME = "bankName";
    public static final String KEY_LEGAL_NAME = "legalName";
    public static final String KEY_ROUTING = "routingNumber";
    public static final String KEY_ACCOUNT = "accountNumber";
    public static final String KEY_VERIFIED = "isVerified";

    // change as needed
    private boolean hasEnoughMoney = true;
    private boolean depositSuccess = true;

    public String getBankName() {
        String name = "";
        try {
            name = fetchIfNeeded().getString(KEY_BANK_NAME);
        } catch (ParseException e) {
            Log.d("bank", e.toString());
            e.printStackTrace();
        }
        return name;
    }

    public void setBankName(String name) {
        put(KEY_BANK_NAME, name);
    }

    public String getLegalName() {
        return getString(KEY_LEGAL_NAME);
    }

    public void setLegalName(String name) {
        put(KEY_LEGAL_NAME, name);
    }

    public String getAccountNumber() {
        String account = "";
        try {
            account = fetchIfNeeded().getString(KEY_ACCOUNT);
        } catch (ParseException e) {
            Log.d("bank", e.toString());
            e.printStackTrace();
        }
        return account;
    }

    public void setAccountNumber(String number) {
        put(KEY_ACCOUNT, number);
    }

    public String getRoutingNumber() {
        return getString(KEY_ROUTING);
    }

    public void setRoutingtNumber(String number) {
        put(KEY_ROUTING, number);
    }

    public boolean getVerified() {
        boolean isVerified = false;
        try {
            isVerified = fetchIfNeeded().getBoolean(KEY_VERIFIED);
        } catch (ParseException e) {
            Log.d("user", e.toString());
            e.printStackTrace();
        }
        return isVerified;
    }

    public void setVerified(boolean bool) {
        put(KEY_VERIFIED, bool);
    }

    // Simulate money transfer
    // TODO implement feature if not enough money is present
    public Double withdraw(Double amount) {
        if (hasEnoughMoney) {
            return amount;
        } else {
            return null;
        }
    }

    public boolean deposit(Double amount) {
        return depositSuccess;
    }

}
