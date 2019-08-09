package com.ejsfbu.app_main.Activities;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.ejsfbu.app_main.DialogFragments.EarnedBadgeDialogFragment;
import com.ejsfbu.app_main.DialogFragments.NeedsParentDialogFragment;
import com.ejsfbu.app_main.Fragments.BanksListFragment;
import com.ejsfbu.app_main.Fragments.GoalsListFragment;
import com.ejsfbu.app_main.Fragments.ProfileFragment;
import com.ejsfbu.app_main.Fragments.RewardsFragment;
import com.ejsfbu.app_main.Models.Reward;
import com.ejsfbu.app_main.Models.User;
import com.ejsfbu.app_main.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    public final static int BANK_REQUEST_CODE = 20;

    public static BottomNavigationView bottomNavigationView;
    public static ImageButton ibGoalDetailsBack;
    public static ImageButton ibRewardGoalDetailsBack;
    public static ImageButton ibBanksListBack;
    public static ImageButton ibBankDetailsBack;

    public static FragmentManager fragmentManager;
    private ArrayList<Reward> earnedRewards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        ibGoalDetailsBack = findViewById(R.id.ibGoalDetailsBack);
        ibRewardGoalDetailsBack = findViewById(R.id.ibRewardGoalDetailsBack);
        ibBanksListBack = findViewById(R.id.ibBanksListBack);
        ibBankDetailsBack = findViewById(R.id.ibBankDetailsBack);

        ibGoalDetailsBack.setVisibility(View.GONE);
        ibRewardGoalDetailsBack.setVisibility(View.GONE);
        ibBanksListBack.setVisibility(View.GONE);
        ibBankDetailsBack.setVisibility(View.GONE);

        fragmentManager = getSupportFragmentManager();
        setNavigationClick();

        ibGoalDetailsBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomNavigationView.setSelectedItemId(R.id.miGoals);
                ibGoalDetailsBack.setVisibility(View.GONE);
            }
        });
        ibRewardGoalDetailsBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomNavigationView.setSelectedItemId(R.id.miRewards);
                ibRewardGoalDetailsBack.setVisibility(View.GONE);
            }
        });
        ibBanksListBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomNavigationView.setSelectedItemId(R.id.miProfile);
                ibBanksListBack.setVisibility(View.GONE);
            }
        });
        ibBankDetailsBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new BanksListFragment();
                MainActivity.fragmentManager.beginTransaction()
                        .replace(R.id.flMainContainer, fragment).commit();
                ibBankDetailsBack.setVisibility(View.GONE);
            }
        });

        User user = (User) ParseUser.getCurrentUser();
        earnedRewards = new ArrayList<>();
        if (user.hasUpdatedGoals()) {
            user.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        earnedRewards.addAll(Reward.checkEarnedRewards(user));
                        if (earnedRewards.size() != 0) {
                            showEarnedBadgeDialogFragment();
                        }
                    } else {
                        e.printStackTrace();
                    }
                }
            });
        }
        if (user.getNeedsParent()) {
            Intent intent = new Intent(this, NeedsParentActivity.class);
            this.startActivity(intent);
            finish();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("MyNotifications", "MyNotifications", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        // user subscribe to topics
        subscribeTopic("general");
        for (User parent: user.getParents()) {
            subscribeTopic(parent.getObjectId() + user.getObjectId());
        }
    }

    public static void subscribeTopic(String id) {
        FirebaseMessaging.getInstance().subscribeToTopic(id)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = id;
                        if (!task.isSuccessful()) {
                            msg = "Failed";
                        }
                        Log.d("TopicSubscription", msg);
                    }
                });
    }

    private void setNavigationClick() {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.miRewards:
                    fragment = new RewardsFragment();
                    break;
                case R.id.miGoals:
                    fragment = new GoalsListFragment();
                    break;
                case R.id.miProfile:
                default:
                    fragment = new ProfileFragment();
                    break;
            }
            fragmentManager.beginTransaction().replace(R.id.flMainContainer, fragment).commit();
            return true;
        });
        bottomNavigationView.setSelectedItemId(R.id.miGoals);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == BANK_REQUEST_CODE) {
            Fragment bankFragment = new BanksListFragment();
            fragmentManager.beginTransaction().replace(R.id.flMainContainer, bankFragment)
                    .commitAllowingStateLoss();
        }
    }

    private void showConnectParentDialog() {
        NeedsParentDialogFragment needsParentDialogFragment =
                NeedsParentDialogFragment.newInstance("Needs Parent");
        needsParentDialogFragment.show(MainActivity.fragmentManager, "fragment_needs_parent");

    }

    private void showEarnedBadgeDialogFragment() {
        EarnedBadgeDialogFragment earnedBadge
                = EarnedBadgeDialogFragment.newInstance("Earned Badge", earnedRewards);
        earnedBadge.show(fragmentManager, "fragment_earned_badge");
    }

}
