package com.example.foodapp.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.Adapter.FoodListAdapter;
import com.example.foodapp.Helper.FoodRepository;
import com.example.foodapp.Helper.OnFoodListener;
import com.example.foodapp.Model.Foods;
import com.example.foodapp.R;
import com.example.foodapp.databinding.ActivityListFoodsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class ListFoodsActivity extends AppCompatActivity {
    ActivityListFoodsBinding binding;
    private RecyclerView.Adapter adapterListFood;
    private int categoryId;
    private String categoryName;
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

    private void initList() {
        binding.progressBar.setVisibility(View.VISIBLE);

        FoodRepository repository = new FoodRepository();
        repository.getFoods(isSearch, searchText, categoryId, new OnFoodListener() {
            @Override
            public void onDataReceived(ArrayList<Foods> foodList) {
                adapterListFood = new FoodListAdapter(foodList);
                binding.foodListView.setLayoutManager(new GridLayoutManager(ListFoodsActivity.this, 2));
                binding.foodListView.setAdapter(adapterListFood);
                binding.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onDataEmpty() {
                binding.notListFoodTxt.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(String errorMessage) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void getIntentExtra() {
        categoryId = getIntent().getIntExtra("categoryId", 0);
        categoryName = getIntent().getStringExtra("categoryName");
        searchText = getIntent().getStringExtra("searchText");
        isSearch = getIntent().getBooleanExtra("isSearch", false);

        if (Objects.equals(searchText, "All"))
            categoryName = "All Foods";

        binding.tilteTxt.setText(categoryName);
        binding.backBtn.setOnClickListener(v -> finish());
    }
}