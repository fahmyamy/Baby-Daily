package com.health.baby_daily.model;

public class Chat {
    private String id;
    private String userA;
    private String userB;

    public Chat(){

    }

    public Chat(String id, String userA, String userB) {
        this.id = id;
        this.userA = userA;
        this.userB = userB;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserA() {
        return userA;
    }

    public void setUserA(String userA) {
        this.userA = userA;
    }

    public String getUserB() {
        return userB;
    }

    public void setUserB(String userB) {
        this.userB = userB;
    }
}
