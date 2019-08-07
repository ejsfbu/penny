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

import com.ejsfbu.app_main.BarcodeLookup;
import com.ejsfbu.app_main.BitmapScaler;
import com.ejsfbu.app_main.Fragments.DatePickerFragment;
import com.ejsfbu.app_main.Models.Product;
import com.ejsfbu.app_main.Models.User;
import com.ejsfbu.app_main.R;
import com.ejsfbu.app_main.Models.Goal;
import com.google.android.gms.vision.barcode.Barcode;
import com.notbytes.barcode_reader.BarcodeReaderActivity;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;

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

import static com.ejsfbu.app_main.Adapters.GoalAdapter.formatDecimal;
import static com.ejsfbu.app_main.Models.Goal.calculateDailySaving;

public class AddGoalActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static final String TAG = "AddGoalActivity";
    private static final int BARCODE_READER_ACTIVITY_REQUEST = 987;

    @BindView(R.id.etAddGoalGoalName)
    EditText etAddGoalGoalName;
    @BindView(R.id.etAddGoalGoalCost)
    EditText etAddGoalGoalCost;
    @BindView(R.id.bAddGoalAdd)
    Button bAddGoalAdd;
    @BindView(R.id.ibAddGoalCamera)
    ImageButton ibAddGoalCamera;
    @BindView(R.id.ibAddGoalPhotos)
    ImageButton ibAddGoalPhotos;
    @BindView(R.id.ivAddGoalGoalImage)
    ImageView ivAddGoalGoalImage;
    @BindView(R.id.etAddGoalEndDate)
    EditText etAddGoalEndDate;
    @BindView(R.id.bAddGoalDate)
    ImageButton bAddGoalDate;
    @BindView(R.id.ibAddGoalBack)
    ImageButton ibAddGoalBack;
    @BindView(R.id.bAddGoalScan)
    Button bAddGoalScan;

    private final static int PICK_PHOTO_CODE = 1046;
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    private File photoFile;
    public String photoFileName = "photo.jpg";
    private FragmentManager fragmentManager;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal);
        ButterKnife.bind(this);
        fragmentManager = getSupportFragmentManager();
        user = (User) ParseUser.getCurrentUser();
        user.setACL(new ParseACL(user));
        user.saveInBackground();
    }

    @OnClick(R.id.ibAddGoalBack)
    public void onClickAddGoalBack() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.bAddGoalDate)
    public void onClickDate() {
        showDatePickerDialog();
    }

    @OnClick(R.id.bAddGoalAdd)
    public void onClickAddGoal() {

        final String goalName = etAddGoalGoalName.getText().toString();
        if (goalName.equals("")) {
            Toast.makeText(this, "Please enter a goal name",
                    Toast.LENGTH_LONG).show();
            return;
        }

        final String goalPriceString = etAddGoalGoalCost.getText().toString();
        if (goalPriceString.equals("")) {
            Toast.makeText(this, "Please enter a goal price",
                    Toast.LENGTH_LONG).show();
            return;
        }
        final Double goalPrice = Double.parseDouble(etAddGoalGoalCost.getText().toString());


        final String endDateString = etAddGoalEndDate.getText().toString();
        if (endDateString.equals("")) {
            Toast.makeText(this, "Please enter an end date",
                    Toast.LENGTH_LONG).show();
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
            long today = System.currentTimeMillis();
            long diffInMillies = endDate.getTime() - today;
            if (diffInMillies < 0) {
                Toast.makeText(this, "Enter a valid end date later than today",
                        Toast.LENGTH_LONG).show();
                return;
            }
        } else {
            Toast.makeText(this, "Enter end date as mm/dd/yyyy",
                    Toast.LENGTH_LONG).show();
            return;
        }

        if (photoFile == null || ivAddGoalGoalImage.getDrawable() == null) {
            Toast.makeText(this, "Please upload or take a photo",
                    Toast.LENGTH_LONG).show();
            return;
        }

        final ParseFile image = new ParseFile(photoFile);
        image.saveInBackground();

        addGoal(goalName, goalPrice, endDate, image);
    }

    @OnClick(R.id.bAddGoalScan)
    public void onClickScan() {
        Intent launchIntent = BarcodeReaderActivity.getLaunchIntent(this, true, false);
        startActivityForResult(launchIntent, BARCODE_READER_ACTIVITY_REQUEST);
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

    @OnClick(R.id.ibAddGoalPhotos)
    public void OnClickPhotos() {
        requestPerms();
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(this.getPackageManager()) != null) {
            startActivityForResult(intent, PICK_PHOTO_CODE);
        }
    }

    @OnClick(R.id.ibAddGoalCamera)
    public void OnClickCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFileUri(photoFileName, this);

        Uri fileProvider = FileProvider.getUriForFile(this,
                "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
        if (intent.resolveActivity(this.getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    private void addGoal(String goalName, Double goalPrice, Date endDate, ParseFile image) {
        Goal goal = new Goal();
        goal.setName(goalName);
        goal.setCost(goalPrice);
        goal.setEndDate(endDate);
        goal.setImage(image);
        goal.setUser(user);
        goal.setSaved(0.0);
        goal.setCompleted(false);
        goal.setPurchased(false);
        // TODO SET ACL CHANGE LATER
        ParseACL acl = new ParseACL(user);
        acl.setPublicWriteAccess(true);
        acl.setPublicReadAccess(true);
        goal.setACL(acl);
        goal.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(AddGoalActivity.this, "Goal Created",
                            Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Goal Created");
                    goal.setDailySavings(calculateDailySaving(goal));
                    goal.saveInBackground();
                    user.addInProgressGoal(goal);
                    user.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Intent intent = new Intent(AddGoalActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                e.printStackTrace();
                                Toast.makeText(AddGoalActivity.this, "Error Creating Goal",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(AddGoalActivity.this, "Error Creating Goal",
                            Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error Creating Goal");
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_PHOTO_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                Uri photoUri = data.getData();
                photoFile = new File(getRealPathFromURI(photoUri, this));
                Bitmap selectedImage = null;
                try {
                    selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                    Bitmap resizedBitmap = BitmapScaler.scaleToFill(selectedImage, 200, 200);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
                    File resizedFile = getPhotoFileUri(photoFileName + "_resized", this);
                    resizedFile.createNewFile();
                    FileOutputStream fos = new FileOutputStream(resizedFile);
                    fos.write(bytes.toByteArray());
                    fos.close();
                    photoFile = resizedFile;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ivAddGoalGoalImage.setVisibility(View.VISIBLE);
                ivAddGoalGoalImage.setImageBitmap(selectedImage);
            }
        }
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri takenPhotoUri = Uri.fromFile(getPhotoFileUri(photoFileName, this));
                Bitmap rotatedBitmap = rotateBitmapOrientation(takenPhotoUri.getPath());
                int width = rotatedBitmap.getWidth();
                int height = rotatedBitmap.getHeight();
                Bitmap resizedBitmap;
                if (width <= height) {
                    resizedBitmap = BitmapScaler.scaleToFitWidth(rotatedBitmap, 200);
                } else {
                    resizedBitmap = BitmapScaler.scaleToFitHeight(rotatedBitmap, 200);
                }
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
                ivAddGoalGoalImage.setVisibility(View.VISIBLE);
                ivAddGoalGoalImage.setImageBitmap(cropImg);
                Log.d(TAG, photoFile.getAbsolutePath());
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
        // Barcode scanner
        if (resultCode != RESULT_OK) {
            Toast.makeText(this, "error in scanning", Toast.LENGTH_SHORT).show();
            return;
        }
        if (requestCode == BARCODE_READER_ACTIVITY_REQUEST && data != null) {
            Barcode barcode = data.getParcelableExtra(BarcodeReaderActivity.KEY_CAPTURED_BARCODE);
            Toast.makeText(this, barcode.rawValue, Toast.LENGTH_LONG).show();
            try {
                Product product = BarcodeLookup.lookUpItem(barcode.rawValue);
                loadProductData(product);
            } catch (JSONException e) {
                Toast.makeText(this, "Couldn't load product information", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            Log.d("Scanner", barcode.rawValue);
        }

    }

    public static void getProduct(Product product){
        //TODO
    }

    private void loadProductData(Product product) {

        etAddGoalGoalName.setText(product.getName());
        etAddGoalGoalCost.setText(formatDecimal(product.getPrice().toString()));

    }

    public static Bitmap rotateBitmapOrientation(String photoFilePath) {
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoFilePath, bounds);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bm = BitmapFactory.decodeFile(photoFilePath, opts);
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
        Matrix matrix = new Matrix();
        matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);
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

    public static File getPhotoFileUri(String fileName, Context context) {
        File mediaStorageDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "ImageUpload");

        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory");
        }

        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    public void requestPerms() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity) this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    0);
        } else {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0) {

        }
    }

    public void showDatePickerDialog() {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(fragmentManager, "datePicker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        Log.d(TAG, String.valueOf(c.get(Calendar.DAY_OF_MONTH)));
        String date = formatDate(monthOfYear, "month") + "/" + formatDate(dayOfMonth, "day") + "/" + formatDate(year, "year");
        etAddGoalEndDate.setText(date);
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
