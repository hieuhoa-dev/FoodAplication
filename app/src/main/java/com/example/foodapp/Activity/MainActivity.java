package com.example.foodapp.Activity;


import android.os.Bundle;


import androidx.activity.EdgeToEdge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;


import com.example.foodapp.Adapter.MangerFragmentAdapter;
import com.example.foodapp.databinding.ActivityMainBinding;

import me.ibrahimsn.lib.OnItemSelectedListener;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
//                if(i == 2)
//                    getWindow().setStatusBarColor(getResources().getColor(R.color.accent));
//                else
//                    getWindow().setStatusBarColor(getResources().getColor(R.color.white));
                return true;
            }
        });
    }
}
