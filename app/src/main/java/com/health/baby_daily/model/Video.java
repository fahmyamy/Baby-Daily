package com.health.baby_daily.model;

public class Video {
    private String id;
    private String title;
    private String type;
    private String desc;
    private String video;
    private String created;
    private String uid;

    public Video(){}

    public Video(String id, String title, String type, String desc, String video, String created, String uid) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.desc = desc;
        this.video = video;
        this.created = created;
        this.uid = uid;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
