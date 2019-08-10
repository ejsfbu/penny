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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.ejsfbu.app_main.Models.Reward;
import com.ejsfbu.app_main.Models.User;
import com.ejsfbu.app_main.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;

import static com.ejsfbu.app_main.Models.Reward.userHasClaimedBadge;

public class RewardDetailDialogFragment extends DialogFragment {

    private Reward badge;
    private User user;

    private Context context;

    private ImageView ivRewardDetailBadgeImage;
    private TextView tvRewardDetailBadgeName;
    private TextView tvRewardDetailBadgeDescription;
    private TextView tvRewardDetailStatus;
    private Button bRewardDetailClose;
    private ImageView ivAmazonGiftCard;
    private TextView tvGiftCardAmount;
    private TextView tvDiscountCode;
    private ImageView ivAmazonLogo;
    private TextView tvDiscountText;

    public RewardDetailDialogFragment() {

    }

    public static RewardDetailDialogFragment newInstance(String title, Reward badge, User user) {
        RewardDetailDialogFragment rewardDetailDialogFragment = new RewardDetailDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putParcelable("badge", badge);
        args.putParcelable("user", user);
        rewardDetailDialogFragment.setArguments(args);
        return rewardDetailDialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        context = getContext();
        return inflater.inflate(R.layout.fragment_reward_detail, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivRewardDetailBadgeImage = view.findViewById(R.id.ivRewardDetailBadgeImage);
        tvRewardDetailBadgeName = view.findViewById(R.id.tvRewardDetailBadgeName);
        tvRewardDetailBadgeDescription = view.findViewById(R.id.tvRewardDetailBadgeDescription);
        tvRewardDetailStatus = view.findViewById(R.id.tvRewardDetailStatus);
        bRewardDetailClose = view.findViewById(R.id.bRewardDetailClose);
        tvGiftCardAmount = view.findViewById(R.id.tvGiftCardAmount);
        ivAmazonGiftCard = view.findViewById(R.id.ivAmazonGiftCard);
        tvDiscountCode = view.findViewById(R.id.tvDiscountCode);
        ivAmazonLogo = view.findViewById(R.id.ivAmazonLogo);
        tvDiscountText = view.findViewById(R.id.tvDiscountText);

        badge = getArguments().getParcelable("badge");
        user = getArguments().getParcelable("user");

        ParseFile image = badge.getBadgeImage();
        String imageUrl = image.getUrl();
        imageUrl = imageUrl.substring(4);
        imageUrl = "https" + imageUrl;
        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .transform(new CenterCrop())
                .transform(new CircleCrop());
        Glide.with(context)
                .load(imageUrl)
                .apply(options)
                .into(ivRewardDetailBadgeImage);

        tvRewardDetailBadgeName.setText(badge.getName());
        tvRewardDetailBadgeDescription.setText(badge.getDescription());


        if (user.hasInProgressBadge(badge.getObjectId())) {
            tvRewardDetailStatus.setText("In Progress");
        } else {
            tvRewardDetailStatus.setText("Completed");
        }

        if (tvRewardDetailStatus.getText().toString().equals("Completed")) {
            checkSpecialReward();
        } else {
            tvGiftCardAmount.setVisibility(View.GONE);
            ivAmazonGiftCard.setVisibility(View.GONE);
            tvDiscountCode.setVisibility(View.GONE);
            ivAmazonLogo.setVisibility(View.GONE);
            tvDiscountText.setVisibility(View.GONE);
            bRewardDetailClose.setText("Close");
            bRewardDetailClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
        }

        String title = getArguments().getString("title", "Reward Detail");
        getDialog().setTitle(title);
        tvRewardDetailBadgeName.requestFocus();
    }

    public void checkSpecialReward() {
        if (badge.hasGiftCard() && !userHasClaimedBadge(user, badge.getObjectId())) {
            tvGiftCardAmount.setVisibility(View.VISIBLE);
            ivAmazonGiftCard.setVisibility(View.VISIBLE);
            ivAmazonLogo.setVisibility(View.GONE);
            tvDiscountText.setVisibility(View.GONE);
            tvDiscountCode.setVisibility(View.GONE);
            bRewardDetailClose.setText("Claim");
            bRewardDetailClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    user.claimReward(badge);
                    user.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Toast.makeText(context, "Reward claimed!",
                                        Toast.LENGTH_LONG).show();
                                dismiss();
                            } else {
                                e.printStackTrace();
                            }
                        }
                    });

                }
            });
        } else if (badge.hasDiscount()) {
            tvGiftCardAmount.setVisibility(View.GONE);
            ivAmazonGiftCard.setVisibility(View.GONE);
            ivAmazonLogo.setVisibility(View.VISIBLE);
            tvDiscountText.setVisibility(View.VISIBLE);
            tvDiscountCode.setText(badge.getDiscountCode());
            tvDiscountCode.setVisibility(View.VISIBLE);
            bRewardDetailClose.setText("Close");
            bRewardDetailClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
        } else {
            tvGiftCardAmount.setVisibility(View.GONE);
            ivAmazonGiftCard.setVisibility(View.GONE);
            tvDiscountCode.setVisibility(View.GONE);
            ivAmazonLogo.setVisibility(View.GONE);
            tvDiscountText.setVisibility(View.GONE);
            bRewardDetailClose.setText("Close");
            bRewardDetailClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
        }
    }

    public String formatDate(String dateString) {
        String finalDateString;
        String[] datePieces = dateString.split(" ");
        finalDateString = getMonth(datePieces[1]) + getDate(datePieces[2])
                + datePieces[datePieces.length - 1];
        return finalDateString;
    }

    public String getMonth(String monthString) {
        String month;
        switch (monthString) {
            case "Jan":
                month = "January";
                break;
            case "Feb":
                month = "February";
                break;
            case "Mar":
                month = "March";
                break;
            case "Apr":
                month = "April";
                break;
            case "May":
                month = "May";
                break;
            case "Jun":
                month = "June";
                break;
            case "Jul":
                month = "July";
                break;
            case "Aug":
                month = "August";
                break;
            case "Sep":
                month = "September";
                break;
            case "Oct":
                month = "October";
                break;
            case "Nov":
                month = "November";
                break;
            case "Dec":
                month = "December";
                break;
            default:
                month = "monthString was " + monthString;
        }
        return month + " ";
    }

    public String getDate(String dateString) {
        if (dateString.substring(0, 1).equals("0")) {
            return dateString.substring(1) + ", ";
        } else {
            return dateString + ", ";
        }
    }

    @Override
    public void onResume() {
        Window window = getDialog().getWindow();
        Point size = new Point();
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout((int) (size.x * 0.95), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        super.onResume();
    }
}
