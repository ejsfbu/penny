package com.ejsfbu.app_main.DialogFragments;

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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ejsfbu.app_main.Activities.AddGoalActivity;
import com.ejsfbu.app_main.Activities.SignUpActivity;
import com.ejsfbu.app_main.Models.User;
import com.ejsfbu.app_main.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class NeedsParentDialogFragment extends DialogFragment {

    private TextView tvNeedsParentChildCode;
    private Button bNeedsParentParentSignUp;
    private Button bNeedsParentSubmit;
    private EditText etNeedsParentParentCode;

    private String parentCode;
    private User parent;
    private User user;

    public NeedsParentDialogFragment() {

    }

    public static NeedsParentDialogFragment newInstance(String title) {
        NeedsParentDialogFragment needsParentDialogFragment = new NeedsParentDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        needsParentDialogFragment.setArguments(args);
        return needsParentDialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_needs_parent, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = (User) ParseUser.getCurrentUser();

        tvNeedsParentChildCode = view.findViewById(R.id.tvNeedsParentChildCode);
        tvNeedsParentChildCode.setText(user.getObjectId());

        bNeedsParentParentSignUp = view.findViewById(R.id.bNeedsParentParentSignUp);
        bNeedsParentParentSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();

                Intent intent = new Intent(getContext(), SignUpActivity.class);
                intent.putExtra("isParent", true);
                getContext().startActivity(intent);
                getActivity().finish();
            }
        });

        etNeedsParentParentCode = view.findViewById(R.id.etNeedsParentParentCode);

        bNeedsParentSubmit = view.findViewById(R.id.bNeedsParentSubmit);
        bNeedsParentSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parentCode = etNeedsParentParentCode.getText().toString();
                getParentFromCode(parentCode);
            }
        });

        String title = getArguments().getString("title", "Needs Parent");
        getDialog().setTitle(title);
        tvNeedsParentChildCode.requestFocus();
        getDialog().setCanceledOnTouchOutside(false);
    }

    @Override
    public void onResume() {
        Window window = getDialog().getWindow();
        Point size = new Point();
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout((int) (size.x * 0.9), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        super.onResume();
    }

    public void getParentFromCode(String parentCode) {
        User.Query userQuery = new User.Query();
        userQuery.whereEqualTo("objectId", parentCode);
        userQuery.findInBackground(new FindCallback<User>() {
            @Override
            public void done(List<User> objects, ParseException e) {
                if (objects.size() == 0) {
                    Toast.makeText(NeedsParentDialogFragment.this.getContext(),
                            "Invalid Parent Code", Toast.LENGTH_LONG).show();
                    parent = null;
                } else {
                    parent = objects.get(0);
                    if (parent.getIsParent()) {
                        updateUser();
                    } else {
                        Toast.makeText(NeedsParentDialogFragment.this.getContext(),
                                "Invalid Parent Code", Toast.LENGTH_LONG).show();
                        parent = null;
                    }

                }
            }
        });
    }

    public void updateUser() {
        user.addParent(parent);
        user.setNeedsParent(false);

        /*ParseACL parseACL = new ParseACL();
        parseACL.setReadAccess(parent.getObjectId(), true);
        parseACL.setWriteAccess(parent.getObjectId(), true);
        user.setACL(parseACL);*/

        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(NeedsParentDialogFragment.this.getContext(),
                            "Account Validated", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(
                            NeedsParentDialogFragment.this.getContext(),
                            AddGoalActivity.class);
                    NeedsParentDialogFragment.this.getContext().startActivity(intent);
                    NeedsParentDialogFragment.this.getActivity().finish();
                } else {
                    Toast.makeText(NeedsParentDialogFragment.this.getContext(),
                            "Validation Error",
                            Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }
}
