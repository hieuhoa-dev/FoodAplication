package com.example.foodapp.Helper;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.foodapp.Activity.ListFoodsActivity;
import com.example.foodapp.Adapter.FoodListAdapter;
import com.example.foodapp.Model.Foods;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class DataAccess {
    public static DataAccess instance;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseDatabase database;
    FirebaseFirestore firestore ;

    public static DataAccess getInstance() {
        if (instance == null) {
            instance = new DataAccess();
        }
        return instance;
    }
    private DataAccess() {
        // Firebase Realtime Database
        database = FirebaseDatabase.getInstance();
        // Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        // Firestore
        firestore = FirebaseFirestore.getInstance();
    }

    List<Foods> SearchFood(String searchText)
    {
        List<Foods> list = new ArrayList<>();
        DatabaseReference myRef = database.getReference("Foods");
        Query query = myRef.orderByChild("Title").startAt(searchText).endAt(searchText + "\uf8ff");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        Foods foods = issue.getValue(Foods.class);
                        list.add(foods);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return list;
    }

}
