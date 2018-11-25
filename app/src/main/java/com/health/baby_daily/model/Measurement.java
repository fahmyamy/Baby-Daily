package com.health.baby_daily.model;

public class Measurement {
    private String id;
    private String dateTime;
    private String height;
    private String weight;
    private String headC;
    private String desc;
    private String created;
    private String timestamp_babyId;
    private String baby_id;

    public Measurement(){

    }

    public Measurement(String id, String dateTime, String height, String weight, String headC, String desc, String created, String timestamp_babyId, String baby_id){
        this.id = id;
        this.dateTime = dateTime;
        this.height = height;
        this.weight = weight;
        this.headC = headC;
        this.desc = desc;
        this.created = created;
        this.timestamp_babyId = timestamp_babyId;
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

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeadC() {
        return headC;
    }

    public void setHeadC(String headC) {
        this.headC = headC;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getTimestamp_babyId() {
        return timestamp_babyId;
    }

    public void setTimestamp_babyId(String timestamp_babyId) {
        this.timestamp_babyId = timestamp_babyId;
    }

    public String getBaby_id() {
        return baby_id;
    }

    public void setBaby_id(String baby_id) {
        this.baby_id = baby_id;
    }
}
