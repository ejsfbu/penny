package com.ejsfbu.app_main.models;

import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Date;


@ParseClassName("Transaction")
public class Transaction extends ParseObject {
    public static final String KEY_DATE = "createdAt";
    public static final String KEY_AMOUNT = "amount";


    public static class Query extends ParseQuery<Transaction> {
        public Query() {
            super(Transaction.class);
        }

        public Transaction.Query setTop(int top) {
            setLimit(top);
            orderByDescending(KEY_DATE);
            return this;
        }

    }

    public Date getDate() {
        return (Date) get(KEY_DATE);
    }

    public String getAmount() {
        return get(KEY_AMOUNT).toString();
    }
}
