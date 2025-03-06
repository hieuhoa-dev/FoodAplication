package com.example.foodapp.Activity;


import android.os.Bundle;


import androidx.activity.EdgeToEdge;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;


import com.example.foodapp.Fragment.MangerFragmentAdapter;
import com.example.foodapp.R;
import com.example.foodapp.databinding.ActivityMainBinding;

import me.ibrahimsn.lib.OnItemSelectedListener;


public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;

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

        setVariable();
    }

    public void setVariable() {

        MangerFragmentAdapter adapter = new MangerFragmentAdapter(this);
        binding.fragmentContainer.setAdapter(adapter);
        binding.fragmentContainer.setUserInputEnabled(false);
        binding.fragmentContainer.setPageTransformer(null);
        binding.fragmentContainer.setOffscreenPageLimit(1);

        binding.fragmentContainer.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.bottomNavigation.setItemActiveIndex(position);
            }
        });

        binding.bottomNavigation.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int i) {
                binding.fragmentContainer.setCurrentItem(i,false);
                return true;
            }
        });
    }
}
