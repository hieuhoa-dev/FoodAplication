package com.example.foodapp.Model;

import java.util.List;

public class Bill {
    private String Id;
    private String IdUser;
    private List<Foods> foods;

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

    public String getIdUser() {
        return IdUser;
    }

    public void setIdUser(String idUser) {
        IdUser = idUser;
    }

    public Bill(String id, String idUser, List<Foods> foods) {
        Id = id;
        IdUser = idUser;
        this.foods = foods;
    }
}
