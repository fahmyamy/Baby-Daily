package com.health.baby_daily.model;

public class Medicine {
    private String id;
    private String dateTime;
    private String medId;
    private String desc;
    private String created;
    private String timestamp_babyId;
    private String baby_id;

    public Medicine(){

    }

    public Medicine(String id, String dateTime, String medId, String desc, String created, String timestamp_babyId, String baby_id) {
        this.id = id;
        this.dateTime = dateTime;
        this.medId = medId;
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

    public String getMedId() {
        return medId;
    }

    public void setMedId(String medId) {
        this.medId = medId;
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
