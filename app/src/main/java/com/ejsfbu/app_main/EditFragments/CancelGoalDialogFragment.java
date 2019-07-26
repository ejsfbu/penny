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

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.ejsfbu.app_main.Fragments.GoalsListFragment;
import com.ejsfbu.app_main.Fragments.TransferGoalFragment;
import com.ejsfbu.app_main.R;
import com.ejsfbu.app_main.models.BankAccount;
import com.ejsfbu.app_main.models.Goal;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.List;

import butterknife.Unbinder;

public class CancelGoalDialogFragment extends DialogFragment {

    Context context;
    Button exit_btn;
    Button comp_cancel_btn;
    Button transfer_opt_btn;
    Unbinder unbinder;
    static Goal cancelledGoal;
    List<BankAccount> bankAccounts;

    public CancelGoalDialogFragment() {

    }

    public static CancelGoalDialogFragment newInstance(String title, Goal goal) {
        CancelGoalDialogFragment frag = new CancelGoalDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        cancelledGoal = goal;
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
        Window window = getDialog().getWindow();
        Point size = new Point();
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout((int) (size.x * 0.75), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        super.onResume();
    }


    public void setClickers() {
        comp_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Double saved = cancelledGoal.getSaved();


                Goal.Query query = new Goal.Query();
                query.whereEqualTo("objectId", cancelledGoal.getObjectId());
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

                GoalsListFragment goalsListFragment = new GoalsListFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.flMainContainer, goalsListFragment).commit();
                dismiss();
            }
        });

        transfer_opt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("Cancelled Goal", cancelledGoal);
                Fragment transferGoalFragment = new TransferGoalFragment();
                transferGoalFragment.setArguments(bundle);
                getFragmentManager().beginTransaction()
                        .replace(R.id.flMainContainer, transferGoalFragment).commit();
                dismiss();
            }
        });

        exit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public static Goal getCancelledGoal() {
        return cancelledGoal;
    }
}
