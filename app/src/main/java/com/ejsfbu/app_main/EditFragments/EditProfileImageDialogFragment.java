package com.ejsfbu.app_main.EditFragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ejsfbu.app_main.Activities.AddGoalActivity;
import com.ejsfbu.app_main.BitmapScaler;
import com.ejsfbu.app_main.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;
import static com.ejsfbu.app_main.Activities.AddGoalActivity.rotateBitmapOrientation;

public class EditProfileImageDialogFragment extends DialogFragment {
    // Request codes
    private final static int PICK_PHOTO_CODE = 1046;
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    // needed values
    private File photoFile;
    public String photoFileName = "photo.jpg";
    private Context context;
    private ParseUser user;

    public EditProfileImageDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static EditProfileImageDialogFragment newInstance(String title) {
        EditProfileImageDialogFragment frag = new EditProfileImageDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getContext();
        return inflater.inflate(R.layout.fragment_edit_profileimage, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = ParseUser.getCurrentUser();
        // Get field from view
        etFirstName = view.findViewById(R.id.etFirstName);
        bConfirm = view.findViewById(R.id.bConfirm);
        bCancel = view.findViewById(R.id.bCancel);
        fillData();
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        etFirstName.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        setOnClick();
    }

    public void onResume() {
        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        // Set the width of the dialog proportional to 75% of the screen width
        window.setLayout((int) (size.x * 0.75), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        // Call super onResume after sizing
        super.onResume();
    }

    private void setOnClick() {
        bCancel.setOnClickListener(view -> {
            dismiss();
        });

        bConfirm.setOnClickListener(view -> {
            user.put("name",  name);
            user.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(context, "Name changed successfully.", Toast.LENGTH_SHORT).show();
                        dismiss();
                    } else {
                        e.printStackTrace();
                    }
                }
            });
        });
    }
    // TODO try to write once
    // handle result of photo choosing
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_PHOTO_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                Uri photoUri = data.getData();
                photoFile = new File(AddGoalActivity.getRealPathFromURI(photoUri, context));
                // by context point we have the camera photo on disk
                // Write the bytes of the bitmap to file
                Bitmap selectedImage = null;
                try {
                    selectedImage = MediaStore.Images.Media.getBitmap(context.getContentResolver(), photoUri);
                    // Resize Image
                    Bitmap resizedBitmap = BitmapScaler.scaleToFill(selectedImage, 200, 200);
                    // Configure byte output stream
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    // Compress the image further
                    resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
                    // Create a new file for the resized bitmap (`getPhotoFileUri` defined above)
                    File resizedFile = AddGoalActivity.getPhotoFileUri(photoFileName + "_resized", context);
                    resizedFile.createNewFile();
                    FileOutputStream fos = new FileOutputStream(resizedFile);
                    fos.write(bytes.toByteArray());
                    fos.close();
                    photoFile = resizedFile;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Load the selected image into a preview
                ivGoalImage.setVisibility(View.VISIBLE);
                ivGoalImage.setImageBitmap(selectedImage);
            }
        }
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri takenPhotoUri = Uri.fromFile(AddGoalActivity.getPhotoFileUri(photoFileName, context));
                Bitmap rotatedBitmap = rotateBitmapOrientation(takenPhotoUri.getPath());
                Bitmap resizedBitmap = BitmapScaler.scaleToFitWidth(rotatedBitmap, 200);
                Bitmap cropImg = Bitmap.createBitmap(resizedBitmap, 0, 0, 200, 200);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                cropImg.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
                File resizedFile = AddGoalActivity.getPhotoFileUri(photoFileName + "_resized", context);
                try {
                    resizedFile.createNewFile();
                    FileOutputStream fos = new FileOutputStream(resizedFile);
                    fos.write(bytes.toByteArray());
                    fos.close();
                    photoFile = resizedFile;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ivGoalImage.setVisibility(View.VISIBLE);
                ivGoalImage.setImageBitmap(cropImg);
                Log.d("ProfileImage", photoFile.getAbsolutePath());
            } else { // Result was a failure
                Toast.makeText(context, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
