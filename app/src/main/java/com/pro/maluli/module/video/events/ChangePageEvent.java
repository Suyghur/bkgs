package com.pro.maluli.module.video.events;

public class ChangePageEvent {
    private int positioning;

    public int getPositioning() {
        return positioning;
    }

    public void setPositioning(int positioning) {
        this.positioning = positioning;
    }

    public ChangePageEvent(int b) {
        this.positioning = b;
    }
}
