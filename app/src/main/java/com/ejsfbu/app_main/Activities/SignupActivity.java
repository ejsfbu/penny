package com.ejsfbu.app_main.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ejsfbu.app_main.R;

// new user can navigate to this screen from login activity
// upon signup app will navigate to create a goal activity/fragment

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }
}
