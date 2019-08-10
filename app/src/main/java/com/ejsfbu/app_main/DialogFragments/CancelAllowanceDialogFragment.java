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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ejsfbu.app_main.Models.Allowance;
import com.ejsfbu.app_main.Models.User;
import com.ejsfbu.app_main.R;
import com.parse.ParseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CancelAllowanceDialogFragment extends DialogFragment {

    @BindView(R.id.bCancelAllowance)
    Button bCancelAllowance;
    @BindView(R.id.bCancelAllowanceBack)
    Button bCancelAllowanceBack;


    Context context;
    Unbinder unbinder;
    static User currentChild;
    static User currentParent;


    public CancelAllowanceDialogFragment() {
    }

    public static CancelAllowanceDialogFragment newInstance(String title, User user) {
        CancelAllowanceDialogFragment frag = new CancelAllowanceDialogFragment();
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


    public void setButtons() {
        bCancelAllowanceBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        bCancelAllowance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendBackResult();
            }
        });
    }

    public interface CancelAllowanceDialogListener {
        void onFinishCancelAllowanceDialog(Allowance allowance);
    }

    public void sendBackResult() {
        CancelAllowanceDialogFragment.CancelAllowanceDialogListener listener
                = (CancelAllowanceDialogFragment.CancelAllowanceDialogListener) getFragmentManager()
                .findFragmentById(R.id.flParentContainer);
        Allowance deleteAllowance = Allowance.getAllowance(currentChild, currentParent).get(0);
        listener.onFinishCancelAllowanceDialog(deleteAllowance);
        Toast.makeText(context, "Successfully deleted", Toast.LENGTH_LONG).show();
        dismiss();
        return;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        context = getContext();
        return inflater.inflate(R.layout.fragment_cancel_allowance, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
