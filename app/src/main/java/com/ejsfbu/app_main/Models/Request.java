package com.ejsfbu.app_main.Models;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("Request")
public class Request extends ParseObject {

    public static final String KEY_USER = "user";
    public static final String KEY_REQUEST_TYPE = "requestType";
    public static final String KEY_REQUEST_DETAILS = "requestDetails";
    public static final String KEY_TRANSACTION = "transaction";

    public User getUser() {
        User user;
        try {
            user = (User) fetchIfNeeded().getParseUser(KEY_USER);
        } catch (ParseException e) {
            e.printStackTrace();
            user = null;
        }
        return user;
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public String getRequestType() {
        String request = "";
        try {
            request = fetchIfNeeded().getString(KEY_REQUEST_TYPE);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return request;
    }

    public void setRequestType(String requestType) {
        put(KEY_REQUEST_TYPE, requestType);
    }

    public String getRequestDetails() {
        String request = "";
        try {
            request = fetchIfNeeded().getString(KEY_REQUEST_DETAILS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return request;
    }

    public void setRequestDetails(String requestDetails) {
        put(KEY_REQUEST_DETAILS, requestDetails);
    }

    public Transaction getTransaction() {
        Transaction transaction;
        try {
            transaction = (Transaction) fetchIfNeeded().get(KEY_TRANSACTION);
        } catch (ParseException e) {
            e.printStackTrace();
            transaction = null;
        }
        return transaction;
    }

    public void setTransaction(ParseObject transaction) {
        put(KEY_TRANSACTION, transaction);
    }

    public static class Query extends ParseQuery<Request> {

        public Query() {
            super(Request.class);
        }

        public Query fromUser(User user) {
            whereEqualTo(KEY_USER, user);
            return this;
        }
    }
}
