package com.pro.maluli.module.video.events;

public class CanScrollEvent {
    private boolean isScroll;

    public CanScrollEvent(boolean b) {
        this.isScroll = b;
    }

    public boolean isScroll() {
        return isScroll;
    }

    public void setScroll(boolean clear) {
        isScroll = clear;
    }
}
