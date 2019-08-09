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
import android.widget.TextView;
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

public class EditAllowanceDialogFragment extends DialogFragment {

    @BindView(R.id.tvEditAllowanceManagerTitle)
    TextView tvEditAllowanceManagerTitle;
    @BindView(R.id.tvEditAllowanceManagerDescription)
    TextView tvEditAllowanceManagerDescription;


    @BindView(R.id.spEditAllowanceBanks)
    Spinner spEditAllowanceBanks;
    @BindView(R.id.spEditAllowanceFrequency)
    Spinner spEditAllowanceFrequency;
    @BindView(R.id.etEditAllowancePaymentAmount)
    EditText etEditAllowancePaymentAmount;

    @BindView(R.id.bEditAllowance)
    Button bEditAllowance;
    @BindView(R.id.bEditAllowanceCancel)
    Button bEditAllowanceCancel;

    Context context;
    User parent;
    Unbinder unbinder;
    static User currentChild;
    static User currentParent;

    String bankName;
    Double allowanceAmount;
    String frequency;

    public EditAllowanceDialogFragment() {

    }

    public static EditAllowanceDialogFragment newInstance(String title, User user) {
        EditAllowanceDialogFragment frag = new EditAllowanceDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        currentChild = user;
        return frag;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);

        currentParent = (User) ParseUser.getCurrentUser();

        setButtons();
        setSpinners();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getContext();
        parent = (User) ParseUser.getCurrentUser();
        return inflater.inflate(R.layout.fragment_edit_allowance, container, false);
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
                bankName = spEditAllowanceBanks.getSelectedItem().toString();

                String amountString = etEditAllowancePaymentAmount.getText().toString();
                if (amountString.equals("")) {
                    Toast.makeText(context,"Please enter a value.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    allowanceAmount = Double.valueOf(amountString);
                }

                frequency = spEditAllowanceFrequency.getSelectedItem().toString();
                sendBackResult();
                return;
            }
        });

        bEditAllowanceCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public interface EditAllowanceDialogListener {
        void onFinishEditAllowanceDialog(String bankName, Double allowance, String frequency, User child);
    }

    public void sendBackResult() {
        EditAllowanceDialogFragment.EditAllowanceDialogListener listener = (EditAllowanceDialogFragment.EditAllowanceDialogListener) getFragmentManager()
                .findFragmentById(R.id.flParentContainer);
        listener.onFinishEditAllowanceDialog(bankName, allowanceAmount, frequency, currentChild);
        Toast.makeText(context, "Allowance has been edited.", Toast.LENGTH_LONG).show();
        dismiss();
        return;
    }


    public void setSpinners() {
        setEditAllowanceFrequencySpinner();
        setEditAllowanceBankSpinner();
    }

    public void setEditAllowanceFrequencySpinner() {
        ArrayList<String> frequency = new ArrayList<>();
        frequency.add("Daily");
        frequency.add("Weekly");
        frequency.add("Monthly");
        ArrayAdapter<String> frequencyAdapter =
                new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, frequency);
        frequencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spEditAllowanceFrequency.setAdapter(frequencyAdapter);
    }


    public void setEditAllowanceBankSpinner() {
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
        spEditAllowanceBanks.setAdapter(bankAdapter);
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
