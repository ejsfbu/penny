package com.ejsfbu.app_main;

import android.app.Application;

import com.ejsfbu.app_main.models.User;
import com.parse.Parse;
import com.parse.ParseObject;

// Implement Parse Server Connections to Models
public class ParseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(User.class);
        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("ejsfbu-money")
                .clientKey("money-makes-the-world-go-around")
                .server("https://youth-financial-planning.herokuapp.com/parse")
                .build();
        Parse.initialize(configuration);
    }
}
