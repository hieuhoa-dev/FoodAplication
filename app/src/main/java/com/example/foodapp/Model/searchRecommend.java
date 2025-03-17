package com.example.foodapp.Model;

public class searchRecommend {
    int Id;
    String Title;
    String Category;

    public searchRecommend(String title, String category) {
        Title = title;
        Category = category;
    }

    public searchRecommend() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}
