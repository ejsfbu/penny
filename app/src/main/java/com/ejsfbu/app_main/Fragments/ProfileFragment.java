package com.ejsfbu.app_main.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


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
import com.ejsfbu.app_main.EditFragments.EditUserNameDialogFragment;
import com.ejsfbu.app_main.R;
import com.ejsfbu.app_main.models.User;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ProfileFragment extends Fragment {

    public static final String TAG = "ProfileFragment";

    @BindView(R.id.bLogOut) Button bLogOut;
    @BindView(R.id.ibEditName) ImageButton ibName;
    @BindView(R.id.ibEditUserName) ImageButton ibUserName;
    @BindView(R.id.ivProfileImage) ImageView ivProfileImage;

    @BindView(R.id.tv_profile_username) TextView username;
    @BindView(R.id.tv_profile_password) TextView password;
    @BindView(R.id.tv_parent_name) TextView name;
    @BindView(R.id.tv_profile_email)TextView email;
    @BindView(R.id.lvParents) ListView lvParents;


    // Butterknife for fragment
    private Unbinder unbinder;
    private ParseUser user;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment98
        context = container.getContext();
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);
        user = ParseUser.getCurrentUser();
        loadProfileData();
    }

    // When change fragment unbind view
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.bLogOut)
    public void onClickLogOut() {
        ParseUser.logOut();

        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @OnClick(R.id.ibEditName)
    public void onClickEditName() {
        showEditNameDialog();
    }

    @OnClick(R.id.ibEditUserName)
    public void onClickEditUserName() {
        showEditUserNameDialog();
    }

    @OnClick(R.id.ibEditEmail)
    public void onClickEditEmail() {
        showEditEmailDialog();
    }

    @OnClick(R.id.ibEditPassword)
    public void onClickEditPassword() {
        showEditPasswordDialog();
    }

    @OnClick(R.id.ivProfileImage)
    public void onClickEditImage() {
        showEditImageDialog();
    }

    private void showEditNameDialog() {
        ///FragmentManager fm = getSupportFragmentManager();
        EditNameDialogFragment editNameDialogFragment = EditNameDialogFragment.newInstance("Edit Name");
        editNameDialogFragment.show(MainActivity.fragmentManager, "fragment_edit_name");
    }

    private void showEditUserNameDialog() {
        ///FragmentManager fm = getSupportFragmentManager();
        EditUserNameDialogFragment editUserNameDialogFragment = EditUserNameDialogFragment.newInstance("Edit User Name");
        editUserNameDialogFragment.show(MainActivity.fragmentManager, "fragment_edit_username");
    }

    private void showEditEmailDialog() {
        ///FragmentManager fm = getSupportFragmentManager();
        EditEmailDialogFragment editEmailDialogFragment = EditEmailDialogFragment.newInstance("Edit Email");
        editEmailDialogFragment.show(MainActivity.fragmentManager, "fragment_edit_email");
    }

    private void showEditPasswordDialog() {
        ///FragmentManager fm = getSupportFragmentManager();
        EditPasswordDialogFragment editPasswordDialogFragment = EditPasswordDialogFragment.newInstance("Edit Password");
        editPasswordDialogFragment.show(MainActivity.fragmentManager, "fragment_edit_password");
    }

    private void showEditImageDialog() {
        ///FragmentManager fm = getSupportFragmentManager();
        EditProfileImageDialogFragment editProfileImageDialogFragment = EditProfileImageDialogFragment.newInstance("Edit Password");
        editProfileImageDialogFragment.show(MainActivity.fragmentManager, "fragment_edit_profileimage");
    }

    // Load user data
    private void loadProfileData() {
        ParseFile image = user.getParseFile("profileImage");
        if (image != null) {
            String imageUrl = image.getUrl();
            imageUrl = imageUrl.substring(4);
            imageUrl = "https" + imageUrl;
            RequestOptions options = new RequestOptions();
            options.placeholder(R.drawable.ic_iconfinder_icons_user_1564534)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .error(R.drawable.ic_iconfinder_icons_user_1564534)
                    .transform(new CenterCrop())
                    .transform(new CircleCrop());
            Glide.with(context)
                    .load(imageUrl)
                    .apply(options) // Extra: round image corners
                    .into(ivProfileImage);
        }
        username.setText(user.getUsername());
        email.setText(user.getEmail());
        name.setText(user.getString("name"));
        System.out.println("start" + user.getString("password") + "end");
        password.setText(user.getString("password"));


        // Construct the data source
        ArrayList<User> arrayOfParents = new ArrayList<User>();


        //TODO ADD THE PARENTS

        // Create the adapter to convert the array to views
        ParentDisplayAdapter adapter = new ParentDisplayAdapter(getContext(), arrayOfParents);
        // Attach the adapter to a ListView
        lvParents.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
