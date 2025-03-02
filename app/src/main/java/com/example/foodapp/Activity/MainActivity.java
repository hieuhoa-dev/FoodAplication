package com.example.foodapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.LocaleList;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.foodapp.Adapter.BannerAdapter;
import com.example.foodapp.Adapter.BestFoodsAdapter;
import com.example.foodapp.Adapter.CategoryAdapter;
import com.example.foodapp.Domain.Banner;
import com.example.foodapp.Domain.Category;
import com.example.foodapp.Domain.Foods;
import com.example.foodapp.Domain.Location;
import com.example.foodapp.Domain.Price;
import com.example.foodapp.Domain.Time;
import com.example.foodapp.R;
import com.example.foodapp.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;
    private final Handler bannerHandler = new Handler();
    private final Runnable bannerRunnable = new Runnable() {
        @Override
        public void run() {
            binding.viewPage2Banner.setCurrentItem(binding.viewPage2Banner.getCurrentItem() + 1, true);
            bannerHandler.postDelayed(this, 3000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            // Đổi màu Status Bar
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black));
            return insets;
        });

//        initLocation();
//        initTime();
//        initPrice();
        initBestFood();
        initCategory();
        initBanner();
        setVariable();
    }

    public void setVariable() {
//        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FirebaseAuth.getInstance().signOut();
//                startActivity(new Intent(MainActivity.this,LoginActivity.class));
//            }
//        });
        binding.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = binding.searchEdit.getText().toString();
                if (!text.isEmpty()) {
                    Intent itent = new Intent(MainActivity.this, ListFoodsActivity.class);
                    itent.putExtra("searchText", text);
                    itent.putExtra("isSearch", true);
                    startActivity(itent);
                }
            }
        });
        binding.cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CartActivity.class));
            }
        });
    }

//    private void initLocation() {
//        DatabaseReference myRef = database.getReference("Location");
//        ArrayList<Location> list = new ArrayList<>();
//        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    for (DataSnapshot issue : snapshot.getChildren()) {
//                        Location location = issue.getValue(Location.class);
//                        list.add(location);
//                    }
//                    ArrayAdapter<Location> adapter = new ArrayAdapter<>(MainActivity.this, R.layout.sp_item, list);
//                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    binding.locationSpin.setAdapter(adapter);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//
//    private void initTime() {
//        DatabaseReference myRef = database.getReference("Time");
//        ArrayList<Time> list = new ArrayList<>();
//        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    for (DataSnapshot issue : snapshot.getChildren()) {
//                        Time time = issue.getValue(Time.class);
//                        list.add(time);
//                    }
//                    ArrayAdapter<Time> adapter = new ArrayAdapter<>(MainActivity.this, R.layout.sp_item, list);
//                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    binding.timeSpin.setAdapter(adapter);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//
//    private void initPrice() {
//        DatabaseReference myRef = database.getReference("Price");
//        ArrayList<Price> list = new ArrayList<>();
//        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    for (DataSnapshot issue : snapshot.getChildren()) {
//                        Price price = issue.getValue(Price.class);
//                        list.add(price);
//                    }
//                    ArrayAdapter<Price> adapter = new ArrayAdapter<>(MainActivity.this, R.layout.sp_item, list);
//                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    binding.priceSpin.setAdapter(adapter);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    private void initBestFood() {
        DatabaseReference myRef = database.getReference("Foods");
        binding.progressBarBestFood.setVisibility(View.VISIBLE);
        ArrayList<Foods> list = new ArrayList<>();
        Query query = myRef.orderByChild("BestFood").equalTo(true);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        list.add(issue.getValue(Foods.class));
                    }
                    if (list.size() > 0) {
                        binding.bestFoodVIew.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                        RecyclerView.Adapter adapter = new BestFoodsAdapter(list);
                        binding.bestFoodVIew.setAdapter(adapter);
                    }
                    binding.progressBarBestFood.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initCategory() {
        DatabaseReference myRef = database.getReference("Category");
        binding.progressBarCategory.setVisibility(View.VISIBLE);
        ArrayList<Category> list = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        list.add(issue.getValue(Category.class));
                    }
                    if (list.size() > 0) {
                        binding.categoryView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                        RecyclerView.Adapter adapter = new CategoryAdapter(list);
                        binding.categoryView.setAdapter(adapter);
                    }
                    binding.progressBarCategory.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initBanner() {
        DatabaseReference myRef = database.getReference("Banner");

        binding.progressBarBanner.setVisibility(View.VISIBLE);
        ArrayList<Banner> banners = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    Banner list = childSnapshot.getValue(Banner.class);
                    if (list != null) {
                        banners.add(list);
                    }
                }
                createBanners(banners);
                binding.progressBarBanner.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void createBanners(List<Banner> originalBanners) {
        BannerAdapter adapter = new BannerAdapter(originalBanners);
        binding.viewPage2Banner.setAdapter(adapter);
        binding.viewPage2Banner.setClipToPadding(false);
        binding.viewPage2Banner.setClipChildren(false);
        binding.viewPage2Banner.setOffscreenPageLimit(3);
        binding.viewPage2Banner.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.85f + r * 0.15f);
        });
        binding.viewPage2Banner.setPageTransformer(compositePageTransformer);

        // Đặt current item ban đầu là 1 để hiển thị phần tử đầu tiên thực
        binding.viewPage2Banner.setCurrentItem(1, false);

        // Thiết lập CircleIndicator: tạo số dot bằng số phần tử trong danh sách gốc
        binding.circleIndicator3.createIndicators(originalBanners.size(), 0);

        binding.viewPage2Banner.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                int realPosition = (position - 1 + originalBanners.size()) % originalBanners.size();
                binding.circleIndicator3.animatePageSelected(realPosition);
                bannerHandler.removeCallbacks(bannerRunnable);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                if (state == binding.viewPage2Banner.SCROLL_STATE_IDLE) {
                    int curr = binding.viewPage2Banner.getCurrentItem();
                    int lastReal = binding.viewPage2Banner.getAdapter().getItemCount() - 2;
                    if (curr == 0) {
                        binding.viewPage2Banner.setCurrentItem(lastReal, false);
                    } else if (curr > lastReal) {
                        binding.viewPage2Banner.setCurrentItem(1, false);
                    }
                }

                if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
                    // Người dùng đang kéo: dừng auto-scroll
                    bannerHandler.removeCallbacks(bannerRunnable);
                } else if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    // Khi cuộn dừng lại, khởi động lại auto-scroll sau 3000ms
                    bannerHandler.postDelayed(bannerRunnable, 3000);
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        bannerHandler.removeCallbacks(bannerRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bannerHandler.postDelayed(bannerRunnable, 3000);
    }
}
