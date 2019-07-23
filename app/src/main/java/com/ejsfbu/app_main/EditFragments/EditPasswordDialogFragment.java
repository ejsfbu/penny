package com.ejsfbu.app_main.EditFragments;

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
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ejsfbu.app_main.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class EditPasswordDialogFragment extends DialogFragment {

    private EditText etPassword;
    private EditText etConfirmPassword;
    private Button bConfirm;
    private Button bCancel;
    private Context context;
    private ParseUser user;

    public EditPasswordDialogFragment() {

    }

    public static EditPasswordDialogFragment newInstance(String title) {
        EditPasswordDialogFragment frag = new EditPasswordDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getContext();
        return inflater.inflate(R.layout.fragment_edit_password, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = ParseUser.getCurrentUser();
        etPassword = view.findViewById(R.id.etPassword);
        etConfirmPassword = view.findViewById(R.id.etConfirmPassword);
        bConfirm = view.findViewById(R.id.bConfirm);
        bCancel = view.findViewById(R.id.bCancel);
        String title = getArguments().getString("title", "Enter Password");
        getDialog().setTitle(title);
        etPassword.requestFocus();
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
        bCancel.setOnClickListener(view -> {
            dismiss();
        });

        bConfirm.setOnClickListener(view -> {
            if (!confirmPasswordsMatch(etPassword.getText().toString(),
                    etConfirmPassword.getText().toString())) {
                Toast.makeText(context, "Passwords do not match.",
                        Toast.LENGTH_LONG).show();
                return;
            }
            user.put("password", etPassword.getText().toString());
            user.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(context, "Password changed successfully.",
                                Toast.LENGTH_SHORT).show();
                        dismiss();
                    } else {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            });
        });
    }

    private boolean confirmPasswordsMatch(String password, String confirmPassword) {
        if (password.equals(confirmPassword)) {
            return true;
        } else {
            return false;
        }
    }
}
