package com.ejsfbu.app_main.SignUpFragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ejsfbu.app_main.Activities.MainActivity;
import com.ejsfbu.app_main.Models.User;
import com.ejsfbu.app_main.R;
import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.SignUpCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;

import static com.ejsfbu.app_main.Activities.SignUpActivity.user;

public class SignUpAccountInfoFragment extends Fragment {

    public static final String TAG = "SignUpAccountInfoFrag";

    @BindView(R.id.etSignUpAccountInfoEmail)
    EditText etSignUpAccountInfoEmail;
    @BindView(R.id.etSignUpAccountInfoUsername)
    EditText etSignUpAccountInfoUsername;
    @BindView(R.id.etSignUpAccountInfoPassword)
    EditText etSignUpAccountInfoPassword;
    @BindView(R.id.etSignUpAccountInfoConfirmPassword)
    EditText etSignUpAccountInfoConfirmPassword;
    @BindView(R.id.bSignUpChild)
    Button bSignUpChild;

    private Unbinder unbinder;
    private boolean emailUnique;
    private boolean usernameUnique;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup_account_info,
                container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);
    }

    @OnClick(R.id.bSignUpChild)
    public void clickSignUp() {

        final String email = etSignUpAccountInfoEmail.getText().toString();
        if (email.equals("")) {
            Toast.makeText(getContext(), "Please enter an email", Toast.LENGTH_LONG).show();
            return;
        }
        if (!emailUnique) {
            Toast.makeText(getContext(), "Email is already associated with an account",
                    Toast.LENGTH_LONG).show();
            return;
        }
        user.setEmail(email);

        final String username = etSignUpAccountInfoUsername.getText().toString();
        if (username.equals("")) {
            Toast.makeText(getContext(), "Please enter a username", Toast.LENGTH_LONG).show();
            return;
        }
        if (!usernameUnique) {
            Toast.makeText(getContext(), "Username is taken", Toast.LENGTH_LONG).show();
            return;
        }
        user.setUsername(username);

        final String password = etSignUpAccountInfoPassword.getText().toString();
        final String confirmPassword = etSignUpAccountInfoConfirmPassword.getText().toString();
        if (password.equals("") || confirmPassword.equals("")) {
            Toast.makeText(getContext(), "Please enter a password", Toast.LENGTH_LONG).show();
            return;
        }
        if (confirmPasswordsMatch(password, confirmPassword)) {
            user.setPassword(password);

            ParseACL parseACL = new ParseACL();
            parseACL.setPublicReadAccess(true);
            parseACL.setPublicWriteAccess(true);
            user.setACL(parseACL);

            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Intent intent = new Intent(getActivity(),
                                MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    } else {
                        Toast.makeText(getActivity(), "Sign Up Failure",
                                Toast.LENGTH_LONG).show();
                        Log.e(TAG, "Sign Up Failure");
                        e.printStackTrace();
                    }
                }
            });
        } else {
            Toast.makeText(getContext(), "Passwords do not match",
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

    // When change fragment unbind view
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnTextChanged(R.id.etSignUpAccountInfoEmail)
    public void checkEmailUnique() {
        String email = etSignUpAccountInfoEmail.getText().toString();
        User.Query userQuery = new User.Query();
        userQuery.testEmail(email);
        userQuery.findInBackground(new FindCallback<User>() {
            @Override
            public void done(List<User> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() == 0) {
                        etSignUpAccountInfoEmail.setTextColor(SignUpAccountInfoFragment.this
                                .getResources().getColor(android.R.color.holo_green_dark));
                        emailUnique = true;
                    } else {
                        etSignUpAccountInfoEmail.setTextColor(SignUpAccountInfoFragment.this
                                .getResources().getColor(android.R.color.holo_red_dark));
                        emailUnique = false;
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    @OnTextChanged(R.id.etSignUpAccountInfoUsername)
    public void checkUsernameUnique() {
        String username = etSignUpAccountInfoUsername.getText().toString();
        User.Query userQuery = new User.Query();
        userQuery.testUsername(username);
        userQuery.findInBackground(new FindCallback<User>() {
            @Override
            public void done(List<User> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() == 0) {
                        etSignUpAccountInfoUsername.setTextColor(SignUpAccountInfoFragment.this
                                .getResources().getColor(android.R.color.holo_green_dark));
                        usernameUnique = true;
                    } else {
                        etSignUpAccountInfoUsername.setTextColor(SignUpAccountInfoFragment.this
                                .getResources().getColor(android.R.color.holo_red_dark));
                        usernameUnique = false;
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}
