package com.ejsfbu.app_main.DialogFragments;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ejsfbu.app_main.Fragments.ChildDetailFragment;
import com.ejsfbu.app_main.Models.Allowance;
import com.ejsfbu.app_main.Models.User;
import com.ejsfbu.app_main.R;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.ejsfbu.app_main.Fragments.ChildDetailFragment.formatAllowanceText;

public class ViewAllowanceDialog extends DialogFragment{


    private ArrayList<Allowance> allowances;
    private static User user;
    private Context context;
    private Unbinder unbinder;

    @BindView(R.id.spViewAllowanceParents)
    Spinner spViewAllowanceParents;
    @BindView(R.id.tvViewAllowanceDisplay)
    TextView tvViewAllowanceDisplay;
    @BindView(R.id.tvViewAllowanceTitle)
    TextView tvViewAllowanceTitle;
    @BindView(R.id.bViewAllowanceBack)
    Button bViewAllowanceBack;


    public ViewAllowanceDialog() {

    }

    public static ViewAllowanceDialog newInstance(String title) {
        ViewAllowanceDialog viewAllowanceDialog = new ViewAllowanceDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        viewAllowanceDialog.setArguments(args);
        return viewAllowanceDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        context = getContext();
        return inflater.inflate(R.layout.fragment_view_allowance, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        user = (User) ParseUser.getCurrentUser();
        allowances = Allowance.getchildAllowances(user);
        tvViewAllowanceDisplay.setVisibility(View.GONE);

        setSpinner();
        setButtons();
    }

    public void setSpinner() {
        ArrayList<String> parents = new ArrayList<>();
        parents.add("Select Parent");
        for (User parent: user.getParents()) {
            parents.add(parent.getName());
        }

        ArrayAdapter<String> parentAdapter =
                new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, parents);
        parentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spViewAllowanceParents.setAdapter(parentAdapter);
        spViewAllowanceParents.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!spViewAllowanceParents.getItemAtPosition(i).equals("Select Parent")) {
                    tvViewAllowanceDisplay.setVisibility(View.VISIBLE);
                    ArrayList<User> parent = new ArrayList<>();
                    User.Query findParent = new User.Query();
                    findParent.whereEqualTo(User.KEY_NAME, (String) spViewAllowanceParents.getItemAtPosition(i));
                    try {
                        parent.addAll(findParent.find());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    ArrayList<Allowance> currentAllowance = Allowance.getAllowance(user, parent.get(0));
                    if (currentAllowance.size() != 0) {
                        tvViewAllowanceDisplay.setText(formatAllowanceText(currentAllowance.get(0)));
                    } else {
                        tvViewAllowanceDisplay.setText(parent.get(0).getName() + " has not set up an allowance for you. Check again later.");
                    }

                } else {
                    tvViewAllowanceDisplay.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
    }

    public void setButtons() {
        bViewAllowanceBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public void onResume() {
        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout((int) (size.x * 0.99), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        // Call super onResume after sizing
        super.onResume();
    }
}
