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

import com.ejsfbu.app_main.Adapters.ChildAdapter;
import com.ejsfbu.app_main.Models.User;
import com.ejsfbu.app_main.R;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ChildListFragment extends Fragment {

    public static final String TAG = "ChildListFragment";

    @BindView(R.id.rvChildListChildren)
    RecyclerView rvChildListChildren;

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



        children = new ArrayList<>();
        adapter = new ChildAdapter(context, children);

        rvChildListChildren.setAdapter(adapter);
        rvChildListChildren.setLayoutManager(new LinearLayoutManager(context));

        loadChildren();
    }

    protected void loadChildren() {
        List<User> children = user.getChildren();
        if (children != null) {
            this.children.addAll(children);
            adapter.notifyDataSetChanged();
        }
        /*JSONArray jsonChildren = user.getChildren();
        for (int i = 0; i < jsonChildren.length(); i++) {
            try {
                JSONObject jsonChild = (JSONObject) jsonChildren.get(i);
                String childUserId = jsonChild.getString("objectId");
                User.Query userQuery = new User.Query();
                userQuery.whereEqualTo("objectId", childUserId);
                userQuery.findInBackground(new FindCallback<User>() {
                    @Override
                    public void done(List<User> objects, ParseException e) {
                        if (e == null) {
                            User child = objects.get(0);
                            children.add(child);
                            adapter.notifyItemInserted(children.size() - 1);
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }*/
    }
}
