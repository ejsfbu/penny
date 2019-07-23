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

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ejsfbu.app_main.R;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import butterknife.OnClick;

public class CancelGoalDialogFragment extends DialogFragment {

    Context context;

    public CancelGoalDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static CancelGoalDialogFragment newInstance(String title) {
        CancelGoalDialogFragment frag = new CancelGoalDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
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


    @OnClick(R.id.comp_cancel_btn)
    public void compCancel(){
//        ParseQuery<ParseObject> query = ParseQuery.getQuery("Favourite");
//        query.whereEqualTo("Tag", "#" + keyword);
//        query.whereEqualTo("User", ParseUser.getCurrentUser());
//        query.getFirstInBackground(new FindCallBack() {
//
//            @Override
//            public void done(ParseObject object, com.parse.ParseException arg0) {
//                // TODO Auto-generated method stub
//                object.delete();
//                object.saveInBackground();
//            }
//        }););
    }

    @OnClick(R.id.transfer_opt_btn)
    public void onClickTransfer() {
        //find the goal in Parse and delete it before that you want to add that amount saved to the goal that you selected from the screen
        //i think it would be great to implement a search bar and you can select the goal you want out of all the options you have
        //later can grey out that option if there is only 1 goal in your list
    }
}
