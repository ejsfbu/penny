package com.ejsfbu.app_main.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.ejsfbu.app_main.Adapters.TransactionAdapter;
import com.ejsfbu.app_main.EndlessRecyclerViewScrollListener;
import com.ejsfbu.app_main.R;
import com.ejsfbu.app_main.models.Goal;
import com.ejsfbu.app_main.models.Transaction;
import com.parse.ParseFile;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GoalDetailsFragment extends Fragment {

    private Unbinder unbinder;
    @BindView(R.id.ivGoalDetailsImage)
    ImageView ivGoalDetailsImage;
    @BindView(R.id.tvGoalDetailsName)
    TextView tvGoalDetailsName;
    @BindView(R.id.pbGoalDetailsPercentDone)
    ProgressBar pbGoalDetailsPercentDone;
    @BindView(R.id.tvGoalDetailsPercentDone)
    TextView tvGoalDetailsPercentDone;
    @BindView(R.id.bGoalDetailsDeposit)
    Button bGoalDetailsDeposit;
    @BindView(R.id.tvGoalDetailsTransactionHistoryTitle)
    TextView tvGoalDetailsTransactionHistoryTitle;
    @BindView(R.id.bGoalDetailsCancelGoal)
    Button bGoalDetailsCancelGoal;
    @BindView(R.id.tvGoalDetailsDateCompletedTitle)
    TextView tvGoalDetailsDateCompletedTitle;
    @BindView(R.id.tvGoalDetailsCompletionDate)
    TextView tvGoalDetailsCompletionDate;
    @BindView(R.id.tvGoalDetailsTotalCostTitle)
    TextView tvGoalDetailsTotalCostTitle;
    @BindView(R.id.tvGoalDetailsTotalCost)
    TextView tvGoalDetailsTotalCost;
    @BindView(R.id.tvGoalDetailsAmountSavedTitle)
    TextView tvGoalDetailsAmountSavedTitle;
    @BindView(R.id.tvGoalDetailsAmountSaved)
    TextView tvGoalDetailsAmountSaved;

    List<Transaction> transactionsList;
    TransactionAdapter adapter;
    private int transactionsLoaded;
    LinearLayoutManager linearLayoutManager;
    EndlessRecyclerViewScrollListener scrollListener;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);

        Goal goal = getArguments().getParcelable("Goal");

        ParseFile image = goal.getParseFile("image");
        if (image != null) {
            String imageUrl = image.getUrl();
            imageUrl = imageUrl.substring(4);
            imageUrl = "https" + imageUrl;
            RequestOptions options = new RequestOptions();
            options.placeholder(R.drawable.icon_user)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .error(R.drawable.icon_user)
                    .transform(new CenterCrop())
                    .transform(new CircleCrop());
            Glide.with(getContext())
                    .load(imageUrl)
                    .apply(options) // Extra: round image corners
                    .into(ivGoalDetailsImage);
        }

        setGoalInfo(goal);


        tvGoalDetailsName.setText(goal.getName());
        //TODO Format the Date
        tvGoalDetailsTotalCost.setText("$" + goal.getCost());
        String endDate = goal.get("endDate").toString();
        String finalizedDate = endDate.substring(4, 10) + ", " + endDate.substring(24, 28);
        System.out.println(endDate.length());
        System.out.println(endDate);
        tvGoalDetailsCompletionDate.setText(finalizedDate);

        Double percentDone = (goal.getSaved() / goal.getCost()) * 100;
        tvGoalDetailsPercentDone.setText(String.format("%.1f", percentDone.floatValue()) + "%");
        pbGoalDetailsPercentDone.setProgress((int) percentDone.doubleValue());
        pbGoalDetailsPercentDone.getProgressDrawable().setTint(getContext()
                .getResources().getColor(R.color.money_green));

        transactionsList = new ArrayList<>();
        adapter = new TransactionAdapter(getContext(), transactionsList);
        //rvTransactions.setAdapter(adapter);
        linearLayoutManager = new LinearLayoutManager(getContext());
        //rvTransactions.setLayoutManager(linearLayoutManager);

        //TODO - Ethan is working on transaction model and recycler view for transactions.

        loadTransactions();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_goal_details, container, false);
    }

    public void setGoalInfo(Goal goal) {
        //set the text for goal name and end date
        tvGoalDetailsName.setText(goal.getName());
        String goalEndDate = formatDate(goal);
        tvGoalDetailsCompletionDate.setText(goalEndDate);

        //setting the total amount and saved amount in correct currency format
        tvGoalDetailsAmountSaved.setText(formatCurrency(goal.getSaved()));
        tvGoalDetailsTotalCost.setText(formatCurrency(goal.getCost()));

        //progress bar and percentage
        Double percentDone = (goal.getSaved() / goal.getCost()) * 100;
        tvGoalDetailsPercentDone.setText(String.format("%.1f", percentDone.floatValue()) + "%");
        pbGoalDetailsPercentDone.setProgress((int) percentDone.doubleValue());
        pbGoalDetailsPercentDone.getProgressDrawable().setTint(getContext()
                .getResources().getColor(R.color.money_green));
    }

    public String formatDate(Goal goal) {
        String endDate = goal.get("endDate").toString();
        String finalizedDate = endDate.substring(4, 10) + ", " + endDate.substring(24, 28);
        return finalizedDate;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void loadTransactions() {
    }

    public String formatCurrency(Double amount) {
        NumberFormat currency = NumberFormat.getCurrencyInstance(Locale.US);
        String convertedAmount = currency.format(amount);
        return convertedAmount;
    }
}
