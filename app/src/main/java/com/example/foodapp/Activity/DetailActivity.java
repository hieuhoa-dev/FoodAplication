package com.example.foodapp.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;

import com.bumptech.glide.Glide;
import com.example.foodapp.Helper.CustomerNotify;
import com.example.foodapp.Model.Foods;

import com.example.foodapp.Helper.ManagementCart;
import com.example.foodapp.R;
import com.example.foodapp.databinding.ActivityDetailBinding;

public class DetailActivity extends BaseActivity {
    ActivityDetailBinding binding;
    private Foods object;
    private int num = 1;
    private ManagementCart managmentCart;
    private boolean isFav = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        EdgeToEdge.enable(this);

        getIntentExtra();
        setVariable();
    }

    private void setVariable() {
        managmentCart = new ManagementCart(this);
        binding.backBtn.setOnClickListener(v -> finish());

        Glide.with(DetailActivity.this)
                .load(object.getImagePath())
                .into(binding.pic);
        binding.priceTxt.setText(" $" + object.getPrice());
        binding.titleTxt.setText(object.getTitle());
        binding.descriptionTxt.setText(object.getDescription());
        binding.rateTxt.setText(object.getStar() + "Rating");
        binding.ratingBar.setRating((float) object.getStar());
        binding.totalTxt.setText(num * object.getPrice() + "$");

        binding.plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num += 1;
                binding.numTxt.setText(num + "");
                binding.totalTxt.setText(num * object.getPrice() + "$");
            }
        });
        binding.minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (num > 1) {
                    num -= 1;
                    binding.numTxt.setText(num + "");
                    binding.totalTxt.setText(num * object.getPrice() + "$");
                }
            }
        });
        binding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                object.setNumberInCart(num);
                managmentCart.insertFood(object);
                new CustomerNotify(DetailActivity.this, "Thông báo", "Đã thêm vào giỏ hàng");
            }
        });
        binding.favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFav) {
                    binding.favBtn.setImageResource(R.drawable.ic_favorite_border); // Bỏ thích
                } else {
                    binding.favBtn.setImageResource(R.drawable.ic_favorite); // Đã thích
                }
                isFav = !isFav;
            }
        });
    }

    private void getIntentExtra() {
        object = (Foods) getIntent().getSerializableExtra("object");
    }
}