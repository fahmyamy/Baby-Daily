package com.health.baby_daily.model;

public class Chatroom {
    private String id;
    private String sender_id;
    private String receiver_id;
    private String textContent;
    private String timestamp;
    private String created;
    private boolean read;
    private String chat_id;

    public Chatroom (){}

    public Chatroom(String id, String sender_id, String receiver_id, String textContent, String timestamp, String created, boolean read, String chat_id) {
        this.id = id;
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.textContent = textContent;
        this.timestamp = timestamp;
        this.created = created;
        this.read = read;
        this.chat_id = chat_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getChat_id() {
        return chat_id;
    }

    public void setChat_id(String chat_id) {
        this.chat_id = chat_id;
    }
}
