package com.example.foodapp.Adapter;

import android.annotation.SuppressLint;
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
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.foodapp.Helper.ChangeNumberItemsListener;
import com.example.foodapp.Helper.ManagementCart;
import com.example.foodapp.Model.Foods;
import com.example.foodapp.R;

import java.util.ArrayList;
import java.util.List;

public class BillDetailsAdapter extends RecyclerView.Adapter<BillDetailsAdapter.viewHolder> {
    List<Foods> list;


    public BillDetailsAdapter(List<Foods> list, Context context) {
        this.list = list;
    }

    @NonNull
    @Override
    public BillDetailsAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_bill_details, parent, false);
        return new BillDetailsAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillDetailsAdapter.viewHolder holder, @SuppressLint("RecyclerView") int position) {
        Foods food = list.get(position);
        holder.title.setText(food.getTitle());
        holder.BillDetailsTotalTxt.setText("$"+food.getPrice());

        Glide.with(holder.itemView.getContext())
                .load(food.getImagePath())
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.pic);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
       TextView title;
       ImageView pic;
       TextView BillDetailsTotalTxt;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleTxt);
            pic = itemView.findViewById(R.id.pic);
            BillDetailsTotalTxt = itemView.findViewById(R.id.BillDetailsTotalTxt);
        }
    }
}
