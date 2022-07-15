package com.ssafy.chatapplication;

public class ChatData {
    private String id;
    private String name;
    private String message;
    private long time;

    public ChatData(String id, String name, String message, long time) {
        this.id = id;
        this.name = name;
        this.message = message;
        this.time = time;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public long getTime() {
        return time;
    }

    public ChatData(){

    }
}
