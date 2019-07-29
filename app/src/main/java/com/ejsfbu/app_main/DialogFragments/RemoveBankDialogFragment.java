package com.ejsfbu.app_main.DialogFragments;

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

import com.ejsfbu.app_main.R;

public class RemoveBankDialogFragment extends DialogFragment {

    private Button bRemoveBankConfirm;
    private Button bRemoveBankCancel;

    public RemoveBankDialogFragment() {

    }

    public static RemoveBankDialogFragment newInstance(String title) {
        RemoveBankDialogFragment frag = new RemoveBankDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_remove_bank, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bRemoveBankConfirm = view.findViewById(R.id.bRemoveBankConfirm);
        bRemoveBankCancel = view.findViewById(R.id.bRemoveBankCancel);
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        setOnClick();
    }

    public interface RemoveBankDialogListener {
        void onFinishEditDialog();
    }

    public void sendBackResult() {
        RemoveBankDialogListener listener = (RemoveBankDialogListener) getFragmentManager()
                .findFragmentById(R.id.flMainContainer);
        listener.onFinishEditDialog();
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

    private void setOnClick() {
        bRemoveBankCancel.setOnClickListener(view -> {
            dismiss();
        });

        bRemoveBankConfirm.setOnClickListener(view -> {
            sendBackResult();
        });
    }
}
