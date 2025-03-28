package com.example.foodapp.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.foodapp.Adapter.CartAdapter;
import com.example.foodapp.Helper.ChangeNumberItemsListener;
import com.example.foodapp.Helper.ManagementCart;
import com.example.foodapp.Model.Bill;
import com.example.foodapp.Model.Users;
import com.example.foodapp.databinding.FragmentCartBinding;

public class CartFragment extends BaseFragment {

    private FragmentCartBinding binding;
    private RecyclerView.Adapter adapter;
    private ManagementCart managmentCart;
    private  double tax;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(getLayoutInflater());

        managmentCart = new ManagementCart(getContext());
        caculateCart();
        initList();
        setVariable();
        return binding.getRoot();
    }

    private void setVariable() {
        binding.orderBtn.setOnClickListener(view -> SaveBill());
    }
    void SaveBill() {
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            return;
        }
        String uid = currentUser.getUid();
        Bill bill = new Bill("",uid, managmentCart.getListCart());

        firestore.collection("Bill")
                .add(bill)
                .addOnSuccessListener(documentReference -> {
                    String billId = documentReference.getId(); // Lấy ID tự động
                    documentReference.update("id", billId) // Cập nhật id
                            .addOnSuccessListener(e ->
                                    Toast.makeText(getContext(), "Order successfully", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e ->
                                    Toast.makeText(getContext(), "Failed to update bill ID", Toast.LENGTH_SHORT).show());
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Order fail", Toast.LENGTH_SHORT).show();
                });
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

    @Override
    public void onResume() {
        super.onResume();
        caculateCart();
        initList();
    }
}