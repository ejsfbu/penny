package com.ejsfbu.app_main.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ejsfbu.app_main.Activities.CreateNewGoalActivity;
import com.ejsfbu.app_main.Adapters.GoalAdapter;
import com.ejsfbu.app_main.R;
import com.ejsfbu.app_main.models.Goal;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GoalsListFragment extends Fragment {

    public static final String TAG = "GoalsListFragment";

    @BindView(R.id.rvGoals) RecyclerView rvGoals;

    // Butterknife for fragment
    private Unbinder unbinder;
    private Context context;
    protected GoalAdapter adapter;
    protected List<Goal> goalList;


    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = parent.getContext();
        return inflater.inflate(R.layout.fragment_goals_list, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);
        // Recycler view set up
        goalList = new ArrayList<>();
        adapter = new GoalAdapter(context, goalList);
        rvGoals.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvGoals.setLayoutManager(linearLayoutManager);
        loadGoals();

        FloatingActionButton fab = view.findViewById(R.id.fab_addGoal);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), CreateNewGoalActivity.class);
                startActivity(i);
            }
        });



    }

    // When change fragment unbind view
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    protected void loadGoals() {
        // set up query
        final Goal.Query goalsQuery = new Goal.Query();
        // Add Query specifications
        goalsQuery.getTop().withUser().orderByDescending(Goal.KEY_CREATED_AT);
        goalsQuery.findInBackground(new FindCallback<Goal>() {
            @Override
            public void done(List<Goal> objects, ParseException e) {
                if (e == null) {
                    goalList.addAll(objects);
                    adapter.notifyDataSetChanged();
                    for (int i = 0; i < objects.size(); i++) {
                        Log.d(TAG, "Post{" + i + "}: " + objects.get(i).getName());
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}
