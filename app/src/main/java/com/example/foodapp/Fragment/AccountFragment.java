package com.example.foodapp.Fragment;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodapp.Helper.GetUser;
import com.example.foodapp.Helper.ManagementAccount;
import com.example.foodapp.Model.Users;
import com.example.foodapp.databinding.FragmentAccountBinding;

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
//        updateUserInfo();
        return binding.getRoot();
    }

    private void setVariable() {
        String uid = currentUser.getUid();
        if (currentUser == null) {
            Log.e("Firestore", "Current user is null");
            return;
        }

        firestore.collection("Users").document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Users user = documentSnapshot.toObject(Users.class);
                        if (user != null) {
                            // Sử dụng thông tin user
                            binding.NameUserTxt.setText(user.getNameUser());
                            binding.EmailUserTxt.setText(user.getEmail());
                            binding.PhoneNumberTxt.setText(user.getPhoneNumber());
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error getting user info", e);
                });
    }


    void updateUserInfo() {
        // Giả sử managmentAccount đã được khởi tạo và user đã được lưu từ Login
        ManagementAccount.getInstance().getAccount(new GetUser() {
            @Override
            public void sendData(Users user) {
                if (user != null) {
                    binding.NameUserTxt.setText(user.getNameUser());
                    binding.EmailUserTxt.setText(user.getEmail());
                    binding.PhoneNumberTxt.setText(user.getPhoneNumber());
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //updateUserInfo();
    }
}