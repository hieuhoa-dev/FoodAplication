package com.example.foodapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.Model.searchRecommend;
import com.example.foodapp.R;

import java.util.List;

public class SearchRecommendAdpater extends RecyclerView.Adapter<SearchRecommendAdpater.viewholder> {

    List<searchRecommend> searchRecommends;

    public SearchRecommendAdpater(List<searchRecommend> searchRecommends) {
        this.searchRecommends = searchRecommends;
    }

    Context context;

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_seach_recommend, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        searchRecommend searchRecommend = searchRecommends.get(position);
        holder.searchRecommendTxt.setText(searchRecommend.getTitle());
        holder.itemSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        if (searchRecommend.getCategory().equals("Recent")) {
            holder.imgSearch1.setImageResource(R.drawable.ic_recent);
            holder.imgSearch2.setImageResource(R.drawable.ic_delete);
            holder.imgSearch2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    searchRecommends.remove(position);
                    notifyItemRemoved(position);
                }
            });
        }
    }

    public void updateData(List<searchRecommend> newList) {
        this.searchRecommends = newList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return searchRecommends.size();
//        return Math.min(searchRecommends.size(), 10);
    }

    public class viewholder extends RecyclerView.ViewHolder {

        ImageView imgSearch1, imgSearch2;
        TextView searchRecommendTxt;
        LinearLayout itemSearch;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            imgSearch1 = itemView.findViewById(R.id.imgSearch1);
            imgSearch2 = itemView.findViewById(R.id.imgSearch2);
            searchRecommendTxt = itemView.findViewById(R.id.searchRecommendTxt);
            itemSearch = itemView.findViewById(R.id.itemSearch);
        }
    }
}
