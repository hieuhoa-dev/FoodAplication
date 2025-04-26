package com.example.foodapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.foodapp.Activity.DetailActivity;
import com.example.foodapp.Components.BottomSheetDialog;
import com.example.foodapp.Model.Banner;
import com.example.foodapp.Model.Bill;
import com.example.foodapp.Model.Foods;
import com.example.foodapp.R;

import java.util.ArrayList;
import java.util.List;

public class WaitConfirmAdapter extends RecyclerView.Adapter<WaitConfirmAdapter.viewholder> {
    ArrayList<Bill> Bills;
    Context context;

    public WaitConfirmAdapter(ArrayList<Bill> bills) {
        this.Bills = bills;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_order, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        Bill order = Bills.get(position);
        holder.billOrderIDTxt.setText("OrderID: " + order.getId());
        holder.billDateTxt.setText(order.getDate());
        holder.billStatusTxt.setText(order.getStatus());
        holder.billTotalTxt.setText(String.valueOf(Math.round(order.getTotal())));
        holder.billOrderQuantity.setText(String.valueOf(order.getQuantity()));
        holder.billDetailbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog dialog = new BottomSheetDialog(order.getFoods(),context);
                dialog.show(((FragmentActivity) context).getSupportFragmentManager(), dialog.getTag());
            }
        });
    }

    @Override
    public int getItemCount() {
        return Bills.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView billDateTxt, billStatusTxt, billTotalTxt, billOrderQuantity, billOrderIDTxt;
        Button billDetailbtn;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            billDateTxt = itemView.findViewById(R.id.BillDateTxt);
            billStatusTxt = itemView.findViewById(R.id.BillStatusTxt);
            billTotalTxt = itemView.findViewById(R.id.BillTotalTxt);
            billOrderQuantity = itemView.findViewById(R.id.BillOrderQuantity);
            billOrderIDTxt = itemView.findViewById(R.id.BIllOrderIDTxt);
            billDetailbtn = itemView.findViewById(R.id.BillDetailBtn);
        }
    }
}
