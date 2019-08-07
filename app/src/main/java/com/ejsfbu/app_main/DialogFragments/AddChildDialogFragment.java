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
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddChildDialogFragment extends DialogFragment {

    private Context context;
    private User user;

    private EditText etAddChildCode;
    private Button bAddChildCancel;
    private Button bAddChildConfirm;

    public AddChildDialogFragment() {

    }

    public static AddChildDialogFragment newInstance(String title) {
        AddChildDialogFragment frag = new AddChildDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getContext();
        return inflater.inflate(R.layout.fragment_add_child, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = (User) ParseUser.getCurrentUser();

        etAddChildCode = view.findViewById(R.id.etAddChildCode);
        bAddChildCancel = view.findViewById(R.id.bAddChildCancel);
        bAddChildConfirm = view.findViewById(R.id.bAddChildConfirm);

        String title = getArguments().getString("title", "Add Child");
        getDialog().setTitle(title);
        etAddChildCode.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        setOnClick();
    }

    public interface AddChildDialogListener {
        void onFinishEditDialog();
    }

    public void sendBackResult() {
        ArrayList<Fragment> fragments = (ArrayList<Fragment>) getFragmentManager().getFragments();
        String fragmentTag = fragments.get(0).getTag();
        int fragmentId = fragments.get(0).getId();
        AddChildDialogListener listener;
        if (fragments.size() > 1) {
            listener = (AddChildDialogListener) getFragmentManager()
                    .findFragmentById(fragmentId);
        } else {
            listener = (AddChildDialogListener) getFragmentManager()
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
        bAddChildCancel.setOnClickListener(view -> {
            dismiss();
        });

        bAddChildConfirm.setOnClickListener(view -> {
            checkChildCode();
        });
    }

    private void checkChildCode() {
        User.Query userQuery = new User.Query();
        userQuery.whereEqualTo("objectId", etAddChildCode.getText().toString());
        userQuery.findInBackground(new FindCallback<User>() {
            @Override
            public void done(List<User> objects, ParseException e) {
                if (e == null) {
                    User child = objects.get(0);
                    if (!child.getIsParent()) {
                        user.addChild(objects.get(0));
                        user.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    sendBackResult();
                                } else {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(context, "Invalid Child Code",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, "Invalid Child Code",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
    }
}
