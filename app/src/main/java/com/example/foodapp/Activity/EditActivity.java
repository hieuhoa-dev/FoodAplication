package com.example.foodapp.Activity;


import static com.example.foodapp.Activity.BaseActivity.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.foodapp.Helper.CloudinaryHelper;
import com.example.foodapp.Helper.OnUserListener;
import com.example.foodapp.Helper.UserRepository;
import com.example.foodapp.Model.Users;
import com.example.foodapp.R;
import com.example.foodapp.databinding.ActivityEditBinding;
import com.google.firebase.auth.FirebaseAuth;


import java.util.Objects;

public class EditActivity extends AppCompatActivity {

    ActivityEditBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initVariable();
        setVariable();


        binding.btnBackCD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Quay lại màn hình trước đó
            }
        });
    }



    private String selectedImageUrl = "";
    Uri imageUri =null;
    void initVariable()
    {
        binding.imgProfile.setOnClickListener(e -> openGallery());

        UserRepository ur = new UserRepository();
        ur.getUser(new OnUserListener() {
            @Override
            public void onDataReceived(Users users) {
                binding.editName.setText(users.getNameUser());
                binding.editEmail.setText(users.getEmail());
                binding.editPhone.setText(users.getPhoneNumber());
                binding.editAddress.setText(users.getAddress());
                Glide.with(EditActivity.this)
                        .load(users.getImg())
                        .transform(new CircleCrop())
                        .placeholder(R.drawable.img_user_default)
                        .into(binding.imgProfile);
                selectedImageUrl = users.getImg();
            }
            @Override
            public void onDataEmpty() {
            }
            @Override
            public void onError() {
            }
        });

    }

    void setVariable()
    {

        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uid = FirebaseAuth.getInstance().getUid();
                String name = binding.editName.getText().toString();
                String email = binding.editEmail.getText().toString();
                String phone = binding.editPhone.getText().toString();
                String address = binding.editAddress.getText().toString();
                UserRepository ur = new UserRepository();
                ur.setUser(new Users(uid,name,phone,email,selectedImageUrl,address));
                if(imageUri != null
                        || !Objects.equals(selectedImageUrl, "")
                )
                {
                    SaveImg(imageUri);
                }
                finish();
            }
        });
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
            imageUri = data.getData();
            // Thiết lập ảnh lên ImageView
            Log.i(TAG, imageUri.toString());
            Glide.with(this)
                    .load(imageUri)
                    .transform(new CircleCrop())
                    .into( binding.imgProfile);

        }
    }

    void SaveImg(Uri imageUri)
    {
        CloudinaryHelper cloudinaryHelper = new CloudinaryHelper();
        cloudinaryHelper.uploadImageToCloudinary(imageUri);
    }

}