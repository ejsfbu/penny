package com.ejsfbu.app_main.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.ejsfbu.app_main.R;

public class ParentActivity extends AppCompatActivity {

    public static final String TAG = "ParentActivity";

    public static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);

        fragmentManager = getSupportFragmentManager();
    }
}
