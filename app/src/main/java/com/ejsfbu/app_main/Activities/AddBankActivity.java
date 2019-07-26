package com.ejsfbu.app_main.Activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ejsfbu.app_main.R;
import com.ejsfbu.app_main.models.BankAccount;
import com.ejsfbu.app_main.models.User;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddBankActivity extends AppCompatActivity {

    public static final String TAG = "AddBankActivity";

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bank);
        ButterKnife.bind(this);
        user = (User) ParseUser.getCurrentUser();
    }

    @OnClick(R.id.bAddBankAdd)
    public void onClickAddBank() {
        final String bankName = etAddBankBankName.getText().toString();
        if (bankName.equals("")) {
            Toast.makeText(this, "Please enter a bank name.",
                    Toast.LENGTH_LONG).show();
            return;
        }
        final String legalName = etAddBankLegalName.getText().toString();
        if (legalName.equals("")) {
            Toast.makeText(this, "Please enter your legal name.",
                    Toast.LENGTH_LONG).show();
            return;
        }
        final String routing = etAddBankRoutingNumber.getText().toString();
        if (routing.equals("") || !routing.matches("^[0-9]*$") || routing.length() < 9) {
            Toast.makeText(this, "Please enter a valid routing number.",
                    Toast.LENGTH_LONG).show();
            return;
        }
        final String accountNumber = etAddBankAccountNumber.getText().toString();
        if (accountNumber.equals("") ||
                !accountNumber.matches("^[0-9]*$") || accountNumber.length() < 4) {
            Toast.makeText(this, "Please enter a valid account number.",
                    Toast.LENGTH_LONG).show();
            return;
        }
        final String confirmAccount = etAddBankConfirmAccountNumber.getText().toString();
        if (!accountNumber.equals(confirmAccount)) {
            Toast.makeText(this, "The account numbers do not match.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        addBank(bankName, legalName, routing, accountNumber);
    }

    private void addBank(String bankName, String legalName, String routing, String accountNumber) {
        BankAccount newBank = new BankAccount();
        newBank.setBankName(bankName);
        newBank.setLegalName(legalName);
        newBank.setRoutingtNumber(routing);
        newBank.setAccountNumber(accountNumber);
        newBank.setVerified(false);
        newBank.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    linkUser(newBank);
                } else {
                    e.printStackTrace();
                    Toast.makeText(AddBankActivity.this, e.getMessage(),
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
                    Toast.makeText(AddBankActivity.this, "Bank account added.",
                            Toast.LENGTH_LONG).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    e.printStackTrace();
                    Toast.makeText(AddBankActivity.this, e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
