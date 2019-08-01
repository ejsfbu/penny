package com.ejsfbu.app_main.DialogFragments;


import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.ejsfbu.app_main.Adapters.ShortBadgeRowAdapter;
import com.ejsfbu.app_main.Models.BadgeRow;
import com.ejsfbu.app_main.Models.Reward;
import com.ejsfbu.app_main.Models.User;
import com.ejsfbu.app_main.Models.ShortBadgeRow;
import com.ejsfbu.app_main.R;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class EarnedBadgeDialogFragment extends DialogFragment {

    private Context context;

    private RecyclerView rvEarnedBadgedBadges;
    private Button bEarnedBadgesGreat;

    private ShortBadgeRowAdapter shortBadgeRowAdapter;
    private List<ShortBadgeRow> shortBadgeRows;

    private EarnedBadgeDialogFragment() {

    }

    public static EarnedBadgeDialogFragment newInstance(String title, ArrayList<Reward> badges) {
        EarnedBadgeDialogFragment earnedBadgeDialogFragment = new EarnedBadgeDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putParcelableArrayList("earnedBadges", badges);
        earnedBadgeDialogFragment.setArguments(args);
        return earnedBadgeDialogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getContext();
        return inflater.inflate(R.layout.fragment_earned_badge, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvEarnedBadgedBadges = view.findViewById(R.id.rvEarnedBadgesBadgesList);
        bEarnedBadgesGreat = view.findViewById(R.id.bEarnedBadgesGreat);

        shortBadgeRows = new ArrayList<>();

        shortBadgeRowAdapter = new ShortBadgeRowAdapter(context, shortBadgeRows);

        rvEarnedBadgedBadges.setAdapter(shortBadgeRowAdapter);
        rvEarnedBadgedBadges.setLayoutManager(new LinearLayoutManager(context));

        ShortBadgeRow.makeShortBadgeRows(getArguments().getParcelableArrayList("earnedBadges"),
                shortBadgeRowAdapter, shortBadgeRows);

        bEarnedBadgesGreat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    @Override
    public void onResume() {
        Window window = getDialog().getWindow();
        Point size = new Point();
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout((int) (size.x * 0.9), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        super.onResume();
    }
}
