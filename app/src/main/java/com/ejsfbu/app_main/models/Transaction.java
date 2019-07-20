package com.ejsfbu.app_main.models;

import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;


@ParseClassName("Transaction")
public class Transaction extends ParseObject {
    public static final String KEY_DATE = "createdAt";

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
}
