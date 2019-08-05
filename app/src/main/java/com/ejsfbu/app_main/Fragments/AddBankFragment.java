package com.ejsfbu.app_main.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ejsfbu.app_main.Activities.MainActivity;
import com.ejsfbu.app_main.Activities.ParentActivity;
import com.ejsfbu.app_main.Fragments.BanksListFragment;
import com.ejsfbu.app_main.Models.BankAccount;
import com.ejsfbu.app_main.Models.User;
import com.ejsfbu.app_main.R;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AddBankFragment extends Fragment {

    public static final String TAG = "AddBankFragment";

    @BindView(R.id.etAddBankBankName)
    EditText etAddBankBankName;
    @BindView(R.id.etAddBankLegalName)
    EditText etAddBankLegalName;
    @BindView(R.id.etAddBankRoutingNumber)
    EditText etAddBankRoutingNumber;
    @BindView(R.id.etAddBankAccountNumber)
    EditText etAddBankAccountNumber;
    @BindView(R.id.etAddBankConfirmAccountNumber)
    EditText etAddBankConfirmAccountNumber;
    @BindView(R.id.bAddBankAdd)
    Button bAddBankAdd;

    private User user;
    private Context context;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = container.getContext();
        user = (User) ParseUser.getCurrentUser();
        user.setACL(new ParseACL(user));
        user.saveInBackground();
        return inflater.inflate(R.layout.fragment_add_bank, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);
    }

    @OnClick(R.id.bAddBankAdd)
    public void onClickAddBank() {
        final String bankName = etAddBankBankName.getText().toString();
        if (bankName.equals("")) {
            Toast.makeText(context, "Please enter a bank name.",
                    Toast.LENGTH_LONG).show();
            return;
        }
        final String legalName = etAddBankLegalName.getText().toString();
        if (legalName.equals("")) {
            Toast.makeText(context, "Please enter your legal name.",
                    Toast.LENGTH_LONG).show();
            return;
        }
        final String routing = etAddBankRoutingNumber.getText().toString();
        if (routing.equals("") || !routing.matches("^[0-9]*$") || routing.length() < 9) {
            Toast.makeText(context, "Please enter a valid routing number.",
                    Toast.LENGTH_LONG).show();
            return;
        }
        final String accountNumber = etAddBankAccountNumber.getText().toString();
        if (accountNumber.equals("") ||
                !accountNumber.matches("^[0-9]*$") || accountNumber.length() < 4) {
            Toast.makeText(context, "Please enter a valid account number.",
                    Toast.LENGTH_LONG).show();
            return;
        }
        final String confirmAccount = etAddBankConfirmAccountNumber.getText().toString();
        if (!accountNumber.equals(confirmAccount)) {
            Toast.makeText(context, "The account numbers do not match.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        addBank(bankName, legalName, routing, accountNumber);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.bAddBankCancel)
    public void onClickAddBankCancel() {
        Fragment banksListFragment = new BanksListFragment();
        if (user.getIsParent()) {
            ParentActivity.fragmentManager.beginTransaction()
                    .replace(R.id.flParentContainer, banksListFragment)
                    .commit();
        } else {
            MainActivity.fragmentManager.beginTransaction()
                    .replace(R.id.flMainContainer, banksListFragment)
                    .commit();
        }
    }

    private void addBank(String bankName, String legalName, String routing, String accountNumber) {
        BankAccount newBank = new BankAccount();
        newBank.setBankName(bankName);
        newBank.setLegalName(legalName);
        newBank.setRoutingtNumber(routing);
        newBank.setAccountNumber(accountNumber);
        newBank.setVerified(true);
        // TODO SET ACL CHANGE LATER
        ParseACL acl = new ParseACL(user);
        acl.setPublicWriteAccess(true);
        acl.setPublicReadAccess(true);
        newBank.setACL(acl);
        newBank.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    linkUser(newBank);
                } else {
                    e.printStackTrace();
                    Toast.makeText(context, e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void linkUser(BankAccount bank) {
        user.addBank(bank);
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(context, "Bank account added.",
                            Toast.LENGTH_LONG).show();
                    Fragment banksListFragment = new BanksListFragment();
                    if (user.getIsParent()) {
                        ParentActivity.fragmentManager.beginTransaction()
                                .replace(R.id.flParentContainer, banksListFragment)
                                .commit();
                    } else {
                        MainActivity.fragmentManager.beginTransaction()
                                .replace(R.id.flMainContainer, banksListFragment)
                                .commit();
                    }
                } else {
                    e.printStackTrace();
                    Toast.makeText(context, e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
