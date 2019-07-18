package com.ejsfbu.app_main.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.ejsfbu.app_main.Fragments.SignupGetStarted;
import com.ejsfbu.app_main.R;
import com.ejsfbu.app_main.models.User;

// new user can navigate to this screen from login activity
// upon signup app will navigate to create a goal activity/fragment

public class SignUpActivity extends AppCompatActivity {

    public static final String TAG = "SignUpActivity";
    public static FragmentManager fragmentManager;
    public static User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        user = new User();

        fragmentManager = getSupportFragmentManager();

        Fragment getStarted = new SignupGetStarted();
        fragmentManager.beginTransaction().replace(R.id.flSignUpContainer, getStarted).commit();
    }


}
