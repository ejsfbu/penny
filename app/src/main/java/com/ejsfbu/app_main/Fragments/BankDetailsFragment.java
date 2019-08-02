package com.ejsfbu.app_main.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ejsfbu.app_main.Activities.MainActivity;
import com.ejsfbu.app_main.Activities.ParentActivity;
import com.ejsfbu.app_main.DialogFragments.RemoveBankDialogFragment;
import com.ejsfbu.app_main.Models.BankAccount;
import com.ejsfbu.app_main.Models.User;
import com.ejsfbu.app_main.R;
import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.ejsfbu.app_main.Activities.MainActivity.fragmentManager;

public class BankDetailsFragment extends Fragment implements RemoveBankDialogFragment.RemoveBankDialogListener {

    @BindView(R.id.ivBankDetailsBankImage)
    ImageView ivBankDetailsBankImage;
    @BindView(R.id.tvBankDetailsBankName)
    TextView tvBankDetailsBankName;
    @BindView(R.id.tvBankDetailsAccountNumber)
    TextView tvBankDetailsAccountNumber;
    @BindView(R.id.tvBankDetailsVerificationStatus)
    TextView tvBankDetailsVerificationStatus;
    @BindView(R.id.bBankDetailsRemove)
    Button bBankDetailsRemove;

    private Unbinder unbinder;
    private User user;
    private Context context;
    private BankAccount bank;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = container.getContext();
        user = (User) ParseUser.getCurrentUser();
        return inflater.inflate(R.layout.fragment_bank_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);
        if (user.getIsParent()) {
            ParentActivity.ibParentBankDetailsBack.setVisibility(View.VISIBLE);
        } else {
            MainActivity.ibBankDetailsBack.setVisibility(View.VISIBLE);
        }
        Bundle bundle = this.getArguments();
        bank = bundle.getParcelable("bank");
        tvBankDetailsBankName.setText(bank.getBankName());
        String accountNum = bank.getAccountNumber();
        tvBankDetailsAccountNumber.setText("****" + accountNum.substring(accountNum.length() - 4));
        if (bank.getVerified()) {
            tvBankDetailsVerificationStatus.setText("Verified");
        } else {
            tvBankDetailsVerificationStatus.setText("Pending");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.bBankDetailsRemove)
    public void onClickRemove() {
        showEditDialog();
    }

    private void showEditDialog() {
        RemoveBankDialogFragment removeBankDialogFragment
                = RemoveBankDialogFragment.newInstance("Remove bank");
        removeBankDialogFragment.show(MainActivity.fragmentManager,
                "fragment_remove_bank");
    }

    @Override
    public void onFinishEditDialog() {
        user.removeBank(bank);
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(context, "Bank account removed.",
                            Toast.LENGTH_LONG).show();
                    Fragment fragment = new BanksListFragment();
                    fragmentManager.beginTransaction()
                            .replace(R.id.flMainContainer, fragment).commit();
                } else {
                    e.printStackTrace();
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void deleteBank() {
        bank.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(context, "Bank account removed.",
                            Toast.LENGTH_LONG).show();
                    Fragment fragment = new BanksListFragment();
                    fragmentManager.beginTransaction()
                            .replace(R.id.flMainContainer, fragment).commit();
                } else {
                    e.printStackTrace();
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
