package com.pro.maluli.module.video.events;

public class ClearPositionEvent {
    private boolean isClear;

    public boolean isClear() {
        return isClear;
    }

    public void setClear(boolean clear) {
        isClear = clear;
    }

    public ClearPositionEvent(boolean b) {
        this.isClear = b;
    }
}
