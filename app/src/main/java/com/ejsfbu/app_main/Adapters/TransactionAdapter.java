package com.ejsfbu.app_main.Adapters;

import android.content.Context;
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

    Context context;
    List<Transaction> transactionsList;

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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTransactionAmount = itemView.findViewById(R.id.tvTransactionAmount);
            tvTransactionDate = itemView.findViewById(R.id.tvTransactionDate);
        }

        public void bind(Transaction transaction) {
            tvTransactionDate.setText(transaction.getTransactionDate().toString());
            tvTransactionAmount.setText(String.valueOf(transaction.getAmount()));
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
