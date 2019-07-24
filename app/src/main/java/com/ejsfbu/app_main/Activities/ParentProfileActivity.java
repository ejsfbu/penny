package com.ejsfbu.app_main.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.ejsfbu.app_main.EditFragments.EditEmailDialogFragment;
import com.ejsfbu.app_main.EditFragments.EditNameDialogFragment;
import com.ejsfbu.app_main.EditFragments.EditPasswordDialogFragment;
import com.ejsfbu.app_main.EditFragments.EditProfileImageDialogFragment;
import com.ejsfbu.app_main.EditFragments.EditUsernameDialogFragment;
import com.ejsfbu.app_main.R;
import com.ejsfbu.app_main.models.User;
import com.parse.ParseFile;
import com.parse.ParseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ParentProfileActivity extends AppCompatActivity
        implements EditNameDialogFragment.EditNameDialogListener,
        EditUsernameDialogFragment.EditUsernameDialogListener,
        EditEmailDialogFragment.EditEmailDialogListener,
        EditProfileImageDialogFragment.EditProfileImageDialogListener {

    public static final String TAG = "ParentProfileFragment";

    @BindView(R.id.tvParentName)
    TextView tvParentName;
    @BindView(R.id.tvParentEmail)
    TextView tvParentEmail;
    @BindView(R.id.tvParentUsername)
    TextView tvParentUsername;
    @BindView(R.id.tvParentPassword)
    TextView getTvParentPassword;
    @BindView(R.id.tvParentAccountCode)
    TextView tvParentCode;
    @BindView(R.id.tvParentProfileEdit)
    TextView tvParentProfileEdit;

    @BindView(R.id.ibEditParentName)
    ImageButton ibEditParentName;
    @BindView(R.id.ibEditParentEmail)
    ImageButton ibEditParentEmail;
    @BindView(R.id.ibEditParentUsername)
    ImageButton ibEditParentUsername;
    @BindView(R.id.ibEditParentPassword)
    ImageButton ibEditParentPassword;

    @BindView(R.id.ivParentProfilePic)
    ImageView ivParentProfilePic;

    @BindView(R.id.bParentLogout)
    Button bParentLogout;

    private User user;
    private static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_profile);

        ButterKnife.bind(this);

        user = (User) ParseUser.getCurrentUser();
        fragmentManager = getSupportFragmentManager();

        loadProfileData();
    }

    public void loadProfileData() {
        tvParentName.setText(user.getName());
        tvParentEmail.setText(user.getEmail());
        tvParentUsername.setText(user.getUsername());
        tvParentCode.setText(user.getObjectId());

        ParseFile image = user.getProfilePic();
        if (image != null) {
            String imageUrl = image.getUrl();
            imageUrl = imageUrl.substring(4);
            imageUrl = "https" + imageUrl;
            RequestOptions options = new RequestOptions();
            options.placeholder(R.drawable.icon_user)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .error(R.drawable.icon_user)
                    .transform(new CenterCrop())
                    .transform(new CircleCrop());
            Glide.with(this)
                    .load(imageUrl)
                    .apply(options) // Extra: round image corners
                    .into(ivParentProfilePic);
        }
    }

    @OnClick(R.id.ibEditParentName)
    public void onClickEditParentName() {
        showEditNameDialog();
    }

    private void showEditNameDialog() {
        EditNameDialogFragment editNameDialogFragment
                = EditNameDialogFragment.newInstance("Edit Name");
        editNameDialogFragment.show(ParentProfileActivity.fragmentManager,
                "fragment_edit_name");
    }

    @OnClick(R.id.ibEditParentUsername)
    public void onClickEditParentUsername() {
        showEditUsernameDialog();
    }

    private void showEditUsernameDialog() {
        EditUsernameDialogFragment editUsernameDialogFragment
                = EditUsernameDialogFragment.newInstance("Edit Username");
        editUsernameDialogFragment.show(ParentProfileActivity.fragmentManager,
                "fragment_edit_username");
    }

    @OnClick(R.id.ibEditParentEmail)
    public void onClickEditParentEmail() {
        showEditEmailDialog();
    }

    private void showEditEmailDialog() {
        EditEmailDialogFragment editEmailDialogFragment
                = EditEmailDialogFragment.newInstance("Edit Email");
        editEmailDialogFragment.show(ParentProfileActivity.fragmentManager,
                "fragment_edit_email");
    }

    @OnClick(R.id.ibEditParentPassword)
    public void onClickEditParentPassword() {
        showEditPasswordDialog();
    }

    private void showEditPasswordDialog() {
        EditPasswordDialogFragment editPasswordDialogFragment
                = EditPasswordDialogFragment.newInstance("Edit Password");
        editPasswordDialogFragment.show(ParentProfileActivity.fragmentManager,
                "fragment_edit_password");
    }

    @OnClick(R.id.ivParentProfilePic)
    public void onClickParentProfilePic() {
        showEditImageDialog();
    }

    @OnClick(R.id.tvParentProfileEdit)
    public void onClickEdit() {
        showEditImageDialog();
    }

    private void showEditImageDialog() {
        EditProfileImageDialogFragment editProfileImageDialogFragment
                = EditProfileImageDialogFragment.newInstance("Edit Password");
        editProfileImageDialogFragment.show(ParentProfileActivity.fragmentManager,
                "fragment_edit_profileimage");
    }

    @OnClick(R.id.bParentLogout)
    public void onClickParentLogout() {
        ParseUser.logOut();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onFinishEditDialog() {
        loadProfileData();
    }


}
