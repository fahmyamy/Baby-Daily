package com.health.baby_daily.reminder;

public class Reminder {
    private int mID;
    private String mTitle;
    private String mBaby;
    private String mDate;
    private String mTime;
    private String mRepeat;
    private String mRepeatNo;
    private String mRepeatType;
    private String mActive;
    private String mParent;


    public Reminder(int ID, String Title, String Baby, String Date, String Time, String Repeat, String RepeatNo, String RepeatType, String Active, String Parent){
        mID = ID;
        mTitle = Title;
        mBaby = Baby;
        mDate = Date;
        mTime = Time;
        mRepeat = Repeat;
        mRepeatNo = RepeatNo;
        mRepeatType = RepeatType;
        mActive = Active;
        mParent = Parent;
    }

    public Reminder(String Title, String Baby, String Date, String Time, String Repeat, String RepeatNo, String RepeatType, String Active, String Parent){
        mTitle = Title;
        mBaby = Baby;
        mDate = Date;
        mTime = Time;
        mRepeat = Repeat;
        mRepeatNo = RepeatNo;
        mRepeatType = RepeatType;
        mActive = Active;
        mParent = Parent;
    }

    public Reminder(){}

    public int getID() {
        return mID;
    }

    public void setID(int ID) {
        mID = ID;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getBaby() {
        return mBaby;
    }

    public void setBaby(String baby) {
        mBaby = baby;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        mTime = time;
    }

    public String getRepeatType() {
        return mRepeatType;
    }

    public void setRepeatType(String repeatType) {
        mRepeatType = repeatType;
    }

    public String getRepeatNo() {
        return mRepeatNo;
    }

    public void setRepeatNo(String repeatNo) {
        mRepeatNo = repeatNo;
    }

    public String getRepeat() {
        return mRepeat;
    }

    public void setRepeat(String repeat) {
        mRepeat = repeat;
    }

    public String getActive() {
        return mActive;
    }

    public void setActive(String active) {
        mActive = active;
    }

    public String getmParent() {
        return mParent;
    }

    public void setmParent(String mParent) {
        this.mParent = mParent;
    }
}
