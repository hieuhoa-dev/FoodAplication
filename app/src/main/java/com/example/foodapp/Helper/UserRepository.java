package com.example.foodapp.Helper;

import static com.example.foodapp.Activity.BaseActivity.TAG;

import android.util.Log;

import com.example.foodapp.Model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

public class UserRepository {
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    FirebaseFirestore firestore;

    public UserRepository() {
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    public String getUidCurrentUser()
    {
        String uid = mAuth.getCurrentUser().getUid();
        return  uid;
    }
        public boolean logout() {
            mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            mAuth.signOut();
            Log.i(TAG,"Logout successfully");
            return true;
        }
        Log.i(TAG,"Logout failed");
        return false;
    }


    public void getUser(OnUserListener getUser) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firestore.collection("Users").document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Users user = documentSnapshot.toObject(Users.class);
                        if (user != null) {
                            // Sử dụng thông tin user
                            Log.i(TAG, "Test 3");
                            getUser.onDataReceived(user);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error getting user info", e);
                    Log.i(TAG, "Test 4");
                });
    }

    public ListenerRegistration getUserOnChangeListener(OnUserListener getUser) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference userRef = firestore.collection("Users").document(uid);

        // Lắng nghe thay đổi thời gian thực
        return userRef.addSnapshotListener((documentSnapshot, e) -> {
            if (e != null) {
                Log.e(TAG, "Error listening to user info", e);
                getUser.onError();
                return;
            }

            if (documentSnapshot != null && documentSnapshot.exists()) {
                Users user = documentSnapshot.toObject(Users.class);
                if (user != null) {
                    Log.i(TAG, "User data received: " + user.getNameUser());
                    getUser.onDataReceived(user);
                } else {
                    Log.w(TAG, "User object is null");
                    getUser.onDataEmpty();
                }
            } else {
                Log.w(TAG, "Document does not exist");
                getUser.onDataEmpty();
        }});
    }

    public void setUser(Users users) {
        String uid = users.getId();
        firestore.collection("Users")
                .document(uid)           // Đặt doc ID = UID
                .set(users)              // Dùng set() thay vì add()
                .addOnSuccessListener(documentReference -> {
                    Log.d("Firestore", "User info saved successfully!");
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error saving user info", e);
                });
    }

    public void setUserImg(String img) {
        String uid = mAuth.getCurrentUser().getUid();
        firestore.collection("Users")
                .document(uid)           // Đặt doc ID = UID
                .update("Img", img)
                .addOnSuccessListener(documentReference -> {
                    Log.d("Firestore", "User info saved successfully!");
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error saving user info", e);
                });
    }


}
