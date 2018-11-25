package com.health.baby_daily.model;

public class Appointment {
    private String id;
    private String dateTime;
    private String doctorName;
    private String type;
    private String desc;
    private String baby_id;

    public Appointment(){

    }

    public Appointment(String id, String dateTime, String doctorName, String type, String desc, String baby_id) {
        this.id = id;
        this.dateTime = dateTime;
        this.doctorName = doctorName;
        this.type = type;
        this.desc = desc;
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

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
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

    public String getBaby_id() {
        return baby_id;
    }

    public void setBaby_id(String baby_id) {
        this.baby_id = baby_id;
    }
}
