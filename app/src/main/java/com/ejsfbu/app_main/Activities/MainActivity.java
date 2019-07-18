package com.ejsfbu.app_main.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ejsfbu.app_main.BitmapScaler;
import com.ejsfbu.app_main.Fragments.BadgesFragment;
import com.ejsfbu.app_main.Fragments.GoalsListFragment;
import com.ejsfbu.app_main.Fragments.ProfileFragment;
import com.ejsfbu.app_main.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ejsfbu.app_main.Activities.AddGoalActivity.rotateBitmapOrientation;

// holder for tabs (goals list, badges, profile fragments)

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    // Request codes
    private final static int PICK_PHOTO_CODE = 1046;
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    // needed values
    private File photoFile;
    public String photoFileName = "photo.jpg";

    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;
    public static FragmentManager fragmentManager;

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
        bottomNavigationView.setSelectedItemId(R.id.miGoals);
    }

}
