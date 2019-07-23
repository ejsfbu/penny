package com.ejsfbu.app_main.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ejsfbu.app_main.R;
import com.ejsfbu.app_main.models.User;
import com.parse.ParseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ParentProfileActivity extends AppCompatActivity {

    public static final String TAG = "ParentProfileFragment";

    @BindView(R.id.tvParentName)
    TextView tvParentName;
    @BindView(R.id.tvParentEmail)
    TextView tvParentEmail;
    @BindView(R.id.tvParentUsername)
    TextView tvParentUsername;
    @BindView(R.id.tvParentPassword)
    TextView getTvParentPassword;
    @BindView(R.id.tvParentCode)
    TextView tvParentCode;

    @BindView(R.id.ibEditParentName)
    ImageButton ibEditParentName;
    @BindView(R.id.ibEditParentEmail)
    ImageButton ibEditParentEmail;
    @BindView(R.id.ibEditParentUsername)
    ImageButton ibEditParentUsername;
    @BindView(R.id.ibEditParentPassword)
    ImageButton ibEditParentPassword;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_profile);

        ButterKnife.bind(this);

        user = (User) ParseUser.getCurrentUser();

        tvParentName.setText(user.getName());
        tvParentEmail.setText(user.getEmail());
        tvParentUsername.setText(user.getUsername());

        String accountCode = getResources().getString(R.string.account_code)
                + " " + user.getObjectId();
        tvParentCode.setText(accountCode);
    }
}
