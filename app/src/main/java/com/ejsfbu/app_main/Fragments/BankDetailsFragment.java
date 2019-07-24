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
import androidx.fragment.app.FragmentManager;


import com.ejsfbu.app_main.Activities.MainActivity;
import com.ejsfbu.app_main.EditFragments.RemoveBankDialogFragment;
import com.ejsfbu.app_main.R;
import com.ejsfbu.app_main.models.BankAccount;
import com.parse.ParseUser;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class BankDetailsFragment extends Fragment implements RemoveBankDialogFragment.RemoveBankDialogListener {

    @BindView(R.id.ivBankImage)
    ImageView ivBankImage;
    @BindView(R.id.tvBankName)
    TextView tvBankName;
    @BindView(R.id.tvAccount)
    TextView tvAccount;
    @BindView(R.id.tvVerificationStatus)
    TextView tvVerificationStatus;
    @BindView(R.id.bRemove)
    Button bRemove;

    // Butterknife for fragment
    private Unbinder unbinder;
    private ParseUser user;
    private Context context;
    private BankAccount bank;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment98
        context = container.getContext();
        return inflater.inflate(R.layout.fragment_bank_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);
        user = ParseUser.getCurrentUser();
        Bundle bundle = this.getArguments();
        bank = bundle.getParcelable("bank");
        tvBankName.setText(bank.getBankName());
        String bankAccount =  bank.getAccountNumber();
        tvAccount.setText("****" + bankAccount.substring(bankAccount.length() - 4));
        if (bank.getVerified()) {
            tvVerificationStatus.setText("Verified");
        } else {
            tvVerificationStatus.setText("Pending");
        }
    }

    // When change fragment unbind view
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.bRemove)
    public void onClickRemove() {
        showEditDialog();
    }

    // Call this method to launch the edit dialog
    private void showEditDialog() {
        RemoveBankDialogFragment removeBankDialogFragment
                = RemoveBankDialogFragment.newInstance("Remove bank");
        removeBankDialogFragment.show(MainActivity.fragmentManager,
                "fragment_edit_bank");
    }

    // This is called when the dialog is completed and the results have been passed
    @Override
    public void onFinishEditDialog() {
        Toast.makeText(context, "Bank account removed.", Toast.LENGTH_LONG).show();

    }

}
