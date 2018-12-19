package com.adamgilchrist.messageapp;

public class MessageObj {
    private String message;
    private String contact;
    private String date;
    private String time;

    public void setMessage(String message){
        this.message = message;
    }
    public String getMessage(){
        return this.message;
    }
    public void setContact(String contact) {
        this.contact = contact;
    }
    public String getContact(){
        return this.contact;
    }
    public void setDate(String date){
        this.date = date;
    }
    public String getDate(){
        return this.date;
    }
    public void setTime(String time){
        this.time = time;
    }
    public String getTime(){
        return this.time;
    }
}
