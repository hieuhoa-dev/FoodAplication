package com.example.foodapp.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodapp.Activity.MainActivity;
import com.example.foodapp.Adapter.CartAdapter;
import com.example.foodapp.Helper.ChangeNumberItemsListener;
import com.example.foodapp.Helper.ManagmentCart;
import com.example.foodapp.R;
import com.example.foodapp.databinding.ActivityCartBinding;
import com.example.foodapp.databinding.FragmentCartBinding;

public class CartFragment extends Fragment {

    private FragmentCartBinding binding;
    private RecyclerView.Adapter adapter;
    private ManagmentCart managmentCart;
    private  double tax;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(getLayoutInflater());

        managmentCart = new ManagmentCart(getContext());
        caculateCart();
        initList();
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        caculateCart();
        initList();
    }

    private void initList() {
        if(managmentCart.getListCart().isEmpty()) {
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.srollViewCart.setVisibility(View.GONE);
        }
        else {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.srollViewCart.setVisibility(View.VISIBLE);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.cartView.setLayoutManager(linearLayoutManager);
        adapter = new CartAdapter(managmentCart.getListCart(), getContext(), new ChangeNumberItemsListener() {
            @Override
            public void change() {
                caculateCart();
            }
        });
        binding.cartView.setAdapter(adapter);
    }

    private void caculateCart() {
        double percentTax = 0.02;
        double delivery = 10;
        tax = Math.round((managmentCart.getTotalFee() * percentTax) * 100.0) / 100.0;

        double total = Math.round((managmentCart.getTotalFee() + tax + delivery) * 100.0) / 100.0;
        double itemTotal = Math.round(managmentCart.getTotalFee() * 100.0) / 100.0;

        binding.totalFeeTxt.setText(itemTotal + "");
        binding.deliveryTxt.setText(delivery + "");
        binding.taxTxt.setText(tax + "");
        binding.totalTxt.setText(total + "");

    }
}