package com.example.mfood.model;

public class Comment {
    private String name,comment;

    public Comment() {
    }

    public Comment(String name,String comment) {
        this.name=name;
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
