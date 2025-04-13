package com.example.foodapp.Helper;

import com.example.foodapp.Model.Users;

public interface OnUserListener {
        void onDataReceived(Users users);
        void onDataEmpty();
        void onError();
}
