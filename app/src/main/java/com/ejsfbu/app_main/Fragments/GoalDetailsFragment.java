package com.ejsfbu.app_main.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.ejsfbu.app_main.Adapters.TransactionAdapter;
import com.ejsfbu.app_main.DialogFragments.CancelGoalDialogFragment;
import com.ejsfbu.app_main.DialogFragments.DepositDialogFragment;
import com.ejsfbu.app_main.DialogFragments.EarnedBadgeDialogFragment;
import com.ejsfbu.app_main.DialogFragments.EditGoalEndDateDialogFragment;
import com.ejsfbu.app_main.DialogFragments.EditGoalImageDialogFragment;
import com.ejsfbu.app_main.DialogFragments.EditGoalNameDialogFragment;
import com.ejsfbu.app_main.DialogFragments.SetUpAutoPaymentDialogFragment;
import com.ejsfbu.app_main.EndlessRecyclerViewScrollListener;
import com.ejsfbu.app_main.Models.BankAccount;
import com.ejsfbu.app_main.Models.Goal;
import com.ejsfbu.app_main.Models.Request;
import com.ejsfbu.app_main.Models.Reward;
import com.ejsfbu.app_main.Models.Transaction;
import com.ejsfbu.app_main.Models.User;
import com.ejsfbu.app_main.R;
import com.parse.ParseACL;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;
import static com.ejsfbu.app_main.Activities.MainActivity.bottomNavigationView;
import static com.ejsfbu.app_main.Activities.MainActivity.fragmentManager;
import static com.ejsfbu.app_main.Activities.MainActivity.ibGoalDetailsBack;
import static com.ejsfbu.app_main.Activities.MainActivity.ibRewardGoalDetailsBack;
import static com.ejsfbu.app_main.Models.Reward.checkCompletedGoals;
import static com.ejsfbu.app_main.Models.Reward.checkEarnedRewards;

public class GoalDetailsFragment extends Fragment implements
        EditGoalNameDialogFragment.EditGoalNameDialogListener,
        EditGoalEndDateDialogFragment.EditGoalDateDialogListener,
        DepositDialogFragment.DepositDialogListener,
        EditGoalImageDialogFragment.EditGoalImageDialogListener,
        SetUpAutoPaymentDialogFragment.SetUpAutoPaymentDialogListener {

    @BindView(R.id.ivGoalDetailsImage)
    ImageView ivGoalDetailsImage;
    @BindView(R.id.tvGoalDetailsName)
    TextView tvGoalDetailsName;
    @BindView(R.id.pbGoalDetailsPercentDone)
    ProgressBar pbGoalDetailsPercentDone;
    @BindView(R.id.tvGoalDetailsPercentDone)
    TextView tvGoalDetailsPercentDone;
    @BindView(R.id.rvGoalTransactionHistory)
    RecyclerView rvTransactions;
    @BindView(R.id.bGoalDetailsDeposit)
    Button bGoalDetailsDeposit;
    @BindView(R.id.tvGoalDetailsTransactionHistoryTitle)
    TextView tvGoalDetailsTransactionHistoryTitle;
    @BindView(R.id.bGoalDetailsCancelGoal)
    Button bGoalDetailsCancelGoal;
    @BindView(R.id.tvGoalDetailsDateCompletedTitle)
    TextView tvGoalDetailsDateCompletedTitle;
    @BindView(R.id.tvGoalDetailsDateCompleted)
    TextView tvGoalDetailsCompletionDate;
    @BindView(R.id.tvGoalDetailsTotalCostTitle)
    TextView tvGoalDetailsTotalCostTitle;
    @BindView(R.id.tvGoalDetailsTotalCost)
    TextView tvGoalDetailsTotalCost;
    @BindView(R.id.tvGoalDetailsAmountSavedTitle)
    TextView tvGoalDetailsAmountSavedTitle;
    @BindView(R.id.tvGoalDetailsAmountSaved)
    TextView tvGoalDetailsAmountSaved;
    @BindView(R.id.noTransactionsText)
    TextView noTransactionText;
    @BindView(R.id.ivEditGoalName)
    ImageButton ivEditGoalName;
    @BindView(R.id.ivEditGoalDate)
    ImageButton ivEditGoalDate;
    @BindView(R.id.tvGoalDetailEdit)
    TextView tvGoalDetailEdit;
    @BindView(R.id.tvGoalDetailStatus)
    TextView tvGoalDetailStatus;
    @BindView(R.id.spinTimeOptions)
    Spinner spinner;
    @BindView(R.id.tvGoalDetailReccomendedSaving)
    TextView tvGoalDetailReccomendedSaving;
    @BindView(R.id.tvGoalDetailSavingText)
    TextView tvGoalDetailSavingText;
    @BindView(R.id.bGoalDetailsPurchaseGoal)
    Button bGoalDetailsPurchaseGoal;
    @BindView(R.id.bGoalDetailsAutoPay)
    Button bAutoPay;
    @BindView(R.id.tvAutoPayFrequency)
    TextView tvAutoPayFrequency;
    @BindView(R.id.tvAutoPaymentText)
    TextView tvAutoPaymentText;

    private Unbinder unbinder;
    List<Transaction> transactionsList;
    TransactionAdapter adapter;
    private int transactionsLoaded;
    private Double dailySavingGoal;
    private Double weeklySavingGoal;
    private Double monthlySavingGoal;
    private LinearLayoutManager linearLayoutManager;
    private EndlessRecyclerViewScrollListener scrollListener;
    private Goal goal;
    private User user;
    private Context context;
    private ArrayList<Reward> earnedBadges;
    public static int BANK_REQUEST_CODE = 321;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);
        if (bottomNavigationView.getSelectedItemId() == R.id.miGoals) {
            ibGoalDetailsBack.setVisibility(View.VISIBLE);
        } else if (bottomNavigationView.getSelectedItemId() == R.id.miRewards) {
            ibRewardGoalDetailsBack.setVisibility(View.VISIBLE);
        }
        goal = getArguments().getParcelable("Clicked Goal");
        setGoalInfo();
        transactionsList = new ArrayList<>();
        earnedBadges = new ArrayList<>();
        adapter = new TransactionAdapter(getContext(), transactionsList);
        rvTransactions.setAdapter(adapter);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rvTransactions.setLayoutManager(linearLayoutManager);
        loadTransactions();
        setSpinnerOnClick();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        user = (User) ParseUser.getCurrentUser();
        user.setACL(new ParseACL(user));
        user.saveInBackground();
        context = container.getContext();
        return inflater.inflate(R.layout.fragment_goal_details, container, false);
    }

    public void setGoalInfo() {
        if (goal.getCompleted()) {
            // change how completed goal is shown
        }
        tvGoalDetailsName.setText(goal.getName());
        String goalEndDate = formatDate(goal.getEndDate().toString());

        if(goal.getCompleted()) {
            tvGoalDetailsDateCompletedTitle.setText("Date Completed:");
            tvGoalDetailsCompletionDate.setText(goalEndDate);
            tvGoalDetailsAmountSavedTitle.setVisibility(View.GONE);
            tvGoalDetailsAmountSaved.setVisibility(View.GONE);
            bGoalDetailsCancelGoal.setVisibility(View.INVISIBLE);
            bGoalDetailsDeposit.setVisibility(View.INVISIBLE);
            bAutoPay.setVisibility(View.INVISIBLE);
            ivEditGoalDate.setVisibility(View.GONE);
            ivEditGoalName.setVisibility(View.GONE);
            tvGoalDetailEdit.setVisibility(View.GONE);
            tvGoalDetailReccomendedSaving.setVisibility(View.GONE);
            tvGoalDetailSavingText.setVisibility(View.GONE);
            spinner.setVisibility(View.GONE);
            if (goal.getPurchased()) {
                bGoalDetailsCancelGoal.setVisibility(View.GONE);
                bGoalDetailsDeposit.setVisibility(View.GONE);
                bGoalDetailsPurchaseGoal.setVisibility(View.GONE);
                bAutoPay.setVisibility(View.GONE);
            } else {
                bGoalDetailsPurchaseGoal.setVisibility(View.VISIBLE);
            }
        } else {
            tvGoalDetailsCompletionDate.setText(goalEndDate);
            tvGoalDetailsAmountSaved.setText(formatCurrency(goal.getSaved()));
        }

        if (goal.getHasAutoPayment()) {
            tvAutoPayFrequency.setVisibility(View.VISIBLE);
            tvAutoPaymentText.setVisibility(View.VISIBLE);
            formatAutoPayText(goal.getAutoPayTimesFrequencyIsRepeated(), goal.getAutoPayFrequency());
        } else {
            tvAutoPayFrequency.setVisibility(View.GONE);
            tvAutoPaymentText.setVisibility(View.GONE);
        }

        tvGoalDetailsTotalCost.setText(formatCurrency(goal.getCost()));

        Double percentDone = (goal.getSaved() / goal.getCost()) * 100;
        tvGoalDetailsPercentDone.setText(String.format("%.1f", percentDone.floatValue()) + "%");
        pbGoalDetailsPercentDone.setProgress((int) percentDone.doubleValue());
        pbGoalDetailsPercentDone.getProgressDrawable().setTint(getContext()
                .getResources().getColor(R.color.money_green));

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
        calculateSavings();
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

    @OnClick(R.id.bGoalDetailsDeposit)
    public void onClickDeposit() {
        showDepositDialog();
    }

    @OnClick(R.id.bGoalDetailsPurchaseGoal)
    public void onClickPurchase() {
        goal.setPurchased(true);
        goal.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(context, "Money transferred to bank for purchase.", Toast.LENGTH_LONG).show();
                    setGoalInfo();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showDepositDialog() {
        Double limit = 0.0;
        if (goal.getTransactions().size() == 0) {
            limit = Double.MAX_VALUE;
        } else {
            for (Transaction transaction: goal.getTransactions()) {
                limit += transaction.getAmount();
            }
            limit = goal.getCost() - limit;
        }

        DepositDialogFragment depositDialogFragment
                = DepositDialogFragment.newInstance("Deposit", goal.getCost() - goal.getSaved(), limit);
        depositDialogFragment.show(MainActivity.fragmentManager,
                "fragment_deposit");
    }


    @Override
    public void onFinishSetUpAutoPaymentDialog(String bankName, Double amount, String timesRepeated, String frequency) {
        goal.setHasAutoPayment(true);
        goal.setAutoPayAmount(amount);
        goal.setAutoPayFrequency(frequency);
        goal.setAutoPayTimesFrequencyIsRepeated(timesRepeated);
        goal.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    setGoalInfo();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }


    public void formatAutoPayText(String timesRepeated, String frequency) {
        StringBuilder frequencyDisplay = new StringBuilder();
        if (Integer.valueOf(timesRepeated) == 1) {
            frequencyDisplay.append("Once a " + frequency.toLowerCase());
        } else {
            frequencyDisplay.append("Every " + timesRepeated + " " + frequency.toLowerCase());
        }
        tvAutoPayFrequency.setText(frequencyDisplay.toString());
    }

    @Override
    public void onFinishEditDialog(String bankName, Double amount) {
        BankAccount bankSet = null;
        boolean approval;
        for (BankAccount bank : user.getVerifiedBanks()) {
            if (bankName.equals(bank.getBankName())) {
                bankSet = bank;
            }
        }
        if (user.getRequiresApproval()) {
            approval = false;
        } else {
            approval = true;
        }
        Transaction transaction = new Transaction(user, bankSet, amount, goal, approval, false);
        // TODO SET ACL CHANGE LATER
        ParseACL acl = new ParseACL(user);
        acl.setPublicWriteAccess(true);
        acl.setPublicReadAccess(true);
        transaction.setACL(acl);
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
            goal.setDailySavings(Goal.calculateDailySaving(goal));
            user.setTotalSaved(user.getTotalSaved() + transaction.getAmount());
        } else {
            createRequest(transaction);
        }
        goal.addTransaction(transaction);
        goal.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    transactionsList.clear();
                    adapter.notifyDataSetChanged();
                    loadTransactions();
                    setGoalInfo();
                    if (transaction.getApproval()) {
                        Toast.makeText(context, "Deposit complete.", Toast.LENGTH_SHORT).show();
                        checkCompleted(goal);
                    }
                } else {
                    e.printStackTrace();
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void createRequest(Transaction transaction) {
        Request request = new Request();
        request.setUser(user);
        request.setRequestType("Goal Deposit");
        request.setRequestDetails(user.getName() + " wants to deposit $"
                + String.format("%.2f", transaction.getAmount()) + " towards " + goal.getName()
                + " from " + transaction.getBank().getBankName());
        request.setTransaction(transaction);
        // TODO SET ACL CHANGE LATER
        ParseACL acl = new ParseACL(user);
        acl.setPublicWriteAccess(true);
        acl.setPublicReadAccess(true);
        request.setACL(acl);
        request.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    JSONObject push = new JSONObject();
                    try {
                        push.put("sender", ParseInstallation.getCurrentInstallation().getInstallationId());
                        push.put("channel", user.getObjectId());
                    } catch (JSONException e2) {
                        e2.printStackTrace();
                    }
                    HashMap<String, String> data = new HashMap<>();
                    data.put("customData", push.toString());
                    ParseCloud.callFunctionInBackground("sendPush", data);
/*ParsePush push = new ParsePush();
push.setChannel(user.getObjectId());
push.setMessage(request.getRequestDetails());
push.sendInBackground(new SendCallback() {
    @Override
    public void done(ParseException e) {
        if (e == null) {
            Toast.makeText(context, "Parent notified for approval.", Toast.LENGTH_SHORT).show();
        } else {
            e.printStackTrace();
        }
    }
});*/

                    Toast.makeText(context, "Parent notified for approval.", Toast.LENGTH_SHORT).show();
                } else {
                    e.printStackTrace();
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @OnClick(R.id.bGoalDetailsCancelGoal)
    public void onClickCancel() {
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

    @OnClick(R.id.bGoalDetailsAutoPay)
    public void onClickAutoPay(){
        showSetUpAutoPaymentDialogFragment();
    }

    private void showSetUpAutoPaymentDialogFragment() {
        SetUpAutoPaymentDialogFragment fragment = SetUpAutoPaymentDialogFragment.newInstance(goal, user);
        fragment.show(fragmentManager, "fragment_automatic_payment");
    }

    @OnClick(R.id.tvGoalDetailEdit)
    public void onClickEdit() {
        showEditGoalImageDialog();
    }

    @OnClick(R.id.ivGoalDetailsImage)
    public void onClickImage() {
        if (!goal.getCompleted()) {
            showEditGoalImageDialog();
        }
    }

    private void showEditGoalImageDialog() {
        EditGoalImageDialogFragment editImage = EditGoalImageDialogFragment.newInstance("Edit Goal Image", goal);
        editImage.show(fragmentManager, "fragment_edit_goal_image");
    }

    @OnClick(R.id.ivEditGoalDate)
    public void onClickDate() {
        showEditGoalEndDateDialog();
    }

    private void showEditGoalEndDateDialog() {
        EditGoalEndDateDialogFragment editDate = EditGoalEndDateDialogFragment.newInstance("Edit Goal End Date", goal);
        editDate.show(fragmentManager, "fragment_edit_goal_end_date");
    }

    private void showEarnedBadgeDialogFragment() {
        EarnedBadgeDialogFragment earnedBadge
                = EarnedBadgeDialogFragment.newInstance("Earned Badge", earnedBadges);
        earnedBadge.show(fragmentManager, "fragment_earned_badge");
    }

    @Override
    public void onFinishEditThisDialog() {
        setGoalInfo();
    }

    public void checkCompleted(Goal goal) {
        if (goal.getSaved() >= goal.getCost()) {
            goal.setCompleted(true);
            Date endDate = goal.getEndDate();
            Date currentTime = Calendar.getInstance().getTime();
            if (endDate.compareTo(currentTime) > 0) {
                goal.setCompletedEarly(true);
            } else {
                goal.setCompletedEarly(false);
            }
            goal.setDateCompleted(currentTime);
            goal.setEndDate(currentTime);
            goal.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(context, "Goal Completed!", Toast.LENGTH_LONG).show();
                        if (goal.getCompletedEarly()) {
                            user.setEarlyGoals(user.getEarlyGoals() + 1);
                        }
                        if (goal.getCost() <= 10.00) {
                            user.setSmallGoals(user.getSmallGoals() + 1);
                        }
                        if ((goal.getCost() >= 20.00) && (goal.getCost() <= 40.00) ) {
                            user.setMediumGoals(user.getMediumGoals() + 1);
                        }
                        if (goal.getCost() >= 100.00) {
                            user.setBigGoals(user.getBigGoals() + 1);
                        }
                        user.addCompletedGoal(goal);
                        user.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    earnedBadges.addAll(checkEarnedRewards(user));
                                    if (earnedBadges.size() != 0) {
                                        showEarnedBadgeDialogFragment();
                                    }
                                } else {
                                    e.printStackTrace();
                                }
                            }
                        });
                        setGoalInfo();
                    } else {
                        e.printStackTrace();
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void calculateSavings() {
        long today = System.currentTimeMillis();
        long diffInMillies = Math.abs(today - goal.getCreatedAt().getTime());
        long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        long diffInWeeks = diffInDays / 7;
        long diffInMonths = diffInDays / 30;
        dailySavingGoal = goal.getDailySavings();
        weeklySavingGoal = dailySavingGoal * 7;
        if (weeklySavingGoal > (goal.getCost() - goal.getSaved())) { weeklySavingGoal = goal.getCost() - goal.getSaved(); }
        monthlySavingGoal = dailySavingGoal * 30;
        if (monthlySavingGoal > (goal.getCost() - goal.getSaved())) { monthlySavingGoal = goal.getCost() - goal.getSaved(); }
        Double onTrackAmount = diffInDays * dailySavingGoal;
        if(goal.getSaved() >= onTrackAmount) {
            if (goal.getCompleted()) {
                tvGoalDetailStatus.setText("Completed");
                tvGoalDetailStatus.setTextColor(context
                        .getResources().getColor(R.color.money_green));
            } else {
                tvGoalDetailStatus.setText("On Track");
                tvGoalDetailStatus.setTextColor(context
                        .getResources().getColor(R.color.money_green));
            }
        } else {
            tvGoalDetailStatus.setText("Behind Schedule");
            tvGoalDetailStatus.setTextColor(context
                    .getResources().getColor(R.color.colorAccent));
        }
        String amount = formatCurrency(dailySavingGoal) + " per day";
        tvGoalDetailReccomendedSaving.setText(amount);
    }

    private void setSpinnerOnClick() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 0) {
                    String amount = formatCurrency(dailySavingGoal) + " per day";
                    tvGoalDetailReccomendedSaving.setText(amount);
                } else if (position == 1) {
                    String amount = formatCurrency(weeklySavingGoal) + " per week";
                    tvGoalDetailReccomendedSaving.setText(amount);
                } else {
                    String amount = formatCurrency(monthlySavingGoal) + " per month";
                    tvGoalDetailReccomendedSaving.setText(amount);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // no action
            }

        });
    }
}

