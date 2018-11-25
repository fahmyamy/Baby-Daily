package com.health.baby_daily.model;

public class User {
    private String id;
    private String username;
    private String image;
    private String role;
    private String email;
    private String created;
    private String modified;

    public User(){

    }

    public User(String uid,String un, String i, String r, String e, String c, String m){
        id = uid;
        username = un;
        image = i;
        role = r;
        email = e;
        created = c;
        modified = m;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
