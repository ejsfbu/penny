package com.ejsfbu.app_main.DialogFragments;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ejsfbu.app_main.Models.User;
import com.ejsfbu.app_main.R;
import com.parse.ParseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class EditAllowanceDialogFragment extends DialogFragment {

//    @BindView(R.id.tvEditAllowanceManagerTitle)
//    TextView tvEditAllowanceManagerTitle;
//    @BindView(R.id.tvEditAllowanceManagerDescription)
//    TextView tvEditAllowanceManagerDescription;
//    @

    Context context;
    User parent;
    Unbinder unbinder;
    static User currentChild;

    public EditAllowanceDialogFragment() {

    }

    public static EditAllowanceDialogFragment newInstance(String title, User user) {
        EditAllowanceDialogFragment frag = new EditAllowanceDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        currentChild = user;
        return frag;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);
        setButtons();
        setSpinners();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getContext();
        parent = (User) ParseUser.getCurrentUser();
        return inflater.inflate(R.layout.fragment_edit_allowance, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void setButtons() {

        //set up cancel and add allowance
    }

    public void setSpinners() {

    }

    public void onResume() {
        Window window = getDialog().getWindow();
        Point size = new Point();
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout((int) (size.x * 0.99), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        super.onResume();
    }
}
