package com.ejsfbu.app_main.Activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.ejsfbu.app_main.Models.Reward;
import com.ejsfbu.app_main.R;
import com.ejsfbu.app_main.SignUpFragments.SignUpParentFragment;
import com.ejsfbu.app_main.SignUpFragments.SignUpPersonalInfoFragment;
import com.ejsfbu.app_main.Models.User;
import java.util.Calendar;

public class SignUpActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener{

    public static final String TAG = "SignUpActivity";
    public static FragmentManager fragmentManager;
    public static User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        user = new User();
        fragmentManager = getSupportFragmentManager();

        if (getIntent().getBooleanExtra("isParent", false)) {
            user.setParentDefaults();
            Fragment parentSignUpFragment = new SignUpParentFragment();
            fragmentManager.beginTransaction().replace(R.id.flSignUpContainer,
                    parentSignUpFragment).commit();
        } else {
            user.setChildDefaults();
            Fragment personalInfoFragment = new SignUpPersonalInfoFragment();
            fragmentManager.beginTransaction().replace(R.id.flSignUpContainer,
                    personalInfoFragment).commit();
        }
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

        String date = formatDate(monthOfYear, "month") + "/" + formatDate(dayOfMonth, "day")
                + "/" + formatDate(year, "year");
        SignUpPersonalInfoFragment.etSignUpPersonalInfoBirthday.setText(date);
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
