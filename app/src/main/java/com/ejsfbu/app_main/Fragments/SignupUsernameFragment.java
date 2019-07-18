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

public class SignupUsernameFragment extends Fragment {
    @BindView(R.id.etUsername)
    EditText etUsername;
    @BindView(R.id.username_next_btn)
    Button username_next_btn;
    private Unbinder unbinder;
    private boolean usernameUnique;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.signup_username, container, false);
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

    @OnClick(R.id.username_next_btn)
    public void onClickNext() {
        if (!usernameUnique) {
            Toast.makeText(getContext(), "Username is taken", Toast.LENGTH_LONG);
            return;
        }
        final String username = etUsername.getText().toString();
        user.setUsername(username);
        Fragment password = new SignupPasswordFragment();
        getFragmentManager().beginTransaction().replace(R.id.flSignUpContainer, password).commit();
    }

    @OnTextChanged(R.id.etUsername)
    public void checkUsernameUnique() {
        String username = etUsername.getText().toString();
        User.Query userQuery = new User.Query();
        userQuery.testUsername(username);
        userQuery.findInBackground(new FindCallback<User>() {
            @Override
            public void done(List<User> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() == 0) {
                        etUsername.setTextColor(SignupUsernameFragment.this.getResources()
                                .getColor(android.R.color.holo_green_light));
                        usernameUnique = true;
                    } else {
                        etUsername.setTextColor(SignupUsernameFragment.this.getResources()
                                .getColor(android.R.color.holo_red_dark));
                        usernameUnique = false;
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}
