package com.health.baby_daily.model;

public class MedicineD {
    private String id;
    private String dateTime;
    private String medName;
    private String doseAmount;
    private String units;
    private String image;
    private String timestamp_babyId;
    private String baby_id;

    public MedicineD(){

    }

    public MedicineD(String id, String dateTime, String medName, String doseAmount, String units, String image, String timestamp_babyId, String baby_id){
        this.id = id;
        this.dateTime = dateTime;
        this.medName = medName;
        this.doseAmount = doseAmount;
        this.units = units;
        this.image = image;
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

    public String getMedName() {
        return medName;
    }

    public void setMedName(String medName) {
        this.medName = medName;
    }

    public String getDoseAmount() {
        return doseAmount;
    }

    public void setDoseAmount(String doseAmount) {
        this.doseAmount = doseAmount;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
