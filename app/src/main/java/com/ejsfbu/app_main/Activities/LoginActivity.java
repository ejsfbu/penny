package com.ejsfbu.app_main.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ejsfbu.app_main.R;
import com.ejsfbu.app_main.Models.User;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";

    @BindView(R.id.etLoginUsername)
    EditText etUsername;
    @BindView(R.id.etLoginPassword)
    EditText etPassword;
    @BindView(R.id.bLogin)
    Button bLogin;
    @BindView(R.id.bSignUp)
    Button bSignUp;
    @BindView(R.id.bLoginParentSignUp)
    Button bParentSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        User currentUser = (User) ParseUser.getCurrentUser();
        if (currentUser != null) {
            if (currentUser.getIsParent()) {
                Intent intent = new Intent(this, ParentActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }

    }

    @OnClick(R.id.bLogin)
    public void clickLogin() {
        final String username = etUsername.getText().toString();
        final String password = etPassword.getText().toString();

        login(username, password);
    }

    @OnClick(R.id.bSignUp)
    public void clickSignUp() {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        intent.putExtra("isParent", false);
        startActivity(intent);
    }

    @OnClick(R.id.bLoginParentSignUp)
    public void clickParentSignUp() {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        intent.putExtra("isParent", true);
        startActivity(intent);
    }

    private void login(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    Toast.makeText(LoginActivity.this, "Login Success",
                            Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Login Success");

                    if (((User) user).getIsParent()) {
                        Intent intent = new Intent(LoginActivity.this,
                                ParentActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        final Intent intent = new Intent(LoginActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Login Failure");
                    e.printStackTrace();
                }
            }
        });
    }
}
