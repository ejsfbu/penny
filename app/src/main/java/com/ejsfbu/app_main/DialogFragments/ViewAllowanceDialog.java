package com.ejsfbu.app_main.DialogFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ejsfbu.app_main.Models.User;
import com.ejsfbu.app_main.R;
import com.parse.ParseUser;

public class ViewAllowanceDialog extends DialogFragment {


    private static User user;

    public ViewAllowanceDialog() {

    }

    public static ViewAllowanceDialog newInstance(String title) {
        ViewAllowanceDialog viewAllowanceDialog = new ViewAllowanceDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        viewAllowanceDialog.setArguments(args);
        return viewAllowanceDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_allowance, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = (User) ParseUser.getCurrentUser();

    }
}
