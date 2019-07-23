package com.ejsfbu.app_main.Fragments;

import android.content.Context;
import android.content.Intent;
import android.icu.lang.UScript;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ejsfbu.app_main.Activities.AddBankActivity;
import com.ejsfbu.app_main.Activities.MainActivity;
import com.ejsfbu.app_main.Adapters.BankAdapter;
import com.ejsfbu.app_main.R;
import com.ejsfbu.app_main.models.BankAccount;
import com.ejsfbu.app_main.models.Goal;
import com.ejsfbu.app_main.models.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class BankAccountsFragment extends Fragment {

    @BindView(R.id.rvBanks)
    RecyclerView rvBanks;
    @BindView(R.id.tvNoBanks)
    TextView tvNoBanks;
    @BindView(R.id.fabAddBank)
    FloatingActionButton fabAdd;

    // Butterknife for fragment
    private Unbinder unbinder;
    private ParseUser user;
    private Context context;
    private List<BankAccount> bankList;
    private BankAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment98
        context = container.getContext();
        return inflater.inflate(R.layout.fragment_banks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);
        user = ParseUser.getCurrentUser();
        bankList = new ArrayList<>();
        adapter = new BankAdapter(context, bankList);
        rvBanks.setAdapter(adapter);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rvBanks.setLayoutManager(linearLayoutManager);
        loadBanks();
    }

    // When change fragment unbind view
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void loadBanks() {
        List<BankAccount> list = ((User) user).getBanks();
        Log.d("banks", list.toString());
        if (list == null) {
            tvNoBanks.setVisibility(View.VISIBLE);
            return;
        }
        tvNoBanks.setVisibility(View.INVISIBLE);
        bankList.addAll(((User) user).getBanks());
        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.fabAddBank)
    public void onClickAdd() {
        Intent intent = new Intent(getContext(), AddBankActivity.class);
        getActivity().startActivityForResult(intent, MainActivity.BANK_REQUEST_CODE);
    }

}
