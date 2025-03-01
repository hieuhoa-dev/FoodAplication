package com.example.foodapp.Domain;

public class Category {
    private int id;
    private  String ImagePath;
    private String Name;

    public Category( ) {
    }

    public int getId() {
        return id;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public String getName() {
        return Name;
    }

    public Category(int id, String imagePath, String name) {
        this.id = id;
        ImagePath = imagePath;
        Name = name;
    }
}
