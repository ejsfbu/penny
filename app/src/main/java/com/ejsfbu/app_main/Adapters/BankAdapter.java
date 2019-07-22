package com.ejsfbu.app_main.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.ejsfbu.app_main.R;
import com.ejsfbu.app_main.models.BankAccount;
import com.ejsfbu.app_main.models.Reward;
import com.parse.ParseFile;

import java.util.List;

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
        View view = LayoutInflater.from(context).inflate(R.layout.item_badge_row, parent, false);
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

        private ImageView ivBadge1;
        private ImageView ivBadge2;
        private ImageView ivBadge3;
        private ImageView ivBadge4;
        private ImageView ivBadge5;
        private TextView tvBadge1;
        private TextView tvBadge2;
        private TextView tvBadge3;
        private TextView tvBadge4;
        private TextView tvBadge5;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivBadge1 = itemView.findViewById(R.id.ivBadge1);
            ivBadge2 = itemView.findViewById(R.id.ivBadge2);
            ivBadge3 = itemView.findViewById(R.id.ivBadge3);
            ivBadge4 = itemView.findViewById(R.id.ivBadge4);
            ivBadge5 = itemView.findViewById(R.id.ivBadge5);
            tvBadge1 = itemView.findViewById(R.id.tvBadge1);
            tvBadge2 = itemView.findViewById(R.id.tvBadge2);
            tvBadge3 = itemView.findViewById(R.id.tvBadge3);
            tvBadge4 = itemView.findViewById(R.id.tvBadge4);
            tvBadge5 = itemView.findViewById(R.id.tvBadge5);
        }

        public void bind(BankAccount bankRow) {

        }

        public void setImage(Reward badge, ImageView ivBadge) {
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
                    .into(ivBadge);
        }
    }
}

