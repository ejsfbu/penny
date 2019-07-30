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

import com.ejsfbu.app_main.Adapters.BadgeRowAdapter;
import com.ejsfbu.app_main.Adapters.GoalAdapter;
import com.ejsfbu.app_main.Models.User;
import com.ejsfbu.app_main.R;
import com.ejsfbu.app_main.Models.BadgeRow;
import com.ejsfbu.app_main.Models.Goal;
import com.ejsfbu.app_main.Models.Reward;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RewardsFragment extends Fragment {

    public static final String TAG = "RewardsFragment";

    @BindView(R.id.rvRewardsCompletedGoals)
    RecyclerView rvRewardsCompletedGoals;
    @BindView(R.id.rvRewardsCompletedBadges)
    RecyclerView rvRewardsCompletedBadges;
    @BindView(R.id.rvRewardsInProgressBadges)
    RecyclerView rvRewardsInProgressBadges;
    @BindView(R.id.tvNoCompletedGoalsText)
    TextView tvNoCompletedGoalsText;

    private Unbinder unbinder;
    private Context context;
    private User user;

    protected GoalAdapter goalAdapter;
    protected BadgeRowAdapter completedBadgeRowAdapter;
    protected BadgeRowAdapter inProgressBadgeRowAdapter;

    protected List<Goal> goals;
    protected List<BadgeRow> completedBadgeRows;
    protected List<BadgeRow> inProgressBadgeRows;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = container.getContext();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_badges, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);
        user = (User) ParseUser.getCurrentUser();
        goals = new ArrayList<>();
        completedBadgeRows = new ArrayList<>();
        inProgressBadgeRows = new ArrayList<>();

        goalAdapter = new GoalAdapter(context, goals);
        completedBadgeRowAdapter = new BadgeRowAdapter(context, completedBadgeRows);
        inProgressBadgeRowAdapter = new BadgeRowAdapter(context, inProgressBadgeRows);

        rvRewardsCompletedGoals.setAdapter(goalAdapter);
        rvRewardsCompletedBadges.setAdapter(completedBadgeRowAdapter);
        rvRewardsInProgressBadges.setAdapter(inProgressBadgeRowAdapter);

        rvRewardsCompletedGoals.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRewardsCompletedBadges.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRewardsInProgressBadges.setLayoutManager(new LinearLayoutManager(getContext()));

        loadCompletedGoals();
        loadCompletedBadges();
        loadInProgressBadges();
    }

    // When change fragment unbind view
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    protected void loadCompletedGoals() {
        List<Goal> completedGoals = user.getCompletedGoals();
        if (completedGoals == null || completedGoals.size() == 0 ) {
            tvNoCompletedGoalsText.setVisibility(View.VISIBLE);
        } else {
            tvNoCompletedGoalsText.setVisibility(View.GONE);
            goals.addAll(completedGoals);
            Collections.sort(goals);
            Collections.reverse(goals);
            goalAdapter.notifyDataSetChanged();
        }
    }

    protected void loadCompletedBadges() {
        final Reward.Query rewardsQuery = new Reward.Query();
        rewardsQuery.getTopCompleted()
                .areCompleted();
        rewardsQuery.findInBackground(new FindCallback<Reward>() {
            @Override
            public void done(List<Reward> rewards, ParseException e) {
                if (e == null) {
                    makeBadgeRows(rewards, completedBadgeRowAdapter, completedBadgeRows);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    protected void loadInProgressBadges() {
        final Reward.Query rewardsQuery = new Reward.Query();
        rewardsQuery.getTopInProgress()
                .areInProgress();
        rewardsQuery.findInBackground(new FindCallback<Reward>() {
            @Override
            public void done(List<Reward> rewards, ParseException e) {
                if (e == null) {
                    makeBadgeRows(rewards, inProgressBadgeRowAdapter, inProgressBadgeRows);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    private void makeBadgeRows(List<Reward> rewards, BadgeRowAdapter badgeRowAdapter,
                               List<BadgeRow> badgeRows) {
        for (int i = 0; i < rewards.size() - (rewards.size() % 5); i += 5) {
            BadgeRow badgeRow = new BadgeRow();
            badgeRow.setBadge1(rewards.get(i));
            badgeRow.setBadge2(rewards.get(i + 1));
            badgeRow.setBadge3(rewards.get(i + 2));
            badgeRow.setBadge4(rewards.get(i + 3));
            badgeRow.setBadge5(rewards.get(i + 4));
            badgeRows.add(badgeRow);
            badgeRowAdapter.notifyItemInserted(completedBadgeRows.size() - 1);
        }
        if (rewards.size() % 5 == 1) {
            BadgeRow badgeRow = new BadgeRow();
            badgeRow.setBadge1(rewards.get(rewards.size() - 1));
            badgeRows.add(badgeRow);
            badgeRowAdapter.notifyItemInserted(completedBadgeRows.size() - 1);
        }
        if (rewards.size() % 5 == 2) {
            BadgeRow badgeRow = new BadgeRow();
            badgeRow.setBadge1(rewards.get(rewards.size() - 2));
            badgeRow.setBadge2(rewards.get(rewards.size() - 1));
            badgeRows.add(badgeRow);
            badgeRowAdapter.notifyItemInserted(completedBadgeRows.size() - 1);
        }
        if (rewards.size() % 5 == 3) {
            BadgeRow badgeRow = new BadgeRow();
            badgeRow.setBadge1(rewards.get(rewards.size() - 3));
            badgeRow.setBadge2(rewards.get(rewards.size() - 2));
            badgeRow.setBadge3(rewards.get(rewards.size() - 1));
            badgeRows.add(badgeRow);
            badgeRowAdapter.notifyItemInserted(completedBadgeRows.size() - 1);
        }
        if (rewards.size() % 5 == 4) {
            BadgeRow badgeRow = new BadgeRow();
            badgeRow.setBadge1(rewards.get(rewards.size() - 4));
            badgeRow.setBadge2(rewards.get(rewards.size() - 3));
            badgeRow.setBadge3(rewards.get(rewards.size() - 2));
            badgeRow.setBadge4(rewards.get(rewards.size() - 1));
            badgeRows.add(badgeRow);
            badgeRowAdapter.notifyItemInserted(completedBadgeRows.size() - 1);
        }
    }
}
