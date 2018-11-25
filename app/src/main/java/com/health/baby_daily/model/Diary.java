package com.health.baby_daily.model;

public class Diary {
    private String id;
    private String title;
    private String content;
    private String image;
    private String timestamp_babyId;
    private String dateTime;
    private String babyId;

    public Diary() {
    }

    public Diary(String id, String title, String content, String image, String timestamp_babyId, String dateTime, String babyId) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.content = content;
        this.timestamp_babyId = timestamp_babyId;
        this.dateTime = dateTime;
        this.babyId = babyId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getBabyId() {
        return babyId;
    }

    public void setBabyId(String babyId) {
        this.babyId = babyId;
    }
}
