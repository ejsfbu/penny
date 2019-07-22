package com.ejsfbu.app_main.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.ejsfbu.app_main.Adapters.ChildAdapter;
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

public class ParentActivity extends AppCompatActivity {

    public static final String TAG = "ParentActivity";

    @BindView(R.id.rvChildren)
    RecyclerView rvChildren;
    @BindView(R.id.ivProfilePic)
    ImageView ivProfilePic;

    private ArrayList<User> children;
    private ChildAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);

        ButterKnife.bind(this);

        User parent = (User) ParseUser.getCurrentUser();

        children = new ArrayList<>();
        adapter = new ChildAdapter(this, children);
        rvChildren.setAdapter(adapter);
        rvChildren.setLayoutManager(new LinearLayoutManager(this));

        ParseFile image = parent.getProfilePic();
        if (image != null) {
            String imageUrl = image.getUrl();
            imageUrl = imageUrl.substring(4);
            imageUrl = "https" + imageUrl;
            RequestOptions options = new RequestOptions();
            options.placeholder(R.drawable.ic_iconfinder_icons_user_1564534)
                    .error(R.drawable.ic_iconfinder_icons_user_1564534)
                    .transform(new CenterCrop())
                    .transform(new CircleCrop());
            Glide.with(this)
                    .load(imageUrl)
                    .apply(options) // Extra: round image corners
                    .into(ivProfilePic);
        }

        loadChildren();
    }

    protected void loadChildren() {
        User parent = (User) ParseUser.getCurrentUser();
        JSONArray jsonChildren = parent.getChildren();
        for (int i = 0; i < jsonChildren.length(); i++) {
            try {
                JSONObject jsonChild = (JSONObject) jsonChildren.get(i);
                String childUserId = jsonChild.getString("objectId");
                User.Query userQuery = new User.Query();
                userQuery.whereEqualTo("objectId", childUserId);
                userQuery.findInBackground(new FindCallback<User>() {
                    @Override
                    public void done(List<User> objects, ParseException e) {
                        if (e == null) {
                            User child = objects.get(0);
                            children.add(child);
                            adapter.notifyItemInserted(children.size() - 1);
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
