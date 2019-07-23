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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ejsfbu.app_main.R;
import com.ejsfbu.app_main.models.Goal;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CancelGoalDialogFragment extends DialogFragment {

    Context context;
    Button exit_btn;
    Button comp_cancel_btn;
    Button transfer_opt_btn;
    Unbinder unbinder;
    static Goal currentGoal;

    public CancelGoalDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static CancelGoalDialogFragment newInstance(String title, Goal goal) {
        CancelGoalDialogFragment frag = new CancelGoalDialogFragment();
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
        return inflater.inflate(R.layout.fragment_cancel_goal, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        exit_btn = view.findViewById(R.id.exit_btn);
        comp_cancel_btn = view.findViewById(R.id.comp_cancel_btn);
        transfer_opt_btn = view.findViewById(R.id.transfer_opt_btn);

        setClickers();
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


    public void setClickers(){

        //cancel option
        comp_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //deletes the goal
                Goal.Query query = new Goal.Query();
                query.whereEqualTo("objectId", currentGoal.getObjectId());
                query.findInBackground(new FindCallback<Goal>() {
                    @Override
                    public void done(List<Goal> objects, ParseException e) {
                        if (e == null) {
                            objects.get(0).deleteInBackground();
                            objects.get(0).saveInBackground();
                        } else {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });


    }

    @OnClick(R.id.transfer_opt_btn)
    public void onClickTransfer() {
        //find the goal in Parse and delete it before that you want to add that amount saved to the goal that you selected from the screen


        //i think it would be great to implement a search bar and you can select the goal you want out of all the options you have
        //later can grey out that option if there is only 1 goal in your list
    }
}
