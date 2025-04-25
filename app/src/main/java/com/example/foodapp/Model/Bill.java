package com.example.foodapp.Model;

import android.util.Log;

import com.example.foodapp.Helper.BillStatus;

import java.util.List;

public class Bill {
    private String Id;
    private String IdUser;
    private List<Foods> foods;
    private String date;
    private String status;
    private double total;
    private int quantity;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public List<Foods> getFoods() {
        return foods;
    }

    public void setFoods(List<Foods> foods) {
        this.foods = foods;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getIdUser() {
        return IdUser;
    }

    public void setIdUser(String idUser) {
        IdUser = idUser;
    }
    public Bill() {
    }

    public Bill( String id,String idUser, List<Foods> foods, String date, String status, int quantity, double total) {
        IdUser = idUser;
        Id = id;
        this.foods = foods;
        this.date = date;
        this.status = status;
        this.total = total;
        this.quantity = quantity;
    }
    // Tìm BillStatus dựa trên chuỗi mô tả
    public BillStatus getBillStatus() {
        try {
            return status != null ? BillStatus.fromDescription(status) : null;
        } catch (IllegalArgumentException e) {
            Log.e("Bill", "Invalid status: " + status, e);
            return null;
        }
    }
}
