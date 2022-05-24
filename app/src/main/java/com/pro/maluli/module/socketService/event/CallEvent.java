package com.pro.maluli.module.socketService.event;

import com.netease.nimlib.sdk.avsignalling.constant.InviteAckStatus;

public class CallEvent {

    public CallEvent(int ackStatus) {
        this.ackStatus = ackStatus;
    }

    public int getAckStatus() {
        return ackStatus;
    }

    public void setAckStatus(int ackStatus) {
        this.ackStatus = ackStatus;
    }

    private int ackStatus;
    private boolean isJump;//是否点击了跳过

    public boolean isJump() {
        return isJump;
    }

    public void setJump(boolean jump) {
        isJump = jump;
    }
}