package com.health.baby_daily.model;

public class CaregiverItem {
    private String caregiverName;
    private String id;

    public CaregiverItem(){}

    public CaregiverItem(String caregiverName, String id) {
        this.caregiverName = caregiverName;
        this.id = id;
    }

    public String getCaregiverName() {
        return caregiverName;
    }

    public void setCaregiverName(String caregiverName) {
        this.caregiverName = caregiverName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return caregiverName;
    }
}
