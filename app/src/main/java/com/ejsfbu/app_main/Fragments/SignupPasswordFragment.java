package com.ejsfbu.app_main.Fragments;

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
import com.ejsfbu.app_main.Activities.SignUpActivity;
import com.ejsfbu.app_main.R;
import com.parse.ParseException;
import com.parse.SignUpCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.ejsfbu.app_main.Activities.SignUpActivity.user;

public class SignupPasswordFragment extends Fragment {
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etConfirmPassword)
    EditText etConfirmPassword;
    @BindView(R.id.bSignUp)
    Button bSignUp;
    private Unbinder unbinder;
    public static final String TAG = "SignUpFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.signup_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);
    }

    // When change fragment unbind view
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private boolean confirmPasswordsMatch(String password, String confirmPassword) {
        if (password.equals(confirmPassword)) {
            return true;
        } else {
            return false;
        }
    }

    @OnClick(R.id.bSignUp)
    public void onClickSignUp() {
        final String password = etPassword.getText().toString();
        final String confirmPassword = etConfirmPassword.getText().toString();
        if (confirmPasswordsMatch(password, confirmPassword)) {
            user.setPassword(password);
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(getActivity(), "Sign Up Success",
                                Toast.LENGTH_LONG).show();
                        Log.d(TAG, "Sign Up Success");

                        Intent intent = new Intent(getActivity(),
                                MainActivity.class);
                        startActivity(intent);
                        //TODO finish();
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
}
