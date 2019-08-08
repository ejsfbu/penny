package com.ejsfbu.app_main.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ejsfbu.app_main.Activities.ParentActivity;
import com.ejsfbu.app_main.Adapters.ChildAdapter;
import com.ejsfbu.app_main.Models.User;
import com.ejsfbu.app_main.R;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AllowanceManagerFragment extends Fragment {

    @BindView(R.id.tvAllowanceManagerTitle)
    TextView tvAllowanceManagerTitle;
    @BindView(R.id.tvAllowanceManagerDescription)
    TextView tvAllowanceManagerDescription;
    @BindView(R.id.bAllowanceManagerBack)
    Button bAllowanceManagerBack;
    @BindView(R.id.rvAllowManagerChildList)
    RecyclerView rvAllowManagerChildList;

    Context context;
    User parent;
    Unbinder unbinder;
    List<User> children;
    ChildAdapter childAdapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);

        setButtons();

        children = new ArrayList<>();
        childAdapter = new ChildAdapter(context, children);

        rvAllowManagerChildList.setAdapter(childAdapter);
        rvAllowManagerChildList.setLayoutManager(new LinearLayoutManager(context));

        loadChildren();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getContext();
        parent = (User) ParseUser.getCurrentUser();
        return inflater.inflate(R.layout.fragment_allowance_manager, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void setButtons() {
        bAllowanceManagerBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ParentActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void loadChildren() {
        List<User> children = parent.getChildren();
        if (children != null) {
            this.children.addAll(children);
            childAdapter.notifyDataSetChanged();
        }
}
}
