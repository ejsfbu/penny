package com.ejsfbu.app_main.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ejsfbu.app_main.R;
import com.ejsfbu.app_main.models.User;
import com.parse.ParseException;
import com.parse.ParseUser;
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

    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etUsername)
    EditText etUsername;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etConfirmPassword)
    EditText etConfirmPassword;
    @BindView(R.id.bSignUp)
    Button bSignUp;
    @BindView(R.id.etBirthday)
    EditText etBirthday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.bSignUp)
    public void clickSignUp() {
        final String name = etName.getText().toString();
        final String email = etEmail.getText().toString();
        final String username = etUsername.getText().toString();
        final String password = etPassword.getText().toString();
        final String confirmPassword = etConfirmPassword.getText().toString();
        final String birthdayString = etBirthday.getText().toString();

        if (confirmCorrectDateFormat(birthdayString)) {
            final Date birthday = parseDate(birthdayString);
            if (birthday == null) {
                Toast.makeText(this, "Enter birthday as dd/mm/yyyy",
                        Toast.LENGTH_LONG).show();
            } else {
                if (confirmPasswordsMatch(password, confirmPassword)) {
                    signUp(name, email, username, password, birthday);
                } else {
                    Toast.makeText(this, "Passwords do not match",
                            Toast.LENGTH_LONG).show();
                }
            }
        } else {
            Toast.makeText(this, "Enter birthday as dd/mm/yyyy",
                    Toast.LENGTH_LONG).show();
        }
    }

    private boolean confirmPasswordsMatch(String password, String confirmPassword) {
        if (password.equals(confirmPassword)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean confirmCorrectDateFormat(String date) {
        String[] pieces = date.split("/");
        if (pieces.length != 3) {
            return false;
        }
        if (pieces[0].length() != 2) {
            return false;
        }
        if (pieces[1].length() != 2) {
            return false;
        }
        if (pieces[2].length() != 4) {
            return false;
        }
        return true;
    }

    private Date parseDate(String date) {
        try {
            return new SimpleDateFormat("dd/MM/yyyy").parse(date);
        } catch (java.text.ParseException e) {
            Log.e(TAG, "Error parsing date");
            e.printStackTrace();
            return null;
        }
    }

    private void signUp(String name, String email, String username,
                        String password, Date birthday) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        user.setBirthday(birthday);

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(SignUpActivity.this, "Sign Up Success",
                            Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Sign Up Success");

                    Intent intent = new Intent(SignUpActivity.this,
                            MainActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(SignUpActivity.this, "Sign Up Failure",
                            Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Sign Up Failure");
                    e.printStackTrace();
                }
            }
        });
    }
}
