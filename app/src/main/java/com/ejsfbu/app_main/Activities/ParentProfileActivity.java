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
import com.ejsfbu.app_main.DialogFragments.EditEmailDialogFragment;
import com.ejsfbu.app_main.DialogFragments.EditNameDialogFragment;
import com.ejsfbu.app_main.DialogFragments.EditPasswordDialogFragment;
import com.ejsfbu.app_main.DialogFragments.EditProfileImageDialogFragment;
import com.ejsfbu.app_main.DialogFragments.EditUsernameDialogFragment;
import com.ejsfbu.app_main.R;
import com.ejsfbu.app_main.Models.User;
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

    @BindView(R.id.tvParentProfileName)
    TextView tvParentProfileName;
    @BindView(R.id.tvParentProfileEmail)
    TextView tvParentProfileEmail;
    @BindView(R.id.tvParentProfileUsername)
    TextView tvParentProfileUsername;
    @BindView(R.id.tvParentProfilePassword)
    TextView tvParentProfilePassword;
    @BindView(R.id.tvParentProfileAccountCode)
    TextView tvParentProfileAccountCode;
    @BindView(R.id.tvParentProfileEdit)
    TextView tvParentProfileEdit;

    @BindView(R.id.ibParentProfileEditName)
    ImageButton ibParentProfileEditName;
    @BindView(R.id.ibParentProfileEditEmail)
    ImageButton ibParentProfileEditEmail;
    @BindView(R.id.ibParentProfileEditUsername)
    ImageButton ibParentProfileEditUsername;
    @BindView(R.id.ibParentProfileEditPassword)
    ImageButton ibParentProfileEditPassword;

    @BindView(R.id.ivParentProfileProfilePic)
    ImageView ivParentProfileProfilePic;

    @BindView(R.id.bParentProfileLogout)
    Button bParentProfileLogout;

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
        tvParentProfileName.setText(user.getName());
        tvParentProfileEmail.setText(user.getEmail());
        tvParentProfileUsername.setText(user.getUsername());
        tvParentProfileAccountCode.setText(user.getObjectId());

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
                    .into(ivParentProfileProfilePic);
        }
    }

    @OnClick(R.id.ibParentProfileEditName)
    public void onClickEditParentName() {
        showEditNameDialog();
    }

    private void showEditNameDialog() {
        EditNameDialogFragment editNameDialogFragment
                = EditNameDialogFragment.newInstance("Edit Name");
        editNameDialogFragment.show(ParentProfileActivity.fragmentManager,
                "fragment_edit_name");
    }

    @OnClick(R.id.ibParentProfileEditUsername)
    public void onClickEditParentUsername() {
        showEditUsernameDialog();
    }

    private void showEditUsernameDialog() {
        EditUsernameDialogFragment editUsernameDialogFragment
                = EditUsernameDialogFragment.newInstance("Edit Username");
        editUsernameDialogFragment.show(ParentProfileActivity.fragmentManager,
                "fragment_edit_username");
    }

    @OnClick(R.id.ibParentProfileEditEmail)
    public void onClickEditParentEmail() {
        showEditEmailDialog();
    }

    private void showEditEmailDialog() {
        EditEmailDialogFragment editEmailDialogFragment
                = EditEmailDialogFragment.newInstance("Edit Email");
        editEmailDialogFragment.show(ParentProfileActivity.fragmentManager,
                "fragment_edit_email");
    }

    @OnClick(R.id.ibParentProfileEditPassword)
    public void onClickEditParentPassword() {
        showEditPasswordDialog();
    }

    private void showEditPasswordDialog() {
        EditPasswordDialogFragment editPasswordDialogFragment
                = EditPasswordDialogFragment.newInstance("Edit Password");
        editPasswordDialogFragment.show(ParentProfileActivity.fragmentManager,
                "fragment_edit_password");
    }

    @OnClick(R.id.ivParentProfileProfilePic)
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
                "fragment_edit_profile_pic");
    }

    @OnClick(R.id.bParentProfileLogout)
    public void onClickParentLogout() {
        ParseUser.logOut();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.ibParentProfileBack)
    public void onClickBack() {
        Intent intent = new Intent(this, ParentActivity.class);
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
