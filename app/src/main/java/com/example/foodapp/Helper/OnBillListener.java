package com.example.foodapp.Helper;

import com.example.foodapp.Model.Bill;
import com.example.foodapp.Model.Foods;

import java.util.ArrayList;

public interface OnBillListener {
    void onDataReceived(ArrayList<Bill> billList,BillStatus status);
    void onDataEmpty();
    void onError();
}
