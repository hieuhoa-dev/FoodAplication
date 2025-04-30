package com.example.foodapp.Fragment;

import static com.example.foodapp.Activity.BaseActivity.TAG;
import static com.example.foodapp.Helper.API.API_KEY;
import static com.example.foodapp.Helper.API.APT_SECRET;
import static com.example.foodapp.Helper.API.CLOUD_NAME;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.cloudinary.android.MediaManager;
import com.example.foodapp.Activity.EditActivity;
import com.example.foodapp.Activity.OrderActivity;
import com.example.foodapp.Activity.SettingActivity;
import com.example.foodapp.Helper.CloudinaryHelper;
import com.example.foodapp.Helper.UserRepository;
import com.example.foodapp.Helper.OnUserListener;
import com.example.foodapp.Model.Users;
import com.example.foodapp.R;
import com.example.foodapp.databinding.FragmentAccountBinding;
import com.google.firebase.firestore.ListenerRegistration;


import java.util.Objects;

public class AccountFragment extends BaseFragment {
    FragmentAccountBinding binding;
    ListenerRegistration userListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(getLayoutInflater());
        setVariable();
        initBtn();

        CloudinaryHelper cloudinaryHelper = new CloudinaryHelper(getContext());
        cloudinaryHelper.setCloudinary();

        return binding.getRoot();
    }

    private void setVariable() {
        UserRepository ma = new UserRepository();
        Log.i(TAG, "Test 1");
        userListener = ma.getUserOnChangeListener(new OnUserListener() {
            @Override
            public void onDataReceived(Users users) {
                binding.NameUserTxt.setText(users.getNameUser());
                binding.EmailUserTxt.setText(users.getEmail());
                binding.PhoneNumberTxt.setText(users.getPhoneNumber());
                binding.AddressTxt.setText(users.getAddress());
                Glide.with(getContext())
                        .load(users.getImg())
                        .transform(new CircleCrop())
                        .placeholder(R.drawable.img_user_default)  // Ảnh tạm hiển thị trong khi tải
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                Log.e(TAG, "Glide load failed", e);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                Log.i(TAG, "Glide load success");
                                return false;
                            }
                        })
                        .into(binding.imgEdit);
                Log.i(TAG, "Test 2");
            }

            @Override
            public void onDataEmpty() {

            }

            @Override
            public void onError() {

            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    void initBtn() {
        binding.btnpayment.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), OrderActivity.class);
            startActivity(intent);
        });


        binding.btnsetting.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), SettingActivity.class);
            startActivity(intent);
        });


        binding.btnnotification.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), EditActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (userListener != null) {
            userListener.remove();
            userListener = null;
        }
    }
}