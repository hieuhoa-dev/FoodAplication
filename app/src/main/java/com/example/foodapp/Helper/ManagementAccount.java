package com.example.foodapp.Helper;

import com.example.foodapp.Model.Users;

public class ManagementAccount {
    // Biến lưu trữ thông tin user hiện tại
    private static ManagementAccount instance;
    private Users currentUser;

    public static ManagementAccount getInstance() {
        if (instance == null) {
            instance = new ManagementAccount();
        }
        return instance;
    }

    public void setCurrentUser(Users user) {
        this.currentUser = user;
    }

    public void getAccount(GetUser getUser) {
        if (currentUser != null) {
            getUser.sendData(currentUser);
        } else {
            getUser.sendData(null);
        }
    }
//    public void SetAccount(Users user,  FirebaseUser currentUser, FirebaseFirestore firestore) {
//        // Lưu vào collection "users", document = uid
//        firestore.collection("Users")
//                .document(user.getId())           // Đặt doc ID = UID
//                .set(user)              // Dùng set() thay vì add()
//                .addOnSuccessListener(documentReference -> {
//                    Log.d("Firestore", "User info saved successfully!");
//                })
//                .addOnFailureListener(e -> {
//                    Log.e("Firestore", "Error saving user info", e);
//                });
//    }
}
