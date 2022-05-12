package com.example.pizzabuy;

public class order {
    int id;
    float price;
    String paid;
    String Address;
    String Status;
    int Courier;

    public order() {
    }

    public order(int id, float price, String paid, String address, String status, int courier) {
        this.id = id;
        this.price = price;
        this.paid = paid;
        Address = address;
        Status = status;
        Courier = courier;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public int getCourier() {
        return Courier;
    }

    public void setCourier(int courier) {
        Courier = courier;
    }

    @Override
    public String toString() {
        return this.paid + " " + this.Address + " " + this.Status;
    }
}
