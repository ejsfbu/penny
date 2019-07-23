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
import androidx.fragment.app.Fragment;

import com.ejsfbu.app_main.R;
import com.ejsfbu.app_main.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class EditEmailDialogFragment extends DialogFragment {

    private EditText etEmail;
    private Button bConfirm;
    private Button bCancel;
    private Context context;
    private ParseUser user;
    private boolean emailUnique;

    public EditEmailDialogFragment() {

    }

    public static EditEmailDialogFragment newInstance(String title) {
        EditEmailDialogFragment frag = new EditEmailDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getContext();
        return inflater.inflate(R.layout.fragment_edit_email, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = ParseUser.getCurrentUser();
        etEmail = view.findViewById(R.id.etEmail);
        bConfirm = view.findViewById(R.id.bConfirm);
        bCancel = view.findViewById(R.id.bCancel);
        etEmail.setText(user.getEmail());
        String title = getArguments().getString("title", "Enter Email");
        getDialog().setTitle(title);
        etEmail.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        setOnClick();
        etEmail.addTextChangedListener(textChanged);
    }

    public interface EditEmailDialogListener {
        void onFinishEditDialog();
    }

    public void sendBackResult() {
        ArrayList<Fragment> fragments = (ArrayList<Fragment>) getFragmentManager().getFragments();
        String fragmentTag = fragments.get(0).getTag();
        int fragmentId = fragments.get(0).getId();
        EditEmailDialogListener listener;
        if (fragments.size() > 1) {
            listener = (EditEmailDialogListener) getFragmentManager()
                    .findFragmentById(fragmentId);
        } else {
            listener = (EditEmailDialogListener) getFragmentManager()
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
        bCancel.setOnClickListener(view -> {
            dismiss();
        });

        bConfirm.setOnClickListener(view -> {
            if (!emailUnique) {
                Toast.makeText(getContext(), "Email is already associated with an account",
                        Toast.LENGTH_LONG).show();
                return;
            }
            user.put("email", etEmail.getText().toString());
            user.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(context, "Email changed successfully.",
                                Toast.LENGTH_SHORT).show();
                        sendBackResult();
                    } else {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            });
        });
    }

    public void checkEmailUnique() {
        String email = etEmail.getText().toString();
        User.Query userQuery = new User.Query();
        userQuery.testEmail(email);
        userQuery.findInBackground(new FindCallback<User>() {
            @Override
            public void done(List<User> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() == 0) {
                        etEmail.setTextColor(EditEmailDialogFragment.this.getResources()
                                .getColor(android.R.color.holo_green_dark));
                        emailUnique = true;
                    } else {
                        if (email.equals(user.getEmail())) {
                            etEmail.setTextColor(EditEmailDialogFragment.this.getResources()
                                    .getColor(android.R.color.holo_red_dark));
                            emailUnique = false;
                        }
                        etEmail.setTextColor(EditEmailDialogFragment.this.getResources()
                                .getColor(android.R.color.holo_green_dark));
                        emailUnique = true;
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
            checkEmailUnique();
        }

        public void afterTextChanged(Editable s) {
        }
    };
}
