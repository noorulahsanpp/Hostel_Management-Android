package com.example.hostelapp;

public class Upload {
    private String name;
    private String imageurl;
    public Upload() {
        //empty constructor needed
    }
    public Upload(String name, String imageurl) {

        name = name;
        imageurl = imageurl;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        name = name;
    }
    public String getImageUrl() {
        return imageurl;
    }
    public void setImageUrl(String imageurl) {
        imageurl = imageurl;
    }
}
