package com.example.foodapp.Model;

public class Category {
    private int Id;
    private  String ImagePath;
    private String Name;

    public Category( ) {
    }

    public int getId() {
        return Id;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public String getName() {
        return Name;
    }

    public Category(int id, String imagePath, String name) {
        this.Id = id;
        ImagePath = imagePath;
        Name = name;
    }
}
