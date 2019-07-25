package com.ejsfbu.app_main.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import butterknife.Unbinder;

public class GoalsListFragment extends Fragment {

    public static final String TAG = "GoalsListFragment";

    @BindView(R.id.rvGoalsListGoals)
    RecyclerView rvGoalsListGoals;
    @BindView(R.id.fabAddGoal)
    FloatingActionButton fabAddGoal;

    private Unbinder unbinder;
    private Context context;
    private EndlessRecyclerViewScrollListener scrollListener;
    private LinearLayoutManager linearLayoutManager;
    private int goalsLoaded;
    protected GoalAdapter adapter;
    protected List<Goal> goalList;


    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        context = parent.getContext();
        return inflater.inflate(R.layout.fragment_goals_list, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);
        goalList = new ArrayList<>();
        adapter = new GoalAdapter(context, goalList);
        rvGoalsListGoals.setAdapter(adapter);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rvGoalsListGoals.setLayoutManager(linearLayoutManager);
        setListeners();
        rvGoalsListGoals.addOnScrollListener(scrollListener);

        loadGoals();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    protected void loadGoals() {
        final Goal.Query goalsQuery = new Goal.Query();
        goalsQuery.getTopByEndDate()
                .areNotCompleted()
                .fromCurrentUser();
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
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromApi(page);
            }
        };

        fabAddGoal.setOnClickListener(view -> {
            Intent i = new Intent(getContext(), AddGoalActivity.class);
            startActivity(i);
        });
    }

    public void loadNextDataFromApi(int offset) {
        if (adapter.getItemCount() < goalsLoaded) {
            return;
        }
        Log.d("data", String.valueOf(offset));
        final Goal.Query goalQuery = new Goal.Query();
        goalQuery.setTop(goalsLoaded + 20)
                .areNotCompleted()
                .fromCurrentUser();
        goalQuery.findInBackground(new FindCallback<Goal>() {
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
