package com.example.ecommerce.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Models.ImageBanner;
import com.example.ecommerce.R;

import java.util.List;

public class BannerViewPager2Adapter extends RecyclerView.Adapter<BannerViewPager2Adapter.BannerViewHolder> {

    private List<ImageBanner> mlistBanner;

    public BannerViewPager2Adapter(List<ImageBanner> mlistBanner) {
        this.mlistBanner = mlistBanner;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner, parent, false);

        return new BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {

        ImageBanner banner = mlistBanner.get(position);
        if(banner == null) {
            return;
        }
        holder.imgBanner.setImageResource(banner.getResourceID());

    }

    @Override
    public int getItemCount() {
        if(mlistBanner != null) {
            return mlistBanner.size();
        }

        return 0;
    }

    public class BannerViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgBanner;

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBanner = itemView.findViewById(R.id.img_banner);
        }
    }
}
