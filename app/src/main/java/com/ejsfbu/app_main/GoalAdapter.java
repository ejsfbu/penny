package com.example.instagram;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.ejsfbu.app_main.Fragments.GoalsListFragment;
import com.ejsfbu.app_main.R;
import com.ejsfbu.app_main.models.Goal;
import com.parse.ParseFile;

import java.util.List;

public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.ViewHolder> {

    private List<Goal> goalsList;
    Context context;

    public GoalAdapter(Context context, List<Goal> goals) {
        goalsList = goals;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View goalView = inflater.inflate(R.layout.goal_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(goalView);
        return viewHolder;

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

        ImageView ivgoalImage;
        TextView tvgoalName;

        public ViewHolder(View itemView) {
            super(itemView);
            ivgoalImage = itemView.findViewById(R.id.ivgoalImage);
            tvgoalName = itemView.findViewById(R.id.tvgoalName);

        }

        public void bind(final Goal goal) {
//            tvgoalName.setText(goal.get); // TODO get the goal name, the goal imgiage, and if there is no image, you can set a default.
//
////            if (photo != null) {
////                Glide.with(context)
////                        .load(photo.getUrl()) //TODOG
////                        .into(ivgoalImage);
////            } else {
////                Glide.with(context)
////                        .load(photo.getUrl())
////                        .into(ivgoalImage);
////            }
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
}

