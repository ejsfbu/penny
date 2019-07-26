package com.ejsfbu.app_main.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.AndroidException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ejsfbu.app_main.R;
import com.ejsfbu.app_main.models.Transaction;

import java.util.List;

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
        View view = LayoutInflater.from(context).inflate(R.layout.item_transaction,
                parent, false);
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

        private TextView tvTransactionDate;
        private TextView tvTransactionAmount;
        private TextView tvBankName;
        private TextView tvStatus;
        private TextView tvAccountNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTransactionAmount = itemView.findViewById(R.id.tvTransactionAmount);
            tvTransactionDate = itemView.findViewById(R.id.tvTransactionDate);
            tvBankName = itemView.findViewById(R.id.tvTransactionBankName);
            tvStatus = itemView.findViewById(R.id.tvTransactionStatus);
            tvAccountNumber = itemView.findViewById(R.id.tvTransactionAccountNumber);
        }

        public void bind(Transaction transaction) {
            tvTransactionDate.setText(GoalDetailsFragment.formatDate(transaction.getTransactionCompleteDate().toString()));
            tvTransactionAmount.setText(GoalDetailsFragment.formatCurrency(transaction.getAmount()));
            tvBankName.setText(transaction.getBank().getBankName());
            tvAccountNumber.setText(BankAdapter.formatAccountNumber(transaction.getBank().getAccountNumber()));
            if (transaction.getApproval()) {
                tvStatus.setText("Status: Completed");
            } else {
                tvStatus.setText("Status: Pending");
            }
            if (transaction.getType()) {
                tvAmount.setTextColor(context.getResources().getColor(R.color.colorAccent));
            } else {
                tvAmount.setTextColor(context.getResources().getColor(R.color.money_green));
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
