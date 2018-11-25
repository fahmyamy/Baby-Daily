package com.health.baby_daily.model;

public class Bottle {
    private String id;
    private String dateTime;
    private String amount;
    private String type;
    private String units;
    private String babyId;
    private String created;
    private String giverId;

    public Bottle(){

    }


    public Bottle(String id, String dateTime, String amount, String type, String units, String baby_id, String created,  String giverId){
        this.id = id;
        this.dateTime = dateTime;
        this.amount = amount;
        this.type = type;
        this.units = units;
        this.babyId = baby_id;
        this.created = created;
        this.giverId = giverId;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getBabyId() {
        return babyId;
    }

    public void setBabyId(String babyId) {
        this.babyId = babyId;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getGiverId() {
        return giverId;
    }

    public void setGiverId(String giverId) {
        this.giverId = giverId;
    }
}
