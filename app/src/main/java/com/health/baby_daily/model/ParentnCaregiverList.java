package com.health.baby_daily.model;

public class ParentnCaregiverList {
    private String id;
    private String parent_id;
    private String caregiver_id;
    private String status;
    private String desc;

    public ParentnCaregiverList(){}

    public ParentnCaregiverList(String id, String parent_id, String caregiver_id, String status, String desc) {
        this.id = id;
        this.parent_id = parent_id;
        this.caregiver_id = caregiver_id;
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

    public String getCaregiver_id() {
        return caregiver_id;
    }

    public void setCaregiver_id(String caregiver_id) {
        this.caregiver_id = caregiver_id;
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
