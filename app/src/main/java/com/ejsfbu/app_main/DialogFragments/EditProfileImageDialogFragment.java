package com.ejsfbu.app_main.DialogFragments;

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
import com.ejsfbu.app_main.Models.User;
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

    private final static int PICK_PHOTO_CODE = 1046;
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    private File photoFile;
    public String photoFileName = "photo.jpg";
    private Context context;
    private User user;

    private Button bEditProfilePicConfirm;
    private Button bEditProfilePicCancel;
    private ImageView ivEditProfilePicProfilePic;
    private ImageButton ibEditProfilePicPhotos;
    private ImageButton ibEditProfilePicCamera;

    public EditProfileImageDialogFragment() {

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
        return inflater.inflate(R.layout.fragment_edit_profile_pic, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = (User) ParseUser.getCurrentUser();

        ivEditProfilePicProfilePic = view.findViewById(R.id.ivEditProfilePicProfilePic);
        ibEditProfilePicPhotos = view.findViewById(R.id.ibEditProfilePicPhotos);
        ibEditProfilePicCamera = view.findViewById(R.id.ibEditProfilePicCamera);
        bEditProfilePicConfirm = view.findViewById(R.id.bEditProfilePicConfirm);
        bEditProfilePicCancel = view.findViewById(R.id.bEditProfilePicCancel);

        fillData();

        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        setOnClick();
    }

    public interface EditProfileImageDialogListener {
        void onFinishEditDialog();
    }

    public void sendBackResult() {
        ArrayList<Fragment> fragments = (ArrayList<Fragment>) getFragmentManager().getFragments();
        String fragmentTag;
        int fragmentId;
        if (user.getIsParent()) {
            fragmentTag = fragments.get(1).getTag();
            fragmentId = fragments.get(1).getId();
        } else {
            fragmentTag = fragments.get(0).getTag();
            fragmentId = fragments.get(0).getId();
        }
        EditProfileImageDialogListener listener;
        if (fragmentTag != null) {
            listener = (EditProfileImageDialogListener) getFragmentManager()
                    .findFragmentByTag(fragmentTag).getContext();
        } else {
            listener = (EditProfileImageDialogListener) getFragmentManager()
                    .findFragmentById(fragmentId);
        }
        listener.onFinishEditDialog();
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

    private void fillData() {
        ParseFile image = user.getParseFile("profileImage");
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
                    .into(ivEditProfilePicProfilePic);
        }
    }

    private void setOnClick() {
        bEditProfilePicCancel.setOnClickListener(view -> {
            dismiss();
        });

        bEditProfilePicConfirm.setOnClickListener(view -> {
            ParseFile parseFile;
            if (photoFile == null || ivEditProfilePicProfilePic.getDrawable() == null) {
                parseFile = null;
                dismiss();
                return;
            } else {
                parseFile = new ParseFile(photoFile);
            }
            user.put("profileImage", parseFile);
            user.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        sendBackResult();
                    } else {
                        e.printStackTrace();
                    }
                }
            });
        });

        ibEditProfilePicPhotos.setOnClickListener(view -> {
            requestPerms();
            Intent intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            if (intent.resolveActivity(context.getPackageManager()) != null) {
                startActivityForResult(intent, PICK_PHOTO_CODE);
            }
        });

        ibEditProfilePicCamera.setOnClickListener(view -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            photoFile = AddGoalActivity.getPhotoFileUri(photoFileName, context);

            Uri fileProvider = FileProvider.getUriForFile(context,
                    "com.codepath.fileprovider", photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

            if (intent.resolveActivity(context.getPackageManager()) != null) {
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_PHOTO_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                Uri photoUri = data.getData();
                photoFile = new File(AddGoalActivity.getRealPathFromURI(photoUri, context));
                Bitmap selectedImage = null;
                try {
                    selectedImage = MediaStore.Images.Media
                            .getBitmap(context.getContentResolver(), photoUri);
                    Bitmap resizedBitmap = BitmapScaler
                            .scaleToFill(selectedImage, 200, 200);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    resizedBitmap.compress(Bitmap.CompressFormat.JPEG,
                            40, bytes);
                    File resizedFile = AddGoalActivity
                            .getPhotoFileUri(photoFileName + "_resized", context);
                    resizedFile.createNewFile();
                    FileOutputStream fos = new FileOutputStream(resizedFile);
                    fos.write(bytes.toByteArray());
                    fos.close();
                    photoFile = resizedFile;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ivEditProfilePicProfilePic.setImageBitmap(selectedImage);
            }
        }
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri takenPhotoUri = Uri.fromFile(AddGoalActivity
                        .getPhotoFileUri(photoFileName, context));
                Bitmap rotatedBitmap = rotateBitmapOrientation(takenPhotoUri.getPath());
                int width = rotatedBitmap.getWidth();
                int height = rotatedBitmap.getHeight();
                Bitmap resizedBitmap;
                if (width <= height) {
                    resizedBitmap = BitmapScaler.scaleToFitWidth(rotatedBitmap, 200);
                } else {
                    resizedBitmap = BitmapScaler.scaleToFitHeight(rotatedBitmap, 200);
                }
                Bitmap cropImg = Bitmap.createBitmap(resizedBitmap,
                        0, 0, 200, 200);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                cropImg.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
                File resizedFile = AddGoalActivity
                        .getPhotoFileUri(photoFileName + "_resized", context);
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
                ivEditProfilePicProfilePic.setImageBitmap(cropImg);
                Log.d("ProfileImage", photoFile.getAbsolutePath());
            } else { // Result was a failure
                Toast.makeText(context, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void requestPerms() {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    0);
        } else {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 0) {
            //testPost();
        }
    }
}
