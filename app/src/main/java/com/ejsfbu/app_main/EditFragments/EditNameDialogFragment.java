package com.ejsfbu.app_main.EditFragments;

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

import com.ejsfbu.app_main.Activities.LoginActivity;
import com.ejsfbu.app_main.Fragments.ProfileFragment;
import com.ejsfbu.app_main.R;
import com.ejsfbu.app_main.models.User;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class EditNameDialogFragment extends DialogFragment {
    // View objects
    private EditText etFirstName;
    private EditText etMiddleInitial;
    private EditText etLastName;
    private Button bConfirm;
    private Button bCancel;
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
        etFirstName = view.findViewById(R.id.etFirstName);
        etMiddleInitial = view.findViewById(R.id.etMiddleInitial);
        etLastName = view.findViewById(R.id.etLastName);
        bConfirm = view.findViewById(R.id.bConfirm);
        bCancel = view.findViewById(R.id.bCancel);
        fillData();
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        etFirstName.requestFocus();
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
        // Notice the use of `getTargetFragment` which will be set when the dialog is displayed
        EditNameDialogListener listener = (EditNameDialogListener) getFragmentManager().findFragmentById(R.id.flContainer);
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
            String name = etFirstName.getText().toString() + " "
                    + etMiddleInitial.getText().toString() + " "
                    + etLastName.getText().toString();
            user.put("name",  name);
            user.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(context, "Name changed successfully.", Toast.LENGTH_SHORT).show();
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
                etFirstName.setText(arr[0]);
                break;
            case 2:
                etFirstName.setText(arr[0]);
                etLastName.setText(arr[1]);
                break;
            case 3:
                etFirstName.setText(arr[0]);
                etLastName.setText(arr[2]);
                etMiddleInitial.setText(arr[1]);
                break;
            default:
                Log.d("Name", fullName);

        }
    }
}
