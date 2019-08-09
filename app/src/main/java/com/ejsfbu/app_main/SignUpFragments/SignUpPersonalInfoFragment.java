package com.ejsfbu.app_main.SignUpFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ejsfbu.app_main.Activities.SignUpActivity;
import com.ejsfbu.app_main.Fragments.DatePickerFragment;
import com.ejsfbu.app_main.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.ejsfbu.app_main.Activities.SignUpActivity.user;

public class SignUpPersonalInfoFragment extends Fragment {

    public static final String TAG = "SignupPersonalInfoFrag";

    @BindView(R.id.etSignUpPersonalInfoFirstName)
    EditText etSignUpPersonalInfoFirstName;
    @BindView(R.id.etSignUpPersonalInfoLastName)
    EditText etSignUpPersonalInfoLastName;
    public static EditText etSignUpPersonalInfoBirthday;
    @BindView(R.id.bSignUpPersonalInfoNext)
    Button bSignUpPersonalInfoNext;
    @BindView(R.id.ibSignUpPersonalInfoDate)
    ImageButton ibSignUpPersonalInfoDate;
    @BindView(R.id.etSignUpPersonalInfoInviterCode)
    EditText etSignUpPersonalInfoInviterCode;

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup_personal_info,
                container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);
        etSignUpPersonalInfoBirthday = view.findViewById(R.id.etSignUpPersonalInfoBirthday);
    }

    @OnClick(R.id.bSignUpPersonalInfoNext)
    public void onClickNext() {

        final String firstName = etSignUpPersonalInfoFirstName.getText().toString();
        if (firstName.equals("")) {
            Toast.makeText(getContext(), "Please enter a first name",
                    Toast.LENGTH_LONG).show();
            return;
        }

        final String lastName = etSignUpPersonalInfoLastName.getText().toString();

        final String name;
        if (lastName.equals("")) {
            name = firstName;
        } else {
            name = firstName + " " + lastName;
        }

        user.setName(name);

        final String birthdayString = etSignUpPersonalInfoBirthday.getText().toString();
        if (birthdayString.equals("")) {
            Toast.makeText(getContext(), "Please enter your birthday",
                    Toast.LENGTH_LONG).show();
            return;
        }
        if (confirmCorrectDateFormat(birthdayString)) {
            final Date birthday = parseDate(birthdayString);
            if (birthday == null) {
                Toast.makeText(getContext(), "Enter birthday as mm/dd/yyyy",
                        Toast.LENGTH_LONG).show();
                return;
            } else {
                long today = System.currentTimeMillis();
                long diffInMillies = today - birthday.getTime();
                if (diffInMillies < 0) {
                    Toast.makeText(getContext(), "Enter a valid birthday",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                long diffInYears = diffInDays / 365;
                if (diffInYears < 18) {
                    user.setNeedsParent(true);
                    user.setRequiresApproval(true);
                } else {
                    user.setNeedsParent(false);
                    user.setRequiresApproval(false);
                }
                user.setBirthday(birthday);

                
                Fragment signUpAccountInfoFragment = new SignUpAccountInfoFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.flSignUpContainer, signUpAccountInfoFragment)
                        .commit();
            }
        } else {
            Toast.makeText(getContext(), "Enter birthday as mm/dd/yyyy",
                    Toast.LENGTH_LONG).show();
            return;
        }

        /*Fragment accountInfoFragment = new SignUpAccountInfoFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.flSignUpContainer, accountInfoFragment)
                .commit();*/
    }

    @OnClick(R.id.ibSignUpPersonalInfoDate)
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

    // attach to an onclick handler to show the date picker
    public void showDatePickerDialog() {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(SignUpActivity.fragmentManager, "datePicker");
    }

    // When change fragment unbind view
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
