package com.ejsfbu.app_main.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.ejsfbu.app_main.EditFragments.CancelGoalDialogFragment;
import com.ejsfbu.app_main.Fragments.GoalDetailsFragment;
import com.ejsfbu.app_main.Fragments.GoalsListFragment;
import com.ejsfbu.app_main.Fragments.TransferGoalFragment;
import com.ejsfbu.app_main.R;
import com.ejsfbu.app_main.models.Goal;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;

import java.util.Date;
import java.util.List;

import static com.ejsfbu.app_main.Activities.MainActivity.fragmentManager;

public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.ViewHolder> {

    private List<Goal> goalsList;
    private Context context;
    private Fragment purpose;
    private Goal cancelled;

    public GoalAdapter(Context context, List<Goal> goals, CancelGoalDialogFragment purpose) {
        this.context = context;
        this.goalsList = goals;
        this.purpose = purpose;
        this.cancelled = purpose.getCancelledGoal();
    }

    public GoalAdapter(Context context, List<Goal> goals) {
        this.context = context;
        this.goalsList = goals;
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
        private TextView tvEndDate;
        private TextView tvPercentDone;
        private ProgressBar pbPercentDone;
        private ConstraintLayout root;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivGoalImage = itemView.findViewById(R.id.ivGoalImage);
            tvGoalName = itemView.findViewById(R.id.tvGoalName);
            tvEndDate = itemView.findViewById(R.id.tvEndDate);
            tvPercentDone = itemView.findViewById(R.id.tvPercentDone);
            pbPercentDone = itemView.findViewById(R.id.pbPercentDone);
            root = itemView.findViewById(R.id.root);
        }

        public void bind(Goal goal) {
            tvGoalName.setText(goal.getName());

            Date endDate = goal.getEndDate();
            if (endDate != null) {
                String endDateString = endDate.toString();
                tvEndDate.setText("by " + formatDateString(endDateString));
            }

            Double percentDone = (goal.getSaved() / goal.getCost()) * 100;
            tvPercentDone.setText(String.format("%.1f", percentDone.floatValue()) + "%");
            pbPercentDone.setProgress((int) percentDone.doubleValue());
            pbPercentDone.getProgressDrawable().setTint(context.getResources().getColor(R.color.money_green));

            ParseFile image = goal.getImage();
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
                Glide.with(context)
                        .load(imageUrl)
                        .apply(options) // Extra: round image corners
                        .into(ivGoalImage);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("Clicked Goal", goal);
                    //launch the details view
                    if ((purpose == null) && (cancelled == null)) {
                        Fragment fragment = new GoalDetailsFragment();
                        fragment.setArguments(bundle);
                        fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                    }
                    else {
                        //transfers money to this goal
                        Double saved = cancelled.getSaved();
                        System.out.println("Send money");

                        //deletes the goal
                        Goal.Query query = new Goal.Query();
                        query.whereEqualTo("objectId", cancelled.getObjectId());
                        query.findInBackground(new FindCallback<Goal>() {
                            @Override
                            public void done(List<Goal> objects, ParseException e) {
                                if (e == null) {
                                    objects.get(0).deleteInBackground();
                                    objects.get(0).saveInBackground();
                                } else {
                                    e.printStackTrace();
                                }
                            }
                        });

                        //sends you to that detail goal
                        Toast.makeText(context, "Transfer has been made & goal is now deleted!", Toast.LENGTH_LONG).show();
                        Fragment fragment = new GoalDetailsFragment();
                        fragment.setArguments(bundle);
                        fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                    }
                }
            });
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

