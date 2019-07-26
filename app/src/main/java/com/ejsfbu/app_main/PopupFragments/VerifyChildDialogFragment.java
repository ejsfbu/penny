package com.ejsfbu.app_main.PopupFragments;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ejsfbu.app_main.R;
import com.ejsfbu.app_main.models.User;
import com.parse.ParseUser;

public class VerifyChildDialogFragment extends DialogFragment {

    private User user;

    private TextView tvVerifyChildParentCode;
    private Button bVerifyChildDismiss;

    public VerifyChildDialogFragment() {

    }

    public static VerifyChildDialogFragment newInstance(String title) {
        VerifyChildDialogFragment verifyChildDialogFragment = new VerifyChildDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        verifyChildDialogFragment.setArguments(args);
        return verifyChildDialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_verify_child, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = (User) ParseUser.getCurrentUser();

        tvVerifyChildParentCode = view.findViewById(R.id.tvVerifyChildParentCode);
        tvVerifyChildParentCode.setText(user.getObjectId());

        bVerifyChildDismiss = view.findViewById(R.id.bVerifyChildDismiss);
        bVerifyChildDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        String title = getArguments().getString("title", "Verify Child");
        getDialog().setTitle(title);
        tvVerifyChildParentCode.requestFocus();
        getDialog().setCanceledOnTouchOutside(false);
    }

    public void onResume() {
        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout((int) (size.x * 0.9), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        // Call super onResume after sizing
        super.onResume();
    }
}
