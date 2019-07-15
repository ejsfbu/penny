package com.ejsfbu.app_main.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ejsfbu.app_main.R;

// holder for tabs (goals list, badges, profile fragments)

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
