package com.ejsfbu.app_main.EditFragments;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ejsfbu.app_main.R;
import com.ejsfbu.app_main.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

import butterknife.OnTextChanged;

public class EditUsernameDialogFragment extends DialogFragment {
    // View objects
    private EditText etUsername;
    private Button bConfirm;
    private Button bCancel;
    private Context context;
    private ParseUser user;
    private boolean usernameUnique;

    public EditUsernameDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static EditUsernameDialogFragment newInstance(String title) {
        EditUsernameDialogFragment frag = new EditUsernameDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getContext();
        return inflater.inflate(R.layout.fragment_edit_username, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = ParseUser.getCurrentUser();
        // Get field from view
        etUsername = view.findViewById(R.id.etUserName);
        bConfirm = view.findViewById(R.id.bConfirm);
        bCancel = view.findViewById(R.id.bCancel);
        etUsername.setText(user.getUsername());
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        etUsername.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        setOnClick();
        etUsername.addTextChangedListener(textChanged);
    }

    public void onResume() {
        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        // Set the width of the dialog proportional to 75% of the screen width
        window.setLayout((int) (size.x * 0.75), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        // Call super onResume after sizing
        super.onResume();
    }

    private void setOnClick() {
        bCancel.setOnClickListener(view -> {
            dismiss();
        });

        bConfirm.setOnClickListener(view -> {
            if (!usernameUnique) {
                Toast.makeText(getContext(), "Username is taken", Toast.LENGTH_LONG).show();
                return;
            }
            user.put("username", etUsername.getText().toString());
            user.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(context, "Username changed successfully.", Toast.LENGTH_SHORT).show();
                        dismiss();
                    } else {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            });
        });
    }

    public void checkUsernameUnique() {
        String username = etUsername.getText().toString();
        User.Query userQuery = new User.Query();
        userQuery.testUsername(username);
        userQuery.findInBackground(new FindCallback<User>() {
            @Override
            public void done(List<User> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() == 0) {
                        etUsername.setTextColor(EditUsernameDialogFragment.this.getResources()
                                .getColor(android.R.color.holo_green_dark));
                        usernameUnique = true;
                    } else {
                        if (username.equals(user.getUsername())) {
                            etUsername.setTextColor(EditUsernameDialogFragment.this.getResources()
                                    .getColor(android.R.color.holo_green_dark));
                            usernameUnique = true;
                        } else {
                            etUsername.setTextColor(EditUsernameDialogFragment.this.getResources()
                                    .getColor(android.R.color.holo_red_dark));
                            usernameUnique = false;
                        }
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    private final TextWatcher textChanged = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            checkUsernameUnique();
        }

        public void afterTextChanged(Editable s) {
        }
    };
}
