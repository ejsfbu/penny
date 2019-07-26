package com.ejsfbu.app_main.Activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.ejsfbu.app_main.Adapters.GoalAdapter;
import com.ejsfbu.app_main.R;
import com.ejsfbu.app_main.Models.Goal;
import com.ejsfbu.app_main.Models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChildDetailActivity extends AppCompatActivity {

    private User child;

    @BindView(R.id.tvChildDetailName)
    TextView tvChildDetailName;
    @BindView(R.id.tvChildDetailAccountCode)
    TextView tvChildDetailAccountCode;

    @BindView(R.id.ivChildDetailProfilePic)
    ImageView ivChildDetailProfilePic;

    @BindView(R.id.rvChildDetailCompletedGoals)
    RecyclerView rvChildDetailCompletedGoals;
    @BindView(R.id.rvChildDetailInProgressGoals)
    RecyclerView rvChildDetailInProgressGoals;
    @BindView(R.id.rvChildDetailPendingRequests)
    RecyclerView rvChildDetailPendingRequests;

    private List<Goal> completedGoals;
    private List<Goal> inProgressGoals;

    private GoalAdapter completedGoalsAdapter;
    private GoalAdapter inProgressGoalsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_detail);

        ButterKnife.bind(this);

        completedGoals = new ArrayList<>();
        inProgressGoals = new ArrayList<>();

        completedGoalsAdapter = new GoalAdapter(this, completedGoals);
        inProgressGoalsAdapter = new GoalAdapter(this, inProgressGoals);

        rvChildDetailCompletedGoals.setAdapter(completedGoalsAdapter);
        rvChildDetailInProgressGoals.setAdapter(inProgressGoalsAdapter);

        rvChildDetailCompletedGoals.setLayoutManager(new LinearLayoutManager(this));
        rvChildDetailInProgressGoals.setLayoutManager(new LinearLayoutManager(this));

        String childCode = getIntent().getStringExtra("childCode");
        getChildFromCode(childCode);
    }

    public void getChildFromCode(String childCode) {
        User.Query userQuery = new User.Query();
        userQuery.whereEqualTo("objectId", childCode);
        userQuery.findInBackground(new FindCallback<User>() {
            @Override
            public void done(List<User> objects, ParseException e) {
                if (e == null) {
                    child = objects.get(0);
                    fillData();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public void fillData() {
        tvChildDetailName.setText(child.getName());

        String accountCode = "Account Code: " + child.getObjectId();
        tvChildDetailAccountCode.setText(accountCode);

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
            Glide.with(ChildDetailActivity.this)
                    .load(imageUrl)
                    .apply(options) // Extra: round image corners
                    .into(ivChildDetailProfilePic);
        }

        loadCompletedGoals();
        loadInProgressGoals();
    }

    public void loadCompletedGoals() {
        final Goal.Query goalQuery = new Goal.Query();
        goalQuery.getTopCompleted().areCompleted().fromUser(child);
        goalQuery.findInBackground(new FindCallback<Goal>() {
            @Override
            public void done(List<Goal> objects, ParseException e) {
                if (e == null) {
                    completedGoals.addAll(objects);
                    completedGoalsAdapter.notifyDataSetChanged();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public void loadInProgressGoals() {
        final Goal.Query goalQuery = new Goal.Query();
        goalQuery.getTopByEndDate().areNotCompleted().fromUser(child);
        goalQuery.findInBackground(new FindCallback<Goal>() {
            @Override
            public void done(List<Goal> objects, ParseException e) {
                if (e == null) {
                    inProgressGoals.addAll(objects);
                    inProgressGoalsAdapter.notifyDataSetChanged();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}
