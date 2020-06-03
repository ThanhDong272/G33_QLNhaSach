package com.qat.android.quanlynhasach.models;

public class UserOrders {
    private String orderID, totalPrice, state, date, time;

    public UserOrders() {

    }

    public UserOrders(String orderID, String totalPrice, String state, String date, String time) {
        this.orderID = orderID;
        this.totalPrice = totalPrice;
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

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
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
