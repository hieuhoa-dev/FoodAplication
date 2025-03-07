package com.example.foodapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.foodapp.Model.Banner;
import com.example.foodapp.R;

import java.util.ArrayList;
import java.util.List;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder> {
    private final List<Banner> banners;
    private List<Banner> fakeBanners = new ArrayList<>();
    private Context context;

    public BannerAdapter(List<Banner> banners) {
        this.banners = banners;
        // Thêm duplicate: ảnh cuối vào đầu
        fakeBanners.add(banners.get(banners.size() - 1));
        // Thêm toàn bộ danh sách gốc
        fakeBanners.addAll(banners);
        // Thêm duplicate: ảnh đầu vào cuối
        fakeBanners.add(banners.get(0));

    }


    @NonNull
    @Override
    public BannerAdapter.BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_banner, parent, false);
        return new BannerAdapter.BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        // Tính vị trí banner thật theo modulo
        int virtualPosition = position % banners.size();
        Banner banner = banners.get(virtualPosition);

        Glide.with(context)
                .load(banner.getImagePath())
                .transform(new CenterCrop(), new RoundedCorners(60))
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return fakeBanners.size();
    }

    public class BannerViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgBanner);
        }
    }
}
