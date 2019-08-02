package com.ejsfbu.app_main.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import com.ejsfbu.app_main.Adapters.RequestAdapter;
import com.ejsfbu.app_main.Models.Request;
import com.ejsfbu.app_main.R;
import com.ejsfbu.app_main.Models.Goal;
import com.ejsfbu.app_main.Models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;

public class ChildDetailActivity extends AppCompatActivity {

    private User child;

    @BindView(R.id.ibChildDetailBack)
    ImageButton ibChildDetailBack;

    @BindView(R.id.tvChildDetailName)
    TextView tvChildDetailName;
    @BindView(R.id.tvChildDetailAccountCode)
    TextView tvChildDetailAccountCode;
    @BindView(R.id.ivChildDetailProfilePic)
    ImageView ivChildDetailProfilePic;

    @BindView(R.id.rvChildDetailCompletedGoals)
    RecyclerView rvChildDetailCompletedGoals;
    @BindView(R.id.vChildDetailsCompletedGoalsTop)
    View vChildDetailsCompletedGoalsTop;
    @BindView(R.id.vChildDetailsCompletedGoalsBottom)
    View vChildDetailsCompletedGoalsBottom;
    @BindView(R.id.vChildDetailsCompletedGoalsLeft)
    View vChildDetailsCompletedGoalsLeft;
    @BindView(R.id.vChildDetailsCompletedGoalsRight)
    View vChildDetailsCompletedGoalsRight;
    @BindView(R.id.tvChildDetailsNoCompletedGoals)
    TextView tvChildDetailsNoCompletedGoals;

    @BindView(R.id.vChildDetailsDoubleDivider2_1)
    View vChildDetailsDoubleDivider2_1;
    @BindView(R.id.vChildDetailsDoubleDivider2_2)
    View vChildDetailsDoubleDivider2_2;

    @BindView(R.id.tvChildDetailInProgressGoals)
    TextView tvChildDetailInProgressGoals;
    @BindView(R.id.rvChildDetailInProgressGoals)
    RecyclerView rvChildDetailInProgressGoals;
    @BindView(R.id.vChildDetailsInProgressGoalsTop)
    View vChildDetailsInProgressGoalsTop;
    @BindView(R.id.vChildDetailsInProgressGoalsBottom)
    View vChildDetailsInProgressGoalsBottom;
    @BindView(R.id.vChildDetailsInProgressGoalsLeft)
    View vChildDetailsInProgressGoalsLeft;
    @BindView(R.id.vChildDetailsInProgressGoalsRight)
    View vChildDetailsInProgressGoalsRight;
    @BindView(R.id.tvChildDetailsNoInProgressGoals)
    TextView tvChildDetailsNoInProgressGoals;

    @BindView(R.id.vChildDetailsDoubleDivider3_1)
    View vChildDetailsDoubleDivider3_1;
    @BindView(R.id.vChildDetailsDoubleDivider3_2)
    View vChildDetailsDoubleDivider3_2;

    @BindView(R.id.tvChildDetailPendingRequests)
    TextView tvChildDetailPendingRequests;
    @BindView(R.id.rvChildDetailPendingRequests)
    RecyclerView rvChildDetailPendingRequests;
    @BindView(R.id.vChildDetailsPendingRequestsTop)
    View vChildDetailsPendingRequestsTop;
    @BindView(R.id.vChildDetailsPendingRequestsBottom)
    View vChildDetailsPendingRequestsBottom;
    @BindView(R.id.vChildDetailsPendingRequestsLeft)
    View vChildDetailsPendingRequestsLeft;
    @BindView(R.id.vChildDetailsPendingRequestsRight)
    View vChildDetailsPendingRequestsRight;
    @BindView(R.id.tvChildDetailsNoPendingRequests)
    TextView tvChildDetailsNoPendingRequests;

    private List<Goal> completedGoals;
    private List<Goal> inProgressGoals;
    private List<Request> pendingRequests;

    private GoalAdapter completedGoalsAdapter;
    private GoalAdapter inProgressGoalsAdapter;
    private RequestAdapter pendingRequestsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_detail);

        ButterKnife.bind(this);

        completedGoals = new ArrayList<>();
        inProgressGoals = new ArrayList<>();
        pendingRequests = new ArrayList<>();

        completedGoalsAdapter = new GoalAdapter(this, completedGoals);
        inProgressGoalsAdapter = new GoalAdapter(this, inProgressGoals);
        pendingRequestsAdapter = new RequestAdapter(this, pendingRequests);

        rvChildDetailCompletedGoals.setAdapter(completedGoalsAdapter);
        rvChildDetailInProgressGoals.setAdapter(inProgressGoalsAdapter);
        rvChildDetailPendingRequests.setAdapter(pendingRequestsAdapter);

        rvChildDetailCompletedGoals.setLayoutManager(new LinearLayoutManager(this));
        rvChildDetailInProgressGoals.setLayoutManager(new LinearLayoutManager(this));
        rvChildDetailPendingRequests.setLayoutManager(new LinearLayoutManager(this));

        String childCode = getIntent().getStringExtra("childCode");
        getChildFromCode(childCode);
    }

    @OnClick(R.id.ibChildDetailBack)
    public void onClickChildDetailBack() {
        Intent intent = new Intent(this, ParentActivity.class);
        startActivity(intent);
        finish();
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

        tvChildDetailAccountCode.setText(child.getObjectId());

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
        loadPendingRequests();
    }

    public void loadCompletedGoals() {
        List<Goal> goals = child.getCompletedGoals();
        if (goals == null || goals.size() == 0) {
            vChildDetailsCompletedGoalsBottom.setVisibility(GONE);
            vChildDetailsCompletedGoalsLeft.setVisibility(GONE);
            vChildDetailsCompletedGoalsRight.setVisibility(GONE);
            vChildDetailsCompletedGoalsTop.setVisibility(GONE);
            tvChildDetailsNoCompletedGoals.setVisibility(View.VISIBLE);
        } else {
            completedGoals.addAll(goals);
            Collections.sort(completedGoals);
            Collections.reverse(completedGoals);
            completedGoalsAdapter.notifyDataSetChanged();
        }
    }

    public void loadInProgressGoals() {
        List<Goal> goals = child.getInProgressGoals();
        if (goals == null || goals.size() == 0) {
            rvChildDetailInProgressGoals.setMinimumHeight(20);
            vChildDetailsInProgressGoalsBottom.setVisibility(GONE);
            vChildDetailsInProgressGoalsLeft.setVisibility(GONE);
            vChildDetailsInProgressGoalsRight.setVisibility(GONE);
            vChildDetailsInProgressGoalsTop.setVisibility(GONE);
            tvChildDetailsNoInProgressGoals.setVisibility(View.VISIBLE);
        } else {
            inProgressGoals.addAll(goals);
            Collections.sort(completedGoals);
            inProgressGoalsAdapter.notifyDataSetChanged();
        }
    }

    public void loadPendingRequests() {
        final Request.Query requestQuery = new Request.Query();
        requestQuery.fromUser(child);
        requestQuery.findInBackground(new FindCallback<Request>() {
            @Override
            public void done(List<Request> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() == 0) {
                        rvChildDetailPendingRequests.setMinimumHeight(20);
                        vChildDetailsPendingRequestsBottom.setVisibility(GONE);
                        vChildDetailsPendingRequestsLeft.setVisibility(GONE);
                        vChildDetailsPendingRequestsRight.setVisibility(GONE);
                        vChildDetailsPendingRequestsTop.setVisibility(GONE);
                        tvChildDetailsNoPendingRequests.setVisibility(View.VISIBLE);
                    } else {
                        pendingRequests.addAll(objects);
                        pendingRequestsAdapter.notifyDataSetChanged();
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}
