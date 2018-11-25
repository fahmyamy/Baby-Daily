package com.health.baby_daily.model;

public class Post {
    private String id;
    private String title;
    private String content;
    private String type;
    private String created;
    private String modified;
    private String uid;

    public Post(){

    }

    public Post(String id, String title, String content, String type, String created, String modified, String uid) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.type = type;
        this.created = created;
        this.modified = modified;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
