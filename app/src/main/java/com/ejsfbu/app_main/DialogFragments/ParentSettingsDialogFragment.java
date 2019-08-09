package com.ejsfbu.app_main.DialogFragments;

import android.content.Context;
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

import com.ejsfbu.app_main.Models.User;
import com.ejsfbu.app_main.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class ParentSettingsDialogFragment extends DialogFragment {

    private TextView tvParentSettingsRequireApprovalStatusInfo;
    private Button bParentSettingsRequireApproval;

    private Context context;

    private User child;

    public ParentSettingsDialogFragment() {

    }

    public static ParentSettingsDialogFragment newInstance(String title) {
        ParentSettingsDialogFragment frag = new ParentSettingsDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        context = getContext();
        return inflater.inflate(R.layout.fragment_parent_settings, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        child = (User) ParseUser.getCurrentUser();

        tvParentSettingsRequireApprovalStatusInfo
                = view.findViewById(R.id.tvParentSettingsRequireApprovalStatusInfo);
        bParentSettingsRequireApproval = view.findViewById(R.id.bParentSettingsRequireApproval);

        setApprovalViews();
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

    public void setApprovalViews() {
        if (child.getRequiresApproval()) {
            tvParentSettingsRequireApprovalStatusInfo.setText(
                    "You currently require parent approval to complete transactions.");
            bParentSettingsRequireApproval.setText("Turn Parent Approval Off");
        } else {
            tvParentSettingsRequireApprovalStatusInfo.setText(
                    "You currently can complete transactions without parent approval.");
            bParentSettingsRequireApproval.setText("Turn Parent Approval On");
        }
        bParentSettingsRequireApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                child.setRequiresApproval(!child.getRequiresApproval());
                child.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
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
