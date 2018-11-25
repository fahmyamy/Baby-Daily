package com.health.baby_daily.model;

public class Question {
    private String id;
    private String dateTime;
    private String question;
    private String answer;
    private String baby_Id;

    public Question(){

    }

    public Question(String id, String dateTime, String question, String answer, String baby_Id) {
        this.id = id;
        this.dateTime = dateTime;
        this.question = question;
        this.answer = answer;
        this.baby_Id = baby_Id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getBaby_Id() {
        return baby_Id;
    }

    public void setBaby_Id(String baby_Id) {
        this.baby_Id = baby_Id;
    }
}
