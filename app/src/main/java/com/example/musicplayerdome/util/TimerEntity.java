package com.example.musicplayerdome.util;


public class TimerEntity {
    private String title;
    private int timeState;
    private float speed;
    private boolean isSelect;

    public TimerEntity(String title) {
        this.title = title;
    }

    public TimerEntity(String title, int timeState, int currentState) {
        this(title, timeState);
        if (currentState == timeState) {
            setSelect(true);
        }
    }

    public TimerEntity(String title, int timeState) {
        this.title = title;
        this.timeState = timeState;
    }

    public TimerEntity(String title, float speed, Boolean... isSelect) {
        this.title = title;
        this.speed = speed;
        if (isSelect != null && isSelect.length > 0) {
            setSelect(isSelect[0]);
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTimeState() {
        return timeState;
    }

    public float getSpeed() {
        return speed;
    }

    public void setTime(int time) {
        this.timeState = time;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
