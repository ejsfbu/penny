package com.ejsfbu.app_main.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.ejsfbu.app_main.Models.BankAccount;
import com.ejsfbu.app_main.Models.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class BanksListFragment extends Fragment {

    @BindView(R.id.rvBanksListBanks)
    RecyclerView rvBanksListBanks;
    @BindView(R.id.tvBanksListNoBanks)
    TextView tvBanksListNoBanks;
    @BindView(R.id.fabAddBank)
    FloatingActionButton fabAddBank;

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
        return inflater.inflate(R.layout.fragment_banks_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);
        MainActivity.ibBanksListBack.setVisibility(View.VISIBLE);
        user = ParseUser.getCurrentUser();
        bankList = new ArrayList<>();
        adapter = new BankAdapter(context, bankList);
        rvBanksListBanks.setAdapter(adapter);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rvBanksListBanks.setLayoutManager(linearLayoutManager);
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
        if (list == null) {
            tvBanksListNoBanks.setVisibility(View.VISIBLE);
            return;
        }
        tvBanksListNoBanks.setVisibility(View.INVISIBLE);
        bankList.addAll(((User) user).getBanks());
        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.fabAddBank)
    public void onClickAdd() {
        Intent intent = new Intent(getContext(), AddBankActivity.class);
        getActivity().startActivityForResult(intent, MainActivity.BANK_REQUEST_CODE);
    }

}
