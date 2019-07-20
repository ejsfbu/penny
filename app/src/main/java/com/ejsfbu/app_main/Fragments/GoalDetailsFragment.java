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

public class GoalDetailsFragment extends Fragment {

    // Butterknife for fragment
    private Unbinder unbinder;
    @BindView(R.id.ivGoalDetailsImage) ImageView ivGoalDetailsImage;
    @BindView(R.id.tvGoalDetailsName) TextView tvGoalDetailsName;
    @BindView(R.id.pbDetailsPercentDone) ProgressBar pbDetailsPercentDone;
    @BindView(R.id.tvDetailsPercentDone) TextView tvDetailsPercentDone;
    @BindView(R.id.rvTransactions) RecyclerView rvTransactions;
    @BindView(R.id.deposit_btn) Button deposit_btn;
    @BindView(R.id.tvTranscationHistory) TextView tvTransactionsHistory;
    @BindView(R.id.edit_goal_btn) Button edit_goal_btn;
    @BindView(R.id.tvCompletionDateTitle) TextView tvCompletionDateTitle;
    @BindView(R.id.tvCompletionDate) TextView tvCompletionDate;
    @BindView(R.id.tvTotalCostTitle) TextView tvTotalCostTitle;
    @BindView(R.id.tvTotalCost) TextView tvTotalCost;

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
        rvTransactions.setAdapter(adapter);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rvTransactions.setLayoutManager(linearLayoutManager);
//        setListeners();
//        // Adds the scroll listener to RecyclerView
//        rvTransactions.addOnScrollListener(scrollListener);

        loadTransactions();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_goal_details, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void loadTransactions() {
        // set up query
        final Transaction.Query transactionQuery = new Transaction.Query();
        // Add Query specifications
        transactionQuery.findInBackground(new FindCallback<Transaction>() {
                @Override
                public void done(List<Transaction> objects, ParseException e) {
                    if (e == null) {
                        transactionsList.addAll(objects);
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
    }

    // Append the next page of data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void loadNextDataFromApi(int offset) {
        if (adapter.getItemCount() < transactionsLoaded) {
            return;
        }
        Log.d("data", String.valueOf(offset));
        // set up query
        final Transaction.Query transactions = new Transaction.Query();
        // TODO Add Query specifications
        transactions.findInBackground(new FindCallback<Transaction>() {
            @Override
            public void done(List<Transaction> objects, ParseException e) {
                if (e == null) {
                    transactionsList.clear();
                    transactionsList.addAll(objects);
                    adapter.notifyDataSetChanged();
                    transactionsLoaded += objects.size();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }


}
