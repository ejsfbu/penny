package com.ejsfbu.app_main.DialogFragments;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
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
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;

public class EditNameDialogFragment extends DialogFragment {
    // View objects
    private EditText etEditNameFirstName;
    private EditText etEditNameMiddleInitial;
    private EditText etEditNameLastName;
    private Button bEditNameConfirm;
    private Button bEditNameCancel;
    private Context context;
    private User user;

    public EditNameDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static EditNameDialogFragment newInstance(String title) {
        EditNameDialogFragment frag = new EditNameDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getContext();
        return inflater.inflate(R.layout.fragment_edit_name, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = (User) ParseUser.getCurrentUser();
        // Get field from view
        etEditNameFirstName = view.findViewById(R.id.etEditNameFirstName);
        etEditNameMiddleInitial = view.findViewById(R.id.etEditNameMiddleInitial);
        etEditNameLastName = view.findViewById(R.id.etEditNameLastName);
        bEditNameConfirm = view.findViewById(R.id.bEditNameConfirm);
        bEditNameCancel = view.findViewById(R.id.bEditNameCancel);
        fillData();
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        etEditNameFirstName.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        setOnClick();
    }

    // Defines the listener interface
    public interface EditNameDialogListener {
        void onFinishEditDialog();
    }

    // Call this method to send the data back to the parent fragment
    public void sendBackResult() {
        ArrayList<Fragment> fragments = (ArrayList<Fragment>) getFragmentManager().getFragments();
        String fragmentTag = fragments.get(1).getTag();
        int fragmentId = fragments.get(1).getId();
        EditNameDialogListener listener;
        if (fragments.size() > 2) {
            listener = (EditNameDialogListener) getFragmentManager()
                    .findFragmentById(fragmentId);
        } else {
            listener = (EditNameDialogListener) getFragmentManager()
                    .findFragmentByTag(fragmentTag).getContext();
        }
        listener.onFinishEditDialog();
        dismiss();
    }


    public void onResume() {
        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout((int) (size.x * 0.85), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        // Call super onResume after sizing
        super.onResume();
    }

    private void setOnClick() {
        bEditNameCancel.setOnClickListener(view -> {
            dismiss();
        });

        bEditNameConfirm.setOnClickListener(view -> {
            String name = etEditNameFirstName.getText().toString() + " "
                    + etEditNameMiddleInitial.getText().toString() + " "
                    + etEditNameLastName.getText().toString();
            user.put("name", name);
            user.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(context, "Name changed successfully.",
                                Toast.LENGTH_SHORT).show();
                        sendBackResult();
                    } else {
                        e.printStackTrace();
                    }
                }
            });
        });
    }

    // fill name fields depending on user's name
    private void fillData() {
        String fullName = user.get("name").toString();
        String arr[] = fullName.split(" ");
        switch (arr.length) {
            case 1:
                etEditNameFirstName.setText(arr[0]);
                break;
            case 2:
                etEditNameFirstName.setText(arr[0]);
                etEditNameLastName.setText(arr[1]);
                break;
            case 3:
                etEditNameFirstName.setText(arr[0]);
                etEditNameLastName.setText(arr[2]);
                etEditNameMiddleInitial.setText(arr[1]);
                break;
            default:
                Log.d("Name", fullName);

        }
    }
}
