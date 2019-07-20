package com.ejsfbu.app_main.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("BankAccount")
public class BankAccount extends ParseObject {
    public static final String KEY_BANK_NAME = "bankName";
    public static final String KEY_LEGAL_NAME = "legalName";
    public static final String KEY_ROUTING = "routingNumber";
    public static final String KEY_ACCOUNT = "accountNumber";

    // change as needed
    private boolean hasEnoughMoney = true;
    private boolean depositSuccess = true;

    public String getBankName() {
        return getString(KEY_BANK_NAME);
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
        return getString(KEY_ACCOUNT);
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
