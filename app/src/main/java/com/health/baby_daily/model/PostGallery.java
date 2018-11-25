package com.health.baby_daily.model;

public class PostGallery {
    private String id;
    private String image;
    private String uid;

    public PostGallery(){

    }

    public PostGallery(String id, String image, String uid) {
        this.id = id;
        this.image = image;
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
