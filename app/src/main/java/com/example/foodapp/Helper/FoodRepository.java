package com.example.foodapp.Helper;

import androidx.annotation.NonNull;

import com.example.foodapp.Model.Foods;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FoodRepository {

    private DatabaseReference foodRef;

    public FoodRepository() {
        foodRef = FirebaseDatabase.getInstance().getReference("Foods");
    }

    /**
     * Lấy dữ liệu Foods theo điều kiện tìm kiếm hoặc theo category.
     *
     * @param isSearch  kiểm tra có phải tìm kiếm theo Title hay không
     * @param searchText chuỗi tìm kiếm hoặc "All" nếu muốn lấy tất cả
     * @param categoryId nếu không tìm kiếm theo Title, dùng CategoryId
     * @param listener callback để trả kết quả
     */
    public void getFoods(boolean isSearch, String searchText, int categoryId,  OnFoodListener listener) {
        Query query;
        if (isSearch) {
            // Tìm kiếm theo Title
            query = foodRef.orderByChild("Title")
                    .startAt(searchText)
                    .endAt(searchText + "\uf8ff");
        } else {
            if ("All".equals(searchText)) {
                query = foodRef;
            } else {
                query = foodRef.orderByChild("CategoryId")
                        .equalTo(categoryId);
            }
        }

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Foods> list = new ArrayList<>();
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        Foods food = issue.getValue(Foods.class);
                        list.add(food);
                    }
                    if (list.size() > 0) {
                        listener.onDataReceived(list);
                    } else {
                        listener.onDataEmpty();
                    }
                } else {
                    listener.onDataEmpty();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onError(error.getMessage());
            }
        });
    }
}
