package com.ejsfbu.app_main.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.ejsfbu.app_main.Fragments.BanksListFragment;
import com.ejsfbu.app_main.Fragments.GoalsListFragment;
import com.ejsfbu.app_main.Fragments.ProfileFragment;
import com.ejsfbu.app_main.Fragments.RewardsFragment;
import com.ejsfbu.app_main.DialogFragments.NeedsParentDialogFragment;
import com.ejsfbu.app_main.R;
import com.ejsfbu.app_main.Models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    public final static int BANK_REQUEST_CODE = 20;

    public static BottomNavigationView bottomNavigationView;
    public static ImageButton ibGoalDetailsBack;
    public static ImageButton ibRewardGoalDetailsBack;
    public static ImageButton ibBanksListBack;
    public static ImageButton ibBankDetailsBack;

    public static FragmentManager fragmentManager;

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
        if (user.getNeedsParent()) {
            showConnectParentDialog();
        }
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
        // REQUEST_CODE is defined above
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

}
