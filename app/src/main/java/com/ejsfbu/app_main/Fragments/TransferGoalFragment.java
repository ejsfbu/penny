package com.ejsfbu.app_main.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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

import com.ejsfbu.app_main.Adapters.GoalAdapter;
import com.ejsfbu.app_main.DialogFragments.CancelGoalDialogFragment;
import com.ejsfbu.app_main.EndlessRecyclerViewScrollListener;
import com.ejsfbu.app_main.Models.Goal;
import com.ejsfbu.app_main.R;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TransferGoalFragment extends Fragment {

    @BindView(R.id.rvTransferGoals)
    RecyclerView rvTransferGoals;

    @BindView(R.id.tvPick)
    TextView tvPick;

    @BindView(R.id.exit_pick_goal_btn)
    Button exit_pick_goal_btn;

    Unbinder unbinder;
    Context context;
    List<Goal> goals;
    Goal transferredGoal;
    Goal cancelledGoal;
    LinearLayoutManager linearLayoutManager;
    GoalAdapter adapter;
    EndlessRecyclerViewScrollListener scrollListener;
    private int goalsLoaded;
    Double moneyTransfer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        context = container.getContext();
        return inflater.inflate(R.layout.fragment_transfer_goal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cancelledGoal = getArguments().getParcelable("Cancelled Goal");


        unbinder = ButterKnife.bind(this, view);
        goals = new ArrayList<>();
        adapter = new GoalAdapter(context, goals, CancelGoalDialogFragment
                .newInstance("Cancel", cancelledGoal));
        rvTransferGoals.setAdapter(adapter);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rvTransferGoals.setLayoutManager(linearLayoutManager);

        setListeners();

        rvTransferGoals.addOnScrollListener(scrollListener);

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
                .fromCurrentUser()
                .whereNotEqualTo("objectId", cancelledGoal.getObjectId());
        goalsQuery.findInBackground(new FindCallback<Goal>() {
            @Override
            public void done(List<Goal> objects, ParseException e) {
                if (e == null) {
                    goals.addAll(objects);
                    goalsLoaded = 20;
                    adapter.notifyDataSetChanged();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public void loadNextDataFromApi(int offset) {
        if (adapter.getItemCount() < goalsLoaded) {
            return;
        }
        Log.d("data", String.valueOf(offset));
        final Goal.Query postsQuery = new Goal.Query();
        postsQuery.setTop(goalsLoaded + 20)
                .areNotCompleted()
                .fromCurrentUser()
                .whereNotEqualTo("objectId", cancelledGoal.getObjectId())
                .setSkip(goalsLoaded);
        postsQuery.findInBackground(new FindCallback<Goal>() {
            @Override
            public void done(List<Goal> objects, ParseException e) {
                if (e == null) {
                    goals.clear();
                    goals.addAll(objects);
                    adapter.notifyDataSetChanged();
                    goalsLoaded += objects.size();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setListeners() {
        exit_pick_goal_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("Cancelled Goal", cancelledGoal);
                Fragment goalDetail = new GoalDetailsFragment();
                goalDetail.setArguments(bundle);
                getFragmentManager().beginTransaction()
                        .replace(R.id.flMainContainer, goalDetail).commit();
            }
        });

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromApi(page);
            }
        };

    }
}
