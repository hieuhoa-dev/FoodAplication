package com.example.foodapp.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodapp.Activity.ListFoodsActivity;
import com.example.foodapp.Activity.SearchActivity;
import com.example.foodapp.Activity.SettingActivity;
import com.example.foodapp.Adapter.BannerAdapter;
import com.example.foodapp.Adapter.BestFoodsAdapter;
import com.example.foodapp.Adapter.CategoryAdapter;
import com.example.foodapp.Components.CustomDialog;
import com.example.foodapp.Model.Banner;
import com.example.foodapp.Model.Category;
import com.example.foodapp.Model.Foods;
import com.example.foodapp.databinding.FragmentHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import java.util.List;

public class HomeFragment extends BaseFragment {

    FragmentHomeBinding binding;


    private final Handler bannerHandler = new Handler();
    private final Runnable bannerRunnable = new Runnable() {
        @Override
        public void run() {
            binding.viewPage2Banner.setCurrentItem(binding.viewPage2Banner.getCurrentItem() + 1, true);
            bannerHandler.postDelayed(this, 3000);
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            return insets;
        });



        initBestFood();
        initCategory();
        initBanner();
        setVariable();
        return binding.getRoot();
    }



    @SuppressLint("ClickableViewAccessibility")
    public void setVariable() {
//        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FirebaseAuth.getInstance().signOut();
//                startActivity(new Intent(MainActivity.this,LoginActivity.class));
//            }
//        });

//        binding.cartBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getContext(), CartActivity.class));
//            }
//        });
        binding.searchAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent itent = new Intent(getContext(), ListFoodsActivity.class);
                itent.putExtra("searchText", "All");
                startActivity(itent);
            }
        });

        binding.searchEdit.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
                return true; // Đánh dấu sự kiện đã xử lý
            }
            return false; // Không xử lý các sự kiện khác
        });

        binding.filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(getContext(), SettingActivity.class));
            }
        });
    }


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
                        binding.bestFoodVIew.setLayoutManager(new GridLayoutManager(getContext(), 2));
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
                        binding.categoryView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
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
    public void onPause() {
        super.onPause();
        bannerHandler.removeCallbacks(bannerRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        bannerHandler.postDelayed(bannerRunnable, 3000);
    }
}