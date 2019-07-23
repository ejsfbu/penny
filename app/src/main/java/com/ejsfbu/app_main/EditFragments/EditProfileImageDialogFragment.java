package com.ejsfbu.app_main.EditFragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.ejsfbu.app_main.Activities.AddGoalActivity;
import com.ejsfbu.app_main.BitmapScaler;
import com.ejsfbu.app_main.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

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
    private Button bConfirm;
    private Button bCancel;
    private ImageView ivProfileImage;
    private ImageButton ibPhotos;
    private ImageButton ibCamera;

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
        ivProfileImage = view.findViewById(R.id.ivChildProfilePic);
        ibPhotos = view.findViewById(R.id.ibPhotos);
        ibCamera = view.findViewById(R.id.ibCamera);
        bConfirm = view.findViewById(R.id.bConfirm);
        bCancel = view.findViewById(R.id.bCancel);
        fillData();
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        //etFirstName.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        setOnClick();
    }

    // Defines the listener interface
    public interface EditProfileImageDialogListener {
        void onFinishEditDialog();
    }

    // Call this method to send the data back to the parent fragment
    public void sendBackResult() {
        ArrayList<Fragment> fragments = (ArrayList<Fragment>) getFragmentManager().getFragments();
        String fragmentTag = fragments.get(0).getTag();
        int fragmentId = fragments.get(0).getId();
        EditProfileImageDialogListener listener;
        if (fragments.size() > 1) {
            listener = (EditProfileImageDialogListener) getFragmentManager()
                    .findFragmentById(fragmentId);
        } else {
            listener = (EditProfileImageDialogListener) getFragmentManager()
                    .findFragmentByTag(fragmentTag).getContext();
        }
        listener.onFinishEditDialog();
        dismiss();
    }

    public void onResume() {
        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        // Set the width of the dialog proportional to 75% of the screen width
        window.setLayout((int) (size.x * 0.85), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        // Call super onResume after sizing
        super.onResume();
    }

    // load user profile image
    private void fillData() {
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
    }

    private void setOnClick() {
        bCancel.setOnClickListener(view -> {
            dismiss();
        });

        bConfirm.setOnClickListener(view -> {
            ParseFile parseFile;
            if (photoFile == null || ivProfileImage.getDrawable() == null) {
                parseFile = null;
                dismiss();
                return;
            } else {
                parseFile = new ParseFile(photoFile);
            }
            user.put("profileImage",  parseFile);
            user.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(context, "Profile Image changed successfully.", Toast.LENGTH_SHORT).show();
                        sendBackResult();
                    } else {
                        e.printStackTrace();
                    }
                }
            });
        });

        ibPhotos.setOnClickListener(view -> {
            requestPerms();
            // Create intent for picking a photo from the gallery
            Intent intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                // Bring up gallery to select a photo
                startActivityForResult(intent, PICK_PHOTO_CODE);
            }
        });

        ibCamera.setOnClickListener(view -> {
            // create Intent to take a picture and return control to the calling application
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Create a File reference to access to future access
            photoFile = AddGoalActivity.getPhotoFileUri(photoFileName, context);

            // wrap File object into a content provider
            // required for API >= 24
            // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
            Uri fileProvider = FileProvider.getUriForFile(context, "com.codepath.fileprovider", photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                // Start the image capture intent to take photo
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
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
                ivProfileImage.setImageBitmap(selectedImage);
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
                //ivGoalImage.setVisibility(View.VISIBLE);
                ivProfileImage.setImageBitmap(cropImg);
                Log.d("ProfileImage", photoFile.getAbsolutePath());
            } else { // Result was a failure
                Toast.makeText(context, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void requestPerms() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // No explanation needed; request the permission
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    0);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        } else {
            //testPost();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0) {
            //testPost();
        }
    }
}
