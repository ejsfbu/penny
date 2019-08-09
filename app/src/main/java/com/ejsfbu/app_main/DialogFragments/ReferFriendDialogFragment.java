package com.ejsfbu.app_main.DialogFragments;


import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ejsfbu.app_main.Models.User;
import com.ejsfbu.app_main.R;
import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class ReferFriendDialogFragment extends DialogFragment {

    private Context context;
    private User user;

    private EditText etReferFriendEmail;
    private Button bReferFriendCancel;
    private Button bReferFriendConfirm;

    public ReferFriendDialogFragment() {

    }

    public static ReferFriendDialogFragment newInstance(String title) {
        ReferFriendDialogFragment frag = new ReferFriendDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getContext();
        return inflater.inflate(R.layout.fragment_refer_friend, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = (User) ParseUser.getCurrentUser();

        etReferFriendEmail = view.findViewById(R.id.etReferFriendEmail);
        bReferFriendCancel = view.findViewById(R.id.bReferFriendCancel);
        bReferFriendConfirm = view.findViewById(R.id.bReferFriendConfirm);

        String title = getArguments().getString("title", "Refer Friend");
        getDialog().setTitle(title);
        etReferFriendEmail.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        setOnClick();
    }

    public void onResume() {
        Window window = getDialog().getWindow();
        Point size = new Point();
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout((int) (size.x * 0.85), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        super.onResume();
    }

    private void setOnClick() {
        bReferFriendCancel.setOnClickListener(view -> {
            dismiss();
        });

        bReferFriendConfirm.setOnClickListener(view -> {
            checkEmailValid();
        });
    }

    private void checkEmailValid() {
        String email = etReferFriendEmail.getText().toString();
        String[] halves = email.split("@");
        if (halves.length != 2) {
            Toast.makeText(context, "Please enter a valid email",
                    Toast.LENGTH_SHORT).show();
        } else {
            String[] dots = halves[1].split(".");
            if (dots.length != 2) {
                Toast.makeText(context, "Please enter a valid email",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Email sent!",
                        Toast.LENGTH_SHORT).show();
                dismiss();
            }
        }
    }
}
