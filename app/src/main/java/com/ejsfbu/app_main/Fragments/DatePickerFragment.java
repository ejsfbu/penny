package com.ejsfbu.app_main.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Calendar;  // do not import java.icu.utils.Calendar


public class DatePickerFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Activity needs to implement this interface
        DatePickerDialog.OnDateSetListener listener;
        if (getActivity().getSupportFragmentManager() != null) {
            ArrayList<Fragment> fragments = (ArrayList<Fragment>) getFragmentManager().getFragments();
            String root = fragments.get(0).getTag();
            //if the root is null, we know that we are in the sign up page
            if (root != null && root.equals("com.bumptech.glide.manager")) {
                listener = (DatePickerDialog.OnDateSetListener) getActivity();
            } else  if (root == null || root.equals("datePicker") ) {
                listener = (DatePickerDialog.OnDateSetListener) getActivity();
            } else { //if not, then we know that we are in the EditGoalEndDateDialogFragment
                String fragmentTag = fragments.get(2).getTag();
                int fragmentId = fragments.get(2).getId();
                listener = (DatePickerDialog.OnDateSetListener) getFragmentManager().findFragmentByTag(fragmentTag);
            }
        } else {
            listener = (DatePickerDialog.OnDateSetListener) getActivity();
        }

        return new DatePickerDialog(getActivity(), listener, year, month, day);
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Log.d("time", "here");
    }

}