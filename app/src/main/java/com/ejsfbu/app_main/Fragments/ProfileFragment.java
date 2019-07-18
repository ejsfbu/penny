package com.ejsfbu.app_main.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;


import com.ejsfbu.app_main.Activities.LoginActivity;
import com.ejsfbu.app_main.Activities.MainActivity;
import com.ejsfbu.app_main.EditFragments.EditEmailDialogFragment;
import com.ejsfbu.app_main.EditFragments.EditNameDialogFragment;
import com.ejsfbu.app_main.EditFragments.EditPasswordDialogFragment;
import com.ejsfbu.app_main.EditFragments.EditUserNameDialogFragment;
import com.ejsfbu.app_main.R;
import com.parse.ParseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ProfileFragment extends Fragment {

    public static final String TAG = "ProfileFragment";

    @BindView(R.id.bLogOut) Button bLogOut;
    @BindView(R.id.ibEditName) ImageButton ibName;
    @BindView(R.id.ibEditUserName) ImageButton ibUserName;

    // Butterknife for fragment
    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
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

    @OnClick(R.id.bLogOut)
    public void onClickLogOut() {
        ParseUser.logOut();

        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @OnClick(R.id.ibEditName)
    public void onClickEditName() {
        showEditNameDialog();
    }

    @OnClick(R.id.ibEditUserName)
    public void onClickEditUserName() {
        showEditUserNameDialog();
    }

    @OnClick(R.id.ibEditEmail)
    public void onClickEditEmail() {
        showEditEmailDialog();
    }

    @OnClick(R.id.ibEditPassword)
    public void onClickEditPassword() {
        showEditPasswordDialog();
    }

    private void showEditNameDialog() {
        ///FragmentManager fm = getSupportFragmentManager();
        EditNameDialogFragment editNameDialogFragment = EditNameDialogFragment.newInstance("Edit Name");
        editNameDialogFragment.show(MainActivity.fragmentManager, "fragment_edit_name");
    }

    private void showEditUserNameDialog() {
        ///FragmentManager fm = getSupportFragmentManager();
        EditUserNameDialogFragment editUserNameDialogFragment = EditUserNameDialogFragment.newInstance("Edit User Name");
        editUserNameDialogFragment.show(MainActivity.fragmentManager, "fragment_edit_username");
    }

    private void showEditEmailDialog() {
        ///FragmentManager fm = getSupportFragmentManager();
        EditEmailDialogFragment editEmailDialogFragment = EditEmailDialogFragment.newInstance("Edit Email");
        editEmailDialogFragment.show(MainActivity.fragmentManager, "fragment_edit_email");
    }

    private void showEditPasswordDialog() {
        ///FragmentManager fm = getSupportFragmentManager();
        EditPasswordDialogFragment editPasswordDialogFragment = EditPasswordDialogFragment.newInstance("Edit Password");
        editPasswordDialogFragment.show(MainActivity.fragmentManager, "fragment_edit_password");
    }

}
