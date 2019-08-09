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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.ejsfbu.app_main.Models.Goal;
import com.ejsfbu.app_main.R;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.util.ArrayList;

public class EditGoalNameDialogFragment extends DialogFragment {

    Context context;
    EditText newName;
    Button bCancel;
    Button bConfirm;
    TextView title;
    static Goal currentGoal;

    public EditGoalNameDialogFragment() {

    }

    public static EditGoalNameDialogFragment newInstance(String title, Goal goal) {
        EditGoalNameDialogFragment frag = new EditGoalNameDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        currentGoal = goal;
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getContext();
        return inflater.inflate(R.layout.fragment_edit_goal_name, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        title = view.findViewById(R.id.tvEditGoalNameTitle);
        newName = view.findViewById(R.id.etEditGoalNameGoalName);
        bCancel = view.findViewById(R.id.bEditGoalNameCancel);
        bConfirm = view.findViewById(R.id.bEditGoalNameConfirm);

        setListeners();
    }

    public void setListeners() {
        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        bConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String goalName = newName.getText().toString();
                if (goalName.equals("")) {
                    Toast.makeText(context, "Please enter a new goal name",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                currentGoal.setName(goalName);
                currentGoal.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            sendBackResult();
                        } else {
                            Toast.makeText(context, "Failed to Update Goal Name",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    // Defines the listener interface
    public interface EditGoalNameDialogListener {
        void onFinishEditThisDialog();
    }

    // Call this method to send the data back to the parent fragment
    public void sendBackResult() {
        ArrayList<Fragment> fragments = (ArrayList<Fragment>) getFragmentManager().getFragments();
        String fragmentTag = fragments.get(0).getTag();
        int fragmentId = fragments.get(1).getId();
        EditGoalNameDialogListener listener;
        listener = (EditGoalNameDialogListener) getFragmentManager()
                .findFragmentById(fragmentId);
        listener.onFinishEditThisDialog();
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
}
