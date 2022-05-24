package com.pro.maluli.module.video.events;

public class CanStartEvent {
    boolean isCanStart;
    boolean isNoseet;

    public boolean isNoseet() {
        return isNoseet;
    }
    public CanStartEvent() {

    }

    public void setNoseet(boolean noseet) {
        isNoseet = noseet;
    }

    public CanStartEvent(boolean isCanStart) {
        this.isCanStart = isCanStart;
    }

    public boolean isCanStart() {
        return isCanStart;
    }

    public void setCanStart(boolean canStart) {
        isCanStart = canStart;
    }
}