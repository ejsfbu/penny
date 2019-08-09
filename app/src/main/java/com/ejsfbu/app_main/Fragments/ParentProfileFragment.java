package com.ejsfbu.app_main.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.ejsfbu.app_main.Activities.LoginActivity;
import com.ejsfbu.app_main.Activities.ParentActivity;
import com.ejsfbu.app_main.DialogFragments.EditEmailDialogFragment;
import com.ejsfbu.app_main.DialogFragments.EditNameDialogFragment;
import com.ejsfbu.app_main.DialogFragments.EditPasswordDialogFragment;
import com.ejsfbu.app_main.DialogFragments.EditProfileImageDialogFragment;
import com.ejsfbu.app_main.DialogFragments.EditUsernameDialogFragment;
import com.ejsfbu.app_main.Models.User;
import com.ejsfbu.app_main.R;
import com.parse.ParseFile;
import com.parse.ParseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ParentProfileFragment extends Fragment
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
    @BindView(R.id.bParentProfileBankInfo)
    Button bParentProfileBankInfo;

    private User user;
    private Unbinder unbinder;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        context = container.getContext();
        user = (User) ParseUser.getCurrentUser();
        return inflater.inflate(R.layout.fragment_parent_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);

        ParentActivity.ibParentProfileBack.setVisibility(View.VISIBLE);
        ParentActivity.ibChildDetailBack.setVisibility(View.GONE);
        ParentActivity.ibParentBanksListBack.setVisibility(View.GONE);
        ParentActivity.ibParentBankDetailsBack.setVisibility(View.GONE);
        ParentActivity.ivParentProfilePic.setVisibility(View.GONE);
        ParentActivity.cvParentProfilePic.setVisibility(View.GONE);

        loadProfileData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
            Glide.with(this)
                    .load(imageUrl)
                    .apply(options.placeholder(R.drawable.icon_user)
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .error(R.drawable.icon_user)
                            .transform(new CenterCrop())
                            .transform(new CircleCrop()))
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
        editNameDialogFragment.show(ParentActivity.fragmentManager,
                "fragment_edit_name");
    }

    @OnClick(R.id.ibParentProfileEditUsername)
    public void onClickEditParentUsername() {
        showEditUsernameDialog();
    }

    private void showEditUsernameDialog() {
        EditUsernameDialogFragment editUsernameDialogFragment
                = EditUsernameDialogFragment.newInstance("Edit Username");
        editUsernameDialogFragment.show(ParentActivity.fragmentManager,
                "fragment_edit_username");
    }

    @OnClick(R.id.ibParentProfileEditEmail)
    public void onClickEditParentEmail() {
        showEditEmailDialog();
    }

    private void showEditEmailDialog() {
        EditEmailDialogFragment editEmailDialogFragment
                = EditEmailDialogFragment.newInstance("Edit Email");
        editEmailDialogFragment.show(ParentActivity.fragmentManager,
                "fragment_edit_email");
    }

    @OnClick(R.id.ibParentProfileEditPassword)
    public void onClickEditParentPassword() {
        showEditPasswordDialog();
    }

    private void showEditPasswordDialog() {
        EditPasswordDialogFragment editPasswordDialogFragment
                = EditPasswordDialogFragment.newInstance("Edit Password");
        editPasswordDialogFragment.show(ParentActivity.fragmentManager,
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
        editProfileImageDialogFragment.show(ParentActivity.fragmentManager,
                "fragment_edit_profile_pic");
    }

    @OnClick(R.id.bParentProfileLogout)
    public void onClickParentLogout() {
        ParseUser.logOut();

        Intent intent = new Intent(context, LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @OnClick(R.id.bParentProfileBankInfo)
    public void onClickParentProfileBankInfo() {
        Fragment bankFragment = new BanksListFragment();
        ParentActivity.fragmentManager.beginTransaction()
                .replace(R.id.flParentContainer, bankFragment)
                .commit();
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
