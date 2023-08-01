package com.example.mfood.model;

public class Food {
    private String key,name,des,price,image,type;
    private int slg;

    public Food() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Food{" +
                "key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", des='" + des + '\'' +
                ", price='" + price + '\'' +
                ", image='" + image + '\'' +
                ", type='" + type + '\'' +
                ", slg=" + slg +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSlg() {
        return slg;
    }

    public void setSlg(int slg) {
        this.slg = slg;
    }

    public Food(String key, String name, String des, String price, String image, String type, int slg) {
        this.key = key;
        this.name = name;
        this.des = des;
        this.price = price;
        this.image = image;
        this.type = type;
        this.slg = slg;
    }
}
