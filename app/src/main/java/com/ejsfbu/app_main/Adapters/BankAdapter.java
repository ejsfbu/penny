package com.ejsfbu.app_main.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.ejsfbu.app_main.Fragments.BankDetailsFragment;
import com.ejsfbu.app_main.R;
import com.ejsfbu.app_main.Models.BankAccount;

import java.util.List;

import static com.ejsfbu.app_main.Activities.MainActivity.fragmentManager;
import static com.ejsfbu.app_main.Activities.MainActivity.ibBanksListBack;

public class BankAdapter extends RecyclerView.Adapter<BankAdapter.ViewHolder> {

    private List<BankAccount> bankRows;
    private Context context;

    public BankAdapter(Context context, List<BankAccount> bankRows) {
        this.context = context;
        this.bankRows = bankRows;
    }

    @NonNull
    @Override
    public BankAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bank, parent, false);
        return new BankAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BankAdapter.ViewHolder holder, int position) {
        BankAccount bankAccount = bankRows.get(position);
        holder.bind(bankAccount);
    }

    @Override
    public int getItemCount() {
        return bankRows.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivBankImage;
        private TextView tvBankName;
        private TextView tvAccountNumber;
        private TextView tvVerified;
        private ConstraintLayout root;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivBankImage = itemView.findViewById(R.id.ivBankImage);
            tvBankName = itemView.findViewById(R.id.tvBankName);
            tvAccountNumber = itemView.findViewById(R.id.tvAccountNumber);
            tvVerified = itemView.findViewById(R.id.tvVerified);
            root = itemView.findViewById(R.id.root);
        }

        public void bind(BankAccount bank) {
            // TODO encapsulate to hide bank
            tvBankName.setText(bank.getBankName());
            tvAccountNumber.setText(formatAccountNumber(bank.getAccountNumber()));
            if (bank.getVerified()) {
                tvVerified.setText("Verified");
            } else {
                tvVerified.setText("Pending");
            }

            root.setOnClickListener(view -> {
                ibBanksListBack.setVisibility(View.GONE);
                Fragment fragment = new BankDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("bank", bank);
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.flMainContainer, fragment).commit();
            });
        }



// set up when we can get bank picture
//        public void setImage() {
//            ParseFile image = badge.getBadgeImage();
//            String imageUrl = image.getUrl();
//            imageUrl = imageUrl.substring(4);
//            imageUrl = "https" + imageUrl;
//            RequestOptions options = new RequestOptions()
//                    .placeholder(R.mipmap.ic_launcher)
//                    .error(R.mipmap.ic_launcher)
//                    .transform(new CenterCrop())
//                    .transform(new CircleCrop());
//            Glide.with(context)
//                    .load(imageUrl)
//                    .apply(options)
//                    .into(ivBadge);
//        }
    }

    public static String formatAccountNumber(String number) {
        return "****" + number.substring(number.length() - 4);
    }
}

