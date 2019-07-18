package com.ejsfbu.app_main.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ejsfbu.app_main.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.ejsfbu.app_main.Activities.SignUpActivity.user;


public class SignupNameFragment extends Fragment {

    @BindView(R.id.etName)
    EditText etName;

    @BindView(R.id.name_next_btn)
    Button name_next_btn;

    private Unbinder unbinder;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.signup_name, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);
    }


    @OnClick(R.id.name_next_btn)
    public void onClickNext() {
        final String name = etName.getText().toString();
        user.setName(name);
        //TODO -- Note to team: Should we track the birthday of parents? Is it necessary?
        Fragment birthday = new SignupBirthdayFragment();
        getFragmentManager().beginTransaction().replace(R.id.flSignUpContainer, birthday).commit();
    }

    // When change fragment unbind view
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
