package com.ejsfbu.app_main.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.ejsfbu.app_main.Activities.AddGoalActivity;
import com.ejsfbu.app_main.Adapters.GoalAdapter;
import com.ejsfbu.app_main.Adapters.TransactionAdapter;
import com.ejsfbu.app_main.EndlessRecyclerViewScrollListener;
import com.ejsfbu.app_main.R;
import com.ejsfbu.app_main.models.Goal;
import com.ejsfbu.app_main.models.Transaction;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import java.text.NumberFormat;
import java.util.Locale;

public class GoalDetailsFragment extends Fragment {

    // Butterknife for fragment
    private Unbinder unbinder;
    @BindView(R.id.ivGoalDetailsImage) ImageView ivGoalDetailsImage;
    @BindView(R.id.tvGoalDetailsName) TextView tvGoalDetailsName;
    @BindView(R.id.pbDetailsPercentDone) ProgressBar pbDetailsPercentDone;
    @BindView(R.id.tvDetailsPercentDone) TextView tvDetailsPercentDone;
    //TODO adding the recycler view
    //@BindView(R.id.rvTransactions) RecyclerView rvTransactions;
    @BindView(R.id.deposit_btn) Button deposit_btn;
    @BindView(R.id.tvTranscationHistory) TextView tvTransactionsHistory;
    @BindView(R.id.edit_goal_btn) Button edit_goal_btn;
    @BindView(R.id.tvCompletionDateTitle) TextView tvCompletionDateTitle;
    @BindView(R.id.tvCompletionDate) TextView tvCompletionDate;
    @BindView(R.id.tvTotalCostTitle) TextView tvTotalCostTitle;
    @BindView(R.id.tvTotalCost) TextView tvTotalCost;
    @BindView(R.id.tvAmountTitle) TextView tvAmountTitle;
    @BindView(R.id.tvAmount) TextView tvAmount;

    List<Transaction>transactionsList;
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
            options.placeholder(R.drawable.ic_iconfinder_icons_user_1564534)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .error(R.drawable.ic_iconfinder_icons_user_1564534)
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
        tvTotalCost.setText("$" + goal.getCost());
        String endDate = goal.get("endDate").toString();
        String finalizedDate = endDate.substring(4,10) + ", " + endDate.substring(24,28);
        System.out.println(endDate.length());
        System.out.println(endDate);
        tvCompletionDate.setText(finalizedDate);

        Double percentDone = (goal.getSaved() / goal.getCost()) * 100;
        tvDetailsPercentDone.setText(String.format("%.1f", percentDone.floatValue()) + "%");
        pbDetailsPercentDone.setProgress((int) percentDone.doubleValue());
        pbDetailsPercentDone.getProgressDrawable().setTint(getContext().getResources().getColor(R.color.money_green));

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_goal_details, container, false);
    }

    public void setGoalInfo(Goal goal) {
        //set the text for goal name and end date
        tvGoalDetailsName.setText(goal.getName());
        String goalEndDate = formatDate(goal);
        tvCompletionDate.setText(goalEndDate);

        //setting the total amount and saved amount in correct currency format
        tvAmount.setText(formatCurrency(goal.getSaved()));
        tvTotalCost.setText(formatCurrency(goal.getCost()));

        //progress bar and percentage
        Double percentDone = (goal.getSaved() / goal.getCost()) * 100;
        tvDetailsPercentDone.setText(String.format("%.1f", percentDone.floatValue()) + "%");
        pbDetailsPercentDone.setProgress((int) percentDone.doubleValue());
        pbDetailsPercentDone.getProgressDrawable().setTint(getContext().getResources().getColor(R.color.money_green));
    }

    public String formatDate(Goal goal) {
        String endDate = goal.get("endDate").toString();
        String finalizedDate = endDate.substring(4,10) + ", " + endDate.substring(24,28);
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
