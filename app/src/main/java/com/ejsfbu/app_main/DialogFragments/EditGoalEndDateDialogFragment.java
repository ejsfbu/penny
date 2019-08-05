package com.ejsfbu.app_main.DialogFragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.ejsfbu.app_main.Fragments.DatePickerFragment;
import com.ejsfbu.app_main.Models.Goal;
import com.ejsfbu.app_main.R;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.ejsfbu.app_main.Models.Goal.calculateDailySaving;

public class EditGoalEndDateDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    Context context;
    Button bCancel;
    Button bConfirm;
    EditText newDate;
    TextView title;
    ImageButton calendar;
    static Goal selectedGoal;

    public EditGoalEndDateDialogFragment() {}

    public static EditGoalEndDateDialogFragment newInstance(String title, Goal goal) {
        EditGoalEndDateDialogFragment frag = new EditGoalEndDateDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        selectedGoal = goal;
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getContext();
        return inflater.inflate(R.layout.fragment_edit_goal_end_date, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bCancel = view.findViewById(R.id.bEditGoalDateCancel);
        bConfirm = view.findViewById(R.id.bEditGoalDateConfirm);
        newDate = view.findViewById(R.id.etEditEndDate);
        title = view.findViewById(R.id.tvEditGoalDate);
        calendar = view.findViewById(R.id.ibEditGoalDate);

        setListeners();

    }


    public void setListeners(){
        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        bConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Date endDate;
                if (confirmCorrectDateFormat(newDate.getText().toString())) {
                    endDate = parseDate(newDate.getText().toString());
                    if (endDate == null) {
                        Toast.makeText(context, "Enter new end date as mm/dd/yyyy", Toast.LENGTH_LONG).show();
                        return;
                    }
                } else {
                    Toast.makeText(context, "Enter new end date as mm/dd/yyyy", Toast.LENGTH_LONG).show();
                    return;
                }

                if (endDate.toString().equals(selectedGoal.getEndDate().toString())) {
                    Toast.makeText(context, "This is the same goal completion date. Please enter a new goal completion date", Toast.LENGTH_LONG).show();
                } else {
                    selectedGoal.setEndDate(endDate);
                    selectedGoal.setDailySavings(calculateDailySaving(selectedGoal));
                    selectedGoal.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Toast.makeText(context, "Successful!", Toast.LENGTH_LONG).show();
                                sendBackResult();
                            } else {
                                Toast.makeText(context, "Failure!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }

        });

       calendar.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               showDatePickerDialog();
           }
       });
    }

    // Defines the listener interface
    public interface EditGoalDateDialogListener {
        void onFinishEditThisDialog();
    }

    // Call this method to send the data back to the parent fragment
    public void sendBackResult() {
        ArrayList<Fragment> fragments = (ArrayList<Fragment>) getFragmentManager().getFragments();
        String fragmentTag = fragments.get(0).getTag();
        int fragmentId = fragments.get(1).getId();
        EditGoalDateDialogListener listener;
        listener = (EditGoalDateDialogListener) getFragmentManager()
                .findFragmentById(fragmentId);
        listener.onFinishEditThisDialog();
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
            Log.e("EditGoalEndDateDialog", "Error parsing date");
            e.printStackTrace();
            return null;
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
        Log.d("EditGoalEndDateDialog", String.valueOf(c.get(Calendar.DAY_OF_MONTH)));

        String date = formatDate(monthOfYear, "month") + "/" + formatDate(dayOfMonth, "day") + "/" + formatDate(year, "year");
        newDate.setText(date);
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

    // attach to an onclick handler to show the date picker
    public void showDatePickerDialog() {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }
}
