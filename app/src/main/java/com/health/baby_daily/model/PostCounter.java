package com.health.baby_daily.model;

public class PostCounter {
    private String id;
    private String postId;
    private String postId_uid;

    public PostCounter() {
    }

    public PostCounter(String id, String postId, String postId_uid) {
        this.id = id;
        this.postId = postId;
        this.postId_uid = postId_uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostId_uid() {
        return postId_uid;
    }

    public void setPostId_uid(String postId_uid) {
        this.postId_uid = postId_uid;
    }
}
