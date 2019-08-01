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
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.ejsfbu.app_main.Activities.LoginActivity;
import com.ejsfbu.app_main.Activities.MainActivity;
import com.ejsfbu.app_main.DialogFragments.EditEmailDialogFragment;
import com.ejsfbu.app_main.DialogFragments.EditNameDialogFragment;
import com.ejsfbu.app_main.DialogFragments.EditPasswordDialogFragment;
import com.ejsfbu.app_main.DialogFragments.EditProfileImageDialogFragment;
import com.ejsfbu.app_main.DialogFragments.EditUsernameDialogFragment;
import com.ejsfbu.app_main.R;
import com.ejsfbu.app_main.Models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ProfileFragment extends Fragment
        implements EditEmailDialogFragment.EditEmailDialogListener,
        EditNameDialogFragment.EditNameDialogListener,
        EditProfileImageDialogFragment.EditProfileImageDialogListener,
        EditUsernameDialogFragment.EditUsernameDialogListener {

    public static final String TAG = "ProfileFragment";

    @BindView(R.id.bProfileLogout)
    Button bProfileLogout;

    @BindView(R.id.ibProfileEditName)
    ImageButton ibProfileEditName;
    @BindView(R.id.ibProfileEditUsername)
    ImageButton ibProfileEditUsername;

    @BindView(R.id.cvProfileParentProfilePic1)
    CardView cvProfileParentProfilePic1;
    @BindView(R.id.cvProfileParentProfilePic2)
    CardView cvProfileParentProfilePic2;
    @BindView(R.id.cvProfileParentProfilePic3)
    CardView cvProfileParentProfilePic3;
    @BindView(R.id.cvProfileParentProfilePic4)
    CardView cvProfileParentProfilePic4;

    @BindView(R.id.ivProfileChildProfilePic)
    ImageView ivProfileChildProfilePic;
    @BindView(R.id.ivProfileParentProfilePic1)
    ImageView ivProfileParentProfilePic1;
    @BindView(R.id.ivProfileParentProfilePic2)
    ImageView ivProfileParentProfilePic2;
    @BindView(R.id.ivProfileParentProfilePic3)
    ImageView ivProfileParentProfilePic3;
    @BindView(R.id.ivProfileParentProfilePic4)
    ImageView ivProfileParentProfilePic4;

    @BindView(R.id.tvProfileUsername)
    TextView tvProfileUsername;
    @BindView(R.id.tvProfileName)
    TextView tvProfileName;
    @BindView(R.id.tvProfileEmail)
    TextView tvProfileEmail;
    @BindView(R.id.tvProfileAccountCode)
    TextView tvProfileAccountCode;
    @BindView(R.id.tvProfileParentName1)
    TextView tvProfileParentName1;
    @BindView(R.id.tvProfileParentName2)
    TextView tvProfileParentName2;
    @BindView(R.id.tvProfileParentName3)
    TextView tvProfileParentName3;
    @BindView(R.id.tvProfileParentName4)
    TextView tvProfileParentName4;

    private Unbinder unbinder;
    private User user;
    private Context context;
    private ArrayList<User> parents;
    private int numParents;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = container.getContext();
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);
        MainActivity.ibGoalDetailsBack.setVisibility(View.GONE);
        MainActivity.ibBankDetailsBack.setVisibility(View.GONE);
        MainActivity.ibBanksListBack.setVisibility(View.GONE);
        MainActivity.ibRewardGoalDetailsBack.setVisibility(View.GONE);
        user = (User) ParseUser.getCurrentUser();
        parents = new ArrayList<>();
        loadProfileData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.bProfileLogout)
    public void onClickLogOut() {
        ParseUser.logOut();

        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @OnClick(R.id.ibProfileEditName)
    public void onClickEditName() {
        showEditNameDialog();
    }

    @OnClick(R.id.ibProfileEditUsername)
    public void onClickEditUserName() {
        showEditUsernameDialog();
    }

    @OnClick(R.id.ibProfileEditEmail)
    public void onClickEditEmail() {
        showEditEmailDialog();
    }

    @OnClick(R.id.ibProfileEditPassword)
    public void onClickEditPassword() {
        showEditPasswordDialog();
    }

    @OnClick(R.id.ivProfileChildProfilePic)
    public void onClickEditImage() {
        showEditImageDialog();
    }

    @OnClick(R.id.tvProfileEdit)
    public void onClickChildProfileEdit() {
        showEditImageDialog();
    }

    @OnClick(R.id.bProfileBankInfo)
    public void onClickBanks() {
        Fragment bankFragment = new BanksListFragment();
        MainActivity.fragmentManager.beginTransaction()
                .replace(R.id.flMainContainer, bankFragment).commit();
    }

    private void showEditNameDialog() {
        EditNameDialogFragment editNameDialogFragment
                = EditNameDialogFragment.newInstance("Edit Name");
        editNameDialogFragment.show(MainActivity.fragmentManager, "fragment_edit_name");
    }

    private void showEditUsernameDialog() {
        EditUsernameDialogFragment editUsernameDialogFragment
                = EditUsernameDialogFragment.newInstance("Edit Username");
        editUsernameDialogFragment.show(MainActivity.fragmentManager,
                "fragment_edit_username");
    }

    private void showEditEmailDialog() {
        EditEmailDialogFragment editEmailDialogFragment
                = EditEmailDialogFragment.newInstance("Edit Email");
        editEmailDialogFragment.show(MainActivity.fragmentManager, "fragment_edit_email");
    }

    private void showEditPasswordDialog() {
        EditPasswordDialogFragment editPasswordDialogFragment
                = EditPasswordDialogFragment.newInstance("Edit Password");
        editPasswordDialogFragment.show(MainActivity.fragmentManager,
                "fragment_edit_password");
    }

    private void showEditImageDialog() {
        EditProfileImageDialogFragment editProfileImageDialogFragment
                = EditProfileImageDialogFragment.newInstance("Edit Password");
        editProfileImageDialogFragment.show(MainActivity.fragmentManager,
                "fragment_edit_profile_pic");
    }

    private void loadProfileData() {
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
            Glide.with(context)
                    .load(imageUrl)
                    .apply(options) // Extra: round image corners
                    .into(ivProfileChildProfilePic);
        }
        tvProfileUsername.setText(user.getUsername());
        tvProfileEmail.setText(user.getEmail());
        tvProfileName.setText(user.getName());
        tvProfileAccountCode.setText(user.getObjectId());

        JSONArray jsonParents = user.getParents();
        if (jsonParents == null) {
            numParents  = 0;
        } else {
            numParents = jsonParents.length();
        }
        for (int i = 0; i < numParents; i++) {
            try {
                JSONObject jsonParent = (JSONObject) jsonParents.get(i);
                String parentUserId = jsonParent.getString("objectId");
                User.Query userQuery = new User.Query();
                userQuery.whereEqualTo("objectId", parentUserId);
                userQuery.findInBackground(new FindCallback<User>() {
                    @Override
                    public void done(List<User> objects, ParseException e) {
                        if (e == null) {
                            User parent = objects.get(0);
                            parents.add(parent);
                        }
                        if (parents.size() == numParents) {
                            loopParents();
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void loopParents() {
        if (parents.size() > 0) {
            cvProfileParentProfilePic1.setVisibility(View.VISIBLE);
            tvProfileParentName1.setVisibility(View.VISIBLE);
            User parent1 = (User) parents.get(0);
            setParent(parent1, ivProfileParentProfilePic1, tvProfileParentName1);
        }
        if (parents.size() > 1) {
            cvProfileParentProfilePic2.setVisibility(View.VISIBLE);
            tvProfileParentName2.setVisibility(View.VISIBLE);
            User parent2 = (User) parents.get(1);
            setParent(parent2, ivProfileParentProfilePic2, tvProfileParentName2);
        }
        if (parents.size() > 2) {
            cvProfileParentProfilePic3.setVisibility(View.VISIBLE);
            tvProfileParentName3.setVisibility(View.VISIBLE);
            User parent3 = (User) parents.get(2);
            setParent(parent3, ivProfileParentProfilePic3, tvProfileParentName3);
        }
        if (parents.size() > 3) {
            cvProfileParentProfilePic4.setVisibility(View.VISIBLE);
            tvProfileParentName4.setVisibility(View.VISIBLE);
            User parent4 = (User) parents.get(3);
            setParent(parent4, ivProfileParentProfilePic4, tvProfileParentName4);
        }
    }

    public void setParent(User parent, ImageView ivParentProfilePic, TextView tvParentName) {
        ParseFile image = parent.getProfilePic();
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
            Glide.with(context)
                    .load(imageUrl)
                    .apply(options) // Extra: round image corners
                    .into(ivParentProfilePic);
        }
        tvParentName.setText(parent.getName());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    // This is called when the dialog is completed and the results have been passed
    @Override
    public void onFinishEditDialog() {
        loadProfileData();
    }

}
