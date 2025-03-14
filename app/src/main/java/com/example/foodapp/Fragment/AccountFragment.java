package com.example.foodapp.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodapp.Model.Users;
import com.example.foodapp.R;
import com.example.foodapp.databinding.FragmentAccountBinding;
import com.example.foodapp.databinding.FragmentCartBinding;

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
}