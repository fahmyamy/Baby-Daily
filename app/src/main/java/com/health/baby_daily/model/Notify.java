package com.health.baby_daily.model;

public class Notify {
    private String from;
    private String type;
    private String content;

    public Notify() {
    }

    public Notify(String from, String type, String content) {
        this.from = from;
        this.type = type;
        this.content = content;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
