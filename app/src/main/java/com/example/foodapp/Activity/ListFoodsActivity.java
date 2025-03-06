package com.example.foodapp.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.Adapter.FoodListAdapter;
import com.example.foodapp.Domain.Foods;
import com.example.foodapp.R;
import com.example.foodapp.databinding.ActivityListFoodsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListFoodsActivity extends BaseActivity {
    ActivityListFoodsBinding binding;
    private RecyclerView.Adapter adapterListFood;
    private  int categoryId;
    private  String categoryName;
    private String searchText;
    private boolean isSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityListFoodsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cartView), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getIntentExtra();
        initList();
    }
    private void initList(){
        DatabaseReference myRef = database.getReference("Foods");
        binding.progressBar.setVisibility(View.VISIBLE);
        ArrayList<Foods> list = new ArrayList<>();

        Query query;

        if(isSearch){
            query = myRef.orderByChild("Title").startAt(searchText).endAt(searchText+"\uf8ff");
        }else{
            if("All".equals(searchText))
            {
                query = myRef;
            }else{
                query = myRef.orderByChild("CategoryId").equalTo(categoryId);
            }

        }
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot issue : snapshot.getChildren()) {
                            Foods foods = issue.getValue(Foods.class);
                            list.add(foods);
                        }
                        if (list.size()>0)
                        {
                            binding.foodListView.setLayoutManager(new GridLayoutManager(ListFoodsActivity.this,2));
                            adapterListFood = new FoodListAdapter(list);
                            binding.foodListView.setAdapter(adapterListFood);

                        }
                        binding.progressBar.setVisibility(View.GONE);
                    };
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getIntentExtra(){
        categoryId = getIntent().getIntExtra("categoryId",0);
        categoryName = getIntent().getStringExtra("categoryName");
        searchText = getIntent().getStringExtra("searchText");
        isSearch = getIntent().getBooleanExtra("isSearch",false);

        binding.tilteTxt.setText(categoryName);
        binding.backBtn.setOnClickListener(v -> finish());
    }


}