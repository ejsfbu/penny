package com.ejsfbu.app_main.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener listener =
                (DatePickerDialog.OnDateSetListener) getActivity();

        return new DatePickerDialog(getActivity(), listener, year, month, day);
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Log.d("time", "here");
    }

}