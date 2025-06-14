package com.example.foodapp.Model;

public class Users {
    String Id;
    String NameUser;
    String Email;
    String PhoneNumber;
    String Img;
    String Address;

    public Users() {
    }

    public Users(String id, String nameUser, String phoneNumber, String email, String Img, String Address) {
        Id = id;
        NameUser = nameUser;
        PhoneNumber = phoneNumber;
        Email = email;
        this.Img = Img;
        this.Address = Address;
    }

    public String getImg() {
        return Img;
    }

    public void setImg(String img) {
        Img = img;
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

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
