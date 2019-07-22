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

import com.ejsfbu.app_main.Activities.AddGoalActivity;
import com.ejsfbu.app_main.Adapters.GoalAdapter;
import com.ejsfbu.app_main.EndlessRecyclerViewScrollListener;
import com.ejsfbu.app_main.R;
import com.ejsfbu.app_main.models.Goal;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class GoalsListFragment extends Fragment {

    public static final String TAG = "GoalsListFragment";

    @BindView(R.id.rvGoals) RecyclerView rvGoals;
    @BindView(R.id.fabAdd) FloatingActionButton fabAdd;

    // Butterknife for fragment
    private Unbinder unbinder;
    private Context context;
    private EndlessRecyclerViewScrollListener scrollListener;
    private LinearLayoutManager linearLayoutManager;
    // keep track of how many goals have been loaded
    private int goalsLoaded;
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
        linearLayoutManager = new LinearLayoutManager(getContext());
        rvGoals.setLayoutManager(linearLayoutManager);
        setListeners();
        // Adds the scroll listener to RecyclerView
        rvGoals.addOnScrollListener(scrollListener);

        loadGoals();
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
        goalsQuery.getTopByEndDate()
                .areNotCompleted()
                .fromUser();
        goalsQuery.findInBackground(new FindCallback<Goal>() {
            @Override
            public void done(List<Goal> objects, ParseException e) {
                if (e == null) {
                    goalList.addAll(objects);
                    goalsLoaded = 20;
                    adapter.notifyDataSetChanged();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setListeners() {
        // Retain an instance so that you can call `resetState()` for fresh searches
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(page);
            }
        };

        fabAdd.setOnClickListener(view -> {
            Intent i = new Intent(getContext(), AddGoalActivity.class);
            startActivity(i);
        });
    }

    // Append the next page of data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void loadNextDataFromApi(int offset) {
        if (adapter.getItemCount() < goalsLoaded) {
            return;
        }
        Log.d("data", String.valueOf(offset));
        // set up query
        final Goal.Query postsQuery = new Goal.Query();
        // Add Query specifications
        postsQuery.setTop(goalsLoaded + 20)
                .areNotCompleted()
                .fromUser();
                //.setSkip(goalsLoaded);
        postsQuery.findInBackground(new FindCallback<Goal>() {
            @Override
            public void done(List<Goal> objects, ParseException e) {
                if (e == null) {
                    goalList.clear();
                    goalList.addAll(objects);
                    adapter.notifyDataSetChanged();
                    goalsLoaded += objects.size();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}
