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

import com.ejsfbu.app_main.Activities.MainActivity;
import com.ejsfbu.app_main.Adapters.BadgeRowAdapter;
import com.ejsfbu.app_main.Adapters.GoalAdapter;
import com.ejsfbu.app_main.Models.User;
import com.ejsfbu.app_main.R;
import com.ejsfbu.app_main.Models.BadgeRow;
import com.ejsfbu.app_main.Models.Goal;
import com.ejsfbu.app_main.Models.Reward;
import com.ejsfbu.app_main.Models.User;
import com.ejsfbu.app_main.R;
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

import static com.ejsfbu.app_main.Models.BadgeRow.makeBadgeRows;

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
        return inflater.inflate(R.layout.fragment_badges, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);
        user = (User) ParseUser.getCurrentUser();
        goals = new ArrayList<>();
        completedBadgeRows = new ArrayList<>();
        inProgressBadgeRows = new ArrayList<>();

        MainActivity.ibGoalDetailsBack.setVisibility(View.GONE);
        MainActivity.ibBankDetailsBack.setVisibility(View.GONE);
        MainActivity.ibBanksListBack.setVisibility(View.GONE);
        MainActivity.ibRewardGoalDetailsBack.setVisibility(View.GONE);

        goalAdapter = new GoalAdapter(context, goals);
        completedBadgeRowAdapter = new BadgeRowAdapter(context, completedBadgeRows, user);
        inProgressBadgeRowAdapter = new BadgeRowAdapter(context, inProgressBadgeRows, user);

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
            goalAdapter.notifyDataSetChanged();
        }
    }

    protected void loadCompletedBadges() {
        List<Reward> completedBadges = user.getCompletedBadges();
        if (completedBadges != null) {
            makeBadgeRows(completedBadges, completedBadgeRowAdapter, completedBadgeRows);
        }
    }

    protected void loadInProgressBadges() {
        List<Reward> inProgressBadges = user.getInProgressBadges();
        if (inProgressBadges != null) {
            makeBadgeRows(inProgressBadges, inProgressBadgeRowAdapter, inProgressBadgeRows);
        }
    }
}
