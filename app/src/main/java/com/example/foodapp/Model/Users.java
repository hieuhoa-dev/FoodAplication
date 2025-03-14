package com.example.foodapp.Model;

public class Users {
    String Id;
    String NameUser;
    String Email;
    String PhoneNumber;

    public Users() {
    }

    public Users(String id, String nameUser, String phoneNumber, String email) {
        Id = id;
        NameUser = nameUser;
        PhoneNumber = phoneNumber;
        Email = email;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getNameUser() {
        return NameUser;
    }

    public void setNameUser(String nameUser) {
        NameUser = nameUser;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }
}
