package com.pro.maluli.common.eventBus;

public class SettingSeeView {
    boolean isSee;

    public SettingSeeView(int hasNewsNumber) {
        this.hasNewsNumber = hasNewsNumber;
    }

    public int getHasNewsNumber() {
        return hasNewsNumber;
    }

    public void setHasNewsNumber(int hasNewsNumber) {
        this.hasNewsNumber = hasNewsNumber;
    }

    int hasNewsNumber;

    public boolean isSee() {
        return isSee;
    }


    public void setSee(boolean see) {
        isSee = see;
    }
}