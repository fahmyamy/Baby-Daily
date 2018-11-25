package com.health.baby_daily.model;

public class Follow {
    private String id;
    private String user_id;
    private String follow_id;
    private String desc;

    public Follow() {
    }

    public Follow(String id, String user_id, String follow_id, String desc) {
        this.id = id;
        this.user_id = user_id;
        this.follow_id = follow_id;
        this.desc = desc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFollow_id() {
        return follow_id;
    }

    public void setFollow_id(String follow_id) {
        this.follow_id = follow_id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
