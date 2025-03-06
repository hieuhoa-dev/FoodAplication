package com.example.foodapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.foodapp.Activity.DetailActivity;
import com.example.foodapp.Domain.Foods;
import com.example.foodapp.R;
import java.util.ArrayList;

public class BestFoodsAdapter extends RecyclerView.Adapter<BestFoodsAdapter.viewholder> {

    ArrayList<Foods> items;
    Context context;

    public BestFoodsAdapter(ArrayList<Foods> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_best_deal, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        Foods food = items.get(position);
        holder.titleTxt.setText(food.getTitle());
        holder.priceTxt.setText("$" + food.getPrice());
        holder.starTxt.setText(String.valueOf(food.getStar()));
        holder.timeTxt.setText(String.valueOf(food.getTimeValue() + " min"));
        holder.saleTxt.setText("Sale "+food.getSale() + "%");
        Glide.with(context)
                .load(food.getImagePath())
                .transform(new CenterCrop(),new RoundedCorners(30))
                .into(holder.pic);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("object",food);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView titleTxt, priceTxt, starTxt, timeTxt,saleTxt;
        ImageView pic;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.titleTxt);
            priceTxt = itemView.findViewById(R.id.priceTxt);
            starTxt = itemView.findViewById(R.id.starTxt);
            timeTxt = itemView.findViewById(R.id.timeTxt);
            pic = itemView.findViewById(R.id.pic);
            saleTxt = itemView.findViewById(R.id.saleTxt);
        }
    }
}
