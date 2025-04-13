package com.example.foodapp.Helper;

import com.example.foodapp.Model.Foods;

import java.util.ArrayList;

public interface OnFoodListener {
    void onDataReceived(ArrayList<Foods> foodList);
    void onDataEmpty();
    void onError(String error);
}
