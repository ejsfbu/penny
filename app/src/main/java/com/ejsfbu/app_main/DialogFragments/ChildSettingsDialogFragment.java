package com.ejsfbu.app_main.DialogFragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.ejsfbu.app_main.Activities.ParentActivity;
import com.ejsfbu.app_main.Fragments.ChildListFragment;
import com.ejsfbu.app_main.Models.User;
import com.ejsfbu.app_main.R;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.concurrent.TimeUnit;

import static android.view.View.GONE;

public class ChildSettingsDialogFragment extends DialogFragment {

    private TextView tvChildSettingsRequireApprovalStatusInfo;
    private Button bChildSettingsRequireApproval;
    private Button bChildSettingsUnlinkChild;

    private Context context;

    private User child;
    private User parent;

    public ChildSettingsDialogFragment() {

    }

    public static ChildSettingsDialogFragment newInstance(String title, User child) {
        ChildSettingsDialogFragment frag = new ChildSettingsDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putParcelable("child", child);
        frag.setArguments(args);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getContext();
        return inflater.inflate(R.layout.fragment_child_settings, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parent = (User) ParseUser.getCurrentUser();
        child = getArguments().getParcelable("child");

        tvChildSettingsRequireApprovalStatusInfo = view.findViewById(R.id.tvChildSettingsRequireApprovalStatusInfo);
        bChildSettingsRequireApproval = view.findViewById(R.id.bChildSettingsRequireApproval);
        bChildSettingsUnlinkChild = view.findViewById(R.id.bChildSettingsUnlinkChild);

        checkChildAge();
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

    public void checkChildAge() {
        long today = System.currentTimeMillis();
        long birthday = child.getBirthday().getTime();
        long diffInMillies = today - birthday;
        long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        long diffInYears = diffInDays / 365;
        if (diffInYears < 18) {
            tvChildSettingsRequireApprovalStatusInfo.setVisibility(View.VISIBLE);
            bChildSettingsRequireApproval.setVisibility(View.VISIBLE);
            bChildSettingsUnlinkChild.setVisibility(GONE);
            setApprovalViews();
        } else {
            tvChildSettingsRequireApprovalStatusInfo.setVisibility(GONE);
            bChildSettingsRequireApproval.setVisibility(GONE);
            bChildSettingsUnlinkChild.setVisibility(View.VISIBLE);
            bChildSettingsUnlinkChild.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    parent.removeChild(child);

                    /*ParseACL parseACL = new ParseACL();
                    parseACL.setReadAccess(child.getObjectId(), false);
                    parent.setACL(parseACL);*/

                    parent.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Fragment childListFragment = new ChildListFragment();
                                ParentActivity.fragmentManager.beginTransaction()
                                        .replace(R.id.flParentContainer, childListFragment)
                                        .commit();
                                dismiss();
                            } else {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
        }
    }

    public void setApprovalViews() {
        if (child.getRequiresApproval()) {
            tvChildSettingsRequireApprovalStatusInfo.setText(child.getName()
                    + " currently requires parent approval to complete transactions.");
            bChildSettingsRequireApproval.setText("Turn Parent Approval Off");
        } else {
            tvChildSettingsRequireApprovalStatusInfo.setText(child.getName()
                    + " currently can complete transactions without parent approval.");
            bChildSettingsRequireApproval.setText("Turn Parent Approval On");
        }
        bChildSettingsRequireApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parent.setChildRecentlyUpdated(true);
                parent.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            child.setRequiresApproval(!child.getRequiresApproval());
                            setApprovalViews();
                        } else {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}
