package com.qat.android.quanlynhasach.models;

public class AdminOrders {
    private String  orderID, fullName, username, phone, totalPrice, address, state, date, time;

    public AdminOrders() {

    }

    public AdminOrders(String orderID, String fullName, String username, String phone, String totalPrice, String address, String state, String date, String time) {
        this.orderID = orderID;
        this.fullName = fullName;
        this.username = username;
        this.phone = phone;
        this.totalPrice = totalPrice;
        this.address = address;
        this.state = state;
        this.date = date;
        this.time = time;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
