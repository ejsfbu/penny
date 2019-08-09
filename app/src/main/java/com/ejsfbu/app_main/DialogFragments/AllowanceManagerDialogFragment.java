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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ejsfbu.app_main.Models.BankAccount;
import com.ejsfbu.app_main.Models.User;
import com.ejsfbu.app_main.R;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AllowanceManagerDialogFragment extends DialogFragment {


    @BindView(R.id.bCancelAllowance)
    Button bCancelAllowance;
    @BindView(R.id.bEditAllowance)
    Button bEditAllowance;

    Context context;
    Unbinder unbinder;
    static User currentChild;
    static User currentParent;

    public AllowanceManagerDialogFragment() { }

    public static AllowanceManagerDialogFragment newInstance(String title, User user) {
        AllowanceManagerDialogFragment frag = new AllowanceManagerDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        currentChild = user;
        currentParent = (User) ParseUser.getCurrentUser();
        return frag;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);
        setButtons();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getContext();
        return inflater.inflate(R.layout.fragment_allowance_manager, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void setButtons() {
        bEditAllowance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditAllowance();
            }
        });

        bCancelAllowance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCancelAllowance();
            }
        });
    }

    private void showEditAllowance() {
        EditAllowanceDialogFragment managerDialogFragment = EditAllowanceDialogFragment.newInstance("Edit Allowance", currentChild);
        managerDialogFragment.show(getFragmentManager(), "fragment_edit_allowance");
        dismiss();
        return;
    }

    private void showCancelAllowance() {
        CancelAllowanceDialogFragment cancelAllowance = CancelAllowanceDialogFragment.newInstance("Cancel Allowance", currentChild);
        cancelAllowance.show(getFragmentManager(), "fragment_cancel_allowance");
        dismiss();
        return;
    }

    public void onResume() {
        Window window = getDialog().getWindow();
        Point size = new Point();
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout((int) (size.x * 0.99), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        super.onResume();
    }
}
