package com.health.baby_daily.model;

public class Vaccine {
    private String id;
    private String dateTime;
    private String vaccineName;
    private String desc;
    private String image;
    private String baby_id;

    public Vaccine(){

    }

    public Vaccine(String id, String dateTime, String vaccineName, String desc, String image, String baby_id) {
        this.id = id;
        this.dateTime = dateTime;
        this.vaccineName = vaccineName;
        this.desc = desc;
        this.image = image;
        this.baby_id = baby_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getVaccineName() {
        return vaccineName;
    }

    public void setVaccineName(String vaccineName) {
        this.vaccineName = vaccineName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBaby_id() {
        return baby_id;
    }

    public void setBaby_id(String baby_id) {
        this.baby_id = baby_id;
    }
}
