package com.ejsfbu.app_main.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ejsfbu.app_main.R;
import com.ejsfbu.app_main.models.Goal;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddGoalActivity extends AppCompatActivity {

    public static final String TAG = "AddGoalActivity";

    @BindView(R.id.etGoalName)
    EditText etGoalName;
    @BindView(R.id.etGoalCost)
    EditText etGoalCost;
    @BindView(R.id.bAddGoal)
    Button bAddGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.bAddGoal)
    public void onClickAddGoal() {
        final String goalName = etGoalName.getText().toString();
        final Double goalPrice = Double.parseDouble(etGoalCost.getText().toString());

        Goal goal = new Goal();

        goal.setName(goalName);
        goal.setCost(goalPrice);
        goal.setUser(ParseUser.getCurrentUser());

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
}
