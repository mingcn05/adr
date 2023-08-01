package com.example.mfood.model;

import java.util.List;

public class Bill {
    private String key,email,name,phone,address,total,status;
    private List<Cart> foods;

    public Bill() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Cart> getFoods() {
        return foods;
    }

    public void setFoods(List<Cart> foods) {
        this.foods = foods;
    }

    public Bill(String key,String name, String phone, String address, String total, List<Cart> foods) {
        this.key=key;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.total = total;
        this.foods = foods;
        this.status="0";
    }
}
