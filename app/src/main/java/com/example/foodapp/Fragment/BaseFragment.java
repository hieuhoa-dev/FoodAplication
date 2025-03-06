package com.example.foodapp.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class BaseFragment extends Fragment {
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }
}
