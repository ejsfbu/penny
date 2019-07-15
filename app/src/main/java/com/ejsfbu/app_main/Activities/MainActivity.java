package com.ejsfbu.app_main.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.ejsfbu.app_main.Fragments.BadgesFragment;
import com.ejsfbu.app_main.Fragments.GoalsListFragment;
import com.ejsfbu.app_main.Fragments.ProfileFragment;
import com.ejsfbu.app_main.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

// holder for tabs (goals list, badges, profile fragments)

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.bottom_navigation) BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        fragmentManager = getSupportFragmentManager();
        setNavigationClick();
    }

    private void setNavigationClick() {
            bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.miRewards:
                        fragment = new BadgesFragment();
                        break;
                    case R.id.miGoals:
                        fragment = new GoalsListFragment();
                        break;
                    case R.id.miProfile:
                    default:
                        fragment = new ProfileFragment();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            });
            // Set default selection
            bottomNavigationView.setSelectedItemId(R.id.miProfile);
    }
}
