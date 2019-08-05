package com.ejsfbu.app_main.DialogFragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ejsfbu.app_main.Models.Goal;
import com.ejsfbu.app_main.Models.Reward;
import com.ejsfbu.app_main.Models.User;

public class SetUpAutoPaymentDialogFragment extends DialogFragment {

    public SetUpAutoPaymentDialogFragment() {

    }

    public static SetUpAutoPaymentDialogFragment newInstance(Goal goal, User user) {
        SetUpAutoPaymentDialogFragment setUpAutoPaymentDialogFragment = new SetUpAutoPaymentDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable("goal", goal);
        args.putParcelable("user", user);
        setUpAutoPaymentDialogFragment.setArguments(args);
        return setUpAutoPaymentDialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }
}
