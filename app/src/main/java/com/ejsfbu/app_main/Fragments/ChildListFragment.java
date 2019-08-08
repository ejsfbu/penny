package com.ejsfbu.app_main.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ejsfbu.app_main.Activities.ParentActivity;
import com.ejsfbu.app_main.Adapters.ChildAdapter;
import com.ejsfbu.app_main.DialogFragments.AddChildDialogFragment;
import com.ejsfbu.app_main.Models.User;
import com.ejsfbu.app_main.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ChildListFragment extends Fragment implements AddChildDialogFragment.AddChildDialogListener {

    public static final String TAG = "ChildListFragment";

    @BindView(R.id.rvChildListChildren)
    RecyclerView rvChildListChildren;
    @BindView(R.id.fabAddChild)
    FloatingActionButton fabAddChild;

    private Unbinder unbinder;
    private Context context;
    private ChildAdapter adapter;
    private List<User> children;
    private User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = container.getContext();
        user = (User) ParseUser.getCurrentUser();
        return inflater.inflate(R.layout.fragment_child_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);

        ParentActivity.ibParentProfileBack.setVisibility(View.GONE);
        ParentActivity.ibChildDetailBack.setVisibility(View.GONE);
        ParentActivity.ibParentBanksListBack.setVisibility(View.GONE);
        ParentActivity.ibParentBankDetailsBack.setVisibility(View.GONE);
        ParentActivity.ivParentProfilePic.setVisibility(View.VISIBLE);
        ParentActivity.cvParentProfilePic.setVisibility(View.VISIBLE);

        children = new ArrayList<>();
        adapter = new ChildAdapter(context, children);

        rvChildListChildren.setAdapter(adapter);
        rvChildListChildren.setLayoutManager(new LinearLayoutManager(context));

        loadChildren();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.fabAddChild)
    public void onClickAddChild() {
        showAddChildDialog();
    }

    private void showAddChildDialog() {
        AddChildDialogFragment addChildDialogFragment
                = AddChildDialogFragment.newInstance("Add Child");
        addChildDialogFragment.show(ParentActivity.fragmentManager, "fragment_add_child");
    }

    @Override
    public void onFinishEditDialog() {
        children.clear();
        loadChildren();
    }

    protected void loadChildren() {
        List<User> childrens = user.getChildren();
        if (childrens != null) {
            children.addAll(childrens);
            adapter.notifyDataSetChanged();
        }
    }
}
