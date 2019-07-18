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
import com.ejsfbu.app_main.models.BadgeRow;
import com.ejsfbu.app_main.models.Reward;
import com.parse.ParseFile;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BadgeRowAdapter extends RecyclerView.Adapter<BadgeRowAdapter.ViewHolder> {

    private List<BadgeRow> badgeRows;
    private Context context;

    public BadgeRowAdapter(Context context, List<BadgeRow> badgeRows) {
        this.context = context;
        this.badgeRows = badgeRows;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_badge_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BadgeRow badgeRow = badgeRows.get(position);
        holder.bind(badgeRow);
    }

    @Override
    public int getItemCount() {
        return badgeRows.size();
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

        public void bind(BadgeRow badgeRow) {

            Reward badge1 = badgeRow.getBadge1();
            if (badge1 != null) {
                tvBadge1.setText(badge1.getName());
                setImage(badge1, ivBadge1);
            }
            Reward badge2 = badgeRow.getBadge2();
            if (badge2 != null) {
                tvBadge2.setText(badge2.getName());
                setImage(badge2, ivBadge2);
            }
            Reward badge3 = badgeRow.getBadge3();
            if (badge3 != null) {
                tvBadge3.setText(badge3.getName());
                setImage(badge3, ivBadge3);
            }
            Reward badge4 = badgeRow.getBadge4();
            if (badge4 != null) {
                tvBadge4.setText(badge4.getName());
                setImage(badge4, ivBadge4);
            }
            Reward badge5 = badgeRow.getBadge5();
            if (badge5 != null) {
                tvBadge5.setText(badge5.getName());
                setImage(badge5, ivBadge5);
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
