package com.ejsfbu.app_main.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ejsfbu.app_main.R;

// app will automatically load to this screen, unless user already logged in
// upon login app will navigate to goals list fragment in main activity

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}
