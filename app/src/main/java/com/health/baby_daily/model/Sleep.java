package com.health.baby_daily.model;

public class Sleep {
    private String id;
    private String startTime;
    private String endTime;
    private String giverId;
    private String created;
    private String babyId;

    public Sleep(){

    }

    public Sleep(String id, String startTime, String endTime, String giverId, String created, String babyId){
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.giverId = giverId;
        this.created = created;
        this.babyId = babyId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getGiverId() {
        return giverId;
    }

    public void setGiverId(String giverId) {
        this.giverId = giverId;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getBabyId() {
        return babyId;
    }

    public void setBabyId(String babyId) {
        this.babyId = babyId;
    }
}
