package com.ejsfbu.app_main.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.ejsfbu.app_main.DialogFragments.VerifyChildDialogFragment;
import com.ejsfbu.app_main.Fragments.BanksListFragment;
import com.ejsfbu.app_main.Fragments.ChildListFragment;
import com.ejsfbu.app_main.Fragments.ParentProfileFragment;
import com.ejsfbu.app_main.Models.User;
import com.ejsfbu.app_main.R;
import com.parse.ParseFile;
import com.parse.ParseUser;

public class ParentActivity extends AppCompatActivity {

    public static final String TAG = "ParentActivity";
    public final static int BANK_REQUEST_CODE = 20;

    public static ImageView ivParentProfilePic;
    public static CardView cvParentProfilePic;
    public static FrameLayout flParentContainer;
    public static ImageButton ibParentProfileBack;
    public static ImageButton ibChildDetailBack;
    public static ImageButton ibParentBanksListBack;
    public static ImageButton ibParentBankDetailsBack;

    public static FragmentManager fragmentManager;

    private User parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);

        ivParentProfilePic = findViewById(R.id.ivParentProfilePic);
        cvParentProfilePic = findViewById(R.id.cvParentProfilePic);
        flParentContainer = findViewById(R.id.flParentContainer);
        ibParentProfileBack = findViewById(R.id.ibParentProfileBack);
        ibChildDetailBack = findViewById(R.id.ibChildDetailBack);
        ibParentBanksListBack = findViewById(R.id.ibParentBanksListBack);
        ibParentBankDetailsBack = findViewById(R.id.ibParentBankDetailsBack);

        fragmentManager = getSupportFragmentManager();

        parent = (User) ParseUser.getCurrentUser();

        ivParentProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment parentProfileFragment = new ParentProfileFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.flParentContainer, parentProfileFragment)
                        .commit();
            }
        });
        ibParentProfileBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ibParentProfileBack.setVisibility(View.GONE);
                ivParentProfilePic.setVisibility(View.VISIBLE);
                cvParentProfilePic.setVisibility(View.VISIBLE);
                Fragment childListFragment = new ChildListFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.flParentContainer, childListFragment)
                        .commit();
            }
        });
        ibChildDetailBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ibChildDetailBack.setVisibility(View.GONE);
                Fragment childListFragment = new ChildListFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.flParentContainer, childListFragment)
                        .commit();
            }
        });
        ibParentBanksListBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ibParentBanksListBack.setVisibility(View.GONE);
                Fragment parentProfileFragment = new ParentProfileFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.flParentContainer, parentProfileFragment)
                        .commit();
            }
        });
        ibParentBankDetailsBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ibParentBankDetailsBack.setVisibility(View.GONE);
                Fragment bankListFragment = new BanksListFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.flParentContainer, bankListFragment)
                        .commit();
            }
        });

        if (getIntent().getBooleanExtra("isFirstLogin", false)) {
            showVerifyChildDialog();
        }

        ParseFile image = parent.getProfilePic();
        if (image != null) {
            String imageUrl = image.getUrl();
            imageUrl = imageUrl.substring(4);
            imageUrl = "https" + imageUrl;
            RequestOptions options = new RequestOptions();
            Glide.with(this)
                    .load(imageUrl)
                    .apply(options.placeholder(R.drawable.icon_user)
                            .error(R.drawable.icon_user)
                            .transform(new CenterCrop())
                            .transform(new CircleCrop()))
                    .into(ivParentProfilePic);
        }

        Fragment childListFragment = new ChildListFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.flParentContainer, childListFragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (ibParentProfileBack.getVisibility() == View.VISIBLE) {
            ibParentProfileBack.setVisibility(View.GONE);
            ivParentProfilePic.setVisibility(View.VISIBLE);
            cvParentProfilePic.setVisibility(View.VISIBLE);
            Fragment childListFragment = new ChildListFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.flParentContainer, childListFragment)
                    .commit();
        }
        if (ibChildDetailBack.getVisibility() == View.VISIBLE) {
            ibChildDetailBack.setVisibility(View.GONE);
            Fragment childListFragment = new ChildListFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.flParentContainer, childListFragment)
                    .commit();
        }
        if (ibParentBanksListBack.getVisibility() == View.VISIBLE) {
            ibParentBanksListBack.setVisibility(View.GONE);
            Fragment parentProfileFragment = new ParentProfileFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.flParentContainer, parentProfileFragment)
                    .commit();
        }
        if (ibParentBankDetailsBack.getVisibility() == View.VISIBLE) {
            ibParentBankDetailsBack.setVisibility(View.GONE);
            Fragment bankListFragment = new BanksListFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.flParentContainer, bankListFragment)
                    .commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == BANK_REQUEST_CODE) {
            Fragment bankFragment = new BanksListFragment();
            fragmentManager.beginTransaction().replace(R.id.flParentContainer, bankFragment)
                    .commitAllowingStateLoss();
        }
    }

    public void showVerifyChildDialog() {
        VerifyChildDialogFragment verifyChildDialogFragment
                = VerifyChildDialogFragment.newInstance("Verify Child");
        verifyChildDialogFragment.show(ParentActivity.fragmentManager,
                "fragment_verify_child");
    }
}
