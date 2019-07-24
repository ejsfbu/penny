package com.ejsfbu.app_main.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.ejsfbu.app_main.Adapters.ParentDisplayAdapter;
import com.ejsfbu.app_main.EditFragments.EditEmailDialogFragment;
import com.ejsfbu.app_main.EditFragments.EditNameDialogFragment;
import com.ejsfbu.app_main.EditFragments.EditPasswordDialogFragment;
import com.ejsfbu.app_main.EditFragments.EditProfileImageDialogFragment;
import com.ejsfbu.app_main.EditFragments.EditUsernameDialogFragment;
import com.ejsfbu.app_main.R;
import com.ejsfbu.app_main.models.User;
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

    @BindView(R.id.bChildLogout)
    Button bChildLogout;

    @BindView(R.id.ibEditChildName)
    ImageButton ibEditChildName;
    @BindView(R.id.ibEditChildUsername)
    ImageButton ibEditChildUsername;

    @BindView(R.id.cvParentProfilePic1)
    CardView cvParentProfilePic1;
    @BindView(R.id.cvParentProfilePic2)
    CardView cvParentProfilePic2;
    @BindView(R.id.cvParentProfilePic3)
    CardView cvParentProfilePic3;
    @BindView(R.id.cvParentProfilePic4)
    CardView cvParentProfilePic4;

    @BindView(R.id.ivChildProfilePic)
    ImageView ivChildProfilePic;
    @BindView(R.id.ivParentProfilePic1)
    ImageView ivParentProfilePic1;
    @BindView(R.id.ivParentProfilePic2)
    ImageView ivParentProfilePic2;
    @BindView(R.id.ivParentProfilePic3)
    ImageView ivParentProfilePic3;
    @BindView(R.id.ivParentProfilePic4)
    ImageView ivParentProfilePic4;

    @BindView(R.id.tvChildUsername)
    TextView tvChildUsername;
    @BindView(R.id.tvChildName)
    TextView tvChildName;
    @BindView(R.id.tvChildEmail)
    TextView tvChildEmail;
    @BindView(R.id.tvChildAccountCode)
    TextView tvChildAccountCode;
    @BindView(R.id.tvParentName1)
    TextView tvParentName1;
    @BindView(R.id.tvParentName2)
    TextView tvParentName2;
    @BindView(R.id.tvParentName3)
    TextView tvParentName3;
    @BindView(R.id.tvParentName4)
    TextView tvParentName4;

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
        user = (User) ParseUser.getCurrentUser();
        parents = new ArrayList<>();
        loadProfileData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.bChildLogout)
    public void onClickLogOut() {
        ParseUser.logOut();

        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @OnClick(R.id.ibEditChildName)
    public void onClickEditName() {
        showEditNameDialog();
    }

    @OnClick(R.id.ibEditChildUsername)
    public void onClickEditUserName() {
        showEditUsernameDialog();
    }

    @OnClick(R.id.ibEditChildEmail)
    public void onClickEditEmail() {
        showEditEmailDialog();
    }

    @OnClick(R.id.ibEditChildPassword)
    public void onClickEditPassword() {
        showEditPasswordDialog();
    }

    @OnClick(R.id.ivChildProfilePic)
    public void onClickEditImage() {
        showEditImageDialog();
    }

    @OnClick(R.id.tvChildProfileEdit)
    public void onClickChildProfileEdit() {
        showEditImageDialog();
    }

    @OnClick(R.id.bChildBankInfo)
    public void onClickBanks() {
        Fragment bankFragment = new BankAccountsFragment();
        MainActivity.fragmentManager.beginTransaction()
                .replace(R.id.flContainer, bankFragment).commit();
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
                "fragment_edit_profileimage");
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
                    .into(ivChildProfilePic);
        }
        tvChildUsername.setText(user.getUsername());
        tvChildEmail.setText(user.getEmail());
        tvChildName.setText(user.getName());
        tvChildAccountCode.setText(user.getObjectId());

        JSONArray jsonParents = user.getParents();
        numParents = jsonParents.length();
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
            cvParentProfilePic1.setVisibility(View.VISIBLE);
            tvParentName1.setVisibility(View.VISIBLE);
            User parent1 = (User) parents.get(0);
            setParent(parent1, ivParentProfilePic1, tvParentName1);
        }
        if (parents.size() > 1) {
            cvParentProfilePic2.setVisibility(View.VISIBLE);
            tvParentName2.setVisibility(View.VISIBLE);
            User parent2 = (User) parents.get(1);
            setParent(parent2, ivParentProfilePic2, tvParentName2);
        }
        if (parents.size() > 2) {
            cvParentProfilePic3.setVisibility(View.VISIBLE);
            tvParentName3.setVisibility(View.VISIBLE);
            User parent3 = (User) parents.get(2);
            setParent(parent3, ivParentProfilePic3, tvParentName3);
        }
        if (parents.size() > 3) {
            cvParentProfilePic4.setVisibility(View.VISIBLE);
            tvParentName4.setVisibility(View.VISIBLE);
            User parent4 = (User) parents.get(3);
            setParent(parent4, ivParentProfilePic4, tvParentName4);
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
