package com.ejsfbu.app_main.Models;

import com.parse.ParseClassName;
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
        return (User) getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public String getRequestType() {
        return getString(KEY_REQUEST_TYPE);
    }

    public void setRequestType(String requestType) {
        put(KEY_REQUEST_TYPE, requestType);
    }

    public String getRequestDetails() {
        return getString(KEY_REQUEST_DETAILS);
    }

    public void setRequestDetails(String requestDetails) {
        put(KEY_REQUEST_DETAILS, requestDetails);
    }

    public Transaction getTransaction() {
        return (Transaction) get(KEY_TRANSACTION);
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
