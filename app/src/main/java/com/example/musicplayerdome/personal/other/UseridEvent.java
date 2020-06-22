package com.example.musicplayerdome.personal.other;

public class UseridEvent {
    private long id;

    public UseridEvent(long id) {
        this.id = id;
    }

    public long getKey() {
        return id;
    }

    public void setKey(long id) {
        this.id = id;
    }

}
