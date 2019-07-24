package com.ejsfbu.app_main.PopupFragments;

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
import com.ejsfbu.app_main.R;
import com.ejsfbu.app_main.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class NeedsParentDialogFragment extends DialogFragment {

    private User user;
    private TextView tvChildCode;
    private Button bParentSignup;
    private Button bSubmit;
    private EditText etParentCode;
    private String parentCode;
    private User parent;

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_needs_parent, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = (User) ParseUser.getCurrentUser();

        tvChildCode = view.findViewById(R.id.tvChildAccountCode);
        tvChildCode.setText(user.getObjectId());

        bParentSignup = view.findViewById(R.id.bParentSignup);
        bParentSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();

                Intent intent = new Intent(getContext(), SignUpActivity.class);
                intent.putExtra("isParent", true);
                getContext().startActivity(intent);
                getActivity().finish();
            }
        });

        etParentCode = view.findViewById(R.id.etParentCode);

        bSubmit = view.findViewById(R.id.bSubmit);
        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parentCode = etParentCode.getText().toString();
                getParentFromCode(parentCode);
            }
        });

        String title = getArguments().getString("title", "Needs Parent");
        getDialog().setTitle(title);
        tvChildCode.requestFocus();
        getDialog().setCanceledOnTouchOutside(false);
    }

    public void onResume() {
        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout((int) (size.x * 0.9), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        // Call super onResume after sizing
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
                            "Parent code is invalid", Toast.LENGTH_LONG).show();
                    parent = null;
                } else {
                    parent = objects.get(0);
                    updateUser();

                }
            }
        });
    }

    public void updateUser() {
        user.addParent(parent);
        user.setNeedsParent(false);
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
