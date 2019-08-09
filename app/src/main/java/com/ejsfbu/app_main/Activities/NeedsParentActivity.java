package com.ejsfbu.app_main.Activities;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.ejsfbu.app_main.DialogFragments.NeedsParentDialogFragment;
import com.ejsfbu.app_main.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NeedsParentActivity extends AppCompatActivity {

    @BindView(R.id.bNeedsParentWhatNow)
    Button bNeedsParentWhatNow;

    public static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_needs_parent);

        ButterKnife.bind(this);

        fragmentManager = getSupportFragmentManager();

        showConnectParentDialog();
    }

    @OnClick(R.id.bNeedsParentWhatNow)
    public void onClickWhatNow() {
        showConnectParentDialog();
    }

    private void showConnectParentDialog() {
        NeedsParentDialogFragment needsParentDialogFragment =
                NeedsParentDialogFragment.newInstance("Needs Parent");
        needsParentDialogFragment.show(NeedsParentActivity.fragmentManager,
                "fragment_needs_parent");

    }
}
