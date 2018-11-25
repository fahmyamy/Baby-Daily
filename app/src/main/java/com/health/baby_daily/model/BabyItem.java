package com.health.baby_daily.model;

public class BabyItem {
    private String babyFullName;
    private String uriImage;
    private String id;

    public BabyItem(){

    }

    public BabyItem(String babyFullName, String uriImage, String id){
        this.babyFullName = babyFullName;
        this.uriImage = uriImage;
        this.id = id;
    }

    public String getBabyFullName() {
        return babyFullName;
    }

    public String getUriImage() {
        return uriImage;
    }

    public String getId() {
        return id;
    }

    public void setBabyFullName(String babyFullName) {
        this.babyFullName = babyFullName;
    }

    public void setUriImage(String uriImage) {
        this.uriImage = uriImage;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String toString(){
        return babyFullName;
    }
}
