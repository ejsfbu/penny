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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ejsfbu.app_main.Activities.ParentActivity;
import com.ejsfbu.app_main.Adapters.ChildAdapter;
import com.ejsfbu.app_main.Models.BankAccount;
import com.ejsfbu.app_main.Models.User;
import com.ejsfbu.app_main.R;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AddAllowanceDialogFragment extends DialogFragment {

    @BindView(R.id.tvAllowanceManagerTitle)
    TextView tvAllowanceManagerTitle;
    @BindView(R.id.tvAllowanceManagerDescription)
    TextView tvAllowanceManagerDescription;
    @BindView(R.id.spAllowanceBanks)
    Spinner spAllowanceBanks;
    @BindView(R.id.spAllowanceFrequency)
    Spinner spAllowanceFrequency;
    @BindView(R.id.etAllowanceAmount)
    EditText etAllowanceAmount;
    @BindView(R.id.bAddAllowance)
    Button bAddAllowance;
    @BindView(R.id.bAddAllowanceCancel)
    Button bAddAllowanceCancel;

    Context context;
    User parent;
    Unbinder unbinder;
    static User currentChild;
    static User currentParent;

    String bankName;
    Double allowanceAmount;
    String frequency;

    public AddAllowanceDialogFragment() {

    }

    public static AddAllowanceDialogFragment newInstance(String title, User user) {
        AddAllowanceDialogFragment frag = new AddAllowanceDialogFragment();
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
        setSpinners();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getContext();
        parent = (User) ParseUser.getCurrentUser();
        return inflater.inflate(R.layout.fragment_add_allowance, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void setButtons() {
        bAddAllowance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bankName = spAllowanceBanks.getSelectedItem().toString();

                String amountString = etAllowanceAmount.getText().toString();
                if (amountString.equals("")) {
                    Toast.makeText(context,"Please enter a value.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    allowanceAmount = Double.valueOf(amountString);
                }

                frequency = spAllowanceFrequency.getSelectedItem().toString();
                sendBackResult();
                return;
            }
        });

        bAddAllowanceCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public interface AddAllowanceDialogListener {
        void onFinishAddAllowanceDialog(String bankName, Double allowance, String frequency);
    }

    public void sendBackResult() {
        AddAllowanceDialogFragment.AddAllowanceDialogListener listener = (AddAllowanceDialogFragment.AddAllowanceDialogListener) getFragmentManager()
                .findFragmentById(R.id.flMainContainer);
        listener.onFinishAddAllowanceDialog(bankName, allowanceAmount, frequency);
        Toast.makeText(context, "Allowance Created", Toast.LENGTH_LONG).show();
        dismiss();
        return;
    }


    public void setSpinners() {
        setAllowanceFrequencySpinner();
        setParentBankSpinner();
    }

    public void setAllowanceFrequencySpinner() {
        ArrayList<String> frequency = new ArrayList<>();
        frequency.add("Daily");
        frequency.add("Weekly");
        frequency.add("Monthly");
        ArrayAdapter<String> frequencyAdapter =
                new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, frequency);
        frequencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAllowanceFrequency.setAdapter(frequencyAdapter);
    }


    public void setParentBankSpinner() {
        List<BankAccount> parentBanks = new ArrayList<>();
        parentBanks = currentParent.getVerifiedBanks();
        ArrayList<String> array = new ArrayList<>();
        if (parentBanks != null) {
            for (BankAccount bank : parentBanks) {
                array.add(bank.getBankName());
            }
        }

        if (array.size() == 0) {
            array.add("No verified banks available");
        }
        ArrayAdapter<String> bankAdapter =
                new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, array);
        bankAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAllowanceBanks.setAdapter(bankAdapter);
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
