package com.ejsfbu.app_main;

import android.app.Application;

import com.ejsfbu.app_main.Models.BankAccount;
import com.ejsfbu.app_main.Models.Goal;
import com.ejsfbu.app_main.Models.Request;
import com.ejsfbu.app_main.Models.Reward;
import com.ejsfbu.app_main.Models.Transaction;
import com.ejsfbu.app_main.Models.User;
import com.parse.Parse;
import com.parse.ParseObject;

// Implement Parse Server Connections to Models
public class ParseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(User.class);
        final Parse.Configuration configurationUser = new Parse.Configuration.Builder(this)
                .applicationId("ejsfbu-money")
                .clientKey(getResources().getString(R.string.master_key))
                .server("https://youth-financial-planning.herokuapp.com/parse")
                .build();
        Parse.initialize(configurationUser);

        ParseObject.registerSubclass(Goal.class);
        final Parse.Configuration configurationGoal = new Parse.Configuration.Builder(this)
                .applicationId("ejsfbu-money")
                .clientKey(getResources().getString(R.string.master_key))
                .server("https://youth-financial-planning.herokuapp.com/parse")
                .build();
        Parse.initialize(configurationGoal);

        ParseObject.registerSubclass(Reward.class);
        final Parse.Configuration configurationReward = new Parse.Configuration.Builder(this)
                .applicationId("ejsfbu-money")
                .clientKey(getResources().getString(R.string.master_key))
                .server("https://youth-financial-planning.herokuapp.com/parse")
                .build();
        Parse.initialize(configurationReward);

        ParseObject.registerSubclass(BankAccount.class);
        final Parse.Configuration configurationBank = new Parse.Configuration.Builder(this)
                .applicationId("ejsfbu-money")
                .clientKey(getResources().getString(R.string.master_key))
                .server("https://youth-financial-planning.herokuapp.com/parse")
                .build();
        Parse.initialize(configurationBank);

        ParseObject.registerSubclass(Transaction.class);
        final Parse.Configuration configurationTransaction = new Parse.Configuration.Builder(this)
                .applicationId("ejsfbu-money")
                .clientKey(getResources().getString(R.string.master_key))
                .server("https://youth-financial-planning.herokuapp.com/parse")
                .build();
        Parse.initialize(configurationTransaction);

        ParseObject.registerSubclass(Request.class);
        final Parse.Configuration configurationRequest = new Parse.Configuration.Builder(this)
                .applicationId("ejsfbu-money")
                .clientKey(getResources().getString(R.string.master_key))
                .server("https://youth-financial-planning.herokuapp.com/parse")
                .build();
        Parse.initialize(configurationRequest);
    }
}
