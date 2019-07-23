package com.ejsfbu.app_main.Fragments;

import android.os.Binder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ejsfbu.app_main.Activities.MainActivity;
import com.ejsfbu.app_main.EditFragments.CancelGoalDialogFragment;
import com.ejsfbu.app_main.EditFragments.EditNameDialogFragment;
import com.ejsfbu.app_main.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.ejsfbu.app_main.Activities.MainActivity.fragmentManager;

public class EditGoalFragment extends Fragment {

    @BindView(R.id.cancel_goal_btn) Button cancel_goal_btn;
    private Unbinder unbinder;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_goal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.cancel_goal_btn)
    public void cancelGoal() {
        showCancelGoalDialog();
    }

    private void showCancelGoalDialog() {
        CancelGoalDialogFragment cancel = CancelGoalDialogFragment.newInstance("Cancel Goal");
        cancel.show(fragmentManager, "fragment_cancel_goal");

    }

}
