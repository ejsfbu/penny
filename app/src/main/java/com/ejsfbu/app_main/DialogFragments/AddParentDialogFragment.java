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
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

public class AddParentDialogFragment extends DialogFragment {

    private Context context;
    private User user;

    private EditText etAddParentCode;
    private Button bAddParentCancel;
    private Button bAddParentConfirm;

    public AddParentDialogFragment() {

    }

    public static AddParentDialogFragment newInstance(String title) {
        AddParentDialogFragment frag = new AddParentDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getContext();
        return inflater.inflate(R.layout.fragment_add_parent, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = (User) ParseUser.getCurrentUser();

        etAddParentCode = view.findViewById(R.id.etAddParentCode);
        bAddParentCancel = view.findViewById(R.id.bAddParentCancel);
        bAddParentConfirm = view.findViewById(R.id.bAddParentConfirm);

        String title = getArguments().getString("title", "Add Parent");
        getDialog().setTitle(title);
        etAddParentCode.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        setOnClick();
    }

    public interface AddParentDialogListener {
        void onFinishEditDialog();
    }

    public void sendBackResult() {
        ArrayList<Fragment> fragments = (ArrayList<Fragment>) getFragmentManager().getFragments();
        String fragmentTag = fragments.get(0).getTag();
        int fragmentId = fragments.get(0).getId();
        AddParentDialogListener listener;
        if (fragments.size() > 1) {
            listener = (AddParentDialogListener) getFragmentManager()
                    .findFragmentById(fragmentId);
        } else {
            listener = (AddParentDialogListener) getFragmentManager()
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
        bAddParentCancel.setOnClickListener(view -> {
            dismiss();
        });

        bAddParentConfirm.setOnClickListener(view -> {
            checkParentCode();
        });
    }

    private void checkParentCode() {
        User.Query userQuery = new User.Query();
        userQuery.whereEqualTo("objectId", etAddParentCode.getText().toString());
        userQuery.findInBackground(new FindCallback<User>() {
            @Override
            public void done(List<User> objects, ParseException e) {
                if (e == null) {
                    User parent = objects.get(0);
                    if (parent.getIsParent()) {
                        user.addParent(objects.get(0));
                        user.setRecentlyAddedParent(true);

                        /*ParseACL parseACL = new ParseACL();
                        parseACL.setReadAccess(parent.getObjectId(), true);
                        parseACL.setWriteAccess(parent.getObjectId(), true);
                        user.setACL(parseACL);*/

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
                        Toast.makeText(context, "Invalid Parent Code",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, "Invalid Parent Code",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
    }
}
