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
import com.ejsfbu.app_main.Models.Reward;
import com.ejsfbu.app_main.Models.ShortBadgeRow;
import com.ejsfbu.app_main.R;
import com.parse.ParseFile;

import java.util.List;

public class ShortBadgeRowAdapter extends RecyclerView.Adapter<ShortBadgeRowAdapter.ViewHolder> {

    private List<ShortBadgeRow> shortBadgeRows;
    private Context context;

    public ShortBadgeRowAdapter(Context context, List<ShortBadgeRow> shortBadgeRows) {
        this.context = context;
        this.shortBadgeRows = shortBadgeRows;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_short_badge_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShortBadgeRow shortBadgeRow = shortBadgeRows.get(position);
        holder.bind(shortBadgeRow);
    }

    @Override
    public int getItemCount() {
        return shortBadgeRows.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivShortRowBadge1;
        private ImageView ivShortRowBadge2;
        private ImageView ivShortRowBadge3;
        private TextView tvShortRowBadge1;
        private TextView tvShortRowBadge2;
        private TextView tvShortRowBadge3;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivShortRowBadge1 = itemView.findViewById(R.id.ivShortRowBadge1);
            ivShortRowBadge2 = itemView.findViewById(R.id.ivShortRowBadge2);
            ivShortRowBadge3 = itemView.findViewById(R.id.ivShortRowBadge3);
            tvShortRowBadge1 = itemView.findViewById(R.id.tvShortRowBadge1);
            tvShortRowBadge2 = itemView.findViewById(R.id.tvShortRowBadge2);
            tvShortRowBadge3 = itemView.findViewById(R.id.tvShortRowBadge3);
        }

        public void bind(ShortBadgeRow shortBadgeRow) {

            Reward badge1 = shortBadgeRow.getBadge1();
            if (badge1 != null) {
                tvShortRowBadge1.setText(badge1.getName());
                setImage(badge1, ivShortRowBadge1);
            }
            Reward badge2 = shortBadgeRow.getBadge2();
            if (badge2 != null) {
                tvShortRowBadge2.setText(badge2.getName());
                setImage(badge2, ivShortRowBadge2);
            }
            Reward badge3 = shortBadgeRow.getBadge3();
            if (badge3 != null) {
                tvShortRowBadge3.setText(badge3.getName());
                setImage(badge3, ivShortRowBadge3);
            }
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
