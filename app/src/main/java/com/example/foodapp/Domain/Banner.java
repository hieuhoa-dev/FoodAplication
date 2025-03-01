package com.example.foodapp.Domain;

public class Banner {
    int Id;
    private String ImagePath;

    public Banner() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }
}
