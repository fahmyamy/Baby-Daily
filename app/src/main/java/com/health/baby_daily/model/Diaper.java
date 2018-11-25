package com.health.baby_daily.model;

public class Diaper {
    private String id;
    private String dateTime;
    private String type;
    private String desc;
    private String image;
    private String created;
    private String giver_id;
    private String babyId;

    public Diaper(){

    }

    public Diaper(String id, String dateTime, String type, String desc, String image, String created, String giver_id, String babyId){
        this.id = id;
        this.dateTime = dateTime;
        this.type = type;
        this.desc = desc;
        this.image = image;
        this.created = created;
        this.giver_id = giver_id;
        this.babyId = babyId;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getGiver_id() {
        return giver_id;
    }

    public void setGiver_id(String giver_id) {
        this.giver_id = giver_id;
    }

    public String getBabyId() {
        return babyId;
    }

    public void setBabyId(String babyId) {
        this.babyId = babyId;
    }
}
