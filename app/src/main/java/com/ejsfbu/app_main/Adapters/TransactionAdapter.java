package com.ejsfbu.app_main.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.ejsfbu.app_main.Fragments.GoalDetailsFragment;
import com.ejsfbu.app_main.R;
import com.ejsfbu.app_main.models.Goal;
import com.ejsfbu.app_main.models.Transaction;
import com.parse.ParseFile;

import java.util.Date;
import java.util.List;

import static com.ejsfbu.app_main.Activities.MainActivity.fragmentManager;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private Context context;
    private List<Transaction> transactionsList;

    public TransactionAdapter(Context context, List<Transaction> transactions) {
        this.context = context;
        this.transactionsList = transactions;
    }

    @NonNull
    @Override
    public TransactionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_transaction, parent, false);
        return new TransactionAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull TransactionAdapter.ViewHolder viewHolder, int i) {
        Transaction transaction = transactionsList.get(i);
        viewHolder.bind(transaction);
    }

    @Override
    public int getItemCount() {
        return transactionsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDate;
        private TextView tvAmount;
        private TextView tvBankName;
        private TextView tvStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvBankName = itemView.findViewById(R.id.tvTransactionBankName);
            tvStatus = itemView.findViewById(R.id.tvTransactionStatus);
        }

        public void bind(Transaction transaction) {
            tvDate.setText(GoalDetailsFragment.formatDate(transaction.getTransactionCompleteDate().toString()));
            tvAmount.setText(GoalDetailsFragment.formatCurrency(transaction.getAmount()));
            String bankInfo = transaction.getBank().getBankName() + BankAdapter.formatAccountNumber(transaction.getBank().getAccountNumber());
            tvBankName.setText(bankInfo);
            //set stuff
            if (transaction.getApproval()) {
                tvStatus.setText("Status: Completed");
            } else {
                tvStatus.setText("Status: Pending");
            }
        }
    }

    public void clear() {
        transactionsList.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Transaction> list) {
        transactionsList.addAll(list);
        notifyDataSetChanged();
    }
}
