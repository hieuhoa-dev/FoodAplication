package com.example.foodapp.Activity;


import static com.example.foodapp.Activity.BaseActivity.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;


import androidx.activity.EdgeToEdge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;


import com.example.foodapp.Fragment.MangerFragmentAdapter;
import com.example.foodapp.Helper.UserRepository;
import com.example.foodapp.Model.Users;
import com.example.foodapp.R;
import com.example.foodapp.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
