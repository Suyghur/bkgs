package com.pro.maluli.module.socketService.event;

public class CallEvent {

    private int ackStatus;
    private boolean isJump;//是否点击了跳过

    public CallEvent(int ackStatus) {
        this.ackStatus = ackStatus;
    }

    public int getAckStatus() {
        return ackStatus;
    }

    public void setAckStatus(int ackStatus) {
        this.ackStatus = ackStatus;
    }

    public boolean isJump() {
        return isJump;
    }

    public void setJump(boolean jump) {
        isJump = jump;
    }
}