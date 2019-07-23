package com.ejsfbu.app_main.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ejsfbu.app_main.R;
import com.ejsfbu.app_main.models.User;

public class ChildDetailActivity extends AppCompatActivity {

    private User child;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_detail);

        String childCode = getIntent().getStringExtra("childCode");
    }
}
