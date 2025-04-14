package com.example.foodapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.foodapp.Components.CustomDialog;
import com.example.foodapp.Helper.DarkMode;
import com.example.foodapp.Helper.UserRepository;
import com.example.foodapp.R;
import com.example.foodapp.databinding.ActivityDetailBinding;
import com.example.foodapp.databinding.ActivityPaymentBinding;
import com.example.foodapp.databinding.ActivitySettingBinding;

public class SettingActivity extends AppCompatActivity {

    ActivitySettingBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        binding.btnBackCD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Quay lại màn hình trước đó
            }
        });

        UserRepository userRepository = new UserRepository();
        binding.btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog dialog = new CustomDialog(SettingActivity.this);
                dialog.setTitle("Notification");
                dialog.setMessage("Are you sure you want to log out?");
                dialog.show();
                dialog.setOkClickListener(v2 -> {
                    if(userRepository.logout()){
                        // Chuyển sang màn hình Login
                        Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                        dialog.dismiss();
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

                });
                dialog.setCancelClickListener(v1 -> dialog.dismiss());
            }
        });

        binding.switchNightMode.setChecked(new DarkMode(this).isDarkModeEnabled());

        binding.switchNightMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            DarkMode darkMode = new DarkMode(this);
            darkMode.setDarkModeEnabled(isChecked);
            AppCompatDelegate.setDefaultNightMode(
                    isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
            );
        });

    }
}