package com.ejsfbu.app_main.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.ejsfbu.app_main.R;
import com.ejsfbu.app_main.models.Goal;
import com.ejsfbu.app_main.models.Request;
import com.ejsfbu.app_main.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;

import java.util.List;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ViewHolder> {

    private List<User> children;
    private Context context;

    private int numGoalsInProgress;
    private int numGoalsCompleted;
    private int numPendingRequests;

    public ChildAdapter(Context context, List<User> children) {
        this.context = context;
        this.children = children;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_child, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User child = children.get(position);
        holder.bind(child);
    }

    @Override
    public int getItemCount() {
        return children.size();
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
            tvNumGoalsInProgress.setText(numGoalsInProgress + " Goals in Progress");

            getNumGoalsCompleted(child);
            tvNumGoalsCompleted.setText(numGoalsCompleted + " Goals Completed");

            getNumPendingRequests(child);
            tvPendingRequests.setText(numPendingRequests + " Requests Pending");

            ParseFile image = child.getProfilePic();
            if (image != null) {
                String imageUrl = image.getUrl();
                imageUrl = imageUrl.substring(4);
                imageUrl = "https" + imageUrl;
                RequestOptions options = new RequestOptions();
                options.placeholder(R.drawable.icon_user)
                        .error(R.drawable.icon_user)
                        .transform(new CenterCrop())
                        .transform(new CircleCrop());
                Glide.with(context)
                        .load(imageUrl)
                        .apply(options) // Extra: round image corners
                        .into(ivChildProfilePic);
            }
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

        public void getNumPendingRequests(User child) {
            Request.Query requestQuery = new Request.Query();
            requestQuery.fromUser(child);
            requestQuery.findInBackground(new FindCallback<Request>() {
                @Override
                public void done(List<Request> objects, ParseException e) {
                    if (e == null) {
                        numPendingRequests = objects.size();
                    } else {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

}
