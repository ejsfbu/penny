package com.ejsfbu.app_main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.ejsfbu.app_main.Fragments.GoalsListFragment;
import com.ejsfbu.app_main.R;
import com.ejsfbu.app_main.models.Goal;
import com.ejsfbu.app_main.models.User;
import com.parse.ParseFile;

import java.text.DecimalFormat;
import java.util.List;

public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.ViewHolder> {

    private List<Goal> goalsList;
    private Context context;

    public GoalAdapter(Context context, List<Goal> goals) {
        this.context = context;
        this.goalsList = goals;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.goal_item, parent, false);
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
        private TextView tvSaved;
        private TextView tvCost;
        private ConstraintLayout root;

        // TODO put spannable for description and make username bold and clickable and share button
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivGoalImage = itemView.findViewById(R.id.ivGoalImage);
            tvGoalName = itemView.findViewById(R.id.tvGoalName);
            tvSaved = itemView.findViewById(R.id.tvSave);
            tvCost = itemView.findViewById(R.id.tvCost);
            root = itemView.findViewById(R.id.root);
        }

        public void bind(Goal goal) {
            tvGoalName.setText(goal.getName());
            // format numbers with commas
            tvSaved.setText(String.format("%,f", goal.getSaved()));
            tvCost.setText(String.format("%,f", goal.getCost()));
            ParseFile image = goal.getImage();
            if (image != null) {
                String imageUrl = image.getUrl();
                imageUrl = imageUrl.substring(4);
                imageUrl = "https" + imageUrl;
                RequestOptions options = new RequestOptions();
                options.placeholder(R.color.colorPrimary)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .error(R.color.colorPrimary)
                        .transform(new CenterCrop())
                        .transform(new CircleCrop());
                Glide.with(context)
                        .load(imageUrl)
                        .apply(options) // Extra: round image corners
                        .into(ivGoalImage);
            }
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

    public Double formatDouble(Double d) {
        d = (d * 100.0);
        //d = DecimalFormat.s
        d /= 100.0;
        return d;
    }
}

