package com.ejsfbu.app_main.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ejsfbu.app_main.Fragments.SignupGetStarted;
import com.ejsfbu.app_main.Fragments.SignupNameFragment;
import com.ejsfbu.app_main.R;
import com.ejsfbu.app_main.models.User;
import com.parse.ParseException;
import com.parse.SignUpCallback;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
