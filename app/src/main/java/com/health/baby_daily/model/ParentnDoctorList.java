package com.health.baby_daily.model;

public class ParentnDoctorList {
    private String id;
    private String parent_id;
    private String doctor_id;
    private String status;
    private String desc;

    public ParentnDoctorList(){

    }

    public ParentnDoctorList(String id, String parent_id, String doctor_id, String status, String desc) {
        this.id = id;
        this.parent_id = parent_id;
        this.doctor_id = doctor_id;
        this.status = status;
        this.desc = desc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
