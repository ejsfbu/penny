package com.ejsfbu.app_main.EditFragments;

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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.ejsfbu.app_main.R;
import com.ejsfbu.app_main.models.Goal;
import com.parse.ParseFile;

import java.io.File;
import java.util.ArrayList;

public class EditGoalImageDialogFragment extends DialogFragment {

    Context context;
    Button bCancel;
    Button bConfirm;
    ImageView ivGoalImage;
    ImageButton ibGalleryPhoto;
    ImageButton ibCameraPhoto;

    private final static int PICK_PHOTO_CODE = 1046;
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    private File photoFile;
    public String photoFileName = "photo.jpg";

    public EditGoalImageDialogFragment() { }

    public static EditGoalImageDialogFragment newInstance(String title, Goal goal) {
        EditGoalImageDialogFragment frag = new EditGoalImageDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getContext();
        return inflater.inflate(R.layout.fragment_edit_goal_image, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bCancel = view.findViewById(R.id.bEditGoalImageCancel);
        bConfirm = view.findViewById(R.id.bEditGoalImageConfirm);
        ivGoalImage = view.findViewById(R.id.ivPrevGoalImage);
        ibGalleryPhoto = view.findViewById(R.id.ibEditGoalImagePhotos);
        ibCameraPhoto = view.findViewById(R.id.ibEditGoalImageCamera);

        setListeners();
    }

    public void setListeners(){
        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        bConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (photoFile == null || ivGoalImage.getDrawable() == null) {
                    Toast.makeText(context, "Please upload or take a photo", Toast.LENGTH_LONG).show();
                    return;
                }

                final ParseFile image = new ParseFile(photoFile);
                image.saveInBackground();
            }
        });
    }

    public void sendBackResult() {
        dismiss();
    }

    public void onResume() {
        Window window = getDialog().getWindow();
        Point size = new Point();
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout((int) (size.x * 0.85), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        super.onResume();
    }

    private void setOnClick() {

    }
}
