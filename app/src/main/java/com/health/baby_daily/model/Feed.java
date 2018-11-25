package com.health.baby_daily.model;

public class Feed {
    private String id;
    private String dateTime;
    private String amount;
    private String type;
    private String units;
    private String created;
    private String giverId;
    private String baby_id;

    public Feed(){

    }

    public Feed(String id, String dateTime, String amount, String type, String units, String created, String giverId, String baby_id){
        this.id = id;
        this.dateTime = dateTime;
        this.amount = amount;
        this.type = type;
        this.units = units;
        this.created = created;
        this.giverId = giverId;
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

    public String getBaby_id() {
        return baby_id;
    }

    public void setBaby_id(String baby_id) {
        this.baby_id = baby_id;
    }
}
