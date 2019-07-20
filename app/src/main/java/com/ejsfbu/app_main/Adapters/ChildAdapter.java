package com.ejsfbu.app_main.Adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ejsfbu.app_main.R;
import com.ejsfbu.app_main.models.Goal;
import com.ejsfbu.app_main.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.List;

public class ChildAdapter {

    private List<User> children;
    private Context context;

    private int numGoalsInProgress;
    private int numGoalsCompleted;

    public ChildAdapter(Context context, List<User> children) {
        this.context = context;
        this.children = children;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivChildProfilePic;
        private TextView tvChildName;
        private TextView tvNumGoalsInProgress;
        private TextView tvNumGoalsCompleted;
        private TextView tvPendingRequests;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivChildProfilePic = itemView.findViewById(R.id.ivChildProfilePic);
            tvChildName = itemView.findViewById(R.id.tvChildName);
            tvNumGoalsCompleted = itemView.findViewById(R.id.tvNumGoalsCompleted);
            tvNumGoalsInProgress = itemView.findViewById(R.id.tvNumGoalsInProgress);
            tvPendingRequests = itemView.findViewById(R.id.tvPendingRequests);
        }

        public void bind(User child) {

            tvChildName.setText(child.getName());

            getNumGoalsInProgress(child);
            tvNumGoalsInProgress.setText(numGoalsInProgress + "Goals in Progress");

            getNumGoalsCompleted(child);
            tvNumGoalsCompleted.setText(numGoalsCompleted + "Goals Completed");
        }

        public void getNumGoalsInProgress(User child) {
            Goal.Query goalQuery = new Goal.Query();
            goalQuery.fromUser(child).areNotCompleted();
            goalQuery.findInBackground(new FindCallback<Goal>() {
                @Override
                public void done(List<Goal> objects, ParseException e) {
                    if (e == null) {
                        numGoalsInProgress = objects.size();
                    } else {
                        e.printStackTrace();
                    }
                }
            });
        }

        public void getNumGoalsCompleted(User child) {
            Goal.Query goalQuery = new Goal.Query();
            goalQuery.fromUser(child).areCompleted();
            goalQuery.findInBackground(new FindCallback<Goal>() {
                @Override
                public void done(List<Goal> objects, ParseException e) {
                    if (e == null) {
                        numGoalsCompleted = objects.size();
                    } else {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

}
