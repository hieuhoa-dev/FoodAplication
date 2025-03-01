package com.example.foodapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.foodapp.Domain.Banner;
import com.example.foodapp.R;

import java.util.ArrayList;
import java.util.List;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder> {
    private final List<Banner> banners;
    private final ViewPager2 viewPager2;
    private Context context;


    public BannerAdapter(List<Banner> banners, ViewPager2 viewPager2) {
        this.banners = banners;
        this.viewPager2 = viewPager2;
    }

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            List<Banner> newItems = new ArrayList<>(banners); // Sao chép danh sách gốc
            banners.addAll(newItems); // Thêm bản sao vào cuối danh sách
            notifyDataSetChanged();
        }
    };

    @NonNull
    @Override
    public BannerAdapter.BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_banner, parent, false);
        return new BannerAdapter.BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        Banner banner = banners.get(position);

        Glide.with(context)
                .load(banner.getImagePath())
                .transform(new CenterCrop(), new RoundedCorners(60))
                .into(holder.img);


        if (position == banners.size() - 2) {
            viewPager2.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return banners.size();
    }

    public class BannerViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgBanner);
        }
    }
}
