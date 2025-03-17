package com.example.foodapp.Activity;


import android.os.Bundle;
import android.util.Log;


import androidx.activity.EdgeToEdge;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;


import com.example.foodapp.Fragment.MangerFragmentAdapter;
import com.example.foodapp.Helper.ManagementAccount;
import com.example.foodapp.Model.Users;
import com.example.foodapp.R;
import com.example.foodapp.databinding.ActivityMainBinding;

import me.ibrahimsn.lib.OnItemSelectedListener;


public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;
    int type = WindowInsetsCompat.Type.systemBars();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            return insets;
        });

        setVariable();
        saveUserInfo();
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
                if(i == 2)
                    getWindow().setStatusBarColor(getResources().getColor(R.color.accent));
                else
                    getWindow().setStatusBarColor(getResources().getColor(R.color.white));
                return true;
            }
        });
    }
    void saveUserInfo() {
        if (mAuth.getCurrentUser()==null)
            return;
        String uid = mAuth.getCurrentUser().getUid();
        firestore.collection("Users").document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Users user = documentSnapshot.toObject(Users.class);
                        if (user != null) {
                            ManagementAccount.getInstance().setCurrentUser(user);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error getting user info", e);
                });
    }
}
