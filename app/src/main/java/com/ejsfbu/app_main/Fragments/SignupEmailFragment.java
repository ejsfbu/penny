package com.ejsfbu.app_main.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ejsfbu.app_main.R;
import com.ejsfbu.app_main.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;

import static com.ejsfbu.app_main.Activities.SignUpActivity.user;

public class SignupEmailFragment extends Fragment {

    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.email_next_btn)
    Button email_next_btn;
    private Unbinder unbinder;
    private boolean emailUnique;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.signup_email, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);
    }

    @OnClick(R.id.email_next_btn)
    public void clickSignUp() {
        if (!emailUnique) {
            Toast.makeText(getContext(), "Email is already associated with an account",
                    Toast.LENGTH_LONG);
            return;
        }
        final String email = etEmail.getText().toString();
        user.setEmail(email);
        Fragment username = new SignupUsernameFragment();
        getFragmentManager().beginTransaction().replace(R.id.flSignUpContainer, username).commit();
    }

    // When change fragment unbind view
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnTextChanged(R.id.etEmail)
    public void checkEmailUnique() {
        String email = etEmail.getText().toString();
        User.Query userQuery = new User.Query();
        userQuery.testEmail(email);
        userQuery.findInBackground(new FindCallback<User>() {
            @Override
            public void done(List<User> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() == 0) {
                        etEmail.setTextColor(SignupEmailFragment.this.getResources()
                                .getColor(android.R.color.holo_green_dark));
                        emailUnique = true;
                    } else {
                        etEmail.setTextColor(SignupEmailFragment.this.getResources()
                                .getColor(android.R.color.holo_red_dark));
                        emailUnique = false;
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}
