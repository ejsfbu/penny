package com.ejsfbu.app_main.DialogFragments;

import android.content.Context;
import android.content.Intent;
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

import com.ejsfbu.app_main.Activities.MainActivity;
import com.ejsfbu.app_main.Fragments.AddBankFragment;
import com.ejsfbu.app_main.R;
import com.ejsfbu.app_main.Models.BankAccount;
import com.ejsfbu.app_main.Models.User;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import static com.ejsfbu.app_main.Fragments.GoalDetailsFragment.BANK_REQUEST_CODE;

public class DepositDialogFragment extends DialogFragment {

    private EditText etDepositAmount;
    private Button bDepositConfirm;
    private Button bDepositCancel;
    private Context context;
    private User user;
    private Spinner spinner;
    private List<BankAccount> banks;
    private Double amountLeft;
    private Double depositLimit;

    public DepositDialogFragment() {

    }

    public static DepositDialogFragment newInstance(String title, Double amountLeft, Double depositLimit) {
        DepositDialogFragment frag = new DepositDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putDouble("amount", amountLeft);
        args.putDouble("limit", depositLimit);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getContext();
        return inflater.inflate(R.layout.fragment_deposit, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = (User) ParseUser.getCurrentUser();
        etDepositAmount = view.findViewById(R.id.etDepositAmount);
        bDepositConfirm = view.findViewById(R.id.bDepositConfirm);
        bDepositCancel = view.findViewById(R.id.bDepositCancel);
        spinner = view.findViewById(R.id.sBanks);
        setOnClick();
        fillData();
        String title = getArguments().getString("title", "Enter Name");
        amountLeft = getArguments().getDouble("amount");
        depositLimit = getArguments().getDouble("limit");
        getDialog().setTitle(title);
        etDepositAmount.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    }

    public interface DepositDialogListener {
        void onFinishEditDialog(String bankName, Double amount);
    }

    public void sendBackResult(String bankName, Double amount) {
        DepositDialogListener listener = (DepositDialogListener) getFragmentManager()
                .findFragmentById(R.id.flMainContainer);
        listener.onFinishEditDialog(bankName, amount);
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
        bDepositCancel.setOnClickListener(view -> {
            dismiss();
        });

        bDepositConfirm.setOnClickListener(view -> {
            String bankName = spinner.getSelectedItem().toString();
            if (bankName.equals("No verified banks available")) {
                Toast.makeText(context, "No bank account selected.",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            String amountString = etDepositAmount.getText().toString();
            Double amount;
            if (amountString.equals("")) {
                Toast.makeText(context,"Please enter a value.", Toast.LENGTH_SHORT).show();
                return;
            } else {
                amount = Double.valueOf(amountString);
            }
            if (amount == 0.0) {
                Toast.makeText(context, "Enter a value greater than 0.",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (amount > amountLeft) {
                Toast.makeText(context, "Amount exceeds remaining cost.",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (amount > depositLimit) {
                Toast.makeText(context, "Amount exceeds remaining cost including pending transactions.",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            sendBackResult(bankName, amount);
        });
    }

    private void fillData() {
        banks = user.getVerifiedBanks();
        ArrayList<String> array = new ArrayList<>();
        if (banks != null) {
            for (BankAccount bank : banks) {
                array.add(bank.getBankName());
            }
        }
        if (array.size() == 0) {
            array.add("No verified banks available");
            bDepositConfirm.setText("Add bank");
            bDepositConfirm.setOnClickListener(view -> {
                Fragment addBankFragment = new AddBankFragment();
                MainActivity.fragmentManager.beginTransaction()
                        .replace(R.id.flMainContainer, addBankFragment)
                .commit();
                dismiss();
            });
        }
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

}
