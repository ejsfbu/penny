package com.ejsfbu.app_main.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;

import com.ejsfbu.app_main.BitmapScaler;
import com.ejsfbu.app_main.Fragments.DatePickerFragment;
import com.ejsfbu.app_main.R;
import com.ejsfbu.app_main.models.Goal;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddGoalActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static final String TAG = "AddGoalActivity";

    @BindView(R.id.etGoalName)
    EditText etGoalName;
    @BindView(R.id.etGoalCost)
    EditText etGoalCost;
    @BindView(R.id.bAddGoal)
    Button bAddGoal;
    @BindView(R.id.ibCamera)
    ImageButton ibCamera;
    @BindView(R.id.ibPhotos)
    ImageButton ibPhotos;
    @BindView(R.id.ivGoalImage)
    ImageView ivGoalImage;
    @BindView(R.id.etEndDate)
    EditText etEndDate;
    @BindView(R.id.bDate)
    ImageButton bDate;

    private final static int PICK_PHOTO_CODE = 1046;
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    private File photoFile;
    public String photoFileName = "photo.jpg";
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal);
        ButterKnife.bind(this);
        fragmentManager = getSupportFragmentManager();
    }

    @OnClick(R.id.bDate)
    public void onClickDate() {
        showDatePickerDialog();
    }

    @OnClick(R.id.bAddGoal)
    public void onClickAddGoal() {

        final String goalName = etGoalName.getText().toString();
        if (goalName.equals("")) {
            Toast.makeText(this, "Please enter a goal name", Toast.LENGTH_LONG).show();
            return;
        }

        final String goalPriceString = etGoalCost.getText().toString();
        if (goalPriceString.equals("")) {
            Toast.makeText(this, "Please enter a goal price", Toast.LENGTH_LONG).show();
            return;
        }
        final Double goalPrice = Double.parseDouble(etGoalCost.getText().toString());


        final String endDateString = etEndDate.getText().toString();
        if (endDateString.equals("")) {
            Toast.makeText(this, "Please enter an end date", Toast.LENGTH_LONG).show();
            return;
        }

        final Date endDate;
        if (confirmCorrectDateFormat(endDateString)) {
            endDate = parseDate(endDateString);
            if (endDate == null) {
                Toast.makeText(this, "Enter end date as mm/dd/yyyy",
                        Toast.LENGTH_LONG).show();
                return;
            }
        } else {
            Toast.makeText(this, "Enter end date as mm/dd/yyyy",
                    Toast.LENGTH_LONG).show();
            return;
        }

        if (photoFile == null || ivGoalImage.getDrawable() == null) {
            Toast.makeText(this, "Please upload or take a photo", Toast.LENGTH_LONG).show();
            return;
        }

        final ParseFile image = new ParseFile(photoFile);
        image.saveInBackground();

        addGoal(goalName, goalPrice, endDate, image);
    }

    private boolean confirmCorrectDateFormat(String date) {
        String[] pieces = date.split("/");
        if (pieces.length != 3) {
            return false;
        }
        if (pieces[0].length() != 2) {
            return false;
        }
        if (pieces[1].length() != 2) {
            return false;
        }
        if (pieces[2].length() != 4) {
            return false;
        }
        return true;
    }

    private Date parseDate(String date) {
        try {
            return new SimpleDateFormat("MM/dd/yyyy").parse(date);
        } catch (java.text.ParseException e) {
            Log.e(TAG, "Error parsing date");
            e.printStackTrace();
            return null;
        }
    }

    @OnClick(R.id.ibPhotos)
    public void OnClickPhotos() {
        requestPerms();
        // Create intent for picking a photo from the gallery
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(this.getPackageManager()) != null) {
            // Bring up gallery to select a photo
            startActivityForResult(intent, PICK_PHOTO_CODE);
        }
    }

    @OnClick(R.id.ibCamera)
    public void OnClickCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference to access to future access
        photoFile = getPhotoFileUri(photoFileName, this);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(this, "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(this.getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    // save goal in parse server
    private void addGoal(String goalName, Double goalPrice, Date endDate, ParseFile image) {
        Goal goal = new Goal();
        goal.setName(goalName);
        goal.setCost(goalPrice);
        goal.setEndDate(endDate);
        goal.setImage(image);
        goal.setUser(ParseUser.getCurrentUser());
        goal.setSaved(0.0);
        goal.setCompleted(false);

        goal.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(AddGoalActivity.this, "Goal Created",
                            Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Goal Created");

                    Intent intent = new Intent(AddGoalActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(AddGoalActivity.this, "Error Creating Goal",
                            Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error Creating Goal");
                    e.printStackTrace();
                }
            }
        });
    }

    // handle result of photo choosing
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_PHOTO_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                Uri photoUri = data.getData();
                photoFile = new File(getRealPathFromURI(photoUri, this));
                // by this point we have the camera photo on disk
                // Write the bytes of the bitmap to file
                Bitmap selectedImage = null;
                try {
                    selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                    // Resize Image
                    Bitmap resizedBitmap = BitmapScaler.scaleToFill(selectedImage, 200, 200);
                    // Configure byte output stream
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    // Compress the image further
                    resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
                    // Create a new file for the resized bitmap (`getPhotoFileUri` defined above)
                    File resizedFile = getPhotoFileUri(photoFileName + "_resized", this);
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
                Uri takenPhotoUri = Uri.fromFile(getPhotoFileUri(photoFileName, this));
                Bitmap rotatedBitmap = rotateBitmapOrientation(takenPhotoUri.getPath());
                Bitmap resizedBitmap = BitmapScaler.scaleToFitWidth(rotatedBitmap, 200);
                Bitmap cropImg = Bitmap.createBitmap(resizedBitmap, 0, 0, 200, 200);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                cropImg.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
                File resizedFile = getPhotoFileUri(photoFileName + "_resized", this);
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
                Log.d(TAG, photoFile.getAbsolutePath());
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static Bitmap rotateBitmapOrientation(String photoFilePath) {
        // Create and configure BitmapFactory
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoFilePath, bounds);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bm = BitmapFactory.decodeFile(photoFilePath, opts);
        // Read EXIF Data
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(photoFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
        int rotationAngle = 0;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
        // Rotate Bitmap
        Matrix matrix = new Matrix();
        matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);
        // Return result
        return rotatedBitmap;
    }

    public static String getRealPathFromURI(Uri contentURI, Context context) {
        String result;
        Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    // Returns the File for a photo stored on disk given the fileName
    public static File getPhotoFileUri(String fileName, Context context) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "ImageUpload");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    public void requestPerms() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // No explanation needed; request the permission
            ActivityCompat.requestPermissions((Activity) this,
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

    // attach to an onclick handler to show the date picker
    public void showDatePickerDialog() {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(fragmentManager, "datePicker");
    }

    // handle the date selected
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // store the values selected into a Calendar instance
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        Log.d(TAG, String.valueOf(c.get(Calendar.DAY_OF_MONTH)));

        String date = formatDate(monthOfYear, "month") + "/" + formatDate(dayOfMonth, "day") + "/" + formatDate(year, "year");
        etEndDate.setText(date);
    }

    // adds the 0 in front of days/months below 10 & formats for the correct month
    public static String formatDate(int dayOrMonth, String type) {
        String date;
        if (type.equals("month")) {
            dayOrMonth += 1;
        }
        if (dayOrMonth < 10) {
            date = "0" + dayOrMonth;
        } else {
            date = String.valueOf(dayOrMonth);
        }
        return date;
    }
}
