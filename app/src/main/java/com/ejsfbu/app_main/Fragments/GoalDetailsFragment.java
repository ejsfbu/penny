package com.ejsfbu.app_main.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.ejsfbu.app_main.Activities.MainActivity;
import com.ejsfbu.app_main.Adapters.GoalAdapter;
import com.ejsfbu.app_main.Adapters.TransactionAdapter;
import com.ejsfbu.app_main.DialogFragments.DepositDialogFragment;
import com.ejsfbu.app_main.Adapters.TransactionAdapter;
import com.ejsfbu.app_main.EditFragments.CancelGoalDialogFragment;
import com.ejsfbu.app_main.EditFragments.EditGoalEndDateDialogFragment;
import com.ejsfbu.app_main.EditFragments.EditGoalImageDialogFragment;
import com.ejsfbu.app_main.EditFragments.EditGoalNameDialogFragment;
import com.ejsfbu.app_main.EndlessRecyclerViewScrollListener;
import com.ejsfbu.app_main.R;
import com.ejsfbu.app_main.models.BankAccount;
import com.ejsfbu.app_main.models.Goal;
import com.ejsfbu.app_main.models.Transaction;
import com.ejsfbu.app_main.models.User;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import java.text.NumberFormat;
import java.util.Locale;

import static com.ejsfbu.app_main.Activities.MainActivity.fragmentManager;

public class GoalDetailsFragment extends Fragment implements DepositDialogFragment.DepositDialogListener {

    @BindView(R.id.ivGoalDetailsImage)
    ImageView ivGoalDetailsImage;
    @BindView(R.id.tvGoalDetailsName)
    TextView tvGoalDetailsName;
    @BindView(R.id.pbDetailsPercentDone)
    ProgressBar pbDetailsPercentDone;
    @BindView(R.id.tvDetailsPercentDone)
    TextView tvDetailsPercentDone;
    @BindView(R.id.rvGoalTransactionHistory)
    RecyclerView rvTransactions;
    @BindView(R.id.deposit_btn)
    Button deposit_btn;
    @BindView(R.id.tvTranscationHistory)
    TextView tvTransactionsHistory;
    @BindView(R.id.cancel_goal_btn)
    Button cancel_goal_btn;
    @BindView(R.id.tvCompletionDateTitle)
    TextView tvCompletionDateTitle;
    @BindView(R.id.tvCompletionDate)
    TextView tvCompletionDate;
    @BindView(R.id.tvTotalCostTitle)
    TextView tvTotalCostTitle;
    @BindView(R.id.tvTotalCost)
    TextView tvTotalCost;
    @BindView(R.id.tvAmountTitle)
    TextView tvAmountTitle;
    @BindView(R.id.tvAmount)
    TextView tvAmount;
    @BindView(R.id.noTransactionsText)
    TextView noTransactionText;

    // Butterknife for fragment
    private Unbinder unbinder;
    private List<Transaction> transactionsList;
    private TransactionAdapter adapter;
    private int transactionsLoaded;
    private LinearLayoutManager linearLayoutManager;
    private EndlessRecyclerViewScrollListener scrollListener;
    private User user;
    private Goal goal;
    private Context context;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);

        goal = getArguments().getParcelable("Clicked Goal");
        setGoalInfo();
        transactionsList = new ArrayList<>();
        adapter = new TransactionAdapter(getContext(), transactionsList);
        rvTransactions.setAdapter(adapter);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rvTransactions.setLayoutManager(linearLayoutManager);
        loadTransactions();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = container.getContext();
        user = (User) ParseUser.getCurrentUser();
        return inflater.inflate(R.layout.fragment_goal_details, container, false);
    }

    public void setGoalInfo() {
        //set the text for goal name and end date
        tvGoalDetailsName.setText(goal.getName());
        String goalEndDate = formatDate(goal.getEndDate().toString());
        tvCompletionDate.setText(goalEndDate);

        //setting the total amount and saved amount in correct currency format
        tvAmount.setText(formatCurrency(goal.getSaved()));
        tvTotalCost.setText(formatCurrency(goal.getCost()));

        //progress bar and percentage
        Double percentDone = (goal.getSaved() / goal.getCost()) * 100;
        tvDetailsPercentDone.setText(String.format("%.1f", percentDone.floatValue()) + "%");
        pbDetailsPercentDone.setProgress((int) percentDone.doubleValue());
        pbDetailsPercentDone.getProgressDrawable().setTint(getContext().getResources().getColor(R.color.money_green));

        ParseFile image = goal.getParseFile("image");
        if (image != null) {
            String imageUrl = image.getUrl();
            imageUrl = imageUrl.substring(4);
            imageUrl = "https" + imageUrl;
            RequestOptions options = new RequestOptions();
            options.placeholder(R.drawable.icon_target)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .error(R.drawable.icon_target)
                    .transform(new CenterCrop())
                    .transform(new CircleCrop());
            Glide.with(getContext())
                    .load(imageUrl)
                    .apply(options) // Extra: round image corners
                    .into(ivGoalDetailsImage);
        }
    }

    public static String formatDate(String date) {
        String finalizedDate = date.substring(4, 10) + ", " + date.substring(24, 28);
        return finalizedDate;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void loadTransactions() {
        List<Transaction> transactions = goal.getTransactions();
        if (transactions == null || transactions.size() == 0) {
            noTransactionText.setVisibility(View.VISIBLE);
            transactionsLoaded = 0;
        } else {
            noTransactionText.setVisibility(View.GONE);
            // add in reverse order
            for (int i = transactions.size() - 1; i >= 0; i--) {
                transactionsList.add(transactions.get(i));
                adapter.notifyDataSetChanged();
            }
            transactionsLoaded = transactions.size();
        }

    }

    public static String formatCurrency(Double amount) {
        NumberFormat currency = NumberFormat.getCurrencyInstance(Locale.US);
        String convertedAmount = currency.format(amount);
        return convertedAmount;
    }

    @OnClick(R.id.deposit_btn)
    public void onClickDeposit() {
        showDepositDialog();
    }

    // Call this method to launch the edit dialog
    private void showDepositDialog() {
        DepositDialogFragment depositDialogFragment
                = DepositDialogFragment.newInstance("Deposit");
        depositDialogFragment.show(MainActivity.fragmentManager,
                "fragment_edit_deposit");
    }

    @Override
    public void onFinishEditDialog(String bankName, Double amount) {
        Transaction transaction = new Transaction();
        for (BankAccount bank : user.getVerifiedBanks()) {
            if (bankName.equals(bank.getBankName())) {
                transaction.setBank(bank);
            }
        }
        transaction.setAmount(amount);
        transaction.setUser(user);
        transaction.setType(false);
        transaction.setGoal(goal);
        if (user.getRequiresApproval()) {
            transaction.setApproval(false);
        } else {
            transaction.setApproval(true);
            Date currentTime = Calendar.getInstance().getTime();
            transaction.setTransactionCompleteDate(currentTime);
        }
        transaction.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    checkTransactionApproval(transaction);
                } else {
                    e.printStackTrace();
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkTransactionApproval(Transaction transaction) {
        if (transaction.getApproval()) {
            transaction.getBank().withdraw(transaction.getAmount());
            goal.addSaved(transaction.getAmount());
            Toast.makeText(context, "Deposit complete.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Parent notified for approval.", Toast.LENGTH_SHORT).show();
        }
        goal.addTransaction(transaction);
        goal.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    transactionsList.clear();
                    adapter.notifyDataSetChanged();
                    loadTransactions();
                } else {
                    e.printStackTrace();
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @OnClick(R.id.cancel_goal_btn)
    public void onClickCancel(){
        showCancelGoalDialog();
    }

    private void showCancelGoalDialog() {
        CancelGoalDialogFragment cancel = CancelGoalDialogFragment.newInstance("Cancel Goal", goal);
        cancel.show(fragmentManager, "fragment_cancel_goal");
    }

    @OnClick(R.id.ivEditGoalName)
    public void onClickGoalName() {
        showEditGoalNameDialog();
    }

    private void showEditGoalNameDialog() {
        EditGoalNameDialogFragment editName = EditGoalNameDialogFragment.newInstance("Edit Goal Name", goal);
        editName.show(fragmentManager, "fragment_edit_goal_name");
    }


    @OnClick(R.id.ivGoalDetailsImage)
    public void onClickImage(){
        showEditGoalImageDialog();
    }

    private void showEditGoalImageDialog() {
        EditGoalImageDialogFragment editImage = EditGoalImageDialogFragment.newInstance("Edit Goal Image", goal);
        editImage.show(fragmentManager, "fragment_edit_goal_image");
    }

    @OnClick(R.id.ivEditGoalDate)
    public void onClickDate(){
        showEditGoalEndDateDialog();
    }

    private void showEditGoalEndDateDialog() {
        EditGoalEndDateDialogFragment editDate = EditGoalEndDateDialogFragment.newInstance("Edit Goal End Date", goal);
        editDate.show(fragmentManager, "fragment_edit_goal_end_date");
    }
}

