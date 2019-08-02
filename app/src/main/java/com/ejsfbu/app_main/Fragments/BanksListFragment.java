package com.ejsfbu.app_main.Fragments;

import android.content.Context;
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

import com.ejsfbu.app_main.Activities.AddBankFragment;
import com.ejsfbu.app_main.Activities.MainActivity;
import com.ejsfbu.app_main.Activities.ParentActivity;
import com.ejsfbu.app_main.Adapters.BankAdapter;
import com.ejsfbu.app_main.Models.BankAccount;
import com.ejsfbu.app_main.Models.User;
import com.ejsfbu.app_main.R;
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
    private User user;
    private Context context;
    private List<BankAccount> bankList;
    private BankAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = container.getContext();
        user = (User) ParseUser.getCurrentUser();
        return inflater.inflate(R.layout.fragment_banks_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);

        if (user.getIsParent()) {
            ParentActivity.ibParentBanksListBack.setVisibility(View.VISIBLE);
            ParentActivity.ibParentProfileBack.setVisibility(View.GONE);
        } else {
            MainActivity.ibBanksListBack.setVisibility(View.VISIBLE);
        }

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
        List<BankAccount> list = user.getBanks();
        if (list == null) {
            tvBanksListNoBanks.setVisibility(View.VISIBLE);
            return;
        }
        tvBanksListNoBanks.setVisibility(View.INVISIBLE);
        bankList.addAll(user.getBanks());
        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.fabAddBank)
    public void onClickAdd() {
        Fragment addBankFragment = new AddBankFragment();
        if (user.getIsParent()) {
            ParentActivity.fragmentManager.beginTransaction()
                    .replace(R.id.flParentContainer, addBankFragment)
                    .commit();
        } else {
            MainActivity.fragmentManager.beginTransaction()
                    .replace(R.id.flMainContainer, addBankFragment)
                    .commit();
        }
    }
}
