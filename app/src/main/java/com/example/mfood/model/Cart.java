package com.example.mfood.model;

public class Cart {
    private String name,email,key,img;
    private String price;
    private int slg;

    public Cart() {
    }


    public Cart(String name, String price,String img, int slg) {
        this.name = name;
        this.img=img;
        this.price = price;
        this.slg = slg;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", key='" + key + '\'' +
                ", img='" + img + '\'' +
                ", price='" + price + '\'' +
                ", slg=" + slg +
                '}';
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getSlg() {
        return slg;
    }

    public void setSlg(int slg) {
        this.slg = slg;
    }
}