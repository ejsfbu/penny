package com.ejsfbu.app_main.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.ejsfbu.app_main.DialogFragments.CancelGoalDialogFragment;
import com.ejsfbu.app_main.Fragments.GoalDetailsFragment;
import com.ejsfbu.app_main.Models.BankAccount;
import com.ejsfbu.app_main.Models.User;
import com.ejsfbu.app_main.R;
import com.ejsfbu.app_main.Models.Goal;
import com.ejsfbu.app_main.Models.Transaction;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Date;
import java.util.List;

import static com.ejsfbu.app_main.Activities.MainActivity.bottomNavigationView;
import static com.ejsfbu.app_main.Activities.MainActivity.fragmentManager;
import static com.ejsfbu.app_main.Activities.MainActivity.ibGoalDetailsBack;
import static com.ejsfbu.app_main.Activities.MainActivity.ibRewardGoalDetailsBack;

public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.ViewHolder> {

    private List<Goal> goalsList;
    private Context context;
    private Fragment purpose;
    private Goal cancelled;
    private User user;

    public GoalAdapter(Context context, List<Goal> goals, CancelGoalDialogFragment purpose) {
        this.context = context;
        this.goalsList = goals;
        this.purpose = purpose;
        this.cancelled = purpose.getCancelledGoal();
        this.user = (User) ParseUser.getCurrentUser();
    }

    public GoalAdapter(Context context, List<Goal> goals) {
        this.context = context;
        this.goalsList = goals;
        this.user = (User) ParseUser.getCurrentUser();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_goal, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Goal goal = goalsList.get(i);
        viewHolder.bind(goal);
    }

    @Override
    public int getItemCount() {
        return goalsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivGoalImage;
        private TextView tvGoalName;
        private TextView tvGoalEndDate;
        private TextView tvGoalPercentDone;
        private ProgressBar pbGoalPercentDone;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivGoalImage = itemView.findViewById(R.id.ivGoalImage);
            tvGoalName = itemView.findViewById(R.id.tvGoalName);
            tvGoalEndDate = itemView.findViewById(R.id.tvGoalEndDate);
            tvGoalPercentDone = itemView.findViewById(R.id.tvGoalPercentDone);
            pbGoalPercentDone = itemView.findViewById(R.id.pbGoalPercentDone);

        }

        public void bind(Goal goal) {
            tvGoalName.setText(goal.getName());

            Date endDate = goal.getEndDate();
            if (endDate != null) {
                String endDateString = endDate.toString();
                if (!goal.getCompleted()) {
                    tvGoalEndDate.setText("by " + formatDateString(endDateString));
                } else {
                    tvGoalEndDate.setText("on " + formatDateString(endDateString));
                }
            }

            Double percentDone = (goal.getSaved() / goal.getCost()) * 100;
            tvGoalPercentDone.setText(String.format("%.1f", percentDone.floatValue()) + "%");
            pbGoalPercentDone.setProgress((int) percentDone.doubleValue());
            pbGoalPercentDone.getProgressDrawable().setTint(context.getResources()
                    .getColor(R.color.money_green));

            ParseFile image = goal.getImage();
            if (image != null) {
                String imageUrl = image.getUrl();
                imageUrl = imageUrl.replace("http://", "https://");
                RequestOptions options = new RequestOptions();
                options.placeholder(R.drawable.icon_target)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .error(R.drawable.icon_target)
                        .transform(new CenterCrop())
                        .transform(new CircleCrop());
                Glide.with(context)
                        .load(imageUrl)
                        .apply(options) // Extra: round image corners
                        .into(ivGoalImage);
            }
            setOnClick(goal);
        }
      
        private void setOnClick(Goal goal) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("Clicked Goal", goal);
                    //launch the details view
                    if (!user.getIsParent()) {
                        if ((purpose == null) && (cancelled == null)) {
                            Fragment fragment = new GoalDetailsFragment();
                            fragment.setArguments(bundle);
                            fragmentManager.beginTransaction()
                                    .replace(R.id.flMainContainer, fragment).commitAllowingStateLoss();
                        } else {
                            //transfers money to this goal
                            Double saved = cancelled.getSaved();
                            boolean approval;
                            if (user.getRequiresApproval()) {
                                approval = false;
                            } else {
                                approval = true;
                            }
                            Transaction transfer = new Transaction(user, cancelled.getName(), saved, goal, approval, false);
                            transfer.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        handleCancelledGoal(goal, transfer);
                                        //sends you to that detail goal
                                        Fragment fragment = new GoalDetailsFragment();
                                        fragment.setArguments(bundle);
                                        fragmentManager.beginTransaction()
                                                .replace(R.id.flMainContainer, fragment).commit();
                                    } else {
                                        Toast.makeText(context, "Transfer Failed",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                }
            });
        }

        private void handleCancelledGoal(Goal goal, Transaction transaction) {
            if (transaction.getApproval()) {
                goal.setSaved(goal.getSaved() + transaction.getAmount());
                Toast.makeText(context, "Money Transferred",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Parent notified for approval",
                        Toast.LENGTH_SHORT).show();
            }
            //deletes the goal
            cancelled.deleteInBackground();
            // might need to delay these for fragment change
            goal.addTransaction(transaction);
            goal.saveInBackground();
        }

        public String formatDateString(String date) {
            String[] datePieces = date.split(" ");
            return datePieces[1] + " " + datePieces[2] + ", " + datePieces[datePieces.length - 1];

        }
    }

    public void clear() {
        goalsList.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Goal> list) {
        goalsList.addAll(list);
        notifyDataSetChanged();
    }

    // limit double to 2 decimal places
    public static String formatDecimal(String number) {
        //if (places < 0) throw new IllegalArgumentException();
        int place = number.indexOf('.');

        return number.substring(0, place + 3);
    }
}

