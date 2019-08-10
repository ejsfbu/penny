package com.ejsfbu.app_main.DialogFragments;

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
import androidx.fragment.app.Fragment;

import com.ejsfbu.app_main.Models.User;
import com.ejsfbu.app_main.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class EditUsernameDialogFragment extends DialogFragment {

    private EditText etEditUsernameUsername;
    private Button bEditUsernameConfirm;
    private Button bEditUsernameCancel;
    private Context context;
    private ParseUser user;
    private boolean usernameUnique;

    public EditUsernameDialogFragment() {

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
        etEditUsernameUsername = view.findViewById(R.id.etEditUsernameUsername);
        bEditUsernameConfirm = view.findViewById(R.id.bEditUsernameConfirm);
        bEditUsernameCancel = view.findViewById(R.id.bEditUsernameCancel);
        etEditUsernameUsername.setText(user.getUsername());
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);
        etEditUsernameUsername.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        setOnClick();
        etEditUsernameUsername.addTextChangedListener(textChanged);
    }

    public interface EditUsernameDialogListener {
        void onFinishEditDialog();
    }

    public void sendBackResult() {
        ArrayList<Fragment> fragments = (ArrayList<Fragment>) getFragmentManager().getFragments();
        String fragmentTag = fragments.get(0).getTag();
        int fragmentId = fragments.get(0).getId();
        EditUsernameDialogListener listener;
        if (fragments.size() > 1) {
            listener = (EditUsernameDialogListener) getFragmentManager()
                    .findFragmentById(fragmentId);
        } else {
            listener = (EditUsernameDialogListener) getFragmentManager()
                    .findFragmentByTag(fragmentTag).getContext();
        }
        listener.onFinishEditDialog();
        dismiss();
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
        bEditUsernameCancel.setOnClickListener(view -> {
            dismiss();
        });

        bEditUsernameConfirm.setOnClickListener(view -> {
            if (!usernameUnique) {
                Toast.makeText(getContext(), "Username is taken", Toast.LENGTH_LONG).show();
                return;
            }
            user.put("username", etEditUsernameUsername.getText().toString());
            user.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        sendBackResult();
                    } else {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            });
        });
    }

    public void checkUsernameUnique() {
        String username = etEditUsernameUsername.getText().toString();
        User.Query userQuery = new User.Query();
        userQuery.testUsername(username);
        userQuery.findInBackground(new FindCallback<User>() {
            @Override
            public void done(List<User> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() == 0) {
                        etEditUsernameUsername.setTextColor(
                                EditUsernameDialogFragment.this.getResources()
                                        .getColor(android.R.color.holo_green_dark));
                        usernameUnique = true;
                    } else {
                        if (username.equals(user.getUsername())) {
                            etEditUsernameUsername.setTextColor(
                                    EditUsernameDialogFragment.this.getResources()
                                            .getColor(android.R.color.holo_green_dark));
                            usernameUnique = true;
                        } else {
                            etEditUsernameUsername.setTextColor(
                                    EditUsernameDialogFragment.this.getResources()
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
