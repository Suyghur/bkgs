package com.pro.maluli.module.video.events;

public class CanScrollEvent {
    private boolean isScroll;

    public boolean isScroll() {
        return isScroll;
    }

    public void setScroll(boolean clear) {
        isScroll = clear;
    }

    public CanScrollEvent(boolean b) {
        this.isScroll = b;
    }
}
