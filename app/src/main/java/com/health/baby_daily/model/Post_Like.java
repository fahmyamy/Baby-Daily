package com.health.baby_daily.model;

public class Post_Like {
    private String id;
    private String post_id;
    private String user_id;
    private String type;

    public Post_Like() {
    }

    public Post_Like(String id, String post_id, String user_id, String type) {
        this.id = id;
        this.post_id = post_id;
        this.user_id = user_id;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
