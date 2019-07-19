package com.ejsfbu.app_main.SignupFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ejsfbu.app_main.Fragments.SignupBirthdayFragment;
import com.ejsfbu.app_main.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.ejsfbu.app_main.Activities.SignUpActivity.user;


public class SignupPersonalInfoFragment extends Fragment {

    public static final String TAG = "SignupPersonalInfoFrag";

    @BindView(R.id.etFirstName)
    EditText etFirstName;
    @BindView(R.id.etLastName)
    EditText etLastName;
    @BindView(R.id.etBirthday)
    EditText etBirthday;
    @BindView(R.id.bNext)
    Button bNext;

    private Unbinder unbinder;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup_personal_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);
    }


    @OnClick(R.id.bNext)
    public void onClickNext() {

        final String firstName = etFirstName.getText().toString();
        if (firstName.equals("")) {
            Toast.makeText(getContext(), "Please enter a first name", Toast.LENGTH_LONG);
            return;
        }

        final String lastName = etLastName.getText().toString();

        final String name;
        if (lastName.equals("")) {
            name = firstName;
        } else {
            name = firstName + " " + lastName;
        }

        user.setName(name);

        final String birthdayString = etBirthday.getText().toString();
        if (confirmCorrectDateFormat(birthdayString)) {
            final Date birthday = parseDate(birthdayString);
            if (birthday == null) {
                Toast.makeText(getContext(), "Enter birthday as dd/mm/yyyy", Toast.LENGTH_LONG).show();
            } else {
                user.setBirthday(birthday);
                Fragment email = new SignupAccountInfoFragment();
                getFragmentManager().beginTransaction().replace(R.id.flSignUpContainer, email).commit();
            }
        } else {
            Toast.makeText(getContext(), "Enter birthday as dd/mm/yyyy", Toast.LENGTH_LONG).show();
        }

        Fragment accountInfoFragment = new SignupAccountInfoFragment();
        getFragmentManager().beginTransaction().replace(R.id.flSignUpContainer, accountInfoFragment).commit();
    }

    private Date parseDate(String date) {
        try {
            return new SimpleDateFormat("dd/MM/yyyy").parse(date);
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
}
