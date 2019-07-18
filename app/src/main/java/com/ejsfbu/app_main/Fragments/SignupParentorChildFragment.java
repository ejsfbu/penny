package com.ejsfbu.app_main.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ejsfbu.app_main.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.ejsfbu.app_main.Activities.SignUpActivity.user;

public class SignupParentorChildFragment extends Fragment {

    private Unbinder unbinder;
    @BindView(R.id.parent_btn)
    Button parent_btn;

    @BindView(R.id.child_btn)
    Button child_btn;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.signup_parent_or_child, container, false);
    }


    @OnClick(R.id.parent_btn)
    public void onClickParent() {
        user.setisParent(true);
        Fragment name = new SignupNameFragment();
        getFragmentManager().beginTransaction().replace(R.id.flSignUpContainer, name).commit();
    }

    @OnClick(R.id.child_btn)
    public void onClickChild() {
        user.setisParent(false);
        Fragment name = new SignupNameFragment();
        getFragmentManager().beginTransaction().replace(R.id.flSignUpContainer, name).commit();
    }

    // When change fragment unbind view
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
