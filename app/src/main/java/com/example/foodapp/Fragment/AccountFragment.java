package com.example.foodapp.Fragment;

import static android.app.Activity.RESULT_OK;
import static com.example.foodapp.Activity.BaseActivity.TAG;
import static com.example.foodapp.Helper.API.API_KEY;
import static com.example.foodapp.Helper.API.APT_SECRET;
import static com.example.foodapp.Helper.API.CLOUD_NAME;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import com.example.foodapp.Activity.NotificationActivity;
import com.example.foodapp.Activity.PaymentActivity;
import com.example.foodapp.Activity.SettingActivity;
import com.example.foodapp.Helper.CloudinaryHelper;
import com.example.foodapp.Helper.UserRepository;
import com.example.foodapp.Helper.OnUserListener;
import com.example.foodapp.Model.Users;
import com.example.foodapp.R;
import com.example.foodapp.databinding.FragmentAccountBinding;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AccountFragment extends BaseFragment {
    FragmentAccountBinding binding;

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

        Map config = new HashMap();
        config.put("cloud_name", CLOUD_NAME); // Thay bằng Cloud Name của bạn
        config.put("api_key", API_KEY);       // Thay bằng API Key của bạn
        config.put("api_secret", APT_SECRET); // Thay bằng API Secret của bạn
        MediaManager.init(getContext(), config);

        return binding.getRoot();
    }

    private void setVariable() {
        UserRepository ma = new UserRepository();
        Log.i(TAG, "Test 1");
        ma.getUser(new OnUserListener() {
            @Override
            public void onDataReceived(Users users) {
                binding.NameUserTxt.setText(users.getNameUser());
                binding.EmailUserTxt.setText(users.getEmail());
                binding.PhoneNumberTxt.setText(users.getPhoneNumber());
                if (!Objects.equals(users.getImg(), "")) {
                        Log.i(TAG, users.getImg());
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
                }
                Log.i(TAG, "Test 2");
            }

            @Override
            public void onDataEmpty() {

            }

            @Override
            public void onError() {

            }
        });

        binding.imgEdit.setOnClickListener(e -> openGallery());
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            // Thiết lập ảnh lên ImageView
            Log.i(TAG, imageUri.toString());
//            binding.imgEdit.setImageURI(imageUri);
            Glide.with(getContext())
                    .load(imageUri)
                    .transform(new CircleCrop())
                    .into( binding.imgEdit);
            SaveImg(imageUri, imageUri);
        }
    }

    void SaveImg(Uri imageUri, Uri publicId)
    {
        CloudinaryHelper cloudinaryHelper = new CloudinaryHelper();
        cloudinaryHelper.uploadImageToCloudinary(imageUri);
        cloudinaryHelper.deleteImageFromCloudinary(publicId.toString());
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    void initBtn (){
        binding.btnpayment.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), PaymentActivity.class);
            startActivity(intent);
        });


        binding.btnsetting.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), SettingActivity.class);
            startActivity(intent);
        });


        binding.btnnotification.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), NotificationActivity.class);
            startActivity(intent);
        });
    }
}