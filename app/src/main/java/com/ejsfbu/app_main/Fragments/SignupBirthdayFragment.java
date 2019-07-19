package com.ejsfbu.app_main.Fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ejsfbu.app_main.Activities.SignUpActivity;
import com.ejsfbu.app_main.EditFragments.DatePickerFragment;
import com.ejsfbu.app_main.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.ejsfbu.app_main.Activities.SignUpActivity.user;

public class SignupBirthdayFragment extends Fragment {
    public static EditText etBirthday;
    @BindView(R.id.bDate) ImageButton bDate;
    private Unbinder unbinder;
    public static final String TAG = "Birthday";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.signup_birthday, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);
        etBirthday = view.findViewById(R.id.etBirthday);
    }

    @OnClick(R.id.birthday_next_btn)
    public void onClickNext() {
        final String birthdayString = etBirthday.getText().toString();

        if (confirmCorrectDateFormat(birthdayString)) {
            final Date birthday = parseDate(birthdayString);
            if (birthday == null) {
                Toast.makeText(getContext(), "Enter birthday as mm/dd/yyyy", Toast.LENGTH_LONG).show();
            } else {
                user.setBirthday(birthday);
                Fragment email = new SignupEmailFragment();
                getFragmentManager().beginTransaction().replace(R.id.flSignUpContainer, email).commit();
            }
        } else {
            Toast.makeText(getContext(), "Enter birthday as mm/dd/yyyy", Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.bDate)
    public void onDatePick() {
        showDatePickerDialog();
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

    // When change fragment unbind view
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    // attach to an onclick handler to show the date picker
    public void showDatePickerDialog() {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(SignUpActivity.fragmentManager, "datePicker");
    }

}
