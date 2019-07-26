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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.ejsfbu.app_main.R;
import com.ejsfbu.app_main.models.BankAccount;
import com.ejsfbu.app_main.models.Transaction;
import com.ejsfbu.app_main.models.User;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class DepositDialogFragment extends DialogFragment {
    // View objects
    private EditText etAmount;
    private Button bConfirm;
    private Button bCancel;
    private Context context;
    private User user;
    private Spinner spinner;
    private List<BankAccount> banks;

    public DepositDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static DepositDialogFragment newInstance(String title) {
        DepositDialogFragment frag = new DepositDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getContext();
        return inflater.inflate(R.layout.fragment_edit_deposit, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = (User) ParseUser.getCurrentUser();
        // Get field from view
        etAmount = view.findViewById(R.id.etAmount);
        bConfirm = view.findViewById(R.id.bConfirm);
        bCancel = view.findViewById(R.id.bCancel);
        spinner = view.findViewById(R.id.sBanks);
        fillData();
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        etAmount.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        setOnClick();
    }

    // Defines the listener interface
    public interface DepositDialogListener {
        void onFinishEditDialog(String bankName, Double amount);
    }

    // Call this method to send the data back to the parent fragment
    public void sendBackResult(String bankName, Double amount) {
        DepositDialogListener listener = (DepositDialogListener) getFragmentManager().findFragmentById(R.id.flMainContainer);
        listener.onFinishEditDialog(bankName, amount);
        dismiss();
    }


    public void onResume() {
        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout((int) (size.x * 0.85), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        // Call super onResume after sizing
        super.onResume();
    }

    private void setOnClick() {
        bCancel.setOnClickListener(view -> {
            dismiss();
        });

        bConfirm.setOnClickListener(view -> {
            String bankName = spinner.getSelectedItem().toString();
            if (bankName.equals("No verified banks available")) {
                Toast.makeText(context, "No bank account selected.", Toast.LENGTH_SHORT).show();
                return;
            }
            Double amount = Double.valueOf(etAmount.getText().toString());
            sendBackResult(bankName, amount);
        });
    }

    private void fillData() {
        banks = user.getVerifiedBanks();
        ArrayList<String> array = new ArrayList<>();
        for (BankAccount bank: banks) {
            array.add(bank.getBankName());
        }
        if (array.size() == 0) {
            array.add("No verified banks available");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}
