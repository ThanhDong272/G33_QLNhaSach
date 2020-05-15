package com.qat.android.quanlynhasach.models;

public class Users {
    private String username, password, phone, image;

    public Users() {

    }

    public Users(String username, String password, String phone, String image) {
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
