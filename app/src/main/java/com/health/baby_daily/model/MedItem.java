package com.health.baby_daily.model;

public class MedItem {
    private String medName;
    private String id;

    public MedItem(String medName, String id){
        this.medName = medName;
        this.id = id;
    }

    public String getMedName() {
        return medName;
    }

    public void setMedName(String medName) {
        this.medName = medName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String toString(){
        return medName;
    }
}
